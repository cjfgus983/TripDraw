// src/stores/user.ts
import { defineStore } from 'pinia'

interface UserProfile {
  id: string
  email: string
  nickname: string
}

export const useUserStore = defineStore('user', {
  state: () => ({
    id: null as string | null,
    email: null as string | null,
    nickname: null as string | null,

    // 여기를 추가하세요:
    passwordConfirmed: false as boolean,
  }),
  getters: {
    isLoggedIn: (state) => !!state.nickname,

    // 비밀번호 인증 여부를 쉽게 꺼내 쓰고 싶으면 getter 추가
    isPasswordConfirmed: (state) => state.passwordConfirmed,
  },
  actions: {
    setProfile(profile: UserProfile) {
      this.id = profile.id
      this.email = profile.email
      this.nickname = profile.nickname
    },
    clearProfile() {
      this.id = null
      this.email = null
      this.nickname = null
    },

    // 비밀번호 인증이 성공했을 때 호출
    confirmPassword() {
      this.passwordConfirmed = true
    },
    // (필요시) 인증 상태 리셋
    resetPasswordConfirmation() {
      this.passwordConfirmed = false
    },
  }
})
