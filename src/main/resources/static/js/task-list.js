document.addEventListener('DOMContentLoaded', function() {
    const elements = document.querySelectorAll('.status-list__task-status');

    elements.forEach(function(element) {
        const status = element.textContent.trim();

        switch (status) {
            case 'Не началось':
                element.classList.add('status-list__task-status_state_pending');
                break;
            case 'В процессе':
                element.classList.add('status-list__task-status_state_in-progress');
                break;
            case 'Сдано':
                element.classList.add('status-list__task-status_state_delivered');
                break;
            case 'Проверено':
                element.classList.add('status-list__task-status_state_checked');
                break;
            case 'Завершено':
                element.classList.add('status-list__task-status_state_completed');
                break;
            default:
                break;
        }
    });
});
