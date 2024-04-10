document.addEventListener('DOMContentLoaded', function() {
    function renderFiles(files) {
        files.forEach(file => {
            const ulElement = document.getElementById('file-container');

            const folderContentLi = document.createElement('li');
            folderContentLi.classList.add('folder-content__nested-item');

            const contentFile = document.createElement('div');
            contentFile.classList.add('file');
            const aElement = document.createElement('a');
            aElement.href = 'http://localhost:8080/portal/download-file/file-sharing/' + file.id;
            aElement.classList.add('file__name');
            aElement.textContent = file.fileName;
            contentFile.appendChild(aElement);

            const divElementDelete = document.createElement('div');
            divElementDelete.classList.add('file__action-list', 'action-list');
            const deleteButton = document.createElement('button');
            deleteButton.classList.add('action-list__button', 'action-list__button_purpose_delete');
            deleteButton.setAttribute("data-fileId", file.id);
            deleteButton.onclick = function() {
                openDeleteFileWindow(this.getAttribute("data-fileId"));
            };
            divElementDelete.appendChild(deleteButton);
            contentFile.appendChild(divElementDelete);

            const pElement = document.createElement('p');
            pElement.classList.add('file__add-date');
            pElement.textContent = file.dateAdd;
            contentFile.appendChild(pElement);

            folderContentLi.appendChild(contentFile);
            ulElement.appendChild(folderContentLi);
        });
    }

    console.log(files)
    renderFiles(files);
});

document.querySelector('.overlay').addEventListener('click', function(event) {
    if (event.target === this) {
        closePopup();
    }
});

function closePopup() {
    document.querySelectorAll('.popup').forEach(popup => {
        popup.style.display = 'none';
    });
    document.querySelector('.overlay').style.display = 'none';
}

function openAddFileWindow() {
    document.getElementById('addFilePopup').style.display = 'block';
    document.querySelector('.overlay').style.display = 'block';
}

function openDeleteFileWindow(idElement) {
    document.getElementById('deleteFilePopup').style.display = 'block';
    document.querySelector('.overlay').style.display = 'block';

    const buttonSave = document.getElementById('deleteFileButtonConfirm');
    buttonSave.setAttribute("data-fileId", idElement);
}

function saveFile(element) {
    const fileInput = document.querySelector('.add-file__adding-input');
    const file = fileInput.files[0];

    if (!file) {
        console.error('No file selected');
        return;
    }

    const formData = new FormData();
    formData.append('file', file);

    fetch('/portal/discipline/' + disciplineId + '/file-sharing/save-file', {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.text();
        })
        .then(() => {
            window.location.href = "http://localhost:8080/portal/discipline/" + disciplineId + "/file-sharing";
        })
        .catch(error => {
            console.error('Произошла ошибка:', error);
        });
}

function deleteFile(element) {
    const fileId = element.getAttribute('data-fileId');

    const formData = new FormData();
    formData.append('fileId', fileId);

    fetch('/portal/discipline/' + disciplineId + '/file-sharing/delete-file', {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.text();
        })
        .then(() => {
            window.location.href = "http://localhost:8080/portal/discipline/" + disciplineId + "/file-sharing";
        })
        .catch(error => {
            console.error('Произошла ошибка:', error);
        });
}