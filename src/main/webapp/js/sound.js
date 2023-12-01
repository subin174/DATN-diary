document.getElementById('audioInput').addEventListener('change', function (event) {
    const audioPlayer = document.getElementById('audioPlayer');
    const file = event.target.files[0];

    if (file) {
        const objectURL = URL.createObjectURL(file);
        audioPlayer.src = objectURL;
    }
});
function displayImage(input) {
    const previewImage = document.getElementById('previewImage');
    if (input.files && input.files[0]) {
        const reader = new FileReader();
        reader.onload = function (e) {
            previewImage.src = e.target.result;
        };
        reader.readAsDataURL(input.files[0]);
    }
}
$("#create").click(function(){
    create();
});

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
function create() {
    const body = {
        author:$("#author").val(),
        title:$("#title").val(),
        moodSoundId: $("#moodSound").val()
    }
    console.log(body)

    const options = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + readCookie('token')
        },
        body: JSON.stringify(body)
    };
    fetch('/api/v1/admin/sound', options)
        .then((res) => res.json())
        .then(resp => {
            console.log(resp);
            if (resp.status === 'SUCCESS') {
                removeData()
            }
        });
}
function create() {
    const body = {
        author:$("#author").val(),
        title:$("#title").val(),
        moodSoundId: $("#moodSound").val()
    }
    console.log(body)

    const options = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + readCookie('token')
        },
        body: JSON.stringify(body)
    };
    fetch('/api/v1/admin/sound/upload2', options)
        .then((res) => res.json())
        .then(resp => {
            console.log(resp);
            if (resp.status === 'SUCCESS') {
                removeData()
            }
        });
}