
if (!window.location.pathname.includes('/login')) {
    const retrievedAvatar = localStorage.getItem('avatar');
    const avatarImage = document.getElementById('avatarImage');
    avatarImage.src = retrievedAvatar;
    const avatarImage2 = document.getElementById('avatarImage2');
    avatarImage2.src = retrievedAvatar;


    var storedData = localStorage.getItem('nickName');
    document.getElementById('username').textContent = storedData;
    document.getElementById('username2').textContent = storedData;


    var storedDatafn = localStorage.getItem('firstName');
    document.getElementById('firstName').textContent = storedDatafn;

    var storedDataPhone = localStorage.getItem('phone');
    document.getElementById('phone').textContent = storedDataPhone;

    var storedDataEmail = localStorage.getItem('email');
    document.getElementById('email').textContent = storedDataEmail;

    var storedDataDate = localStorage.getItem('date');
    document.getElementById('date').textContent = storedDataDate;

    var storedDataln = localStorage.getItem('lastName');
    document.getElementById('lastName').textContent = storedDataln;
    var storedDataAge = localStorage.getItem('age');
    document.getElementById('age').textContent = storedDataAge;
    const avatarImage3 = document.getElementById('avaProfile');
    avatarImage3.src = retrievedAvatar;
    document.getElementById('nickname').textContent = storedData;
}


