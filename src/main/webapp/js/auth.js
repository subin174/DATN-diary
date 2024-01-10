$("#signIn").click(async function(){
    await signIn();
});

async function signIn(){
    const body = {
        username : $("#username").val(),
        password : $("#password").val(),
    }
    const options = {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(body)
    };

    try {
        let data = await fetch('api/v1/authenticate/login', options);
        data = await data.json();
        setToken("token", data.token);
        const _data = await getInfo(data.token);

        if (_data && _data.id) {
            const isAdmin = _data.role.find(item => item.id === 1);
            if (isAdmin && isAdmin.id) {
                localStorage.setItem("nickName", _data.nickName);
                localStorage.setItem("avatar", _data.avatar);
                localStorage.setItem("firstName", _data.firstName);
                localStorage.setItem("lastName", _data.lastName);
                localStorage.setItem("phone", _data.phone);
                localStorage.setItem("date", _data.date);
                localStorage.setItem("email", _data.email);
                localStorage.setItem("age", _data.age);
                window.location.href = 'profile';
            } else {
                Swal.fire({
                    icon: "error",
                    title: "Oops...",
                    text: "Please login again, account is not admin!",
                });
            }
        } else {
            Swal.fire({
                icon: "error",
                title: "Oops...",
                text: "Please enter correct login information!",
            });
        }
    } catch (e) {
        console.log(e)
    }
}

async function getInfo(token) {
    const options = {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
    };
    try {
        const _data = await fetch('/api/v1/account/info', options);
        const { status, data } = await _data.json();
        if (status === 'SUCCESS') {
            return data;
        }
    } catch (e) {
        console.log('e', e)
    }
}

function setToken(name,token){
    document.cookie = name +'='+ token +';';
}
function deleteToken(name) {
    document.cookie = name +'=;';
}






