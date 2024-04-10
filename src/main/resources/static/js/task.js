function addFile(fileInput) {
    let filesList = document.querySelector('.file-attachment-container__file-list');

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
}

let finalFiles = [];
function sendTask(element) {
    const comment = document.getElementById("comment-to-task").value;
    const disciplineId = element.getAttribute("data-disciplineId");
    const taskId = element.getAttribute("data-taskId");

    let formData = new FormData();
    finalFiles.forEach(function(file) {
        formData.append('files', file);
    });

    formData.append('comment', comment);

    fetch('/portal/discipline/' + disciplineId + '/task-list/' + taskId + '/send', {
        method: 'POST',
        body: formData
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