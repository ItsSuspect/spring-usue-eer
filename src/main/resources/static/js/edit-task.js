function addFile(fileInput) {
    let filesList = document.querySelector('.file-attachment-container__file-list');
    filesList.style.display = 'flex';

    for (let i = 0; i < fileInput.files.length; i++) {
        let file = fileInput.files[i];

        let newFile = document.createElement('span');
        newFile.classList.add('file-attachment-container__file');

        let fileLink = document.createElement('a');
        fileLink.classList.add('file-attachment-container__file-name');
        fileLink.textContent = file.name;
        fileLink.href = window.URL.createObjectURL(file);
        fileLink.download = file.name;
        fileLink.addEventListener('click', function(event) {
            event.stopPropagation();
        });

        let deleteButton = document.createElement('button');
        deleteButton.classList.add('file-attachment-container__delete-button');
        deleteButton.addEventListener('click', function() {
            removeFile(newFile);
        });

        newFile.appendChild(fileLink);
        newFile.appendChild(deleteButton);
        filesList.appendChild(newFile);

        finalFiles.push(file);
    }
}

function removeFile(fileElement) {
    let fileName = fileElement.querySelector('.file-attachment-container__file-name').textContent;

    let fileIndex = finalFiles.findIndex(file => file.name === fileName);
    if (fileIndex !== -1) {
        finalFiles.splice(fileIndex, 1);
    }

    fileElement.remove();

    let filesList = document.querySelector('.file-attachment-container__file-list');
    if (filesList.children.length === 0) {
        filesList.style.display = 'none';
    }
}

function autoGrow(element) {
    element.style.height = "16px";
    element.style.height = (element.scrollHeight) + "px";
}

let finalFiles = [];

function editTask(element) {
    const disciplineId = element.getAttribute('data-disciplineId');
    const taskId = element.getAttribute('data-taskId');

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

    fetch('/portal/discipline/' + disciplineId + '/task-list/' + taskId + "/edit", {
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