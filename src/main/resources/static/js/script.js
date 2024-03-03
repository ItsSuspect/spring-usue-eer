$(document).ready(function() {
  let currentPageUrl = window.location.href;
  let parts = currentPageUrl.split('/');

  let disciplineId = parts[parts.length - 2];
  let category = parts[parts.length - 1];

  let liElement = $('li[data-discipline-id="' + disciplineId + '"]');
  liElement.removeClass('discipline-list__item_selection_unselected').addClass('discipline-list__item_selection_selected');

  let navElement = $('nav[data-discipline-nav="' + disciplineId + '"]');
  navElement.toggle();

  let aElement = navElement.find('a[data-discipline-category="' + category + '"]');
  aElement.removeClass('tab-navigation__link tab-navigation__link_selection_unselected').addClass('tab-navigation__link tab-navigation__link_selection_selected');
});

function disciplineSelection(element) {
  window.location.href = 'http://localhost:8080/portal/discipline/' + element.getAttribute("data-discipline-id") + '/information';
}