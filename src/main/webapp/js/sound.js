document.getElementById('audioInput').addEventListener('change', function (event) {
    const audioPlayer = document.getElementById('audioPlayer');
    const file = event.target.files[0];

    if (file) {
        const objectURL = URL.createObjectURL(file);
        audioPlayer.src = objectURL;
    }
});

$("#create").click(function(){
    create();
});

let audioUrl = '';
let imgUrl = '';

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
function create(imageData,audioData) {
    const body = {
        author:$("#author").val(),
        title:$("#title").val(),
        moodSoundId: $("#moodSound").val(),
        poster: imgUrl,
        track: audioUrl
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
            }
        });
}

function uploadAudio() {
    const audioInput = document.getElementById('audioInput');
    const audioFile = audioInput.files[0];

    if (!audioFile) {
        alert('Please select an audio file.');
        return;
    }

    const formData = new FormData();
    formData.append('audio', audioFile);

    fetch('/api/v1/admin/sound/upload', {
        method: 'POST',
        headers: {
            'Authorization': 'Bearer ' + readCookie('token')
        },
        body: formData
    })
        .then((res) => res.json())
        .then(resp => {
            console.log(resp);
            if (resp.status === 'SUCCESS') {
                audioUrl = resp.data;
            }
        })
}
function uploadImage() {
    const input = document.getElementById('imageInput');
    const file = input.files[0];

    if (!file) {
        alert('Please select an image file.');
        return;
    }

    const formData = new FormData();
    formData.append('multipartFile', file);

    fetch('/api/v1/admin/sound/upload-img', {
        method: 'POST',
        headers: {
            'Authorization': 'Bearer ' + readCookie('token')
        },
        body: formData
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(resp => {
            console.log('Upload success:', resp);
            if (resp.status === 'SUCCESS') {
                imgUrl = resp.data;
            }
        })
}
