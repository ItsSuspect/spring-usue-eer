function editAdvertisement(element) {
    const disciplineId = element.getAttribute('data-disciplineId');
    const announcementId = element.getAttribute('data-announcementId');

    const name = document.getElementById('name-announcement').value;
    const content = document.getElementById('text-announcement').value;

    const AnnouncementRequest = {
        name: name,
        content: content
    };

    fetch('/portal/discipline/' + disciplineId + '/announcements/' + announcementId + '/edit-announcement', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(AnnouncementRequest)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
        })
        .then(() => {
            window.location.href = "http://localhost:8080/portal/discipline/" + disciplineId + "/announcements";
        })
        .catch(error => {
            console.error('Произошла ошибка:', error);
        });
}