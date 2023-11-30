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
            window.location.href = 'address';
        })
        .catch(error => {
            console.error('Error during sign-in:', error);
        });
}

function setToken(name,token){
    document.cookie = name +'='+ token +';';
}
function deleteToken(name) {
    document.cookie = name +'=;';
}

//
// $("#signIn").click(function(){
//     signIn();
// });

// function signIn(){
//
//     const body = {
//         username : $("#username").val(),
//         password : $("#password").val(),
//     }
//     const options = {
//         method: 'POST',
//         headers: {'Content-Type': 'application/json'},
//         body: JSON.stringify(body)
//     };
//
//     fetch('api/v1/auth/login', options)
//         .then((res) => res.json())
//         .then(resp => {
//             if (resp.status === 'SUCCESS'){
//                 setToken("accessToken",resp.token)
//                 window.location.href = '../../../../../../client-admin/nhatro/src/main/webapp';
//             }
//             if (resp.status === 'ERROR'){
//                 deleteToken("accessToken")
//                 window.location.href = '/sign-in';
//             }
//         });
// }
//
// function setToken(name,token){
//     document.cookie = name +'='+ token +';';
// }
// function deleteToken(name) {
//     document.cookie = name +'=;';
// }


