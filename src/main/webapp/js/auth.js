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
        await getInfo(data.token);
        window.location.href = 'profile';
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
            localStorage.setItem("nickName", data.nickName);
            localStorage.setItem("avatar", data.avatar);
            localStorage.setItem("firstName", data.firstName);
            localStorage.setItem("lastName", data.lastName);
            localStorage.setItem("phone", data.phone);
            localStorage.setItem("date", data.date);
            localStorage.setItem("email", data.email);
            localStorage.setItem("age", data.age);

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






