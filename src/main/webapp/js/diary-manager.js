let timeElement = $(".time-box");
for (let i = 0; i < timeElement.length; i++) {
    resetTime(timeElement.get(i));
}

function resetTime(timeElement) {

    //console.log('CountDownTime');
    let strDate = timeElement.getAttribute("data-action-time").substring(0,19) + 'z';
    // console.log("strDate " + strDate)

    let countDownDate = new Date(strDate);
    if (Number.isNaN(countDownDate.getTime())) { // Safari browser cannot parse above date
        countDownDate = new Date((timeElement.getAttribute("data-action-time") + ":00"));
    }
    // countDownDate = new Date(countDownDate.getTime() + (countDownDate.getTimezoneOffset()*60*1000));

    // console.log(countDownDate)
    let x = setInterval(function() {
        //let distance = countDownDate - timeNow;

        let distance = (new Date().getTime()) - countDownDate.getTime();

        let days = Math.floor(distance / (1000 * 60 * 60 * 24));
        let hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        let minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
        let seconds = Math.floor((distance % (1000 * 60)) / 1000);

        if (distance < 0) {
            clearInterval(x);
        }

        if (days > 0)
            timeElement.innerHTML = days + ' day ago'
        else if (hours > 0)
            timeElement.innerHTML = hours + ' hour ago'
        else if (minutes > 0)
            timeElement.innerHTML = minutes + ' min ago'
    }, 1000);
}
const getDiaryById = async () => {
    const options = {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + readCookie('token')
        },
    };

    try {
        const diaryItemElement = document.querySelectorAll('.diary-item-detail');
        if (diaryItemElement && diaryItemElement.length) {
            diaryItemElement.forEach(item => {
                item.onclick = async function () {
                    const id = this.id;
                    if (id) {
                        const req = await fetch(`api/v1/admin/diary/${id}`, options);
                        const { status, data } = await req.json();
                        if (status === 'SUCCESS') {
                            $('.card-body').empty();

                            const diaryNickname = document.querySelector('.diary-nickname');
                            if (diaryNickname) {
                                diaryNickname.innerText = data.nickname;
                            }

                            const diaryMood = document.querySelector('.diary-mood');
                            if (diaryMood) {
                                diaryMood.innerText = data.mood;
                            }

                            const diaryAvatar = document.querySelector('.diary-avatar');
                            if (diaryAvatar) {
                                diaryAvatar.setAttribute('src', data.avatar);
                            }

                            const diaryDetail = document.querySelector('.card-body');
                            if (diaryDetail) {
                                diaryDetail.insertAdjacentHTML('afterbegin', `
                                     <div class="card-text">
                                        ${data.happened}
                                     </div>
                                `);
                            }
                        }
                    }
                }
            })
        }
    } catch (e) {
        console.log('Error', e);
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

getDiaryById();