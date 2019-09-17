if (document.getElementById("register-form")) {
    document.getElementById("btn-submit").onclick = function() {
        let registerForm = document.getElementById("register-form");
        let email = document.getElementById("email");
        let password = document.getElementById("password");
        let name = document.getElementById("name");
        let confirmPassword = document.getElementById("confirm-password");
        let emailError = document.getElementById("email-error");
        let passwordError = document.getElementById("password-error");
        let nameError = document.getElementById("name-error");
        let confirmPasswordError = document.getElementById("confirm-password-error");
        let isError = false;

        if (email.value === '') {
            isError = true;
            setError(emailError, "Email cannot be empty", email)
        }

        if (name.value === '') {
            isError = true;
            setError(nameError, "Name cannot be empty", name)
        }

        if (password.value === '') {
            isError = true;
            setError(passwordError, "Password cannot be empty", password)
        }

        if (confirmPassword.value !== password.value || confirmPassword.value === '') {
            isError = true;
            setError(confirmPasswordError, "Password verify is incorrect", confirmPassword);
        }

        if (isError) {
            console.log(isError);
        } else {
            registerForm.submit();
        }
    }

    document.getElementById("password").onfocus = function() {
        let password = document.getElementById("password");
        let passwordError = document.getElementById("password-error");
        setDefault(passwordError, password);
    }

    document.getElementById("email").onfocus = function() {
        let email = document.getElementById("email");
        let emailError = document.getElementById("email-error");
        setDefault(emailError, email);
    }

    document.getElementById("name").onfocus = function() {
        let name = document.getElementById("name");
        let nameError = document.getElementById("name-error");
        setDefault(nameError, name);
    }

    document.getElementById("confirm-password").onfocus = function() {
        let confirmPassword = document.getElementById("confirm-password");
        let confirmPasswordError = document.getElementById("confirm-password-error");
        setDefault(confirmPasswordError, confirmPassword);
    }
}

if (document.getElementById("login-form")) {
    document.getElementById("btn-submit").onclick = function() {
        let loginForm = document.getElementById("login-form");
        let email = document.getElementById("email");
        let password = document.getElementById("password");
        let emailError = document.getElementById("email-error");
        let passwordError = document.getElementById("password-error");
        let isError = false;

        if (email.value === '') {
            isError = true;
            setError(emailError, "Email cannot be empty", email)
        }

        if (password.value === '') {
            isError = true;
            setError(passwordError, "Password cannot be empty", password)
        }

        if (isError) {
            console.log(isError);
        } else {
            loginForm.submit();
        }
    }

    document.getElementById("password").onfocus = function() {
        let password = document.getElementById("password");
        let passwordError = document.getElementById("password-error");
        setDefault(passwordError, password);
    }

    document.getElementById("email").onfocus = function() {
        let password = document.getElementById("email");
        let passwordError = document.getElementById("email-error");
        setDefault(passwordError, password);
    }
}

if (document.getElementById("post-lists") && document.getElementById("action-more-btn")) {

        var listNodes = document.querySelectorAll("#action-more-btn");
        listNodes.forEach(item =>
            item.addEventListener('click', function(event) {
                console.log(event.target.parentNode.parentNode.parentNode);
                let postID = event.target.getAttribute('post-id');
                for (let i = 0; i < event.target.parentNode.parentNode.parentNode.childNodes.length; i++) {
                    if (event.target.parentNode.parentNode.parentNode.childNodes[i].className == "popup") {
                        if (event.target.parentNode.parentNode.parentNode.childNodes[i].classList.contains("show")) {
                            event.target.parentNode.parentNode.parentNode.childNodes[i].classList.remove("show");
                        } else {
                            event.target.parentNode.parentNode.parentNode.childNodes[i].classList.add("show");
                        }
                    }
                }
            })
        );
    if (document.getElementById("inner")) {
        var listNodes = document.querySelectorAll("#inner");
        listNodes.forEach(item =>
        item.addEventListener('click', function(event) {
            event.target.parentNode.parentNode.classList.remove("show");
        }));
    }

    let listActionUpdateNodes = document.querySelectorAll("#update-post");
    listActionUpdateNodes.forEach(item =>
    item.addEventListener('click', function(event) {
        let postId = item.getAttribute("post-id");
        let postData = event.target.parentNode.parentNode.parentNode;
        let title = postData.querySelector(".title").textContent;
        let content = postData.querySelector(".content").textContent;
        document.getElementById("modal-update").classList.add("show");

        document.getElementById("update-post-title").value = title;
        document.getElementById("update-post-content").value = content;
        document.getElementById("update-post-id").value = postId;
        event.target.parentNode.parentNode.classList.remove("show");
    }));

    document.getElementById("close-update-modal").onclick = function () {
        document.getElementById("modal-update").classList.remove("show");
    }

    document.getElementById("post-update-submit").onclick = async function (event) {
        let title = document.getElementById("update-post-title").value;
        let content = document.getElementById("update-post-content").value;
        let postId = document.getElementById("update-post-id").value;
        let data = {
            title, content
        }
        document.getElementById("modal-update").classList.remove("show");
            await sendAjax(`/posts/${postId}`, data, "PUT", (data) => {

            });

            let postItem = document.getElementById(`post-${postId}`);
            console.log("post", postItem.childNodes.item("title"));
            for (let i = 0; i < postItem.childNodes.length; i++) {
                if (postItem.childNodes[i].className == "title") {
                    console.log(postItem.childNodes[i])
                }
            }
            console.log("data", data);

            postItem.querySelector(".title").innerHTML = data.title;
            postItem.querySelector(".content").innerHTML = data.content;
            event.target.parentNode.parentNode.classList.remove("show");
    }

    //  TODO: show update post model

    let listActionRemoveNodes = document.querySelectorAll("#remove-post");
    listActionRemoveNodes.forEach(item =>
    item.addEventListener('click', function(event) {
        let postId = item.getAttribute("post-id");
        sendAjax(`/posts/${postId}`, null, "DELETE", (response) => {
            item.parentNode.parentNode.parentNode.remove();
        })
    }));

    document.getElementById("post-create-submit").onclick = function (event) {
        let postForm = document.getElementById("create-post-form");
        let title = document.getElementById("form-post-title");
        let content = document.getElementById("form-post-content");
        console.log ("title", "content", title.value, content.value)
        if (content.value !== "", title.value !== "") {
            postForm.submit();
        }
    }
}

async function sendAjax (url, data, method, action) {
    let response = null;
    try {
        response = await $.ajax({
            url,
            method,
            data
        });
    }catch (e) {

    }
    action(response);
}

function setError (messageElm, message, inputElm) {
    messageElm.innerHTML = message;
    inputElm.classList.add("error");
}

function setDefault (messageElm, inputElm) {
    messageElm.innerHTML = "";
    inputElm.classList.remove("error");
}