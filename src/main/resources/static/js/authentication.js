function signIn() {
    const username = document.getElementById("username-input").value;
    const password = document.getElementById("password-input").value;

    const signInRequest = { username, password };

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