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

                            const diaryTime = document.querySelector('.diary-createdAt');
                            if (diaryTime) {
                                const createdAtDate = new Date(data.createdAt);

                                // Format the date as "December 21, 2023 10:45 AM"
                                const formattedDate = createdAtDate.toLocaleString('en-US', {
                                    year: 'numeric',
                                    month: 'long',
                                    day: 'numeric',
                                    hour: 'numeric',
                                    minute: 'numeric',
                                    hour12: true // Use 12-hour clock with AM/PM
                                });

                                // Set the formatted date to the element
                                diaryTime.innerText = formattedDate;
                            }

                            const diaryDetail = document.querySelector('.card-body');
                            if (diaryDetail) {
                                diaryDetail.insertAdjacentHTML('afterbegin', `
                                    
                                    <div class="card-body">
                                        <div class="card-title">Thời điểm tâm trạng / sự việc diễn ra:</div>
                                        <div class="card-text"> Lúc ${data.time} ngày ${data.date} 
                                        </div>
                                    </div>
                                    <div class="card-body">
                                        <div class="card-title">Bạn đã ở đâu tại thời điểm sự việc diễn ra</div>
                                        <div class="card-text"> ${data.place} 
                                        </div>
                                    </div>
                                    <div class="card-body">
                                        <div class="card-title">Bạn gặp phải chuyện gì thế?</div>
                                        <div class="card-title">Trước tiên hay ghi lại những gì xảy ra khiến tâm trạng bạn thay đổi
                                            nhé!
                                        </div>
                                        <div class="card-text">${data.happened}
                                        </div>
                                    </div>
                                    <div class="card-body">
                                        <div class="card-title">Cảm xúc và suy nghĩ của bạn tại thời điểm đó như thế nào:</div>
                                        <div class="card-text">${data.thinkingMoment}</div>
                                    </div>
                                    <div class="card-body">
                                        <div class="card-title">Hãy dành một chút thời gian để suy nghĩ về những sự việc vừa qua nhé!
                                        </div>
                                        <div class="card-text">${data.thinkingFelt}
                                        </div>
                                    </div>
                                    <div class="card-body">
                                        <div class="card-title">Nếu bạn thay đổi cách bạn nghĩ, bạn sẽ thay đổi cách bạn cảm nhận!</div>
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



function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, '\\$&');
    var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, ' '));
}

function setProfile(elementId, content) {
    var element = document.getElementById(elementId);
    if (elementId === 'avatarUser') {
        element.src = content;
    }

    else {
        element.innerHTML = content;
    }
}
function getInfoById(id) {
    console.log("id: " + id);
    fetch(`/api/v1/admin/account/${id}`, {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + readCookie('token')
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Get request failed');
            }
            return response.json();
        })
        .then(resp => {
            console.log('Get success:', resp);
            if (resp.status === 'SUCCESS') {
                setProfile('nickName', resp.data.nickName);
                setProfile('avatarUser', resp.data.avatar);
                setProfile('date', resp.data.date);
                setProfile('phone', resp.data.phone);
                setProfile('email', resp.data.email);
                setProfile('role', resp.data.role);
                console.log('Retrieved data:', resp.data);
            }
        })
        .catch(error => {
            console.error('Error:', error.message);
        });
}

var createdByParam = getParameterByName('createdBy');

if (createdByParam) {
    getInfoById(createdByParam);
}


