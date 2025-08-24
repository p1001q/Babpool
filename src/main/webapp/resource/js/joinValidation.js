function validateJoinForm() {
    const pw = document.getElementById("passwordInput").value;
    const confirm = document.getElementById("confirmPassword").value;

    // 최소 8자, 숫자 + 영문 포함
    const pwPattern = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/;

    if (!pwPattern.test(pw)) {
        alert("비밀번호는 8자 이상, 영문+숫자를 포함해야 합니다.");
        return false;
    }

    if (pw !== confirm) {
        alert("비밀번호가 일치하지 않습니다.");
        return false;
    }

    return true;
}

function hashAndSubmit() {
    if (!validateJoinForm()) return false;

    const pw = document.getElementById("passwordInput").value;
    const hash = CryptoJS.SHA256(pw).toString(); // SHA-256 해싱

    document.getElementById("passwordHashed").value = hash;

    // 비밀번호 입력칸 초기화 (서버로 원본 전송 안 되게)
    document.getElementById("passwordInput").value = '';
    document.getElementById("confirmPassword").value = '';

    return true;
}
