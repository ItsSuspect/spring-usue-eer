function toggleAccessButton(button) {
    let $accessButton = $(button);
    if ($accessButton.text() === "Участник") {
        $accessButton.text("Руководитель");
    } else $accessButton.text("Участник");
}

function getUserAccess(accessText) {
    if (accessText === 'Участник') {
        return 'PARTICIPANT';
    } else if (accessText === 'Руководитель') {
        return 'LEADER';
    } else {
        return 'PARTICIPANT';
    }
}

function acceptUser(element) {
    const disciplineId = element.getAttribute("data-disciplineId");
    const userId = element.getAttribute("data-userId");

    const buttonText = element.closest('.request-list__member').querySelector('.member__member-access').innerText;
    const formData = new FormData();
    formData.append('accessType', getUserAccess(buttonText));

    fetch('/portal/discipline/' + disciplineId + '/member-list/requests/acceptUser/' + userId, {
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
            const divElement = element.parentElement;
            const liElement = divElement.parentElement;

            const ulElement = liElement.parentElement;
            liElement.remove();

            if (ulElement.children.length === 0) {
                const groupingBlock = ulElement.parentElement;
                groupingBlock.remove();
            }

            const groupingBlocks = document.querySelectorAll('.request-list__grouping-block');

            if (groupingBlocks.length === 0) {
                const requestList = document.querySelector('.request-block__request-list');
                requestList.innerHTML = '';

                const noRequestsDiv = document.createElement('div');
                noRequestsDiv.classList.add('no-requests', 'request-list__no-requests');
                noRequestsDiv.innerHTML = '<p class="no-requests__text">На данный момент заявки отсутствуют</p>';
                requestList.appendChild(noRequestsDiv);
            }
        })
        .catch(error => {
            console.error('Произошла ошибка:', error);
        });
}

function declineUser(element) {
    const disciplineId = element.getAttribute("data-disciplineId");
    const userId = element.getAttribute("data-userId");

    fetch('/portal/discipline/' + disciplineId + '/member-list/requests/declineUser/' + userId, {
        method: 'POST'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.text();
        })
        .then(() => {
            const divElement = element.parentElement;
            const liElement = divElement.parentElement;

            const ulElement = liElement.parentElement;
            liElement.remove();

            if (ulElement.children.length === 0) {
                const groupingBlock = ulElement.parentElement;
                groupingBlock.remove();
            }

            const groupingBlocks = document.querySelectorAll('.request-list__grouping-block');

            if (groupingBlocks.length === 0) {
                const requestList = document.querySelector('.request-block__request-list');
                requestList.innerHTML = '';

                const noRequestsDiv = document.createElement('div');
                noRequestsDiv.classList.add('no-requests', 'request-list__no-requests');
                noRequestsDiv.innerHTML = '<p class="no-requests__text">На данный момент заявки отсутствуют</p>';
                requestList.appendChild(noRequestsDiv);
            }
        })
        .catch(error => {
            console.error('Произошла ошибка:', error);
        });
}