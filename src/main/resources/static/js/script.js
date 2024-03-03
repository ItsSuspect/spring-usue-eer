let selectedDiscipline = localStorage.getItem('selectedDiscipline');
let selectedCategory = localStorage.getItem('selectedCategory');

function updateSelectedData(discipline, category) {
  selectedDiscipline = discipline;
  selectedCategory = category;
  localStorage.setItem('selectedDiscipline', discipline);
  localStorage.setItem('selectedCategory', category);
}

$(document).ready(function() {
  let selectedDiscipline = localStorage.getItem('selectedDiscipline');

  if (selectedDiscipline) {
    let liElement = $('li[data-discipline-name="' + selectedDiscipline + '"]');
    liElement.removeClass('discipline-list__item_selection_unselected').addClass('discipline-list__item_selection_selected');

    let navElement = $('nav[data-discipline-nav="' + selectedDiscipline + '"]');
    navElement.toggle();
  }
});


// Функция выбора дисциплины
function disciplineSelection(element) {
  const selectedDisciplineListItem = $(element).closest('li');
  const selectedDisciplineNav = $(element).next('.discipline-list__tab-navigation');

  selectedDiscipline = selectedDisciplineListItem.data('discipline-name');
  updateSelectedData(selectedDiscipline, selectedCategory);

  if (selectedDisciplineNav.is(':hidden')) {
    $('.discipline-list__item_selection_selected').not(selectedDisciplineListItem).removeClass('discipline-list__item_selection_selected').addClass('discipline-list__item_selection_unselected');
    $('.discipline-list__tab-navigation').not(selectedDisciplineNav).hide();
    selectedDisciplineListItem.removeClass('discipline-list__item_selection_unselected').addClass('discipline-list__item_selection_selected');
    selectedDisciplineNav.toggle();
  }
}
