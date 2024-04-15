// Определение конструктора UserCalendar, который создает календарь для указанного месяца и года
function UserCalendar(month, year) {
    // Массив с названиями месяцев
    const months = ["Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"];
    // Получение количества дней в указанном месяце
    const monthDays = new Date(year, month + 1, 0).getDate();
    // Определение смещения окончания месяца относительно дня недели
    const monthEndOffset = new Date(year, month, monthDays).getDay() || 7;
    // Получение текущей даты
    const currentDay = new Date();
    // Инициализация строки для хранения HTML-разметки календаря
    let calendar = '<div class="calendar__weekdays"><span class="calendar__weekday">Пн</span><span class="calendar__weekday">Вт</span><span class="calendar__weekday">Ср</span><span class="calendar__weekday">Чт</span><span class="calendar__weekday">Пт</span><span class="calendar__weekday">Сб</span><span class="calendar__weekday">Вс</span></div><div class="calendar__week">';

    // Определение дней предыдущего месяца
    const prevMonthDays = new Date(year, month, 0).getDate();
    // Определение смещения окончания предыдущего месяца относительно дня недели
    const prevMonthEndOffset = new Date(year, month - 1, prevMonthDays).getDay();

    // Создание HTML-разметки для дней предыдущего месяца
    for (let i = prevMonthDays + 1 - prevMonthEndOffset; i <= prevMonthDays; i++) {
        const weekDay = new Date(year, month - 1, i).getDay();
        const weekendClass = (weekDay === 6) ? ' calendar__date_weekend' : '';
        const currentDate = month > 0 ? `${i}.${month}.${year}` : `${i}.${12}.${year-1}`;
        const hasTasksClass = tasks.some(task => task.dateTask === currentDate) ? ' calendar__date_has-tasks' : '';
        calendar += `<span class="calendar__date calendar__date_secondary${weekendClass}${hasTasksClass}" data-date="${currentDate}"><p class="calendar__date-day">${i}</p></span>`;
    }

    // Создание HTML-разметки для дней текущего месяца
    for (let i = 1; i <= monthDays; i++) {
        const weekDay = new Date(year, month, i).getDay();
        const weekendClass = (weekDay === 6 || weekDay === 0) ? ' calendar__date_weekend' : '';
        const currentDate = `${i}.${month + 1}.${year}`;
        const hasTasksClass = tasks.some(task => task.dateTask === currentDate) ? ' calendar__date_has-tasks' : '';
        calendar += `<span class="calendar__date${weekendClass}${hasTasksClass}" data-date="${currentDate}""><p class="calendar__date-day">${i}</p></span>`;
        // Закрытие и открытие новой недели, если текущий день - воскресенье
        if (weekDay === 0 && i !== monthDays) calendar += '</div><div class="calendar__week">';
        else if (weekDay === 0) calendar += '</div>';
    }

    // Создание HTML-разметки для дней следующего месяца, необходимых для заполнения календаря
    for (let i = 1; i <= 7 - monthEndOffset; i++) {
        const weekDay = new Date(year, month + 1, i).getDay();
        const weekendClass = (weekDay === 6 || weekDay === 0) ? ' calendar__date_weekend' : '';
        const currentDate = month < 11 ? `${i}.${month + 2}.${year}` : `${i}.${1}.${year+1}`;
        const hasTasksClass = tasks.some(task => task.dateTask === currentDate) ? ' calendar__date_has-tasks' : '';
        calendar += `<span class="calendar__date calendar__date_secondary${weekendClass}${hasTasksClass}" data-date="${currentDate}""><p class="calendar__date-day">${i}</p></span>`;
    }

    // Вставка HTML-разметки календаря в соответствующий контейнер
    $('#calendar .calendar__calendar-content').html(calendar);
    // Обновление заголовка календаря с текущим месяцем и годом
    $('#calendar .calendar__current-month').html(`${months[month]} ${year}`).data('month', month).data('year', year);
    // Добавление класса к текущей дате
    $('[data-date="' + currentDay.getDate() + '.' + (currentDay.getMonth() + 1) + '.' + currentDay.getFullYear() + '"]').addClass('calendar__date_today');
}

// Вызов функции UserCalendar при загрузке документа
$(document).ready(function() { UserCalendar(new Date().getMonth(), new Date().getFullYear()) });

// Функция для отображения предыдущего месяца
function prevMonth() {
    // Получение текущего месяца и года
    const $currentMonthElement = $('#calendar .calendar__current-month');
    const currentMonth = parseInt($currentMonthElement.data('month'));
    const currentYear = parseInt($currentMonthElement.data('year'));
    // Определение предыдущего месяца и года
    const prevMonth = (currentMonth === 0) ? 11 : currentMonth - 1;
    const prevYear = (currentMonth === 0) ? currentYear - 1 : currentYear;

    // Вызов функции UserCalendar с предыдущим месяцем и годом
    UserCalendar(prevMonth, prevYear);
}

// Функция для отображения следующего месяца
function nextMonth() {
    // Получение текущего месяца и года
    const $currentMonthElement = $('#calendar .calendar__current-month');
    const currentMonth = parseInt($currentMonthElement.data('month'));
    const currentYear = parseInt($currentMonthElement.data('year'));
    // Определение следующего месяца и года
    const nextMonth = (currentMonth === 11) ? 0 : currentMonth + 1;
    const nextYear = (currentMonth === 11) ? currentYear + 1 : currentYear;

    // Вызов функции UserCalendar со следующим месяцем и годом
    UserCalendar(nextMonth, nextYear);
}

function formatDate(dateString) {
    const parts = dateString.split('.');
    const day = parseInt(parts[0]);
    const month = parseInt(parts[1]) - 1;

    const months = ["Января", "Февраля", "Марта", "Апреля", "Мая", "Июня", "Июля", "Августа", "Сентября", "Октября", "Ноября", "Декабря"];

    const monthName = months[month];
    return `${day} ${monthName}`;
}

$(document).on('click', '.calendar__date', function() {
    const date = $(this); // Получаем элемент, на котором произошел клик

    if (!$(date).hasClass('calendar__date_selected')) {
        $('.calendar__date_selected').not(date).removeClass('calendar__date_selected');
        $(date).addClass('calendar__date_selected');

        const selectedDate = $(date).data('date'); // Получаем дату из атрибута data-date

        const tasksForSelectedDate = tasks.filter(task => task.dateTask === selectedDate);

        const taskList = $('#main');
        taskList.empty();
        taskList.append('<p class="task-list__selected-date"></p>');
        const selectedDateElement = taskList.find('.task-list__selected-date');

        if (tasksForSelectedDate.length > 0) {
            selectedDateElement.text(formatDate(selectedDate));

            tasksForSelectedDate.forEach(task => {
                const taskElement = `
                    <div class="task-list__task">
                        <div class="task-list__task-information">
                            <p class="task-list__discipline">${task.disciplineName}</p>
                            <a href="http://localhost:8080/portal/discipline/${task.disciplineId}/task-list/${task.taskId}" class="task-list__task-name">${task.taskName}</a>
                        </div>
                        <p class="task-list__delivery-time">${task.timeTask}</p>
                    </div>
                `;
                taskList.append(taskElement);
            });
        } else {
            selectedDateElement.text(formatDate(selectedDate));
            taskList.append('<div class="no-tasks task-list__no-tasks"><p class="no-tasks__text">Нет заданий</p></div>');
        }
    }
});