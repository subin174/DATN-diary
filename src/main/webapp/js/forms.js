this.getAddress();

function getAddress(){
    const options = {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + readCookie('accessToken')
        },
    };
    fetch('/api/v1/address', options)
        .then((res) => res.json())
        .then(resp => {
            console.log(resp)
            if (resp.status === 'SUCCESS'){
                addAddress(resp.data)
            }
        });
}


function addAddress(items){
    removeOption('#address');
    $.each(items, function (i, item) {
        $('#address').append($('<option>', {
            value: item.id,
            text : getNameAddress(item)
        }));
    });
}

function getNameAddress(item){
    const elements = [];
    if (item.number != null && item.number !== '')elements.push('Số ' + item.number);
    if (item.alley != null && item.alley !== '')elements.push('ngh. ' + item.alley);
    if (item.lane != null && item.lane !== '')elements.push('ng. ' + item.lane);
    if (item.street != null && item.street !== '')elements.push('d. ' + item.street);
    if (item.wardName != null && item.wardName !== '')elements.push(item.wardName);
    if (item.districtName != null && item.districtName !== '')elements.push(item.districtName);
    if (item.provinceName != null && item.provinceName !== '')elements.push(item.provinceName);

    return elements.join(', ');
}


function removeOption(element){
    $(element)
        .find('option')
        .remove()
        .end()
        .append('<option selected disabled>Chọn</option>')
    ;
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





