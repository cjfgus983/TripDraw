// src/stores/user.ts
import { defineStore } from 'pinia'
import axios from 'axios'

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

    token: localStorage.getItem('token') as string | null,  // ← 추가
    passwordConfirmed: false as boolean,
  }),
  getters: {
    isLoggedIn: (state) => !!state.nickname,
    isPasswordConfirmed: (state) => state.passwordConfirmed,
    authHeader: (state) => state.token ? `Bearer ${state.token}` : null
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

    setToken(token: string) {
      this.token = token
      localStorage.setItem('token', token)
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
    },
    clearToken() {
      this.token = null
      localStorage.removeItem('token')
      delete axios.defaults.headers.common['Authorization']
    },

    confirmPassword() {
      this.passwordConfirmed = true
    },
    resetPasswordConfirmation() {
      this.passwordConfirmed = false
    },

    logout() {
      this.clearProfile()
      this.resetPasswordConfirmation()
      this.clearToken()
    }
  }
})
