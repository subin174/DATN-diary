this.getProvince();

$('#province').on('change', function () {
    const id = $(this).find(":checked").val();
    getDistrict(id);
});

$('#district').on('change', function () {
    const id = $(this).find(":checked").val();
    getWard(id);
});


function getProvince() {
    const options = {
        method: 'GET',
        headers: {'Content-Type': 'application/json'},
        // body: JSON.stringify(body)
    };
    fetch('/api/v1/address/data/province', options)
        .then((res) => res.json())
        .then(resp => {
            if (resp.status === 'SUCCESS') {
                addProvince(resp.data)
            }
        });
}

function getDistrict(provinceId) {
    const options = {
        method: 'GET',
        headers: {'Content-Type': 'application/json'},
        // body: JSON.stringify(body)
    };
    fetch('/api/v1/address/data/district?province_id=' + provinceId, options)
        .then((res) => res.json())
        .then(resp => {
            if (resp.status === 'SUCCESS') {
                addDistrict(resp.data)
            }
        });
}

function getWard(districtId) {
    const options = {
        method: 'GET',
        headers: {'Content-Type': 'application/json'},
        // body: JSON.stringify(body)
    };
    fetch('/api/v1/address/data/ward?district_id=' + districtId, options)
        .then((res) => res.json())
        .then(resp => {
            if (resp.status === 'SUCCESS') {
                addWard(resp.data)
            }
        });
}

function addProvince(items) {
    $.each(items, function (i, item) {
        $('#province').append($('<option>', {
            value: item.provinceID,
            text: item.provinceName
        }));
    });
}

function addDistrict(items) {
    removeOption('#district');
    $.each(items, function (i, item) {
        $('#district').append($('<option>', {
            value: item.districtID,
            text: item.districtName
        }));
    });
}

function addWard(items) {
    removeOption('#ward');
    $.each(items, function (i, item) {
        $('#ward').append($('<option>', {
            value: item.wardCode,
            text: item.wardName
        }));
    });
}

function removeOption(element) {
    $(element)
        .find('option')
        .remove()
        .end()
        .append('<option selected disabled>Chọn</option>')
    ;
}

$("#create").click(function () {
    create();
});

function create() {
    const body = {
        wardCode: $("#ward").val(),
        districtId: $("#district").val(),
        provinceId: $("#province").val(),
        number: $("#number").val(),
        alley: $("#alley").val(),
        lane: $("#lane").val(),
        street: $("#street").val(),
        lat: $("#lat").val(),
        lng: $("#lng").val(),
        wardName: $("#ward").find(":selected").text(),
        districtName: $("#district").find(":selected").text(),
        provinceName: $("#province").find(":selected").text()
    }
    console.log(body)

    const options = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + readCookie('accessToken')
        },
        body: JSON.stringify(body)
    };
    fetch('/api/v1/address', options)
        .then((res) => res.json())
        .then(resp => {
            console.log(resp);
            if (resp.status === 'SUCCESS') {
                removeData()
            }
        });
}

function removeData() {
    $("#ward").val('')
    $("#district").val('')
    $("#province").val('')
    $("#number").val('')
    $("#alley").val('')
    $("#lane").val()
    $("#street").val('')
    $("#lat").val('')
    $("#lng").val('')
    removeOption('#ward');
    removeOption('#district');
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








