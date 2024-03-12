function editAdvertisement(element) {
    const disciplineId = element.getAttribute('data-disciplineId');
    const advertisementId = element.getAttribute('data-advertisementId');

    const name = document.getElementById('name-advertisement').value;
    const content = document.getElementById('text-advertisement').value;

    const AdvertisementRequest = {
        name: name,
        content: content
    };

    fetch('/portal/discipline/' + disciplineId + '/advertisements/' + advertisementId + '/edit-advertisement', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(AdvertisementRequest)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
        })
        .then(() => {
            window.location.href = "http://localhost:8080/portal/discipline/" + disciplineId + "/advertisements";
        })
        .catch(error => {
            console.error('Произошла ошибка:', error);
        });
}