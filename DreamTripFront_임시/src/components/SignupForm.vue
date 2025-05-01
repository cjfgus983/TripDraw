<template>
  <div class="signup-container">
    <h2>회원 가입</h2>

    <!-- 이메일 인증 -->
    <div class="form-group">
      <input v-model="email" placeholder="이메일 입력" />
      <button @click="sendCode">이메일 인증</button>
    </div>

    <!-- 인증 코드 입력 + 확인 -->
    <div class="form-group">
      <input v-model="code" placeholder="인증 코드 입력" />
      <button @click="verifyCode">코드 확인</button>
    </div>

    <!-- 비밀번호 -->
    <input v-model="password" type="password" placeholder="비밀번호" />
    <input v-model="confirmPassword" type="password" placeholder="비밀번호 확인" />

    <!-- 닉네임 + 중복 확인 -->
    <div class="form-group">
      <input v-model="nickname" placeholder="닉네임 입력" />
      <button @click="checkNickname">중복 확인</button>
    </div>

    <!-- 약관 동의 -->
    <div class="checkbox-group">
      <label><input type="checkbox" v-model="agreeService" /> 서비스 이용약관 및 개인정보 처리 방침 동의</label>
      <label><input type="checkbox" v-model="agreeMarketing" /> 마케팅 정보 수신 동의 (선택)</label>
      <label><input type="checkbox" v-model="agreeAll" @change="toggleAllAgreements" /> 전체 동의</label>
    </div>

    <button class="submit-btn" @click="signup">회원 가입 완료</button>

    <p>{{ resultMessage }}</p>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const email = ref('')
const password = ref('')
const confirmPassword = ref('')
const nickname = ref('')
const code = ref('')
const resultMessage = ref('')
const codeVerified = ref(false)

const agreeService = ref(true)
const agreeMarketing = ref(true)
const agreeAll = ref(true)

const toggleAllAgreements = () => {
  agreeService.value = agreeAll.value
  agreeMarketing.value = agreeAll.value
}

const sendCode = async () => {
  try {
    const res = await fetch('http://localhost:8080/api/send-code', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email: email.value })
    })
    const text = await res.text()
    resultMessage.value = `[이메일 인증] ${text}`
    codeVerified.value = false // 인증 다시 필요
  } catch (err) {
    resultMessage.value = '이메일 인증 실패'
  }
}

const verifyCode = async () => {
  try {
    const res = await fetch('http://localhost:8080/api/verify-code', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email: email.value, code: code.value })
    })
    const text = await res.text()
    if (res.ok) {
      resultMessage.value = `[인증 성공] ${text}`
      codeVerified.value = true
    } else {
      resultMessage.value = `[인증 실패] ${text}`
      codeVerified.value = false
    }
  } catch (err) {
    resultMessage.value = '코드 확인 중 오류 발생'
    codeVerified.value = false
  }
}

const checkNickname = async () => {
  try {
    const res = await fetch(`http://localhost:8080/api/check-nickname?nickname=${nickname.value}`)
    const text = await res.text()
    resultMessage.value = `[닉네임 확인] ${text}`
  } catch (err) {
    resultMessage.value = '닉네임 중복 확인 실패'
  }
}

const signup = async () => {
  if (!codeVerified.value) {
    resultMessage.value = '이메일 인증을 완료해주세요.'
    return
  }

  if (password.value !== confirmPassword.value) {
    resultMessage.value = '비밀번호가 일치하지 않습니다.'
    return
  }

  if (!agreeService.value) {
    resultMessage.value = '서비스 이용약관에 동의해주세요.'
    return
  }

  try {
    const res = await fetch('http://localhost:8080/api/signup', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        email: email.value,
        password: password.value,
        code: code.value,
        nickname: nickname.value
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
.signup-container {
  max-width: 400px;
  margin: 0 auto;
  text-align: center;
}
.logo {
  width: 100px;
  margin: 1rem auto;
}
.form-group {
  display: flex;
  gap: 8px;
  margin: 1rem 0;
}
input {
  width: 100%;
  padding: 0.5rem;
  margin: 0.4rem 0;
}
button {
  padding: 0.5rem 1rem;
  background-color: #3366ff;
  color: white;
  border: none;
  border-radius: 4px;
}
.submit-btn {
  width: 100%;
  margin-top: 1rem;
}
.checkbox-group {
  text-align: left;
  margin-top: 1rem;
}
</style>
