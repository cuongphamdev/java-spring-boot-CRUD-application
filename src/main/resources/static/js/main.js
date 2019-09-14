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

function setError (messageElm, message, inputElm) {
    messageElm.innerHTML = message;
    inputElm.classList.add("error");
}

function setDefault (messageElm, inputElm) {
    messageElm.innerHTML = "";
    inputElm.classList.remove("error");
}