function getLineColor(index) {
    const colors = ["#FF5733", "#FFD700", "#8A2BE2", "#00BFFF", "#32CD32", "#FF1493", "#808080"];
    return colors[index % colors.length];
}

document.addEventListener("DOMContentLoaded", function () {
    fetch('http://localhost:8080/api/v1/diary/count-diary-mood-public-by-year')
        .then(response => response.json())
        .then(data => {
            const labels = Object.keys(data);
            const emotions = ["Trầm cảm", "Lo lắng", "Áp lực", "Mất kiểm soát", "Vui vẻ", "Hạnh phúc", "Buồn bã"];

            const datasets = emotions.map((emotion, index) => {
                const countValues = labels.map(month => {
                    const monthData = data[month];
                    const count = monthData.length > 0
                        ? monthData.find(entry => entry[1] === emotion)?.[0] || 0
                        : 0;

                    return count;
                });

                return {
                    label: emotion,
                    data: countValues,
                    borderColor: getLineColor(index),
                    fill: false
                };
            });

            new Chart(document.getElementById("chartjs-line-3"), {
                type: "line",
                data: {
                    labels: labels,
                    datasets: datasets
                },
                options: {
                    legend: {
                        display: true,
                        position: 'bottom',
                    }
                }
            });
        })
        .catch(error => {
            console.error('Error fetching data from the API:', error);
        });
});

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
                            const diaryLevel = document.querySelector('.diary-level');
                            if (diaryLevel) {
                                diaryLevel.innerText = data.level;
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


function deleteEntity(diaryId) {
    // Check if diaryId is a valid number
    if (!diaryId || isNaN(parseInt(diaryId, 10))) {
        console.error('Invalid diaryId:', diaryId);
        return;
    }

    // Confirm deletion
    const confirmed = window.confirm('Are you sure you want to delete this entity?');
    if (!confirmed) {
        return;
    }

    const options = {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + readCookie('token')
        },
    };

    fetch(`/api/v1/diary/deleteById/${diaryId}`, options)
        .then((res) => res.json())
        .then(resp => {
            console.log(resp);
            if (resp.status === 'SUCCESS') {
                // Reload the page after successful deletion
                // location.reload();
            }
        })
        .catch(error => {
            console.error('Error deleting entity:', error);
        });
}
//cmt
function getComments(diaryId) {
    // Specify the API endpoint
    const apiUrl = `http://localhost:8080/api/v1/comments/list/${diaryId}`;

    // Make a GET request using the Fetch API
    fetch(apiUrl)
        .then(response => response.json())
        .then(data => {
            if (data.status === "SUCCESS") {
                // If the API response is successful, display the comments
                displayComments(data.data);
            } else {
                console.error("Error fetching comments:", data.status);
            }
        })
        .catch(error => {
            console.error("Error fetching comments:", error);
        });
}

// Function to display comments in the HTML
function displayComments(comments) {
    const commentContainer = document.getElementById('cmt');

    // Clear existing comments
    commentContainer.innerHTML = '';

    // Iterate through comments and append them to the container
    comments.forEach(comment => {
        const commentElement = document.createElement('div');
        commentElement.className = 'comment';

        // Add comment content (you can customize this based on your needs)
        commentElement.innerHTML = `
        <strong>${comment.nickName}</strong>
        <div class="text-muted small mt-1">${comment.createdAt} ago</div>
        <p>${comment.comment}</p>
        <img src="${comment.avatar}" alt="User Avatar" class="avatar img-fluid rounded-circle cmt-avatar">
      `;

        // Append the comment element to the container
        commentContainer.appendChild(commentElement);
    });
}

// Call the function with the diaryId you want to fetch comments for
getComments(15);


$("#close").click(function(){
    close();
    location.reload();
});
$("#exampleModal").click(function(){
    location.reload();
});
$("#close2").click(function(){
    close();
    location.reload();
});
$("#delete").click(function(){
    const diaryId = $(this).attr("data-diary-id");
    deleteEntity(diaryId);
});

