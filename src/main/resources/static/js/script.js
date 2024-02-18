function changeState(target, state1, state2) {
  const element = document.getElementById(target);
  
  if (element.classList.contains(state1)) {
    element.classList.remove(state1);
    element.classList.add(state2);
  } else {
    element.classList.remove(state2);
    element.classList.add(state1);
  }
}