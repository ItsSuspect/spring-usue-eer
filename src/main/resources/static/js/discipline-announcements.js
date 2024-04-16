$(document).ready(function() {
    $('.expand-textarea').each(function() {
        if ($(this).val().trim() !== '') {
            autoGrow(this);
        } else {
            $(this).hide();
        }
    });
});