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

function openWindow(element) {
    document.getElementById('popup-main').style.display = 'block';
    document.querySelector('.overlay').style.display = 'block';

    const buttonSave = document.getElementById('confirm_button');
    buttonSave.setAttribute("data-disciplineId", element.getAttribute("data-disciplineId"));
}

function toggleDisciplineList(button) {
    const toggleButton = $(button);
    const header = toggleButton.parent();
    const disciplinesBlock = header.parent();
    const disciplineList = disciplinesBlock.find('.available-disciplines');

    toggleButton.toggleClass('discipline-block__toggle-list_state_unfolded').toggleClass('discipline-block__toggle-list_state_folded');

    if (toggleButton.hasClass('discipline-block__toggle-list_state_folded')) {
        disciplineList.hide();
    } else {
        disciplineList.show();
    }
}

function toggleYourDisciplineList(button) {
    const toggleButton = $(button);
    const header = toggleButton.parent();
    const disciplinesBlock = header.parent();
    const disciplineList = disciplinesBlock.find('.user-disciplines');

    toggleButton.toggleClass('discipline-block__toggle-list_state_unfolded').toggleClass('discipline-block__toggle-list_state_folded');

    if (toggleButton.hasClass('discipline-block__toggle-list_state_folded')) {
        disciplineList.hide();
    } else {
        disciplineList.show();
    }
}

function confirmRequest(element) {
    const disciplineId = element.getAttribute("data-disciplineId");

    fetch('/portal/disciplines/confirm-request/' + disciplineId, {
        method: 'POST'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
        })
        .then(() => {
            closePopup();
        })
        .catch(() => {
            closePopup();
        });
}