document.addEventListener('DOMContentLoaded', function() {
    const resultDiscipline = document.getElementById('result-discipline');

    resultDiscipline.addEventListener('change', function(event) {
        if (event.target.classList.contains('member-list__group-checkbox')) {
            const memberCheckboxes = event.target.parentNode.nextElementSibling.querySelectorAll('.member__member-checkbox');

            memberCheckboxes.forEach(checkbox => {
                checkbox.checked = event.target.checked;
            });
        }
    });
});

function deleteSelected() {
    const resultDiscipline = document.getElementById('result-discipline');

    resultDiscipline.querySelectorAll('.member__member-checkbox:checked').forEach(checkbox => {
        checkbox.closest('.member-list__member').remove();
    });

    resultDiscipline.querySelectorAll('.member-list__group-checkbox:checked').forEach(checkbox => {
        const groupingBlock = checkbox.closest('.member-list__grouping-block');
        groupingBlock.remove();
    });

    resultDiscipline.querySelectorAll('.member-list__group-members').forEach(ul => {
        if (ul.children.length === 0) {
            const groupingBlock = ul.closest('.member-list__grouping-block');
            groupingBlock.remove();
        }
    });
}

function deleteAll() {
    const resultDiscipline = document.getElementById('result-discipline');

    const deleteBlocks = resultDiscipline.querySelectorAll('.member-list__grouping-block');
    deleteBlocks.forEach(block => {
        block.remove();
    });
}

function searchGroups(query) {
    const resultsGroupList = document.getElementById('group-results');
    resultsGroupList.innerHTML = '';

    if (query.length >= 2) {
        const filteredGroups = groups.filter(group => {
            return group.name.toLowerCase().includes(query.toLowerCase());
        });

        if (filteredGroups.length === 0) {
            const listItem = document.createElement('li');
            listItem.classList.add('group-search__result');
            listItem.textContent = 'Данной группы не существует';
            resultsGroupList.appendChild(listItem);
        } else {
            filteredGroups.forEach(group => {
                const listItem = document.createElement('li');
                listItem.classList.add('group-search__result');
                listItem.innerHTML = `
                    <p class="group-search__group-name">${group.name}</p>
                `;
                listItem.addEventListener('click', function() {
                    addGroupToList(group);
                });
                listItem.style.cursor = 'pointer';
                resultsGroupList.appendChild(listItem);
            });
        }

        resultsGroupList.style.display = 'block';
    } else {
        resultsGroupList.style.display = 'none';
    }
}

function searchUsers(query) {
    const resultsUserList = document.getElementById('user-results');
    resultsUserList.innerHTML = '';

    if (query.length >= 2) {
        const filteredUsers = users.filter(user => {
            const fullName = `${user.surname} ${user.name} ${user.middleName}`;
            return fullName.toLowerCase().includes(query.toLowerCase());
        });

        if (filteredUsers.length === 0) {
            const listItem = document.createElement('li');
            listItem.classList.add('person-search__result');
            listItem.textContent = 'Данного пользователя не существует';
            resultsUserList.appendChild(listItem);
        } else {
            filteredUsers.forEach(user => {
                const fullName = `${user.surname} ${user.name} ${user.middleName}`;
                const listItem = document.createElement('li');
                listItem.classList.add('person-search__result');
                listItem.innerHTML = `
                    <p class="person-search__personal-info">${fullName}</p>
                `;
                listItem.addEventListener('click', function() {
                    addUserToList(user);
                });
                listItem.style.cursor = 'pointer';
                resultsUserList.appendChild(listItem);
            });
        }

        resultsUserList.style.display = 'block';
    } else {
        resultsUserList.style.display = 'none';
    }
}

function addUserToList(user) {
    const resultDiscipline = document.getElementById('result-discipline');

    let existingGroup = Array.from(resultDiscipline.querySelectorAll('.member-list__group-name')).find(group => group.textContent.trim() === user.groups[0].name);

    if (existingGroup) {
        const groupingBlock = existingGroup.closest('.member-list__grouping-block');
        const ulItem = groupingBlock.querySelector('.member-list__group-members');
        const isUserExist = Array.from(ulItem.querySelectorAll('.member__personal-info')).some(info => info.dataset.userUsername === user.username);

        if (!isUserExist) {
            const liElement = document.createElement('li');
            liElement.classList.add('member-list__member', 'member');
            liElement.innerHTML = `
                <p class="member__personal-info" data-user-username="${user.username}">${user.surname} ${user.name} ${user.middleName}</p>
                <input class="member__member-checkbox checkbox" type="checkbox">
            `;
            ulItem.appendChild(liElement);
        }
    } else {
        const groupingBlock = document.createElement('div');
        groupingBlock.classList.add('member-list__grouping-block');

        const divItem = document.createElement('div');
        divItem.classList.add('member-list__grouping-header');
        divItem.innerHTML = `
            <p class="member-list__group-name">${user.groups[0].name}</p>
            <input class="member-list__group-checkbox checkbox" type="checkbox">
        `;

        const ulItem = document.createElement('ul');
        ulItem.classList.add('member-list__group-members');

        const liElement = document.createElement('li');
        liElement.classList.add('member-list__member', 'member');
        liElement.innerHTML = `
            <p class="member__personal-info" data-user-username="${user.username}">${user.surname} ${user.name} ${user.middleName}</p>
            <input class="member__member-checkbox checkbox" type="checkbox">
        `;
        ulItem.appendChild(liElement);

        groupingBlock.appendChild(divItem);
        groupingBlock.appendChild(ulItem);
        resultDiscipline.appendChild(groupingBlock);
    }
}

function addGroupToList(group) {
    fetch('/portal/disciplines/create-getUserGroup', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(group)
    })
        .then(response => response.json())
        .then(data => {
            const resultDiscipline = document.getElementById('result-discipline');
            let existingGroup = Array.from(resultDiscipline.querySelectorAll('.member-list__group-name')).find(groupElement => groupElement.textContent.trim() === group.name);

            if (existingGroup) {
                const ulItem = existingGroup.parentNode.nextElementSibling.querySelector('.member-list__group-members');
                const existingUsernames = Array.from(ulItem.querySelectorAll('.member__personal-info')).map(info => info.dataset.userUsername);
                const missingUsers = data.filter(user => !existingUsernames.includes(user.username));

                if (missingUsers.length > 0) {
                    missingUsers.forEach(user => {
                        const liElement = document.createElement('li');
                        liElement.classList.add('member-list__member', 'member');
                        liElement.innerHTML = `
                            <p class="member__personal-info" data-user-username="${user.username}">${user.surname} ${user.name} ${user.middleName}</p>
                            <input class="member__member-checkbox checkbox" type="checkbox">
                        `;
                        ulItem.appendChild(liElement);
                    });
                }
            } else {
                const groupingBlock = document.createElement('div');
                groupingBlock.classList.add('member-list__grouping-block');

                const divItem = document.createElement('div');
                divItem.classList.add('member-list__grouping-header');
                divItem.innerHTML = `
                    <p class="member-list__group-name">${group.name}</p>
                    <input class="member-list__group-checkbox checkbox" type="checkbox">
                `;

                const ulItem = document.createElement('ul');
                ulItem.classList.add('member-list__group-members');
                data.forEach(user => {
                    const liElement = document.createElement('li');
                    liElement.classList.add('member-list__member', 'member');
                    liElement.innerHTML = `
                        <p class="member__personal-info" data-user-username="${user.username}">${user.surname} ${user.name} ${user.middleName}</p>
                        <input class="member__member-checkbox checkbox" type="checkbox">
                    `;
                    ulItem.appendChild(liElement);
                });

                groupingBlock.appendChild(divItem);
                groupingBlock.appendChild(ulItem);
                resultDiscipline.appendChild(groupingBlock);
            }
        })
        .catch(error => {
            console.error('Произошла ошибка:', error);
        });
}

function createDiscipline() {
    const resultDiscipline = document.getElementById('result-discipline');
    const disciplineName = document.getElementById('discipline-name').value;

    const userList = resultDiscipline.querySelectorAll('.member__personal-info');
    const users = Array.from(userList).map(userElement => userElement.getAttribute('data-user-username'));

    const data = {
        name: disciplineName,
        users: users
    };
    console.log(data)

    fetch('/portal/disciplines/create', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => response.json())
        .then(data => {
            console.log('Ответ от сервера:', data);
        })
        .catch(error => {
            console.error('Произошла ошибка:', error);
        });
}