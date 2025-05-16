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
  }),
  getters: {
    isLoggedIn: (state) => !!state.nickname,
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
    }
  }
})
