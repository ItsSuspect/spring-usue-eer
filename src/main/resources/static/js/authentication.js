function toggleRememberMe(text) {
    const checkbox = $(text).siblings('.authorization-form__input_assignment_remember-me');
    checkbox.prop('checked', !checkbox.prop('checked'));
}

function signIn() {
    const username = document.getElementById("username-input").value;
    const password = document.getElementById("password-input").value;
    const checkbox = document.getElementById("remember-me");

    const signInRequest = {
        "username": username,
        "password": password,
        "rememberMe": checkbox.checked
    };

    fetch("/auth/signIn", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(signInRequest),
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`Ошибка: ${response.status}`);
            }
            return response.json();
        })
        .then(() => {
            window.location.href = "http://localhost:8080/portal";
        })
        .catch(() => {
            document.getElementById("error-message").textContent = "Ошибка: Неверное имя пользователя или пароль";
            document.getElementById("error-message").style.display = "flex";
        });
}