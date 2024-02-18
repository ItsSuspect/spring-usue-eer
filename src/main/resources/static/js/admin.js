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

function createNewUser() {
    const username = document.getElementById("username-input").value;
    const password = document.getElementById("password-input").value;

    const role = document.getElementById("roles-list").value;
    const email = document.getElementById("email-input").value;
    const fullName = document.getElementById("fullName-input").value;
    const group = document.getElementById("group-input").value;

    const signUpRequest = {
        "username": username,
        "password": password,

        "role": role,
        "email": email,
        "fullName": fullName
    };

    if (role === "ROLE_STUDENT") {
        signUpRequest.group = group;
    }

    fetch("/admin/signUp", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(signUpRequest)
    })
        .then(response => response.json())
        .then(data => {
            console.log(data);
        })
}