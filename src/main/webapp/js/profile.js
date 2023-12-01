this.getProfile();

function getProfile(){
    const options = {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + readCookie('token')
        },
    };
    fetch('/api/v1/..', options)
        .then((res) => res.json())
        .then(resp => {
            console.log(resp)
            if (resp.status === 'SUCCESS'){
                addAddress(resp.data)
            }
        });
}