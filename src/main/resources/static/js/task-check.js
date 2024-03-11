document.addEventListener('DOMContentLoaded', function() {
    const resultScoreInput = document.getElementById('result-score');
    const maxScore = parseInt(document.querySelector('.task-mark__max-mark').textContent);

    resultScoreInput.addEventListener('input', function(event) {
        let value = event.target.value;

        value = value.replace(/\D/g, '');

        if (parseInt(value) > maxScore) {
            value = maxScore.toString();
        }

        event.target.value = value;
    });
});

function saveTaskTeacher(element) {
    const resultScore = document.getElementById('result-score').value;
    const commentTeacher = document.getElementById('comment-teacher').value;

    const disciplineId = element.getAttribute("data-disciplineId");
    const taskId = element.getAttribute("data-taskId");
    const username = element.getAttribute("data-username");

    const SendTaskTeacherRequest = {
        resultScore: resultScore,
        commentTeacher: commentTeacher,
        username: username
    };

    fetch('/portal/discipline/' + disciplineId + '/task-list/' + taskId + '/answer-teacher', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(SendTaskTeacherRequest)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
        })
        .then(() => {
            window.location.href = "http://localhost:8080/portal/discipline/" + disciplineId + "/task-list/" + taskId + "/completed-tasks";
        })
        .catch(error => {
            console.error('Произошла ошибка:', error);
        });
}