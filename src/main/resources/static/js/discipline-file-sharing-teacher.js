document.addEventListener('DOMContentLoaded', function() {
    function renderFolders(parentElement, folders) {
        folders.forEach(folder => {
            const mainFolderElement = document.createElement('div');
            mainFolderElement.classList.add('file-sharing__folder-container', 'folder-container');
            mainFolderElement.setAttribute("data-folderId", folder.id);

            const folderElement = document.createElement('div');
            folderElement.classList.add('folder-container__folder', 'folder');

            const folderNameElement = document.createElement('p');
            folderNameElement.classList.add('folder__name');
            folderNameElement.textContent = folder.user;
            folderNameElement.onclick = function () {
                hiddenFolder(folder.id);
            }
            folderElement.appendChild(folderNameElement);

            const addDateElement = document.createElement('p');
            addDateElement.classList.add('folder__add-date');
            folderElement.appendChild(addDateElement);

            const folderContentUl = document.createElement('div');
            folderContentUl.classList.add('folder-container__folder-content', 'folder-content');
            folderContentUl.style.display = "none";
            const nestedListElement = document.createElement('ul');
            nestedListElement.setAttribute("userId", folder.id);
            nestedListElement.classList.add('folder-content__nested-list');
            folderContentUl.appendChild(nestedListElement);

            mainFolderElement.appendChild(folderElement);
            mainFolderElement.appendChild(folderContentUl)


            mainFolderElement.appendChild(folderContentUl);
            parentElement.appendChild(mainFolderElement);
        });
    }

    function renderFiles(files) {
        files.forEach(file => {
            const ulElement = document.querySelector(`ul[userId="${file.userId}"]`);

            const folderContentLi = document.createElement('li');
            folderContentLi.classList.add('folder-content__nested-item');

            const contentFile = document.createElement('div');
            contentFile.classList.add('file');
            const aElement = document.createElement('a');
            aElement.href = 'http://localhost:8080/portal/download-file/file-sharing/' + file.fileId;
            aElement.classList.add('file__name');
            aElement.textContent = file.fileName;
            contentFile.appendChild(aElement);

            const pElement = document.createElement('p');
            pElement.classList.add('file__add-date');
            pElement.textContent = file.dateAdd;
            contentFile.appendChild(pElement);

            folderContentLi.appendChild(contentFile);
            ulElement.appendChild(folderContentLi);
        });
    }

    const mainFolderContainer = document.querySelector('.file-sharing__content');
    renderFolders(mainFolderContainer, folders);
    renderFiles(files);
});

function hiddenFolder(folderId) {
    const $folderContainer = $('.file-sharing__folder-container.folder-container[data-folderId="' + folderId + '"]');

    if ($folderContainer.length > 0) {
        const $folderContent = $folderContainer.children('.folder-container__folder-content.folder-content').first();

        const currentDisplay = $folderContent.css('display');
        $folderContent.css('display', currentDisplay === 'block' ? 'none' : 'block');
    }
}