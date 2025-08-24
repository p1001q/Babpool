// 현재 비밀번호 검증 + 새 비밀번호 형식 + 일치 검증
function validateChangePassword(sessionPassword) {
    const current = document.getElementById('currentPassword').value;
    const newPw = document.getElementById('newPassword').value;
    const confirmPw = document.getElementById('confirmPassword').value;
    const currentCheck = document.getElementById('currentPwCheck');

    if (current !== sessionPassword) {
        currentCheck.innerText = '현재 비밀번호가 일치하지 않습니다.';
        currentCheck.style.color = 'red';
        return false;
    }

    if (!checkPasswordPattern()) return false;
    if (newPw !== confirmPw) {
        alert("새 비밀번호가 일치하지 않습니다.");
        return false;
    }

    return true;
}

// 새 비밀번호 형식 (영문+숫자 6~20자) 검증
function checkPasswordPattern() {
    const pw = document.getElementById('newPassword').value;
    const warning = document.getElementById('pw-warning');
    const pattern = /^(?=.*[a-zA-Z])(?=.*\d).{8,15}$/;

    if (!pattern.test(pw)) {
        warning.innerText = ' (영문+숫자 8~15자)';
        warning.style.color = 'red';
        return false;
    } else {
        warning.innerText = ' ✅';
        warning.style.color = 'green';
        return true;
    }
}

// 비밀번호 일치 여부 확인
function checkPasswordMatch() {
    const pw = document.getElementById('newPassword').value;
    const confirm = document.getElementById('confirmPassword').value;
    const icon = document.getElementById('pw-match-icon');

    if (confirm === "") {
        icon.innerText = '';
    } else if (pw === confirm) {
        icon.innerText = '✓';
        icon.style.color = 'green';
    } else {
        icon.innerText = '✗';
        icon.style.color = 'red';
    }
}
