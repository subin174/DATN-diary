// this.getList();
// function getList() {
//     const options = {
//         method: 'GET',
//         headers: {
//             'Content-Type': 'application/json',
//             'Authorization': 'Bearer ' + readCookie('token')
//         },
//     };
//     fetch('/api/v1/admin/sound/all', options)
//         .then((res) => res.json())
//         .then(resp => {
//             console.log(resp)
//             if (resp.status === 'SUCCESS') {
//                 addAddress(resp.data)
//             }
//         });
// }

const ITEMS_PER_PAGE = 7
let listSoundData = [];
const getAllSounds = async () => {
    const options = {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + readCookie('token')
        },
    };

    const getListSoundReq = await fetch(`/api/v1/admin/sound`, options);
    const getListSoundRes = await getListSoundReq.json();

    if (getListSoundRes && getListSoundRes.status === 'SUCCESS') {
        listSoundData = getListSoundRes.data;

        showListSound(listSoundData);

        const inputSearch = document.querySelector('.key-search');
        if (inputSearch) {
            inputSearch.onchange = function (e) {
                const filteredList = listSoundData.filter(item => item.title.includes(e.target.value.trim()) || item.author.includes(e.target.value.trim()) || item.moodSound.includes(e.target.value.trim()));
                showListSound(filteredList);
            }
        }

        // Add pagination
        addPagination(getListSoundRes.paginate.totalPages);
        deleteSound();
    }
}

const showListSound = (listSound, page = 1) => {
    const elementListSound = document.querySelector('.list-sound');
    if (elementListSound) {
        $('.list-sound').empty();

        const startIndex = (page - 1) * ITEMS_PER_PAGE;
        const endIndex = startIndex + ITEMS_PER_PAGE;

        listSound.slice(startIndex, endIndex).forEach(sound => {
            elementListSound.insertAdjacentHTML('beforeend',
                `
                        <tr class=".text-md-center text-center">
                            
                                <td>${sound.id}</td>
                                <th scope="row">
                                    
                                        <img class="rounded" alt="Cinque Terre" src="${sound.poster}" style="width: 100px;height: 100px;">
                                    
                                </th>
                                <td style="text-align: center">${sound.title}</td>
                                <td>${sound.author}</td>
                                <td>${sound.moodSound}</td>
                                
                                <td>
                                    <button class="btn btn-outline-secondary btn-active-user" sound-id="${sound.id}">Update
                                    </button>
                                </td>
                                <td>
                                    <button class="btn btn-outline-danger btn-delete-sound" sound-id="${sound.id}">Delete
                                    </button>
                                </td>
                            
                            
                        </tr>
                        
                    `
            );
        })
    }
}

const addPagination = (totalPages) => {
    const paginationElement = document.querySelector('.pagination');
    if (paginationElement) {
        $('.pagination').empty();

        for (let i = 1; i <= totalPages; i++) {
            const liElement = document.createElement('li');
            liElement.classList.add('page-item');
            const aElement = document.createElement('a');
            aElement.classList.add('page-link');
            aElement.textContent = i;
            aElement.addEventListener('click', (function(page) {
                return function() {
                    handlePageClick(page);
                };
            })(i));
            liElement.appendChild(aElement);
            paginationElement.appendChild(liElement);
        }
    }
}

const handlePageClick = (page) => {
    showListSound(listSoundData, page);
}

const deleteSound = async () => {
    try {
        const elementDelete = document.querySelectorAll('.btn-delete-sound');
        console.log('elementDelete', elementDelete)
        if (elementDelete && elementDelete.length) {
            elementDelete.forEach(item => {
                console.log('item', item)
                item.onclick = async function () {
                    const id = $(this).attr('sound-id');
                    try {
                        const options = {
                            method: 'DELETE',
                            headers: {
                                'Content-Type': 'application/json',
                                'Authorization': 'Bearer ' + readCookie('token')
                            },
                        };

                        const deleteSoundReq = await fetch(`/api/v1/admin/sound/${id}`, options);
                        const deleteSoundRes = await deleteSoundReq.json();
                        console.log('deleteSoundRes', deleteSoundRes)
                        window.location.reload();
                    } catch (e) {
                        console.log('Error delete', e)
                    }
                }
            })
        }
    } catch (e) {
        console.log('Error delete sound', e)
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

document.addEventListener("DOMContentLoaded", function () {
    // Make an AJAX request to the API endpoint
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "http://localhost:8080/api/v1/admin/sound/getCountMood", true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            var response = JSON.parse(xhr.responseText);
            var thuGianData = response.data.find(function (item) {
                return item[1] === "Thư giãn";
            });
            if (thuGianData) {
                document.getElementById("thugian").textContent = thuGianData[0];
            } else {
                // Handle the case where "Thư giãn" is not found in the response
                document.getElementById("thugian").textContent = "0";
            }
        }
        if (xhr.readyState === 4 && xhr.status === 200) {
            var response = JSON.parse(xhr.responseText);
            var thuGianData = response.data.find(function (item) {
                return item[1] === "Giải toả";
            });
            if (thuGianData) {
                document.getElementById("giaitoa").textContent = thuGianData[0];
            } else {
                // Handle the case where "Thư giãn" is not found in the response
                document.getElementById("giaitoa").textContent = "0";
            }
        }
        if (xhr.readyState === 4 && xhr.status === 200) {
            var response = JSON.parse(xhr.responseText);
            var thuGianData = response.data.find(function (item) {
                return item[1] === "Dễ ngủ";
            });
            if (thuGianData) {
                document.getElementById("dengu").textContent = thuGianData[0];
            } else {
                // Handle the case where "Thư giãn" is not found in the response
                document.getElementById("dengu").textContent = "0";
            }
        }
        if (xhr.readyState === 4 && xhr.status === 200) {
            var response = JSON.parse(xhr.responseText);
            var thuGianData = response.data.find(function (item) {
                return item[1] === "Hạnh phúc";
            });
            if (thuGianData) {
                document.getElementById("hanhphuc").textContent = thuGianData[0];
            } else {
                // Handle the case where "Thư giãn" is not found in the response
                document.getElementById("hanhphuc").textContent = "0";
            }
        }
        if (xhr.readyState === 4 && xhr.status === 200) {
            var response = JSON.parse(xhr.responseText);
            var thuGianData = response.data.find(function (item) {
                return item[1] === "Vui vẻ";
            });
            if (thuGianData) {
                document.getElementById("vuive").textContent = thuGianData[0];
            } else {
                // Handle the case where "Thư giãn" is not found in the response
                document.getElementById("vuive").textContent = "0";
            }
        }
    };
    xhr.send();
});

getAllSounds();