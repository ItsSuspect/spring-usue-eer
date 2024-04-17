$(document).ready(function(){
    $('.discipline-rating__progressbar').each(function() {
        let progressClasses = $(this).attr('class').split(' ');
        let value = $(this).val();
        if (value < 50) {
            $(this).addClass(progressClasses + '_rating_very-low');
        } else if (value < 75) {
            $(this).addClass(progressClasses + '_rating_low');
        } else if (value < 90) {
            $(this).addClass(progressClasses + '_rating_medium');
        } else {
            $(this).addClass(progressClasses + '_rating_high');
        }
    });
});

function toggleTaskList(toggleButton) {
    let parent = $(toggleButton).parent();
    let taskList = parent.siblings('.discipline-rating__task-list');

    if (taskList.css('display') === 'none') {
        taskList.css('display', 'flex');
        $(toggleButton).toggleClass('discipline-rating__toggle-list_unfolded');
    } else {
        taskList.css('display', 'none');
        $(toggleButton).toggleClass('discipline-rating__toggle-list_unfolded');
    }
}