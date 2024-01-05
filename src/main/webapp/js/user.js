document.addEventListener('DOMContentLoaded', function () {
    var deleteButtons = document.querySelectorAll('.delete-button');

    deleteButtons.forEach(function (button) {
        button.addEventListener('click', function () {
            var accountId = button.getAttribute('data-account-id');

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

const getAllUsers = async () => {
    try {
        const options = {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + readCookie('token')
            },
        };

        const getListUserReq = await fetch(`/api/v1/admin/account`, options);
        const getListUserRes = await getListUserReq.json();

        if (getListUserRes && getListUserRes.status === 'SUCCESS') {
            let listUser = getListUserRes.data;

            showListUser(listUser);

            const inputSearch = document.querySelector('.key-search');
            if (inputSearch) {
                inputSearch.onchange = function (e) {
                    listUser = getListUserRes.data.filter(item => item.nickName.includes(e.target.value.trim()) || item.phone.includes(e.target.value.trim()) || item.email.includes(e.target.value.trim()));
                    showListUser(listUser);
                }
            }

            deleteUser();
        }
    } catch (e) {
        console.log('Error', e);
    }
}

const showListUser = (listUser) => {
    const elementListUser = document.querySelector('.list-user-active');
    if (elementListUser) {
        $('.list-user-active').empty();

        listUser.map(account => {
            elementListUser.insertAdjacentHTML('afterbegin',
                `
                        <tr class=".text-md-center text-center">
                            
                                <td>${account.id}</td>
                                <th scope="row">
                                    <a href="/user-detail?createdBy=${account.id}">
                                        <img class="rounded" alt="Cinque Terre" src="${account.avatar}" style="width: 100px;height: 100px;">
                                    </a>
                                    
                                </th>
                                <td><a href="/user-detail?createdBy=${account.id}" style="color: #0a0a0a">${account.firstName}</a></td>
                                <td><a href="/user-detail?createdBy=${account.id}" style="color: #0a0a0a">${account.lastName}</a></td>
                                <td><a href="/user-detail?createdBy=${account.id}" style="color: #0a0a0a">${account.nickName}</a></td>
                                <td><a href="/user-detail?createdBy=${account.id}" style="color: #0a0a0a">${account.email}</a></td>
                                <td><a href="/user-detail?createdBy=${account.id}" style="color: #0a0a0a">${account.phone}</a></td>
                                <td>${account.username}</td>
                                <td>
                                    <button class="btn btn-outline-warning btn-active-user" account-id="${account.id}">ACTIVE
                                    </button>
                                </td>
                                <td>
                                    <button class="btn btn-outline-danger btn-delete-user" account-id="${account.id}">Delete
                                    </button>
                                </td>
                            
                            
                        </tr>
                    `
            );
        })
    }
}

const deleteUser = async () => {
    try {
        const elementDelete = document.querySelectorAll('.btn-delete-user');
        console.log('elementDelete', elementDelete)
        if (elementDelete && elementDelete.length) {
            elementDelete.forEach(item => {
                console.log('item', item)
                item.onclick = async function () {
                    const id = $(this).attr('account-id');
                    try {
                        const options = {
                            method: 'DELETE',
                            headers: {
                                'Content-Type': 'application/json',
                                'Authorization': 'Bearer ' + readCookie('token')
                            },
                        };

                        const deleteUserReq = await fetch(`/api/v1/admin/account/${id}`, options);
                        const deleteUserRes = await deleteUserReq.json();
                        console.log('deleteUserRes', deleteUserRes)
                        window.location.reload();
                    } catch (e) {
                        console.log('Error delete', e)
                    }
                }
            })
        }
    } catch (e) {
        console.log('Error delete user', e)
    }
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

getAllUsers();