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
                        // || item.firstName.includes(e.target.value.trim()) || item.lastName.includes(e.target.value.trim()));
                    showListUser(listUser);
                }
            }

            deleteUser();
            approveAdmin();
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
            const roleName = account.role.length > 0 ? account.role[0].name : '';
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
                                    <button class="btn btn-outline-warning btn-approve-admin" account-id="${account.id}">${roleName}
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

                        if (deleteUserRes.status === 'SUCCESS') {
                            Swal.fire({
                                icon: "success",
                                title: "Delete success!",
                                text: "Delete user success",
                            }).then(() => {
                                location.reload();
                            });
                        }
                    } catch (e) {
                        console.log('Error delete', e)
                        Swal.fire({
                            icon: "error",
                            title: "Error =((((",
                            text: "Delete fail!",
                        });
                    }
                }
            })
        }
    } catch (e) {
        console.log('Error delete user', e)
    }
}

const approveAdmin = async () => {
    try {
        const elementApprove = document.querySelectorAll('.btn-approve-admin');
        console.log('elementApprove', elementApprove)
        if (elementApprove && elementApprove.length) {
            elementApprove.forEach(item => {
                console.log('item', item)
                item.onclick = async function () {
                    const id = $(this).attr('account-id');
                    try {
                        const options = {
                            method: 'GET',
                            headers: {
                                'Content-Type': 'application/json',
                                'Authorization': 'Bearer ' + readCookie('token')
                            },
                        };

                        const approveUserReq = await fetch(`/api/v1/admin/account/approve/${id}`, options);
                        const approveUserRes = await approveUserReq.json();
                        console.log('approveUserRes', approveUserRes)

                        if (approveUserRes.status === 'SUCCESS') {
                            Swal.fire({
                                icon: "success",
                                title: "Approve success!",
                                text: "Account role switch successful",
                            }).then(() => {
                                location.reload();
                            });
                        }
                    } catch (e) {
                        console.log('Error Approve', e)
                        Swal.fire({
                            icon: "error",
                            title: "Error =((((",
                            text: "Account role switch fail!",
                        });
                    }
                }
            })
        }
    } catch (e) {
        console.log('Error Approve user', e)
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