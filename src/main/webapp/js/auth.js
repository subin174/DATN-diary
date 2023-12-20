$("#signIn").click(function(){
    signIn();
});

function signIn(){

    const body = {
        username : $("#username").val(),
        password : $("#password").val(),
    }
    const options = {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(body)
    };

    fetch('api/v1/authenticate/login', options)
        .then((res) => {
            if (!res.ok) {
                throw new Error(`HTTP error! Status: ${res.status}`);
            }
            return res.json();
        })
        .then(resp => {
            setToken("token", resp.token);
            getInfo(resp.token);
            window.location.href = 'user';
        })
        .catch(error => {
            console.error('Error during sign-in:', error);
        });
}

function getInfo(token) {
    const options = {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
    };
    fetch('/api/v1/account/info', options)
        .then((res) => res.json())
        .then(resp => {
            console.log(resp)
            if (resp.status === 'SUCCESS') {
                localStorage.setItem("nickName", resp.data.nickName);
                localStorage.setItem("avatar", resp.data.avatar);

            }
        });
}

function setToken(name,token){
    document.cookie = name +'='+ token +';';
}
function deleteToken(name) {
    document.cookie = name +'=;';
}






