<template>
    <div class="h-screen flex items-center justify-center bg-gray-100">
      <p class="text-lg text-gray-700">로그인 처리 중입니다…</p>
    </div>
  </template>
  
  <script setup lang="ts">
  import { onMounted } from 'vue'
  import { useRouter } from 'vue-router'
  import axios from 'axios'
  import { useUserStore } from '@/stores/user'
  
  const router = useRouter()
  const userStore = useUserStore()
  
  onMounted(async () => {
    const params = new URLSearchParams(window.location.search)
    const token = params.get('token')
    if (!token) {
      // 토큰이 없으면 로그인 페이지로 이동
      return router.replace({ name: 'Login' })
    }
  
    try {
      // 1) 토큰 저장
      localStorage.setItem('accessToken', token)
  
      // 2) 프로필 조회
      const { data } = await axios.get('/api/users/me', {
        headers: { Authorization: `Bearer ${token}` }
      })
  
      // 3) Pinia 스토어에 프로필 저장
      userStore.setProfile({
        id: data.userId,
        email: data.email,
        nickname: data.nickname
      })
  
      // 4) 홈 페이지로 이동
      router.replace({ name: 'Main' })
    } catch {
      // 실패 시 로그인 페이지로 복귀
      router.replace({ name: 'Login' })
    }
  })
  </script>
  
  <style scoped>
  /* 추가 스타일이 필요하면 여기에 */
  </style>
  