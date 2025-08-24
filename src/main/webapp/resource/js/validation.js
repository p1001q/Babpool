let timerInterval;
let timeLeft = 180;

function startTimer() {
    timeLeft = 180;
    updateTimer();
    clearInterval(timerInterval);
    timerInterval = setInterval(updateTimer, 1000);
}

function updateTimer() {
    const timerSpan = document.getElementById("timer");
    if (timeLeft <= 0) {
        clearInterval(timerInterval);
        timerSpan.innerText = "만료됨";

        // 세션 상태 갱신을 위해 인증 만료 요청
        fetch("ExpireAuthServlet");  // 서버에서 emailAuthStatus = fail 처리
        return;
    }

    const min = Math.floor(timeLeft / 60);
    const sec = String(timeLeft % 60).padStart(2, '0');
    timerSpan.innerText = `${min}:${sec}`;
    timeLeft--;
}

function verifyCode() {
    const inputCode = document.getElementById("code").value.trim();
    fetch("VerifyCodeServlet", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: "code=" + encodeURIComponent(inputCode)
    })
    .then(res => res.text())
    .then(msg => alert(msg));
}

function checkPasswordPattern() {
    const pw = document.getElementById("password").value;
    const warning = document.getElementById("pw-warning");
    const regex = /^[A-Za-z0-9]{8,15}$/;
    if (pw === "") {
        warning.innerText = "";
    } else if (!regex.test(pw)) {
        warning.innerText = "8~15자의 영문자와 숫자만 입력하세요.";
    } else {
        warning.innerText = "";
    }
}

function validateForm() {
    const password = document.getElementById("password").value;
    const confirm = document.getElementById("confirmPassword").value;
    const regex = /^[A-Za-z0-9]{8,15}$/;

    if (!regex.test(password)) {
        alert("비밀번호는 8~15자의 영문자와 숫자만 사용 가능합니다.");
        return false;
    }

    if (password !== confirm) {
        alert("비밀번호가 일치하지 않습니다.");
        return false;
    }

    return true;
}
