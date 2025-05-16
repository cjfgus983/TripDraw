import { defineStore } from 'pinia'

import axios from 'axios'

// 1) PlaceInfo 타입 선언
interface PlaceInfo {
  name: string
  description: string
  imageUrl: string
}

// (백엔드에서 내려오는 형태)
interface BackendPlace {
  name: string
  description: string
  photoReference: string
}

// stores/ai.ts
export const useAiStore = defineStore('ai', {
  state: () => ({
    originalDataUrl: '' as string,
    originalBlob: null as Blob | null,
    resultUrl: '' as string,
    // 여기를 추가합니다:
    recommendedPlaces: [] as PlaceInfo[],
  }),
  actions: {
    setOriginalDataUrl(dataUrl: string) { this.originalDataUrl = dataUrl },
    setOriginalBlob(blob: Blob) { this.originalBlob = blob },
    setResultUrl(url: string) { this.resultUrl = url },
     // 3) PlaceInfo[]를 받을 수 있도록 시그니처 수정
    setRecommendedPlaces(places: PlaceInfo[]) {
        this.recommendedPlaces = places
      },
  
      // 4) 백엔드에서 PlaceInfo[]를 받아와 state에 넣는 액션 추가
      async fetchRecommendedPlaces() {
        // 1) 백엔드에서 photoReference까지 내려주는 DTO를 받고
              const { data } = await axios.post<BackendPlace[]>(
                '/api/ai/recommend',
                { imageUrl: this.resultUrl }
              )
        
              // 2) photoReference를 our proxy URL로 바꿔서 PlaceInfo[] 생성
              const places: PlaceInfo[] = data.map(p => ({
                name: p.name,
                description: p.description,
                imageUrl: `/api/places/photo?reference=${encodeURIComponent(p.photoReference)}`
          }))
          // 3) state에 저장
          this.setRecommendedPlaces(places)
        }
        
  },
})

