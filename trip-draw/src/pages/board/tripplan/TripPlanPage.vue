<template>
  <!-- 로딩 오버레이 -->
  <div v-if="isLoading" class="fixed inset-0 bg-white bg-opacity-70 z-50 flex items-center justify-center">
    <div class="animate-spin rounded-full h-16 w-16 border-4 border-blue-400 border-t-transparent"></div>
  </div>
  <div class="min-h-screen bg-gray-50">
    <div class="container mx-auto px-6 py-8 max-w-7xl">
      <!-- 상단 컨트롤 바 -->
      <div class="flex flex-col md:flex-row md:justify-between md:items-center mb-4 gap-2">
        <!-- 검색바 -->
        <input
          ref="searchInput"
          type="text"
          placeholder="장소를 검색하세요"
          class="w-full md:w-3/5 p-2 border rounded shadow-sm focus:outline-none focus:ring"
        />

        <!-- 오른쪽: 일차 선택 옵션 + 추천 버튼 -->
        <div class="flex items-center space-x-2">
          <select
            v-model="dayOption"
            class="border rounded px-2 py-1 text-sm focus:outline-none focus:ring"
          >
            <option v-for="n in 5" :key="n" :value="n">{{ n }}일차</option>
          </select>

          <button
            @click="fetchItinerary"
            class="bg-[#9FB3DF] text-white px-4 py-2 rounded hover:bg-[#7E97D6]"
          >
            추천 여행경로 받기
          </button>
        </div>
      </div>

      <!-- 카테고리 토글 버튼 + 일차 버튼을 나란히 -->
      <div class="flex justify-between items-center mb-4 flex-wrap gap-2">
        <div class="flex space-x-3">
          <button
            v-for="cat in categories"
            :key="cat.id"
            @click="selectCategory(cat.id)"
            :class="[
              'px-4 py-2 rounded-lg flex items-center gap-2 transition',
              selectedCategory === cat.id
                ? `text-white bg-${getCategoryColorById(cat.id)}`
                : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
            ]"
          >
            <img :src="cat.icon" alt="" class="w-4 h-4" />
            {{ cat.label }}
          </button>
        </div>

        <!-- 일차 선택 버튼들 (위 오른쪽 정렬) -->
        <div class="flex space-x-2">
          <button
            v-for="(day, idx) in itinerary"
            :key="idx"
            @click="onDayClick(idx)"
            :class="[
              'px-4 py-2 rounded',
              selectedDay === idx
                ? 'bg-[#9FB3DF] text-white'
                : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
            ]"
          >
            {{ idx + 1 }}일차
          </button>
        </div>
      </div>

      <div class="flex flex-col md:flex-row gap-6">
        <!-- 지도 -->
        <div class="w-full md:w-3/5">
          <div class="bg-white rounded-lg shadow-md p-4 mb-4">
            <div ref="mapContainer" class="w-full h-[600px] rounded-lg overflow-hidden"></div>
          </div>
        </div>

        <!-- 드래그 가능한 리스트 -->
        <div class="w-full md:w-2/5 flex flex-col">
          <div class="bg-white rounded-lg shadow-md flex-1 flex flex-col max-h-[668px]">
            <!-- 상단: 선택된 일차 텍스트 -->
            <div class="p-4 border-b border-gray-200">
              <h2 class="text-xl font-semibold text-gray-800">
                {{ selectedDay + 1 }}일차 일정
              </h2>
            </div>

            <!-- 수동 드래그 리스트 영역 -->
            <div class="flex-1 overflow-y-auto p-4 space-y-2">
              <div
                v-for="(item, idx) in dayItems"
                :key="item.name + item.startTime"
                class="p-3 border border-gray-200 rounded-lg hover:shadow-sm cursor-move bg-white"
                draggable="true"
                @dragstart="handleDragStart(idx)"
                @dragover.prevent="handleDragOver(idx)"
                @drop.prevent="handleDrop(idx)"
                @dragend="handleDragEnd"
                @click="onListItemClick(idx)"
              >
                <div class="flex justify-between items-center">
                  <span class="font-medium text-gray-800">{{ item.category }}</span>
                  <span class="text-sm text-gray-600">{{ item.startTime }} – {{ item.endTime }}</span>
                </div>
                <div class="mt-1 text-gray-700">{{ item.name }}</div>
                <!-- 카테고리 라벨 -->
                <div class="flex items-center mt-2">
                  <span
                    class="inline-block w-3 h-3 rounded-full mr-2"
                    :class="`bg-${getCategoryColor(normalizeCategory(item.category))}`"></span>
                  <span class="text-sm text-gray-600">
                    {{ getCategoryName(normalizeCategory(item.category)) }}
                  </span>
                </div>
                <!-- 드래그 오버 시 상단 표시 -->
                <div
                  v-if="dragOverIndex === idx && dragStartIndex !== idx"
                  class="absolute top-0 left-0 w-full h-1 bg-blue-500"
                ></div>
              </div>
            </div>
          </div>
          <!-- 하단: 적용하기 버튼 -->
          <div class="mt-4 flex justify-end">
            <button
              @click="applyChanges"
              :disabled="!isModified"
              :class="[
                'ml-auto block px-6 py-3 rounded whitespace-nowrap transition-colors',
                isModified ? 'bg-[#9FB3DF] text-white hover:bg-[#8FA3CF]' : 'bg-gray-200 text-gray-500 cursor-not-allowed'
              ]"
            >
              적용하기
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="hidden">
    bg-teal-200 bg-indigo-200 bg-rose-200 bg-gray-300
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, computed, watch } from 'vue'
import axios from 'axios'
import { getGoogleMapsLoader } from '@/services/googleMapsLoader'
import { usePlacesStore } from '@/stores/places'
const store = usePlacesStore()
const currentCenter = {
  lat: store.center.latitude,
  lng: store.center.longitude
}

/** 일정 아이템 인터페이스 */
interface ItineraryItem {
  name: string
  category: string    // 예: 'TOUR', 'LUNCH', 'CAFE'
  startTime: string
  endTime: string
}

/** Itinerary → Google Places API type 매핑 */
const itemTypeMap: Record<string, string> = {
  TOUR:         'tourist_attraction',
  BREAKFAST:    'restaurant',
  LUNCH:        'restaurant',
  CAFE:         'cafe',
  DINNER:       'restaurant',
  EVENING_TOUR: 'tourist_attraction',
  EVENING_CAFE: 'cafe'
}

function getCategoryColor(category: string): string {
  
  switch (category.toUpperCase()) {
    case 'TOUR':
    case 'EVENING_TOUR':
      return 'indigo-200'  // Soft Indigo
    case 'LUNCH':
    case 'DINNER':
    case 'BREAKFAST':
      return 'rose-200'    // Soft Rose
    case 'CAFE':
    case 'EVENING_CAFE':
      return 'teal-200'    // Soft Teal
    default:
      return 'gray-300'
  }
}

function normalizeCategory(raw: string): string {
  switch (raw.toLowerCase()) {
    case 'tours': return 'TOUR'
    case 'restaurants': return 'LUNCH'
    case 'cafes': return 'CAFE'
    default: return raw.toUpperCase()
  }
}

function getCategoryIcon(category: string): string {
  switch (category.toUpperCase()) {
    case 'TOUR': return '/icons/landmark.svg'
    case 'LUNCH':
    case 'DINNER':
    case 'BREAKFAST': return '/icons/utensils.svg'
    case 'CAFE':
    case 'EVENING_CAFE': return '/icons/coffee.svg'
    default: return ''
  }
}


function getCategoryName(category: string): string {
  switch (category.toUpperCase()) {
    case 'TOUR':
    case 'EVENING_TOUR':
      return '관광지'
    case 'LUNCH':
    case 'DINNER':
    case 'BREAKFAST':
      return '음식점'
    case 'CAFE':
    case 'EVENING_CAFE':
      return '카페'
    default:
      return '기타'
  }
}

function getCategoryColorById(id: string): string {
  switch (id) {
    case 'tours': return 'indigo-200'  // 관광지
    case 'restaurants': return 'rose-200'  // 음식점
    case 'cafes': return 'teal-200'    // 카페
    default: return 'gray-300'
  }
}


const categories = [
  { id: 'tours',       type: 'tourist_attraction', label: '관광지', icon: '/icons/landmark.svg' },
  { id: 'restaurants', type: 'restaurant',         label: '음식점', icon: '/icons/utensils.svg' },
  { id: 'cafes',       type: 'cafe',               label: '카페',    icon: '/icons/coffee.svg' }
]

const selectedCategory = ref<string | null>(null)

function selectCategory(catId: string) {
  selectedCategory.value = selectedCategory.value === catId ? null : catId
  refreshMarkers()
}

function refreshMarkers() {
  clearMarkers()

  if (polyline) {
  polyline.setMap(null)
  polyline = null
}


  if (!selectedCategory.value) return

  const rawCategory = selectedCategory.value || ''
  const normalizedCategory = normalizeCategory(rawCategory)
  const list = nearbyPlaces.value[rawCategory] || []

  list.forEach(place => {
    geocoder.geocode({ address: place.description }, (results, status) => {
      if (status === google.maps.GeocoderStatus.OK && results?.[0]) {
        const iconUrl = getCategoryIcon(normalizedCategory)

        const marker = new google.maps.Marker({
          map,
          position: results[0].geometry.location,
          title: place.name,
          icon: {
            url: iconUrl,
            scaledSize: new google.maps.Size(32, 32)
          }
        })

        marker.addListener('click', () => {
          openInfo(marker, {
            name: place.name,
            category: normalizedCategory, // 여기도 정확히!
            startTime: '',
            endTime: ''
          })
        })

        markers.push(marker)
      }
    })
  })

  // 3) 찍힌 마커 전부를 포함하도록 줌·센터 조정
  // (geocode 콜백 안에서 비동기이므로, 약간 딜레이를 줄 수도 있습니다)
  setTimeout(() => {
    if (markers.length === 0) return
    const bounds = new google.maps.LatLngBounds()
    markers.forEach(m => {
      if (m.getPosition()) bounds.extend(m.getPosition()!)
    })
    map.fitBounds(bounds)
  }, 500)
}

const itinerary = ref<ItineraryItem[][]>([])
const selectedDay = ref(0)
const dayItems = ref<ItineraryItem[]>([])
const isLoading = ref(false)

// 일차 선택 옵션 (v-model)
const dayOption = ref(1)

const dragStartIndex = ref<number | null>(null)
const dragOverIndex = ref<number | null>(null)

function onDayClick(idx: number) {
  selectedDay.value = idx
  dayItems.value = itinerary.value[idx] || []
}

function handleDragStart(idx: number) {
  dragStartIndex.value = idx
}

function handleDragOver(idx: number) {
  dragOverIndex.value = idx
}

function handleDrop(idx: number) {
  if (dragStartIndex.value === null) return
  const items = [...dayItems.value]
  const moved = items.splice(dragStartIndex.value, 1)[0]
  items.splice(idx, 0, moved)
  dayItems.value = items
  itinerary.value[selectedDay.value] = items
  dragStartIndex.value = null
  dragOverIndex.value = null
}

function handleDragEnd() {
  dragStartIndex.value = null
  dragOverIndex.value = null
}

const nearbyPlaces = ref<{ [key: string]: any[]; cafes: any[]; restaurants: any[]; tours: any[] }>({
  cafes: [], restaurants: [], tours: []
})
const mapContainer = ref<HTMLDivElement | null>(null)
const searchInput = ref<HTMLInputElement|null>(null)
const isModified = ref(false)
let map: google.maps.Map
let placesService: google.maps.places.PlacesService
let geocoder: google.maps.Geocoder
let markers: google.maps.Marker[] = []
let infoWindow: google.maps.InfoWindow
let polyline: google.maps.Polyline | null = null

// /** 선택된 카테고리 (PlaceType) */
// const categories = [
//   'tourist_attraction',
//   'restaurant',
//   'cafe'
// ] as const
// type Cat = typeof categories[number]
// const selectedCategory = ref<Cat | null>(null)

// /** 버튼 클릭: 토글 */
// function selectCategory(catId: Cat) {
//   selectedCategory.value = selectedCategory.value === catId ? null : catId
//   refreshMarkers()
// }

// /** 마커 갱신 */
// function refreshMarkers() {
//   // 1) 기존 마커 지우기
//   markers.forEach(m => m.setMap(null))
//   markers = []

//   // 2) 해당 타입만 검색 (selectedCategory가 null이면 전체 dayItems)
//   const toSearch = dayItems.value.filter(item => {
//     const type = itemTypeMap[item.category]
//     return !selectedCategory.value || type === selectedCategory.value
//   })

//   toSearch.forEach(item => {
//     const type = itemTypeMap[item.category]
//     placesService.nearbySearch(
//       {
//         location: new google.maps.LatLng(currentCenter.lat, currentCenter.lng),
//         radius: 5000,
//         type
//       },
//       (results, status) => {
//         if (
//           status === google.maps.places.PlacesServiceStatus.OK &&
//           Array.isArray(results) &&
//           results.length > 0
//         ) {
//           const place = results[0]
//           if (place.geometry?.location) {
//             const m = new google.maps.Marker({
//               map,
//               position: place.geometry.location,
//               title: item.name
//             })
//             markers.push(m)
//           }
//         }
//       }
//     )
//   })
// }

function clearMarkers() {
  markers.forEach(m => m.setMap(null))
  markers = []
}

function onListItemClick(idx: number) {
  const marker = markers[idx]
  if (marker) {
    map.panTo(marker.getPosition()!)
    openInfo(marker, dayItems.value[idx])
  }
}

function openInfo(marker: google.maps.Marker, item: ItineraryItem) {
  const content = `
    <div style="min-width:180px; padding:8px">
      <h3 style="margin:0 0 4px">${item.name}</h3>
      <p style="margin:0 0 8px; font-size:0.875em;">
        ${item.category} | ${item.startTime}–${item.endTime}
      </p>
      <button id="add-route-btn" style="
        display:block;
        width:100%;
        padding:6px 0;
        background:#9FB3DF;
        color:#fff;
        border:none;
        border-radius:4px;
        cursor:pointer;
      ">
        경로에 추가하기
      </button>
    </div>`
  infoWindow.setContent(content)
  infoWindow.open({ anchor: marker, map })

  // infoWindow의 DOM이 렌더링 된 뒤 이벤트 바인딩
  google.maps.event.addListenerOnce(infoWindow, 'domready', () => {
    const btn = document.getElementById('add-route-btn')
    btn?.addEventListener('click', () => {
      // dayItems에 추가
      dayItems.value.push({
        name: item.name,
        category: item.category,
        startTime: item.startTime,
        endTime: item.endTime
      })
      infoWindow.close()
    })
  })
}
function fetchNearbyByCenter(lat: number, lng: number) {
  axios.get('/api/nearby', {
    params: { lat, lng }
  }).then(resp => {
    nearbyPlaces.value = resp.data
  }).catch(e => {
    console.error('주변 장소 요청 실패:', e)
  })
}

async function updateMarkers() {
  if (!map || !placesService) return
  clearMarkers()
  if (polyline) polyline.setMap(null)

  for (const item of dayItems.value) {
    const results = await new Promise<google.maps.places.PlaceResult[]>((resolve) => {
      placesService.textSearch({ query: item.name }, (res, status) => {
        resolve(status === google.maps.places.PlacesServiceStatus.OK && res ? res : [])
      })
    })
    const place = results[0]
    if (place?.geometry?.location) {
      const marker = new google.maps.Marker({
        map,
        position: place.geometry.location,
        title: item.name,
        label: { text: item.startTime, color: 'white', fontWeight: 'bold' }
      })
      marker.addListener('click', () => openInfo(marker, item))
      markers.push(marker)
    }
  }

  const path = markers.map(m => ({ lat: m.getPosition()!.lat(), lng: m.getPosition()!.lng() }))
  polyline = new google.maps.Polyline({ path, geodesic: true, strokeColor: '#3366FF', strokeOpacity: 0.8, strokeWeight: 4 })
  polyline.setMap(map)

  const bounds = new google.maps.LatLngBounds()
  path.forEach(p => bounds.extend(p))
  map.fitBounds(bounds)
}

watch(selectedDay, updateMarkers)

watch(() => dayItems.value, () => {
  isModified.value = true
}, { deep: true })

async function applyChanges() {
  if (isModified.value) {
    const token = localStorage.getItem('accessToken')
    if (!token) {
      alert('로그인이 필요합니다.')
      return
    }

    isLoading.value = true;
      try {
        const resp = await axios.post(
          'http://localhost:8080/api/itinerary/update-times',
          { itinerary: itinerary.value },
          { headers: { Authorization: `Bearer ${token}` } }
        );
        itinerary.value = resp.data;
        dayItems.value = itinerary.value[selectedDay.value] || [];
        await updateMarkers();
      } catch (e) {
        alert("일정 재계산에 실패했습니다.");
        console.error(e);
      } finally {
      // 브라우저가 마커와 선을 렌더링할 시간 확보
        requestAnimationFrame(() => {
          setTimeout(() => {
            isLoading.value = false
          }, 800)  
        })
        isModified.value = false
      }    
  }
}

onMounted(async () => {
  const loader = getGoogleMapsLoader()
  const google = await loader.load()

  // 1) 지도를 Pinia 의 center 로 초기화
  map = new google.maps.Map(mapContainer.value!, {
    center: {
      lat: store.center.latitude,
      lng: store.center.longitude
    },
    zoom: 12
  })
  placesService = new google.maps.places.PlacesService(map)
  geocoder = new google.maps.Geocoder()
  infoWindow = new google.maps.InfoWindow()
  infoWindow = new google.maps.InfoWindow()

  // SearchBox 설정
  if (searchInput.value) {
    const searchBox = new google.maps.places.SearchBox(searchInput.value)
    // 입력창에 엔터 또는 추천 목록에서 선택 시
    searchBox.addListener('places_changed', () => {
      const places = searchBox.getPlaces()
      if (!places || places.length === 0) return

      // 이전 마커 제거
      clearMarkers()

      const bounds = new google.maps.LatLngBounds()
      places.forEach(p => {
        if (!p.geometry?.location) return

        const item = {
          name: p.name || '이름 없음',
          category: 'TOUR',  // 또는 기본값
          startTime: '',
          endTime: ''
        }

        const marker = new google.maps.Marker({
          map,
          position: p.geometry.location,
          title: p.name
        })

        marker.addListener('click', () => {
          openInfo(marker, item)
        })

        openInfo(marker, item)  // ← 생성하자마자 info 창 열기
        markers.push(marker)
        bounds.extend(p.geometry.location)

        fetchNearbyByCenter(p.geometry.location.lat(), p.geometry.location.lng())
      })
      // 검색된 장소 모두 보이도록 화면 조정
      map.fitBounds(bounds)
    })

    // 입력창에 포커스 시, 지도의 바운드 내로 자동 제한
    map.addListener('bounds_changed', () => {
      searchBox.setBounds(map.getBounds() as google.maps.LatLngBounds)
    })
  }

  // 2) Pinia center 위치에 마커 하나
  const centerMarker = new google.maps.Marker({
  map,
  position: {
    lat: store.center.latitude,
    lng: store.center.longitude
    },
    title: store.center.name
  })

    centerMarker.addListener('click', () => {
      openInfo(centerMarker, {
        name: store.center.name,
        category: 'TOUR',
        startTime: '',
        endTime: ''
      })
    })
  const resp = await axios.get('/api/nearby', {
    params: { lat: currentCenter.lat, lng: currentCenter.lng }
  })
  nearbyPlaces.value = resp.data
})
async function fetchItinerary() {
  const token = localStorage.getItem('accessToken')
  if (!token) {
    alert('로그인이 필요합니다.')
    return
  }

  isLoading.value = true

  try {
    const resp = await axios.post<ItineraryItem[][]>(
      'http://localhost:8080/api/itinerary',
      { places: [ store.center.name ], days: dayOption.value},
      { headers: { Authorization: `Bearer ${token}` } }
    )
    // 1) 일정 데이터 세팅
    itinerary.value = resp.data
    selectedDay.value = 0
    dayItems.value = itinerary.value[0] || []

    // 2) 지도에 일정 경로 & 마커
    updateMarkers()
  } catch (e) {
    console.error(e)
    alert('일정 추천에 실패했습니다.')
  } finally{
    // 브라우저가 마커와 선을 렌더링할 시간 확보
    requestAnimationFrame(() => {
      setTimeout(() => {
        isLoading.value = false
      }, 800)  
    })
  }
}
</script>

<style scoped>
.cursor-move { cursor: move; }
::-webkit-scrollbar { width: 8px; }
::-webkit-scrollbar-track { background: #f1f1f1; border-radius: 10px; }
::-webkit-scrollbar-thumb { background: #c1c1c1; border-radius: 10px; }
::-webkit-scrollbar-thumb:hover { background: #a1a1a1; }
    .map-container {
    position: relative;
    width: 100%;
    height: 100%;
    }
    
    [draggable="true"] {
    cursor: move;
    user-select: none;
    }
    
    [draggable="true"]:hover {
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    }
    /* 스크롤바 스타일 */
    ::-webkit-scrollbar {
    width: 8px;
    }
    ::-webkit-scrollbar-track {
    background: #f1f1f1;
    border-radius: 10px;
    }
    ::-webkit-scrollbar-thumb {
    background: #c1c1c1;
    border-radius: 10px;
    }
    ::-webkit-scrollbar-thumb:hover {
    background: #a1a1a1;
    }
    </style>
    