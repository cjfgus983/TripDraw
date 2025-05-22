import { defineStore } from 'pinia'

export interface Place {
  id: string
  name: string
  latitude: number
  longitude: number
  rating: number
  address?: string
}

export const usePlacesStore = defineStore('places', {
  state: () => ({
    // 초기 중심 위치
    center: { name: '후지산, 일본', latitude: 35.36, longitude: 138.728 },
    // 버튼별 검색 결과
    nearby: {
      cafe: [] as Place[],
      restaurant: [] as Place[],
      tourist_attraction: [] as Place[]
    }
  }),
  actions: {
    setNearby(type: keyof typeof this.nearby, list: Place[]) {
      this.nearby[type] = list
    }
  }
})