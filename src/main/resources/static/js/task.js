function sendTask(element) {
    const comment = document.getElementById("comment-to-task").value;
    const disciplineId = element.getAttribute("data-disciplineId");
    const taskId = element.getAttribute("data-taskId");

    const SendTaskRequest = {
        comment: comment
    };

    fetch('/portal/discipline/' + disciplineId + '/task-list/' + taskId + '/send', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(SendTaskRequest)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
        })
        .then(() => {
            window.location.href = "http://localhost:8080/portal/discipline/" + disciplineId + "/task-list";
        })
        .catch(error => {
            console.error('Произошла ошибка:', error);
        });
}