console.log("abc")


let timeElement = $(".time-box");
for (let i = 0; i < timeElement.length; i++) {
    resetTime(timeElement.get(i));
}

function resetTime(timeElement) {

    //console.log('CountDownTime');
    let strDate = timeElement.getAttribute("data-action-time").substring(0,19) + 'z';
    console.log("strDate " + strDate)

    let countDownDate = new Date(strDate);
    if (Number.isNaN(countDownDate.getTime())) { // Safari browser cannot parse above date
        countDownDate = new Date((timeElement.getAttribute("data-action-time") + ":00"));
    }
    // countDownDate = new Date(countDownDate.getTime() + (countDownDate.getTimezoneOffset()*60*1000));

    console.log(countDownDate)
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
            timeElement.innerHTML = days + 'd ago'
        else if (hours > 0)
            timeElement.innerHTML = hours + 'h ago'
        else if (minutes > 0)
            timeElement.innerHTML = minutes + 'm ago'
        // timeElement.innerHTML = days + 'd ' + hours + 'h ' + minutes + 'm ' + seconds + 's';
        // timeElement.querySelectorAll('.time-num')[0].innerHTML = days;
        // timeElement.querySelectorAll('.time-num')[1].innerHTML = hours;
        // timeElement.querySelectorAll('.time-num')[2].innerHTML = minutes;
        // timeElement.querySelectorAll('.time-num')[3].innerHTML = seconds;
    }, 1000);
}