document.addEventListener('DOMContentLoaded', function () {
    var deleteButtons = document.querySelectorAll('.delete-button');

    deleteButtons.forEach(function (button) {
        button.addEventListener('click', function () {
            // Get the account ID from the data attribute
            var accountId = button.getAttribute('data-account-id');

            // Call the deleteAccount function to handle the delete operation
            deleteAccount(accountId);
        });
    });
});

function deleteAccount(accountId) {
    const options = {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + readCookie('token')
        },
    };

    fetch('/api/v1/admin/account/' + accountId, options)
        .then((res) => res.json())
        .then(resp => {
            console.log(resp);
            if (resp.status === 'SUCCESS') {
                // You might want to update the UI or display a success message
                console.log('Account deleted successfully');
                // You might want to remove the corresponding HTML element as well
                // Assuming each table row has a unique id, you can remove it like this:
                document.getElementById('accountRow' + accountId).remove();
            } else {
                // Handle errors (e.g., display an error message)
                console.error('Error deleting account', resp.message);
            }
        })
        .catch(error => {
            // Handle network errors
            console.error('Network error', error);
        });
}

function readCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') c = c.substring(1, c.length);
        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length, c.length);
    }
    return null;
}