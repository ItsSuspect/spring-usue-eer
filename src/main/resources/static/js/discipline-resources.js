document.querySelector('.overlay').addEventListener('click', function(event) {
    if (event.target === this) {
        closePopup();
    }
});

document.addEventListener('DOMContentLoaded', function() {
    function renderFolders(parentElement, folders) {
        folders.forEach(folder => {
            const mainFolderElement = document.createElement('div');
            mainFolderElement.classList.add('resources__folder-container', 'folder-container');
            mainFolderElement.setAttribute("data-folderId", folder.id);

            const folderElement = document.createElement('div');
            folderElement.classList.add('folder-container__folder', 'folder');

            const folderNameElement = document.createElement('p');
            folderNameElement.classList.add('folder__name');
            folderNameElement.textContent = folder.folderName;
            if (folder.parentFolder !== null) {
                folderNameElement.onclick = function () {
                    hiddenFolder(folder.id);
                }
            }
            folderElement.appendChild(folderNameElement);

            if (authorities) {
                const actionListElement = document.createElement('div');
                actionListElement.classList.add('folder__action-list', 'action-list');

                const attachFileButton = document.createElement('button');
                attachFileButton.classList.add('action-list__button', 'action-list__button_purpose_attach-file');
                attachFileButton.setAttribute("data-parent-folder", folder.id);
                attachFileButton.onclick = function() {
                    openAddFileWindow(this.getAttribute("data-parent-folder"));
                };
                actionListElement.appendChild(attachFileButton);

                const attachFolderButton = document.createElement('button');
                attachFolderButton.classList.add('action-list__button', 'action-list__button_purpose_attach-folder');
                attachFolderButton.setAttribute("data-parent-folder", folder.id);
                attachFolderButton.onclick = function() {
                    openAddFolderWindow(this.getAttribute("data-parent-folder"));
                };
                actionListElement.appendChild(attachFolderButton);

                if (folder.parentFolder !== null) {
                    const attachDeleteButton = document.createElement('button');
                    attachDeleteButton.classList.add('action-list__button', 'action-list__button_purpose_delete');
                    attachDeleteButton.setAttribute("data-parent-folder", folder.id);
                    attachDeleteButton.onclick = function() {
                        openDeleteFolderWindow(this.getAttribute("data-parent-folder"));
                    };
                    actionListElement.appendChild(attachDeleteButton)
                }
                folderElement.appendChild(actionListElement);
            }

            const addDateElement = document.createElement('p');
            const authorElement = document.createElement('p');
            addDateElement.classList.add('folder__add-date');
            authorElement.classList.add('folder__author');
            if (folder.parentFolder !== null) {
                addDateElement.textContent = folder.dateAdd;
                authorElement.textContent = folder.author;
            }
            folderElement.appendChild(authorElement);
            folderElement.appendChild(addDateElement);

            const folderContentUl = document.createElement('div');
            folderContentUl.classList.add('folder-container__folder-content', 'folder-content');
            if (folder.parentFolder !== null) {
                folderContentUl.style.display = "none";
            }
            const nestedListElement = document.createElement('ul');
            nestedListElement.classList.add('folder-content__nested-list');
            folderContentUl.appendChild(nestedListElement);

            mainFolderElement.appendChild(folderElement);
            mainFolderElement.appendChild(folderContentUl)

            if (folder.parentFolder === null) {
                mainFolderElement.appendChild(folderContentUl);
                parentElement.appendChild(mainFolderElement);
            } else {
                const parentLi = document.querySelector(`[data-folderId="${folder.parentFolder.id}"]`);
                const parentUl = parentLi.querySelector('.folder-content__nested-list');

                const folderContentLi = document.createElement('li');
                folderContentLi.classList.add('folder-content__nested-item');
                folderContentLi.appendChild(mainFolderElement);

                parentUl.appendChild(folderContentLi);
            }
        });
    }

    function renderFiles(files) {
        files.forEach(file => {
            const ulElement = document.querySelector('.resources__folder-container[data-folderId="' + file.folder.id + '"] > .folder-container__folder-content > .folder-content__nested-list');

            const folderContentLi = document.createElement('li');
            folderContentLi.classList.add('folder-content__nested-item');

            const contentFile = document.createElement('div');
            contentFile.classList.add('file');
            const aElement = document.createElement('a');
            aElement.href = 'http://localhost:8080/portal/discipline/' + file.discipline.id +'/resources/download/' + file.id;
            aElement.classList.add('file__name');
            aElement.textContent = file.fileName;
            contentFile.appendChild(aElement);

            if (authorities) {
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
            }

            const pElement = document.createElement('p');
            pElement.classList.add('file__add-date');
            pElement.textContent = file.dateAdd;

            const authorElement = document.createElement('p');
            authorElement.classList.add('file__author');
            authorElement.textContent = file.author;
            contentFile.appendChild(authorElement);
            contentFile.appendChild(pElement);

            folderContentLi.appendChild(contentFile);
            ulElement.appendChild(folderContentLi);
        });
    }

    const mainFolderContainer = document.querySelector('.resources__content');
    renderFolders(mainFolderContainer, folders);
    renderFiles(files);
});

function hiddenFolder(folderId) {
    const $folderContainer = $('.resources__folder-container.folder-container[data-folderId="' + folderId + '"]');

    if ($folderContainer.length > 0) {
        const $folderContent = $folderContainer.children('.folder-container__folder-content.folder-content').first();

        const currentDisplay = $folderContent.css('display');
        $folderContent.css('display', currentDisplay === 'block' ? 'none' : 'block');
    }
}

function openAddFileWindow(parentFolder) {
    document.getElementById('addFilePopup').style.display = 'block';
    document.querySelector('.overlay').style.display = 'block';

    const buttonSave = document.getElementById('saveFileButton');
    buttonSave.setAttribute("data-parent-folder", parentFolder);
}

function openAddFolderWindow(parentFolder) {
    document.getElementById('addFolderPopup').style.display = 'block';
    document.querySelector('.overlay').style.display = 'block';

    const buttonSave = document.getElementById('saveFolderButton');
    buttonSave.setAttribute("data-parent-folder", parentFolder);
}

function openDeleteFileWindow(idElement) {
    document.getElementById('deleteFilePopup').style.display = 'block';
    document.querySelector('.overlay').style.display = 'block';

    const buttonSave = document.getElementById('deleteFileButtonConfirm');
    buttonSave.setAttribute("data-fileId", idElement);
}

function openDeleteFolderWindow(idElement) {
    document.getElementById('deleteFolderPopup').style.display = 'block';
    document.querySelector('.overlay').style.display = 'block';

    const buttonSave = document.getElementById('deleteFolderButtonConfirm');
    buttonSave.setAttribute("data-folderId", idElement);
}

function closePopup() {
    document.querySelectorAll('.popup').forEach(popup => {
        popup.style.display = 'none';
    });
    document.querySelector('.overlay').style.display = 'none';
}

function saveFolder(element) {
    const disciplineId = element.getAttribute('data-disciplineId');
    const parentFolder = element.getAttribute('data-parent-folder');
    const nameFolder = document.getElementById('folder-name').value;

    const FolderRequest = {
        name: nameFolder,
        parentFolder: parentFolder
    };

    fetch('/portal/discipline/' + disciplineId + '/resources/save-folder', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(FolderRequest)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
        })
        .then(() => {
            window.location.href = "http://localhost:8080/portal/discipline/" + disciplineId + "/resources";
        })
        .catch(error => {
            console.error('Произошла ошибка:', error);
        });
}


function saveFile(element) {
    const disciplineId = element.getAttribute('data-disciplineId');
    const parentFolder = element.getAttribute('data-parent-folder');

    const fileInput = document.querySelector('.add-file__adding-input');
    const file = fileInput.files[0];

    if (!file) {
        console.error('No file selected');
        return;
    }

    const formData = new FormData();
    formData.append('file', file);
    formData.append('parentFolder', parentFolder);

    fetch('/portal/discipline/' + disciplineId + '/resources/save-file', {
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
            window.location.href = "http://localhost:8080/portal/discipline/" + disciplineId + "/resources";
        })
        .catch(error => {
            console.error('Произошла ошибка:', error);
        });
}

function deleteFile(element) {
    const fileId = element.getAttribute('data-fileId');
    const disciplineId = element.getAttribute('data-disciplineId');

    const formData = new FormData();
    formData.append('fileId', fileId);

    fetch('/portal/discipline/' + disciplineId + '/resources/delete-file', {
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
            window.location.href = "http://localhost:8080/portal/discipline/" + disciplineId + "/resources";
        })
        .catch(error => {
            console.error('Произошла ошибка:', error);
        });
}

function deleteFolder(element) {
    const folderId = element.getAttribute('data-folderId');
    const disciplineId = element.getAttribute('data-disciplineId');

    const formData = new FormData();
    formData.append('folderId', folderId);

    fetch('/portal/discipline/' + disciplineId + '/resources/delete-folder', {
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
            window.location.href = "http://localhost:8080/portal/discipline/" + disciplineId + "/resources";
        })
        .catch(error => {
            console.error('Произошла ошибка:', error);
        });
}