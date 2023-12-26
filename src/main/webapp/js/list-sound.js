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
function deleteAudio(id) {
    console.log("id"+id)
    fetch(`/api/v1/admin/sound/${id}`, {
        method: 'DELETE',
        headers: {
            'Authorization': 'Bearer ' + readCookie('token')
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('delete er');
            }
            return response.json();
        })
        .then(resp => {
            console.log('delete success:', resp);
            if (resp.status === 'SUCCESS') {
                location.reload();
            }
        })
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