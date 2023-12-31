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

function deleteEntity(accountId) {
    // Check if accountId is a valid number
    if (!accountId || isNaN(parseInt(accountId, 10))) {
        console.error('Invalid accountId:', accountId);
        return;
    }

    // Confirm deletion
    const confirmed = window.confirm('Are you sure you want to delete this entity?');
    if (!confirmed) {
        return;
    }

    const options = {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + readCookie('token')
        },
    };

    fetch(`/api/v1/admin/account/${accountId}`, options)
        .then((res) => res.json())
        .then(resp => {
            console.log(resp);
            if (resp.status === 'SUCCESS') {
                location.reload();
            }
        })
        .catch(error => {
            console.error('Error deleting entity:', error);
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