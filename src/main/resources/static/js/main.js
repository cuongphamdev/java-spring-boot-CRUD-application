if (document.getElementById('register-form')) {
  document.getElementById('btn-submit').onclick = function() {
    let registerForm = document.getElementById('register-form');
    let email = document.getElementById('email');
    let password = document.getElementById('password');
    let name = document.getElementById('name');
    let confirmPassword = document.getElementById('confirm-password');
    let emailError = document.getElementById('email-error');
    let passwordError = document.getElementById('password-error');
    let nameError = document.getElementById('name-error');
    let confirmPasswordError = document.getElementById(
      'confirm-password-error'
    );
    let isError = false;

    if (email.value.trim() === '') {
      isError = true;
      setError(emailError, 'Email cannot be empty', email);
    }

    if (name.value.trim() === '') {
      isError = true;
      setError(nameError, 'Name cannot be empty', name);
    }

    if (password.value.trim() === '') {
      isError = true;
      setError(passwordError, 'Password cannot be empty', password);
    }

    if (password.value.length < 6) {
      isError = true;
      setError(passwordError, 'Password should be more than 6 characters', password);
    }

    if (
      confirmPassword.value !== password.value ||
      confirmPassword.value === ''
    ) {
      isError = true;
      setError(
        confirmPasswordError,
        'Password verify is incorrect',
        confirmPassword
      );
    }

    if (isError) {
      console.log(isError);
    } else {
      registerForm.submit();
    }
  };

  document.getElementById('password').onfocus = function() {
    let password = document.getElementById('password');
    let passwordError = document.getElementById('password-error');
    setDefault(passwordError, password);
  };

  document.getElementById('email').onfocus = function() {
    let email = document.getElementById('email');
    let emailError = document.getElementById('email-error');
    setDefault(emailError, email);
  };

  document.getElementById('name').onfocus = function() {
    let name = document.getElementById('name');
    let nameError = document.getElementById('name-error');
    setDefault(nameError, name);
  };

  document.getElementById('confirm-password').onfocus = function() {
    let confirmPassword = document.getElementById('confirm-password');
    let confirmPasswordError = document.getElementById(
      'confirm-password-error'
    );
    setDefault(confirmPasswordError, confirmPassword);
  };
}

if (document.getElementById('login-form')) {
  document.getElementById('btn-submit').onclick = function() {
    let loginForm = document.getElementById('login-form');
    let email = document.getElementById('email');
    let password = document.getElementById('password');
    let emailError = document.getElementById('email-error');
    let passwordError = document.getElementById('password-error');
    let isError = false;

    if (email.value.trim() === '') {
      isError = true;
      setError(emailError, 'Email cannot be empty', email);
    }

    if (password.value.trim() === '') {
      isError = true;
      setError(passwordError, 'Password cannot be empty', password);
    }

    if (isError) {
      console.log(isError);
    } else {
      loginForm.submit();
    }
  };

  document.getElementById('password').onfocus = function() {
    let password = document.getElementById('password');
    let passwordError = document.getElementById('password-error');
    setDefault(passwordError, password);
  };

  document.getElementById('email').onfocus = function() {
    let password = document.getElementById('email');
    let passwordError = document.getElementById('email-error');
    setDefault(passwordError, password);
  };
}

if (
  document.getElementById('action-more-btn')
) {
  var listNodes = document.querySelectorAll('#action-more-btn');
  listNodes.forEach(item =>
    item.addEventListener('click', function(event) {
      let postID = event.target.getAttribute('post-id');
      for (
        let i = 0;
        i < event.target.parentNode.parentNode.parentNode.childNodes.length;
        i++
      ) {
        if (
          event.target.parentNode.parentNode.parentNode.childNodes[i]
            .className == 'popup'
        ) {
          if (
            event.target.parentNode.parentNode.parentNode.childNodes[
              i
            ].classList.contains('show')
          ) {
            event.target.parentNode.parentNode.parentNode.childNodes[
              i
            ].classList.remove('show');
          } else {
            event.target.parentNode.parentNode.parentNode.childNodes[
              i
            ].classList.add('show');
          }
        }
      }
    })
  );
  if (document.getElementById('inner')) {
    var listNodes = document.querySelectorAll('#inner');
    listNodes.forEach(item =>
      item.addEventListener('click', function(event) {
        event.target.parentNode.parentNode.classList.remove('show');
      })
    );
  }

  let listActionUpdateNodes = document.querySelectorAll('#update-post');
  listActionUpdateNodes.forEach(item =>
    item.addEventListener('click', function(event) {
      let postId = item.getAttribute('post-id');
      let postData = event.target.parentNode.parentNode.parentNode;
      let title = postData.querySelector('.title').textContent;
      console.log(postData.querySelector('.tags small').childNodes);
      let content = postData.querySelector('.content').textContent;
      let tags = postData.querySelector('.tags small').childNodes;
      tagIds = [];
      postData.querySelector('.tags small').childNodes.forEach(tag => {
        let tagId = tag.getAttribute("tag-id");
        tagIds.push(tagId);
      })
      $('#update-tags-list').val(tagIds);
      $('#update-tags-list').selectpicker('refresh')
      $("#modal-update").addClass("show");
      $("#update-post-title").val(title.trim());
      $("#update-post-content").val(content);
      $("#update-post-id").val(postId);
      event.target.parentNode.parentNode.classList.remove('show');
    })
  );

  $('#close-update-modal').on('click', e => {
    document.getElementById('modal-update').classList.remove('show');
  });

  $('#post-update-submit').on('click', async e => {
    let title = document.getElementById('update-post-title').value.trim();
    let content = document.getElementById('update-post-content').value.trim();
    let postId = document.getElementById('update-post-id').value;
    let tagIds = $("#update-tags-list").val();

    if (title === '' || content === '') {
      alert("Title and content cannot leave blank");
    } else {
      let tags = "";
      tagIds.forEach(item => {
        tags += `,${item}`
      })
      let data = {
        title,
        content,
        tags
      };
      document.getElementById('modal-update').classList.remove('show');
      await sendAjax(`/posts/${postId}`, data, 'PUT', data => {
        let postItem = document.getElementById(`post-${postId}`);
        let htmlTitle = `
      <h3 class="title" id="title">
      <a href="/posts/${data.id}" class="link">${data.title}</a>
    </h3>
    `;
        postItem.querySelector('.title ').innerHTML = htmlTitle;
        postItem.querySelector('.content').innerHTML = data.content;

        let tagString = "<small>"

        data.tags != null && data.tags.forEach(item => {
          tagString += `<span tag-id="${item.id}">#${item.name}</span>`
        })
        tagString += "</small>";
        postItem.querySelector('.tags').innerHTML = tagString;
        e.target.parentNode.parentNode.classList.remove('show');
      });
    }


});

  //  TODO: show update post model

  let listActionRemoveNodes = document.querySelectorAll('#remove-post');
  listActionRemoveNodes.forEach(item =>
    item.addEventListener('click', async function(event) {
      let postId = item.getAttribute('post-id');
      await sendAjax(`/posts/${postId}`, null, 'DELETE', response => {
        item.parentNode.parentNode.parentNode.remove();
      });
    })
  );
}

if (document.getElementById('post-create-submit')) {
document.getElementById('post-create-submit').onclick = function(event) {
  let postForm = document.getElementById('create-post-form');
  let title = document.getElementById('form-post-title');
  let content = document.getElementById('form-post-content');
  if ((content.value.trim() !== '' && title.value.trim() !== '')) {
    postForm.submit();
  } else {
    alert("You shouldn't leave title or content blank")
  }
}
}


if (document.getElementById("post-detail")) {
    let listActionUpdateNodes = document.querySelectorAll('#comment-update-button');
    listActionUpdateNodes.forEach(item =>
        item.addEventListener('click', async function(event) {
          let commentId = event.target.getAttribute('comment-id');
          let postId = event.target.getAttribute('post-id');
          let commentEl = document.getElementById(`comment${commentId}`);
          $("#update-comment-post-id").val(postId);
          $("#update-comment-comment-id").val(commentId);
          let commentContentEl = null;
            for (let i = 0; i < commentEl.childNodes.length; i++) {
              
              if (commentEl.childNodes[i].className == 'comment-item__wrap') {
                for (let j = 0; j < commentEl.childNodes[i].childNodes.length; j++) { 
                  if (commentEl.childNodes[i].childNodes[j].className == 'media-body') {
                    for (let k = 0; k < commentEl.childNodes[i].childNodes[j].childNodes.length; k++) { 
                      if (commentEl.childNodes[i].childNodes[j].childNodes[k].className == 'content') {
                        commentContentEl = commentEl.childNodes[i].childNodes[j].childNodes[k];
                      } 
                    }
                  } 
                }
                
              }
            }

          let content = commentContentEl ?  commentContentEl.textContent : '';
          $("#update-comment-content").val(content);
          document.getElementById('modal-update-comment').classList.add('show');
        })
    )

    $('#comment-update-submit-button').on('click', e => {
      let postId = $("#update-comment-post-id").val();
      let commentId = $("#update-comment-comment-id").val();
      let content = $("#update-comment-content").val();

      sendAjax(`/posts/${postId}/comments/${commentId}`, {content}, "PUT",  (response) => {
        let commentEl = document.getElementById(`comment${commentId}`);
        // for (let i = 0; i < commentEl.childNodes.length; i++) {
        //   if (commentEl.childNodes[i].className == 'media-body') {
        //     for (let j = 0; j < commentEl.childNodes[i].childNodes.length; j++) { 
        //       if (commentEl.childNodes[i].childNodes[j].className == 'content') {
        //         commentEl.childNodes[i].childNodes[j].innerHTML = response.content;
        //       } 
        //     }
        //   }
        // }

        for (let i = 0; i < commentEl.childNodes.length; i++) {
              
          if (commentEl.childNodes[i].className == 'comment-item__wrap') {
            for (let j = 0; j < commentEl.childNodes[i].childNodes.length; j++) { 
              if (commentEl.childNodes[i].childNodes[j].className == 'media-body') {
                for (let k = 0; k < commentEl.childNodes[i].childNodes[j].childNodes.length; k++) { 
                  if (commentEl.childNodes[i].childNodes[j].childNodes[k].className == 'content') {
                    commentEl.childNodes[i].childNodes[j].childNodes[k].innerHTML = response.content;
                  } 
                }
              } 
            }
            
          }
        }

        $(`#comment${commentId}`).animate({background:'#f55a2e40'},'slow');
        $(`#comment${commentId}`).animate({background:'transparent'},'slow');
        document.getElementById("modal-update-comment").classList.remove("show");
      });
    })
 
    let listActionRemoveNodes = document.querySelectorAll('#comment-delete-button');
    listActionRemoveNodes.forEach(item =>
        item.addEventListener('click', async function(event) {
          let commentId = event.target.getAttribute('comment-id');
          let postId = event.target.getAttribute('post-id');
          sendAjax(`/posts/${postId}/comments/${commentId}`, null, "DELETE",  (response) => {
            let commentEl = document.getElementById(`comment${commentId}`);
            commentEl.remove();
          })
        })
    );

    $('#close-update-modal-comment').on("click", (e) => {
      e.target.parentNode.classList.remove("show");
    });
}

  let eventTimeout = null;
  $("#input-text-search").keyup(function(event) {
    clearTimeout(eventTimeout);
    eventTimeout = setTimeout(() => {
      $('#dropdown-user-search-list').css("display", "block");
      let searchText = $('#input-text-search').val();
      if (searchText != '') {
        sendAjax(`/search/user/?search${searchText}`, null, "GET", (response) => {
          $("#dropdown-user-search-list").empty();
        if (response.length > 0) {
          $("#dropdown-user-search-list").empty();
          response.forEach(item => (
            $("#dropdown-user-search-list").append(`<a class="dropdown-item" target="_blank" href="/users/${item.id}">${item.name}</a>`)
          ));
        } else {
          $("#dropdown-user-search-list").append(`<p >No result found</p>`);
        }
      });
      }
    }, 500);
  });

$( "#input-post-search" )
  .focusout(function() {
    setTimeout(() => {$('#dropdown-post-search-list').css("display", "none");}, 500)

  });

  let searchPostTimeout = null;
  $("#input-post-search").keyup(function(event) {
    clearTimeout(searchPostTimeout);
    searchPostTimeout = setTimeout(() => {
      $('#dropdown-post-search-list').css("display", "block");
      let searchText = $('#input-post-search').val();
      if (searchText != '') {
        sendAjax(`/search/post/${searchText}`, null, "GET", (response) => {
          $("#dropdown-post-search-list").empty();
        if (response.length > 0) {
          $("#dropdown-post-search-list").empty();
          response.forEach(item => (
            $("#dropdown-post-search-list").append(`<a class="dropdown-item" target="_blank" href="/posts/${item.id}">${item.title}</a>`)
          ));
        } else {
          $("#dropdown-post-search-list").append(`<p >No result found</p>`);
        }
      });
      }
    }, 500);
  });

$(document).on("click", "#reply-comment-btn", (e) => {
  let postId = e.target.getAttribute('post-id');
  let commentId = e.target.getAttribute('comment-id');

  let formHTML = `
    <form
     action="/posts/${postId}/comments"
     class="form"
     method="post"
     id="reply-comment-form"
    ><br>
     <div class="form-group">
     <textarea
       class="form-control"
       placeholder="write a comment..."
       rows="3"
       name="content"
     ></textarea>
             <input type="hidden" value="${commentId}" name="parentId">
         </div>
         <div class="form-group d-flex flex-x--end">
            <button type="button" class="btn btn-default btn-warning pull-left" id="btn-close-reply-comment-form">Close</button>
             <button type="button" class="btn btn-info pull-right" id="submit-reply-form">
                 Reply now
             </button>
         </div>
      </form>
  `;
  if (!e.target.parentNode.querySelector("#reply-comment-form")) {
    e.target.parentElement.insertAdjacentHTML('beforeend', formHTML);
  }
});

$(document).on("click","#btn-close-reply-comment-form",function(e) {
  e.target.parentNode.parentNode.remove();
});

$(document).on("click","#submit-reply-form",function(e) {
  console.log(e.target.parentNode.parentNode.querySelector(".form-group > textarea").value);
  if (e.target.parentNode.parentNode.querySelector(".form-group > textarea").value) {
    e.target.parentNode.parentNode.submit();
  }
});


async function sendAjax(url, data, method, action) {
  try {
    let response = await $.ajax({
      url,
      method,
      data
    });
    action(response);
  } catch (e) {}
}

function setError(messageElm, message, inputElm) {
  messageElm.innerHTML = message;
  inputElm.classList.add('error');
}

function setDefault(messageElm, inputElm) {
  messageElm.innerHTML = '';
  inputElm.classList.remove('error');
}
