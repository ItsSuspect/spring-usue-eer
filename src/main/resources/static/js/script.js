function disciplineSelection(element) {
  const selectedDisciplineListItem = $(element).parent();
  const selectedDisciplineNav = $(element).next('.discipline-list__tab-navigation');

  if (selectedDisciplineNav.is(':hidden')) {
    $('.discipline-list__item_selection_selected').not(selectedDisciplineListItem).removeClass('discipline-list__item_selection_selected').addClass('discipline-list__item_selection_unselected');
    $('.discipline-list__tab-navigation').not(selectedDisciplineNav).hide();
    selectedDisciplineListItem.removeClass('discipline-list__item_selection_unselected').addClass('discipline-list__item_selection_selected');
    selectedDisciplineNav.toggle();
  }
}