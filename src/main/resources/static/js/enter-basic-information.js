function saveMainInformation(element) {
    const disciplineId = element.getAttribute('data-disciplineId')

    const informationOfDiscipline = document.getElementById('information-discipline').value;
    const informationOfTeacher = document.getElementById('information-teacher').value;
    const contacts = document.getElementById('contacts').value;


    const InformationRequest = {
        informationOfDiscipline: informationOfDiscipline,
        informationOfTeacher: informationOfTeacher,
        contacts: contacts
    };

    fetch('/portal/discipline/' + disciplineId + '/information/edit-information', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(InformationRequest)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
        })
        .then(() => {
            window.location.href = "http://localhost:8080/portal/discipline/" + disciplineId + "/information";
        })
        .catch(error => {
            console.error('Произошла ошибка:', error);
        });
}