this.getList();
function getList() {
    const options = {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + readCookie('token')
        },
    };
    fetch('/api/v1/admin/sound/all', options)
        .then((res) => res.json())
        .then(resp => {
            console.log(resp)
            if (resp.status === 'SUCCESS') {
                addAddress(resp.data)
            }
        });
}
function deleteAudio(id) {
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