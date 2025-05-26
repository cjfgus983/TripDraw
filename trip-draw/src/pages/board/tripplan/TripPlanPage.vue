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
                class="relative p-3 border border-gray-200 rounded-lg hover:shadow-sm cursor-move bg-white"
                draggable="true"
                @dragstart="handleDragStart(idx)"
                @dragover.prevent="handleDragOver(idx)"
                @drop.prevent="handleDrop(idx)"
                @dragend="handleDragEnd"
                @click="onListItemClick(idx)"
              >
              <button
                @click.stop="removeItem(idx)"
                class="absolute bottom-2 right-2 text-gray-400 hover:text-red-500"
                title="삭제"
              >
                삭제
              </button>
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
                @click="openPasteModal"
                class="px-6 py-3 bg-gray-100 hover:bg-gray-200 text-gray-700 rounded-button whitespace-nowrap cursor-pointer transition-colors flex items-center"
              >
                <i class="fas fa-clipboard mr-2"></i>
                붙여넣기
              </button>
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
            <button
            @click="addToMyPlan"
             :class="[
               'ml-4 block px-6 py-3 rounded whitespace-nowrap transition-colors',
               dayItems.length > 0
                ? 'bg-[#9FB3DF] text-white hover:bg-[#8FA3CF]'
                : 'bg-gray-200 text-gray-500 cursor-not-allowed'
             ]"
             :disabled="dayItems.length === 0"
           >
             나의 계획에 추가
           </button>
          </div>
          <!-- 붙여넣기 모달 -->
            <div
              v-if="showPasteModal"
              class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
            >
              <div class="bg-white rounded-lg w-[500px] shadow-xl">
                <div class="p-6 border-b border-gray-200">
                  <h3 class="text-lg font-semibold text-gray-800">
                    내용 붙여넣기
                  </h3>
                </div>
                <div class="p-6">
                  <textarea
                    v-model="pasteContent"
                    rows="6"
                    class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-[#9FB3DF] focus:border-[#9FB3DF]"
                    placeholder="내용을 붙여넣으세요"
                  ></textarea>
                </div>
                <div
                  class="p-6 border-t border-gray-200 flex justify-end gap-3"
                >
                  <button
                    @click="closePasteModal"
                    class="px-4 py-2 bg-gray-100 hover:bg-gray-200 text-gray-700 rounded-button whitespace-nowrap"
                  >
                    취소
                  </button>
                  <button
                    @click="handlePaste"
                    class="px-4 py-2 bg-[#9FB3DF] hover:bg-[#8FA3CF] text-white rounded-button whitespace-nowrap"
                  >
                    붙여넣기
                  </button>
                </div>
              </div>
            </div>
        </div>
      </div>
      <!-- 추가 카테고리 및 입력 섹션 -->
      <div class="mt-8 bg-white rounded-lg shadow-md p-6">
        <div class="mb-6">
          <h3 class="text-lg font-semibold mb-4">카테고리 선택</h3>
          <div class="flex flex-wrap gap-3">
            <button
              v-for="tag in additionalTags"
              :key="tag.id"
              @click="toggleTag(tag.id)"
              :class="[
'px-4 py-2 rounded-button whitespace-nowrap cursor-pointer transition-all',
selectedTags.includes(tag.id)
? 'bg-[#9FB3DF] text-white'
: 'bg-gray-100 hover:bg-gray-200'
]"
            >
              {{ tag.name }}
            </button>
          </div>
        </div>
        <div class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2"
              >제목</label
            >
            <input
              type="text"
              v-model="title"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-[#9FB3DF] focus:border-[#9FB3DF]"
              placeholder="제목을 입력하세요"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2"
              >내용</label
            >
            <textarea
              v-model="content"
              rows="20"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-[#9FB3DF] focus:border-[#9FB3DF]"
              placeholder="내용을 입력하세요"
            ></textarea>
          </div>
          <div class="flex justify-end">
            <button
              class="px-6 py-3 bg-[#9FB3DF] text-white rounded-button hover:bg-[#8FA3CF] transition-colors cursor-pointer whitespace-nowrap"
              @click="submitForm"
            >
              등록하기
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="hidden">
    bg-teal-200 bg-indigo-200 bg-rose-200 bg-gray-300 bg-indigo-300 bg-purple-200 bg-green-200 bg-blue-200
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, computed, watch } from 'vue'
import axios from 'axios'
import { getGoogleMapsLoader } from '@/services/googleMapsLoader'
import { useRouter, useRoute } from 'vue-router' 
import { usePlacesStore } from '@/stores/places'
const store = usePlacesStore()
const currentCenter = {
  lat: store.center.latitude,
  lng: store.center.longitude
}
const route          = useRoute()
const selectedSearchPlace = ref<string | null>(null)

/** 일정 아이템 인터페이스 */
interface ItineraryItem {
  name: string
  category: string    // 예: 'TOUR', 'LUNCH', 'CAFE'
  startTime: string
  endTime: string
  photoUrl?: string
}

/** 게시판 등록용 상태 */
const title = ref('')
const content = ref('')
const selectedTags = ref<number[]>([])
const additionalTags = [
  { id: 1, name: '힐링' },
  { id: 2, name: '액티비티' },
  { id: 3, name: '데이트' },
  { id: 4, name: '먹부림' },
  { id: 5, name: '자연' },
  { id: 6, name: '도심' },
  { id: 7, name: '가족' },
  { id: 8, name: '친구' },
  { id: 9, name: '혼자' },
  { id: 10, name: '비즈니스' }
]

/** 태그 토글 */
function toggleTag(tagId: number) {
  const idx = selectedTags.value.indexOf(tagId)
  if (idx === -1) selectedTags.value.push(tagId)
  else selectedTags.value.splice(idx, 1)
}

/** Itinerary → Google Places API type 매핑 */
const itemTypeMap: Record<string, string> = {
  TOUR:         'tourist_attraction',
  BREAKFAST:    'restaurant',
  LUNCH:        'restaurant',
  CAFE:         'cafe',
  DINNER:       'restaurant',
  EVENING_TOUR: 'tourist_attraction',
  EVENING_CAFE: 'cafe',
  LODGING:      'lodging',
  MUSEUM:       'museum',
  THEATER:      'movie_theater',
  SHOPPING_MALL:'shopping_mall',
}

function normalizeCategory(raw: string | undefined): string {
  if (typeof raw !== 'string') {
    return '';      // 혹은 '기타' 등 기본 카테고리
  }
  switch (raw.toLowerCase()) {
    case 'tours':            return 'TOUR'
    case 'restaurants':      return 'LUNCH'
    case 'cafes':            return 'CAFE'
    case 'lodging':
    case 'lodgings':         return 'LODGING'
    case 'museum':
    case 'museums':          return 'MUSEUM'
    case 'theater':
    case 'theaters':
    case 'movie_theater':    return 'MOVIE_THEATER'
    case 'shopping':
    case 'shopping_mall':    return 'SHOPPING_MALL'
    default:                 return raw.toUpperCase()
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
    case 'LODGING':
    case 'LODGINGS':
      return '숙박'
    case 'MUSEUM':
    case 'MUSEUMS':
      return '문화시설'
    case 'THEATER':
    case 'THEATERS':
    case 'MOVIE_THEATER':
      return '공연시설'
    case 'SHOPPING_MALL':
    case 'SHOPPING':
    case 'SHOPPINGS':
      return '쇼핑'
    default:
      return '기타'
  }
}

function getCategoryColor(category: string): string {
  switch (category.toUpperCase()) {
    case 'TOUR':
    case 'EVENING_TOUR':
      return 'indigo-200'   // Soft Indigo
    case 'LUNCH':
    case 'DINNER':
    case 'BREAKFAST':
      return 'rose-200'     // Soft Rose
    case 'CAFE':
    case 'EVENING_CAFE':
      return 'teal-200'     // Soft Teal
    case 'LODGING':
    case 'LODGINGS':
      return 'blue-200'     // Soft Blue
    case 'MUSEUM':
    case 'MUSEUMS':
      return 'indigo-300'   // Slightly deeper Indigo
    case 'THEATER':
    case 'THEATERS':
    case 'MOVIE_THEATER':
      return 'purple-200'   // Soft Purple
    case 'SHOPPING_MALL':
    case 'SHOPPING':
    case 'SHOPPINGS':
      return 'green-200'    // Soft Green
    default:
      return 'gray-300'
  }
}


function getCategoryColorById(id: string): string {
  //console.log(id)
  switch (id) {
    case 'tours':       return 'indigo-200'
    case 'restaurants': return 'rose-200'
    case 'cafes':       return 'teal-200'
    case 'lodgings':    return 'blue-200'
    case 'museums':     return 'indigo-300'
    case 'theaters':    return 'purple-200'
    case 'shopping':    return 'green-200'
    default:            return 'gray-300'
  }
}

function getCategoryIcon(key: string): string {
  switch (key.toLowerCase()) {
    case 'tours':
    case 'tour':
      return '/icons/landmark.svg'
    case 'restaurants':
    case 'lunch':
    case 'dinner':
    case 'breakfast':
      return '/icons/utensils.svg'
    case 'cafes':
    case 'cafe':
      return '/icons/coffee.svg'
    case 'lodgings':
    case 'lodging':
      return '/icons/bed.svg'
    case 'museums':
    case 'museum':
      return '/icons/museum.svg'
    case 'theaters':
    case 'movie_theater':
      return '/icons/theater.svg'
    case 'shopping':
    case 'shopping_mall':
      return '/icons/shopping.svg'
    default:
      return ''
  }
}

const categories = [
  { id: 'tours',       type: 'tourist_attraction', label: '관광지',    icon: '/icons/landmark.svg'   },
  { id: 'restaurants', type: 'restaurant',         label: '음식점',    icon: '/icons/utensils.svg'    },
  { id: 'cafes',       type: 'cafe',               label: '카페',      icon: '/icons/coffee.svg'      },
  { id: 'lodgings',    type: 'lodging',           label: '숙박',      icon: '/icons/bed.svg'         },
  { id: 'museums',     type: 'museum',            label: '문화시설',  icon: '/icons/museum.svg'      },
  { id: 'theaters',    type: 'movie_theater',     label: '공연시설',  icon: '/icons/theater.svg'     },
  { id: 'shopping',    type: 'shopping_mall',     label: '쇼핑',      icon: '/icons/shopping.svg'    }
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

        const opts: google.maps.MarkerOptions = {
          map,
          position: results[0].geometry.location,
          title: place.name
        };

        // 커스텀 아이콘이 빈 문자열이 아닐 때만 붙인다
        if (iconUrl) {
          opts.icon = {
            url: iconUrl,
            scaledSize: new google.maps.Size(32, 32)
          }
        }
        // photoRef 를 클로저에 캡처해 두고
      const photoUrl = place.photoRef
        ? buildPhotoUrl(place.photoRef)
        : undefined;


        const marker = new google.maps.Marker(opts)

        // const marker = new google.maps.Marker({
        //   map,
        //   position: results[0].geometry.location,
        //   title: place.name,
        //   icon: {
        //     url: iconUrl,
        //     scaledSize: new google.maps.Size(32, 32)
        //   }
        // })

        marker.addListener('click', () => {
          console.log(photoUrl)
          openInfo(marker, {
            name: place.name,
            category: normalizedCategory, // 여기도 정확히!
            startTime: '',
            endTime: '',
            photoUrl
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

const nearbyPlaces = ref<Record<string, any[]>>({
  tours: [],restaurants: [], cafes: [], lodgings: [], museums: [], theaters: [], shopping: []
})
const mapContainer = ref<HTMLDivElement | null>(null)
const searchInput = ref<HTMLInputElement|null>(null)
const isModified = ref(false)
const userId = ref<number | null>(null);
let map: google.maps.Map
let placesService: google.maps.places.PlacesService
let geocoder: google.maps.Geocoder
let markers: google.maps.Marker[] = []
let infoWindow: google.maps.InfoWindow
let polyline: google.maps.Polyline | null = null

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
  let html = `
    <div style="min-width:200px; padding:8px">
      <h3 style="margin:0 0 4px">${item.name}</h3>
      <p style="margin:0 0 8px; font-size:0.875em">
        ${item.category} | ${item.startTime}–${item.endTime}
      </p>`;

  if (item.photoUrl) {
    html += `<img src="${item.photoUrl}"
                  style="width:100%; height:auto; margin-bottom:8px;"
                  alt="${item.name} 사진" />`;
  }

  html += `<button id="add-route-btn" style="
              display:block;
              width:100%;
              padding:6px 0;
              background:#9FB3DF;
              color:#fff;
              border:none;
              border-radius:4px;
              cursor:pointer;
            ">경로에 추가하기</button>
    </div>`;

  infoWindow.setContent(html);
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
  axios.get('http://localhost:8080/api/nearby', {
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
  const token = localStorage.getItem('accessToken')

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

  const code = String(route.params.planCode || '').trim()
  if (code) {
    // 붙여넣기 모달 대신 바로 불러오기
    loadPlan(code)
  }

  // SearchBox 설정
  if (searchInput.value) {
    const searchBox = new google.maps.places.SearchBox(searchInput.value)
    // 입력창에 엔터 또는 추천 목록에서 선택 시
    searchBox.addListener('places_changed', () => {
      const places = searchBox.getPlaces()
      if (!places || places.length === 0) return
      selectedSearchPlace.value = places[0].name || null

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
  const resp = await axios.get('http://localhost:8080/api/nearby', {
    params: { lat: currentCenter.lat, lng: currentCenter.lng }
  })
  nearbyPlaces.value = resp.data

  console.log(token)
  if (!token) return
    try {
    const { data } = await axios.get(
      'http://localhost:8080/api/users/me',
      { headers: { Authorization: `Bearer ${token}` } }
    )
    userId.value = data.userId
    console.log(userId.value)
  } catch {
    // 토큰 만료 등 에러 처리
    //localStorage.removeItem('accessToken')
    //nickname.value = null
  }
})
async function fetchItinerary() {
  const token = localStorage.getItem('accessToken')
  if (!token) {
    alert('로그인이 필요합니다.')
    return
  }

  isLoading.value = true

  try {
    const basePlace = selectedSearchPlace.value || store.center.name

    const resp = await axios.post<ItineraryItem[][]>(
      'http://localhost:8080/api/itinerary',
      { places: [ basePlace ], days: dayOption.value},
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
      }, 1000)  
    })
  }
}
const router = useRouter()                       // ← 추가

// “나의 계획에 추가” 클릭 핸들러
async function addToMyPlan() {
  // 배열에 최소 하나 있어야 눌렀을 때만 동작
  if (dayItems.value.length === 0) return
  
const token = localStorage.getItem('accessToken')
if (!token) {
  alert('로그인이 필요합니다.')
  return
}

const firstName = itinerary.value[0]?.[0]?.name || "";
  const [ , planRegion = "" ] = firstName.split(",", 2).map(s => s.trim());

// 1) 전체 itinerary를 dayNo 포함한 locations 배열로 변환
const locations = itinerary.value.flatMap((day, idx) => 
  day.map(item => ({
    dayNo: idx + 1,
    addressName: item.name,
    addressCategory: item.category,
    startTime: item.startTime + ':00',
    endTime:   item.endTime   + ':00'
  }))
)

try {
  // 2) API 호출
  const resp = await axios.post(
    'http://localhost:8080/api/plans-with-locations',
    {
      userId: userId.value,  // 또는 로컬스토리지에서 꺼내든지
      region: planRegion,
      locations
    },
    { headers: { Authorization: `Bearer ${token}` } }
  )
  // 3) 성공 처리
  alert('추가되었습니다.')
  router.push('/mypage')
} catch (err) {
  console.error(err)
  alert('추가에 실패했습니다.')
}                       // ← 이동할 라우트로 변경
}

function removeItem(idx: number) {
  // 1) dayItems에서 삭제
  dayItems.value.splice(idx, 1);
  // 2) itinerary에도 반영
  itinerary.value[selectedDay.value] = [...dayItems.value];
  // 3) 변경 플래그
  isModified.value = true;
}
async function submitForm() {
  const token = localStorage.getItem('accessToken')
  if (!token) {
    alert('로그인이 필요합니다.')
    return
  }

  const firstName = itinerary.value[0]?.[0]?.name || "";
  const [ , planRegion = "" ] = firstName.split(",", 2).map(s => s.trim());

  // 1) itinerary → locations 변환
  const locations = itinerary.value.flatMap((day, idx) =>
    day.map(item => ({
      dayNo: idx + 1,
      addressName:     item.name,
      addressCategory: item.category,
      startTime:       item.startTime + ':00',
      endTime:         item.endTime   + ':00'
    }))
  )

  // 1) 선택된 태그 ID 배열 → 태그 이름 배열
  const tagNames = selectedTags.value
    .map(id => additionalTags.find(t => t.id === id)?.name)
    .filter((n): n is string => !!n)  // undefined 걸러내기
  try {
    // --- 1단계: 계획 + 장소들 생성 ---
    const planResp = await axios.post<string>(
      'http://localhost:8080/api/plans-with-locations',
      {
        userId: userId.value,
        region: planRegion,
        locations
      },
      { headers: { Authorization: `Bearer ${token}` } }
    )
    const planCode = planResp.data  // 백엔드에서 반환한 plan_code

    // --- 2단계: 게시글 등록 ---
    await axios.post(
      'http://localhost:8080/api/trip/boards',
      {
        planCode,
        userId:        userId.value,
        boardTitle:    title.value,
        boardContent:  content.value,
        boardCategory: tagNames.join(',')
        //boardCategory: selectedTags.value.join(',') // 태그를 콤마로 묶어서 문자열로 보내거나,
      },
      { headers: { Authorization: `Bearer ${token}` } }
    )

    alert('게시글이 등록되었습니다.')
    router.push('/mypage')

  } catch (err) {
    console.error(err)
    alert('등록 중 오류가 발생했습니다.')
  }
}
// 붙여넣기 모달 관련
const showPasteModal = ref(false)
const pasteContent   = ref('')

// 모달 열기/닫기
function openPasteModal() {
  showPasteModal.value = true
}
function closePasteModal() {
  pasteContent.value = ''
  showPasteModal.value = false
}

// 붙여넣기 처리 → Plan Code 로 서버 호출
async function handlePaste() {
  const planCode = pasteContent.value.trim()
  if (!planCode) {
    alert('Plan Code를 입력하세요.')
    return
  }

  loadPlan(planCode)
}
/** planCode로 불러오는 공통 함수 **/
async function loadPlan(planCode: string) {
  isLoading.value = true
  try {
    const token = localStorage.getItem('accessToken')
    const { data } = await axios.get<{
      dailyPlans: Array<{
        dayNo: number,
        activities: {
          name: string,
          category: string,
          startTime: string,
          endTime: string
        }[]
      }>
    }>(`http://localhost:8080/api/plans-with-locations/${planCode}`, {
      headers: { Authorization: `Bearer ${token}` }
    })

    // 서버 → itinerary 포맷 변환
    itinerary.value = data.dailyPlans
      .sort((a, b) => a.dayNo - b.dayNo)
      .map(day =>
        day.activities.map(act => ({
          name:      act.name,
          category:  act.category,
          startTime: act.startTime.slice(0,5),
          endTime:   act.endTime.slice(0,5)
        }))
      )

    selectedDay.value = 0
    dayItems.value   = itinerary.value[0] || []
    updateMarkers()
    closePasteModal()
  } catch (e) {
    console.error('Plan Code 로드 실패', e)
    alert('유효한 Plan Code가 아닙니다.')
  } finally {
    isLoading.value = false
  }
}
function buildPhotoUrl(photoRef: string) {
  const maxWidth = 300;
  return `https://maps.googleapis.com/maps/api/place/photo`
       + `?maxwidth=${maxWidth}`
       + `&photoreference=${photoRef}`
       + `&key=${import.meta.env.VITE_GOOGLE_MAPS_API_KEY}`;
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
    