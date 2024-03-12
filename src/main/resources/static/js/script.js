$(document).ready(function() {
  let currentPageUrl = window.location.href;
  let match = currentPageUrl.match(/\/discipline\/(\d+)\/([^/]+)/);

  if (match) {
    let disciplineId = match[1];
    let category = match[2];

    // Применяем классы к элементам навигации
    let liElement = $('li[data-discipline-id="' + disciplineId + '"]');
    liElement.removeClass('discipline-list__item_selection_unselected').addClass('discipline-list__item_selection_selected');

    let navElement = $('nav[data-discipline-nav="' + disciplineId + '"]');
    navElement.toggle();

    let aElement = navElement.find('a[data-discipline-category="' + category + '"]');
    aElement.removeClass('tab-navigation__link tab-navigation__link_selection_unselected').addClass('tab-navigation__link tab-navigation__link_selection_selected');
  } else {
    switch (true) {
      case currentPageUrl.includes("/portal/calendar"):
        selectedNavigation("Календарь");
        break;
      case currentPageUrl.includes("/portal/resources"):
        selectedNavigation("Ресурсы");
        break;
      case currentPageUrl.includes("/portal/disciplines"):
        selectedNavigation("Дисциплины");
        break;
      case currentPageUrl.includes("/portal/details"):
        selectedNavigation("Сведения");
        break;
      default:
        break;
    }
  }
});

function selectedNavigation(name) {
  let navigationElement = $('nav.header__navigation.navigation');
  let aElement = navigationElement.find('a').filter(function() {
    return $(this).text().trim() === name;
  });
  aElement.removeClass('navigation__link navigation__link_selection_unselected').addClass('navigation__link navigation__link_selection_selected');
}

function disciplineSelection(element) {
  window.location.href = 'http://localhost:8080/portal/discipline/' + element.getAttribute("data-discipline-id") + '/information';
}

function autoGrow(element) {
  element.style.height = "16px";
  element.style.height = (element.scrollHeight) + "px";
}

function changeTheme(themeChanger) {
  $(themeChanger).toggleClass('theme-changer_light').toggleClass('theme-changer_dark');
}

function displayNotificationBlock() {
  $('.notification-block').toggle();
}

function displayProfileFunctions() {
  $('.profile__functions-block').toggle();
}

$(document).mouseup(function (e) {
  const notificationCenter = $('.notification-center');
  const notificationBlock = $(".notification-block");
  const profile = $('.profile');
  const profileFunctions = $(".profile__functions-block");

  if (notificationCenter.has(e.target).length === 0){
    notificationBlock.hide();
  }

  if (profile.has(e.target).length === 0){
    profileFunctions.hide();
  }
});

function displayDeleteButton(notification) {
  const notificationHeader = notification.querySelector('.notification-block__header');
  const deleteButton = notificationHeader.querySelector('.notification-block__delete-button');
  deleteButton.style.visibility = 'visible';
}

function hideDeleteButton(notification) {
  const notificationHeader = notification.querySelector('.notification-block__header');
  const deleteButton = notificationHeader.querySelector('.notification-block__delete-button');
  deleteButton.style.visibility = 'hidden';
}

function deleteNotification(element) {
  const parent = element.parentElement.parentElement;
  parent.remove();
}

function clearNotificationCenter() {
  $('.notification-block__notification').remove();
}

function addEditButtonText(editButton) {
  editButton.style.maxWidth = '112px';
  editButton.style.padding = '2px 8px 2px 20px';
  editButton.style.color = '#444';
}

function removeEditButtonText(editButton) {
  editButton.style.maxWidth = '16px';
  editButton.style.padding = '2px 0 2px 16px';
  editButton.style.color = '#f4f4f4';
}

function addEditButtonTextTwo(editButton) {
  editButton.style.maxWidth = '160px';
  editButton.style.padding = '4px 12px 4px 28px';
  editButton.style.color = '#444';
}

function removeEditButtonTextTwo(editButton) {
  editButton.style.maxWidth = '24px';
  editButton.style.padding = '4px 0 4px 24px';
  editButton.style.color = '#f4f4f4';
}

function expandButtonText(button) {
  button.style.maxWidth = '112px';
  button.style.padding = '4px 8px 4px 28px';
  button.style.color = '#444';
}

function hideButtonText(button) {
  button.style.maxWidth = '24px';
  button.style.padding = '4px 0 4px 24px';
  button.style.color = '#eee';
}