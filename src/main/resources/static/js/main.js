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

    if (email.value === '') {
      isError = true;
      setError(emailError, 'Email cannot be empty', email);
    }

    if (name.value === '') {
      isError = true;
      setError(nameError, 'Name cannot be empty', name);
    }

    if (password.value === '') {
      isError = true;
      setError(passwordError, 'Password cannot be empty', password);
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

    if (email.value === '') {
      isError = true;
      setError(emailError, 'Email cannot be empty', email);
    }

    if (password.value === '') {
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
  document.getElementById('post-lists') &&
  document.getElementById('action-more-btn')
) {
  var listNodes = document.querySelectorAll('#action-more-btn');
  listNodes.forEach(item =>
    item.addEventListener('click', function(event) {
      console.log(event.target.parentNode.parentNode.parentNode);
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
      let content = postData.querySelector('.content').textContent;
      document.getElementById('modal-update').classList.add('show');

      document.getElementById('update-post-title').value = title;
      document.getElementById('update-post-content').value = content;
      document.getElementById('update-post-id').value = postId;
      event.target.parentNode.parentNode.classList.remove('show');
    })
  );

  document.getElementById('close-update-modal').onclick = function() {
    document.getElementById('modal-update').classList.remove('show');
  };

  document.getElementById('post-update-submit').onclick = async function(
    event
  ) {
    let title = document.getElementById('update-post-title').value;
    let content = document.getElementById('update-post-content').value;
    let postId = document.getElementById('update-post-id').value;
    let data = {
      title,
      content
    };
    document.getElementById('modal-update').classList.remove('show');
    await sendAjax(`/posts/${postId}`, data, 'PUT', data => {});

    let postItem = document.getElementById(`post-${postId}`);
    console.log('post', postItem.childNodes.item('title'));
    for (let i = 0; i < postItem.childNodes.length; i++) {
      if (postItem.childNodes[i].className == 'title') {
        console.log(postItem.childNodes[i]);
      }
    }
    console.log('data', data);

    postItem.querySelector('.title').innerHTML = data.title;
    postItem.querySelector('.content').innerHTML = data.content;
    event.target.parentNode.parentNode.classList.remove('show');
  };

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

  document.getElementById('post-create-submit').onclick = function(event) {
    let postForm = document.getElementById('create-post-form');
    let title = document.getElementById('form-post-title');
    let content = document.getElementById('form-post-content');
    console.log('title', 'content', title.value, content.value);
    if ((content.value !== '', title.value !== '')) {
      postForm.submit();
    }
  };
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
              if (commentEl.childNodes[i].className == 'media-body') {
                for (let j = 0; j < commentEl.childNodes[i].childNodes.length; j++) { 
                  if (commentEl.childNodes[i].childNodes[j].className == 'content') {
                    commentContentEl = commentEl.childNodes[i].childNodes[j];
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
        for (let i = 0; i < commentEl.childNodes.length; i++) {
          if (commentEl.childNodes[i].className == 'media-body') {
            for (let j = 0; j < commentEl.childNodes[i].childNodes.length; j++) { 
              if (commentEl.childNodes[i].childNodes[j].className == 'content') {
                commentEl.childNodes[i].childNodes[j].innerHTML = response.content;
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
