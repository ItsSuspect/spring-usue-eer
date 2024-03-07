function autoGrow(element) {
    element.style.height = "16px";
    element.style.height = (element.scrollHeight) + "px";
}

function createTask(element) {
    const disciplineId = element.getAttribute('data-disciplineId')
    const name = document.getElementById('name_task').value;
    const score = document.getElementById('score_number').value;

    const dateIssue = document.getElementById('date_issue').value;
    const timeIssue = document.getElementById('time_issue').value;

    const dateDelivery = document.getElementById('date_delivery').value;
    const timeDelivery = document.getElementById('time_delivery').value;

    const instructionTask = document.getElementById('instruction-task').value;

    const TaskRequest = {
        name: name,
        maxScore: score,
        dateIssue: dateIssue,
        timeIssue: timeIssue,
        dateDelivery: dateDelivery,
        timeDelivery: timeDelivery,
        instructionTask: instructionTask
    };

    fetch('/portal/discipline/' + disciplineId + '/task-list/create', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(TaskRequest)
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