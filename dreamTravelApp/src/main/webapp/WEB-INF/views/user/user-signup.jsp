<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"> <!-- ✅ 요게 핵심!! -->
    <title>회원가입 테스트</title>
</head>
<body>
<h2>이메일 인증 테스트</h2>

<input id="email" placeholder="이메일 입력">
<button onclick="sendCode()">인증코드 보내기</button>
<br><br>

<input id="password" placeholder="비밀번호 입력" type="password">
<input id="code" placeholder="인증 코드 입력">
<button onclick="signup()">회원가입</button>

<p id="result"></p>

<script>
    function sendCode() {
        const email = document.getElementById("email").value;

        fetch("<%= request.getContextPath() %>/user/send-code", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email })
        })
        .then(res => res.text())
        .then(data => {
            document.getElementById("result").innerText = "[인증코드 발송] " + data;
        });
    }

    function signup() {
        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;
        const code = document.getElementById("code").value;

        fetch("<%= request.getContextPath() %>/user/signup", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, password, code })
        })
        .then(res => res.text())
        .then(data => {
            document.getElementById("result").innerText = "[회원가입 결과] " + data;
        });
    }
</script>
</body>
</html>