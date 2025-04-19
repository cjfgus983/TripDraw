<template>
    <div class="signup-form">
      <h2>회원가입</h2>
  
      <div>
        <input v-model="email" placeholder="이메일 입력" />
        <button @click="sendCode">인증코드 보내기</button>
      </div>
  
      <div>
        <input v-model="password" placeholder="비밀번호 입력" type="password" />
        <input v-model="code" placeholder="인증 코드 입력" />
        <button @click="signup">회원가입</button>
      </div>
  
      <p>{{ resultMessage }}</p>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue'
  
  const email = ref('')
  const password = ref('')
  const code = ref('')
  const resultMessage = ref('')
  
  const sendCode = async () => {
    try {
      const res = await fetch('http://localhost:8080/api/send-code', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email: email.value })
      })
      const text = await res.text()
      resultMessage.value = `[인증코드 발송] ${text}`
    } catch (err) {
      resultMessage.value = '인증코드 발송 실패'
    }
  }
  
  const signup = async () => {
    try {
      const res = await fetch('http://localhost:8080/api/signup', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          email: email.value,
          password: password.value,
          code: code.value
        })
      })
      const text = await res.text()
      resultMessage.value = `[회원가입 결과] ${text}`
    } catch (err) {
      resultMessage.value = '회원가입 실패'
    }
  }
  </script>
  
  <style scoped>
  .signup-form {
    max-width: 400px;
    margin: 0 auto;
    padding: 1rem;
  }
  input {
    display: block;
    margin: 0.5rem 0;
    width: 100%;
  }
  button {
    margin: 0.5rem 0;
  }
  </style>
  