function createNewDiscipline() {
    const name = document.getElementById("discipline-input").value;
    const group = document.getElementById("group-input").value;

    const disciplineRequest = {
        "name": name,
        "group": group
    };

    fetch("/teacher/new-discipline", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(disciplineRequest),
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`Ошибка: ${response.status}`);
            }
            return response.json();
        })
        // .then(() => {
        //     window.location.href = "http://localhost:8080/portal";
        // })
        // .catch(() => {
        //     document.getElementById("error-message").textContent = "Ошибка: Неверное имя пользователя или пароль";
        //     document.getElementById("error-message").style.display = "flex";
        // });
}