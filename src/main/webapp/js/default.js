
if (!window.location.pathname.includes('/login')) {
    const retrievedAvatar = localStorage.getItem('avatar');
    const avatarImage = document.getElementById('avatarImage');
    avatarImage.src = retrievedAvatar;

    var storedData = localStorage.getItem('nickName');
    document.getElementById('username').textContent = storedData;
}
    // const handleLogout = () => {
    //     // Clear local storage
    //     window.localStorage.clear();
    //     localStorage.removeItem('token');
    //     window.location.reload(true);
    //     // Reload the current page
    //     // window.location.reload(true);
    //
    //     // OR redirect to the login page
    //     window.location.replace('/login');
    // };

