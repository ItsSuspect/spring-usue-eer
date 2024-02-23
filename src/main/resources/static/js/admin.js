function checkOptionalInput() {
    const selectElement = document.getElementById('roles-list');
    const selectedValue = selectElement.options[selectElement.selectedIndex].value;

    const optionalInput = document.getElementById('group-input');

    if (selectedValue === 'ROLE_STUDENT') {
        optionalInput.style.display = 'block';
    } else {
        optionalInput.style.display = 'none';
    }
}

function generateUsername() { //Todo: если существует пользователь с данным логином, то необходимо добавлять единицу, две и т.п. (s.a.sidorov1)
    let name = document.getElementById('name-input').value.trim();
    let surname = document.getElementById('surname-input').value.trim();
    let middleName = document.getElementById('middleName-input').value.trim();

    //Todo: возможно стоит переработать данное условие
    if (name === '' || surname === '' || middleName === '') {
        document.getElementById('username-input').value = '';
        return;
    }

    name = transliterate(name.toLowerCase());
    surname = transliterate(surname.toLowerCase());
    middleName = transliterate(middleName.toLowerCase());

    document.getElementById('username-input').value = name.charAt(0) + '.' + middleName.charAt(0) + '.' + surname;
}

function transliterate(text) {
    var trans = {
        'а': 'a', 'б': 'b', 'в': 'v', 'г': 'g', 'д': 'd', 'е': 'e', 'ё': 'e', 'ж': 'zh', 'з': 'z', 'и': 'i', 'й': 'y', 'к': 'k', 'л': 'l', 'м': 'm', 'н': 'n', 'о': 'o', 'п': 'p', 'р': 'r', 'с': 's', 'т': 't', 'у': 'u', 'ф': 'f', 'х': 'kh', 'ц': 'ts', 'ч': 'ch', 'ш': 'sh', 'щ': 'shch', 'ъ': '', 'ы': 'y', 'ь': '', 'э': 'e', 'ю': 'yu', 'я': 'ya',
    };

    return text.split('').map(function (char) {
        return trans[char] || char;
    }).join('');
}

function createNewUser() {
    const username = document.getElementById("username-input").value;
    const password = document.getElementById("password-input").value;

    const role = document.getElementById("roles-list").value;
    const email = document.getElementById("email-input").value;
    const name = document.getElementById("name-input").value;
    const surname = document.getElementById("surname-input").value;
    const middleName = document.getElementById("middleName-input").value;
    const group = document.getElementById("group-input").value;

    const signUpRequest = {
        "username": username,
        "password": password,

        "role": role,
        "email": email,
        "name": name,
        "surname": surname,
        "middleName": middleName
    };

    if (role === "ROLE_STUDENT") {
        signUpRequest.group = group;
    }

    console.log(signUpRequest)

    fetch("/admin/create-user", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(signUpRequest)
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(errorData => {
                    throw new Error(`Ошибка: ${errorData.message}`);
                });
            }
            return response.json();
        })
        .then(data => {
            document.getElementById("error-message").style.display = "none";
            document.getElementById("success-message").textContent = "Успешно: " + data.message;
            document.getElementById("success-message").style.display = "flex";
        })
        .catch(error => {
            document.getElementById("success-message").style.display = "none";
            document.getElementById("error-message").textContent = error.message;
            document.getElementById("error-message").style.display = "flex";
        });
}

function createNewGroup() {
    const name = document.getElementById("groupName-input").value;

    const groupRequest = {
        "name": name,
    };

    fetch("/admin/create-group", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(groupRequest)
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(errorData => {
                    throw new Error(`Ошибка: ${errorData.message}`);
                });
            }
            return response.json();
        })
        .then(data => {
            document.getElementById("error-message").style.display = "none";
            document.getElementById("success-message").textContent = "Успешно: " + data.message;
            document.getElementById("success-message").style.display = "flex";
        })
        .catch(error => {
            document.getElementById("success-message").style.display = "none";
            document.getElementById("error-message").textContent = error.message;
            document.getElementById("error-message").style.display = "flex";
        });
}