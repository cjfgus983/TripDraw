import { defineStore } from 'pinia'

import axios from 'axios'
import { usePlacesStore } from '@/stores/places'   


// 1) PlaceInfo 타입 선언
interface PlaceInfo {
  name: string
  description: string
  imageUrl: string
  latitude: number
  longitude: number
}

// (백엔드에서 내려오는 형태)
interface BackendPlace {
  name: string
  description: string
  photoURL: string
  latitude: number
  longitude: number

}

// stores/ai.ts
export const useAiStore = defineStore('ai', {
  state: () => ({
    originalDataUrl: '' as string,
    originalBlob: null as Blob | null,
    resultUrl: '' as string,
    // 여기를 추가합니다:
    recommendedPlaces: [] as PlaceInfo[],
    selectedPlaceName: null as string | null,

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
        
              
            // 2) 받아온 imageUrl을 그대로 쓰면 끝
              const places: PlaceInfo[] = data.map(p => ({
                name:        p.name,
                description: p.description,
                imageUrl:    p.photoURL,
                latitude:   p.latitude,
                longitude: p.longitude
              })
            )
          // 3) state에 저장
          this.setRecommendedPlaces(places)
        },
        setSelectedPlaceName(place: PlaceInfo) {
            // 1) ai 스토어 state 갱신
            this.selectedPlaceName = place.name
      
            // 2) places 스토어 인스턴스 가져오기
            const placesStore = usePlacesStore()
        
            // 3) places 스토어의 center 에 name/lat/lng 만 넣기
            placesStore.center = {
              name:      place.name,
              latitude:  place.latitude,
              longitude: place.longitude
            }
          },
    
  },
})