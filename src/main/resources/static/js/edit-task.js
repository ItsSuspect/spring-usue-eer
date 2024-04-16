document.addEventListener('DOMContentLoaded', function() {
    const scoreInput = document.getElementById('score_number');

    scoreInput.addEventListener('input', function(event) {
        let value = event.target.value;
        value = value.replace(/\D/g, '');
        event.target.value = value;
    });
});

$(document).ready(function() {
    $('.expand-textarea').each(function() {
        if ($(this).val().trim() !== '') {
            autoGrow(this);
        } else {
            $(this).next().css('margin-top', '0');
            $(this).hide();
        }
    });
});

function autoGrow(element) {
    element.style.height = "16px";
    element.style.height = (element.scrollHeight) + "px";
}

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

    let formData = new FormData();
    finalFiles.forEach(function(file) {
        formData.append('files', file);
    });

    formData.append('name', name);
    formData.append('maxScore', score);
    formData.append('dateIssue', dateIssue);
    formData.append('timeIssue', timeIssue);
    formData.append('dateDelivery', dateDelivery);
    formData.append('timeDelivery', timeDelivery);
    formData.append('instructionTask', instructionTask);

    fetch('/portal/discipline/' + disciplineId + '/task-list/' + taskId + "/edit", {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
        })
        .then(() => {
            window.location.href = "http://localhost:8080/portal/discipline/" + disciplineId + "/task-list/" + taskId;
        })
        .catch(error => {
            console.error('Произошла ошибка:', error);
        });
}