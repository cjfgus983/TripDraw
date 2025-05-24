<template>
  <div class="min-h-screen bg-gray-50 font-sans">
    <div class="relative">
      <!-- 첫 번째 섹션: 원본 그림 -->
      <section
        ref="section1"
        class="h-screen w-full flex flex-col items-center justify-center px-6 snap-start"
        :class="{ 'active-section': activeSection === 0 }"
      >
        <div class="text-center mb-8 section-content">
          <h2 class="text-3xl font-bold text-gray-800 mb-2">내가 그린 그림</h2>
          <p class="text-gray-600">당신이 직접 그린 원본 그림입니다.</p>
        </div>
        <div class="relative w-full max-w-3xl h-[500px] rounded-lg overflow-hidden shadow-xl section-content">
          <img
            :src="originalDrawingUrl"
            alt="원본 그림"
            class="w-full h-full object-cover object-top"/>
        </div>
      </section>

      <!-- 두 번째 섹션: AI 변환 그림 -->
      <section
        ref="section2"
        class="h-screen w-full flex flex-col items-center justify-center px-6 snap-start"
        :class="{ 'active-section': activeSection === 1 }"
      >
        <div class="text-center mb-8 section-content">
          <h2 class="text-3xl font-bold text-gray-800 mb-2">AI가 변환한 그림</h2>
          <p class="text-gray-600">AI가 당신의 그림을 아름답게 변환했습니다.</p>
        </div>
        <div class="relative w-full max-w-3xl h-[500px] rounded-lg overflow-hidden shadow-xl section-content">
          <img
            :src="aiTransformedUrl"
            alt="AI 변환 그림"
            class="w-full h-full object-cover object-top"/>
        </div>
      </section>

      <!-- 세 번째 섹션: AI 추천 여행지 -->
      <section
        ref="section3"
        class="h-screen w-full flex flex-col items-center justify-center px-6 snap-start"
        :class="{ 'active-section': activeSection === 2 }"
      >
        <div class="text-center mb-8 section-content">
          <h2 class="text-3xl font-bold text-gray-800 mb-2">AI 추천 여행지</h2>
          <p class="text-gray-600 max-w-2xl">
            그림을 분석한 결과, 다음과 같은 여행지를 추천합니다. 마음에 드는 여행지를 선택해주세요.
          </p>
        </div>

        <!-- 추천 여행지 카드 -->
        <div class="grid grid-cols-1 md:grid-cols-3 gap-6 w-full max-w-5xl mb-12 section-content">
          <div
            v-for="(place, index) in placeObjects"
            :key="index"
            @click="selectPlace(index)"
            class="relative bg-white rounded-lg overflow-hidden shadow-lg transition-all duration-300 cursor-pointer"
            :class="{ 'ring-4 ring-blue-500 transform scale-105': selectedPlaceIndex === index }"
          >
            <div class="h-48 overflow-hidden">
              <img
              :src="place.imageUrl || aiTransformedUrl"
               :alt="place.name"
               class="w-full h-full object-cover object-top"
               @error="e => { 
               // 한 번만 발동시키고
               (e.target as HTMLImageElement).onerror = null;
               // AI 변환 이미지를 대신 찍어준다.
               (e.target as HTMLImageElement).src = aiTransformedUrl;
               }"/>
            </div>
            <div class="p-4">
              <h3 class="text-xl font-bold text-gray-800 mb-2">{{ place.name }}</h3>
              <p class="text-gray-600 text-sm">{{ place.description }}</p>
            </div>
          </div>
        </div>

        <!-- 하단 버튼 영역 -->
        <div class="flex justify-between w-full max-w-2xl section-content">
          <button
            class="px-6 py-3 bg-gray-600 text-white rounded-lg shadow-md hover:bg-gray-700 transition-colors duration-300 cursor-pointer !rounded-button whitespace-nowrap"
            @click="redrawImage"
          >
            <i class="fas fa-paint-brush mr-2"></i>다시 그리기
          </button>
          <button
            class="px-6 py-3 rounded-lg shadow-md transition-colors duration-300 cursor-pointer !rounded-button whitespace-nowrap"
            :class="selectedPlaceIndex !== null ? 'bg-blue-600 text-white hover:bg-blue-700' : 'bg-gray-300 text-gray-500 cursor-not-allowed'"
            :disabled="selectedPlaceIndex === null"
            @click="goToPlanning"
          >
            <i class="fas fa-map-marked-alt mr-2"></i>계획 짜러 가기
          </button>
        </div>
      </section>

      <!-- 우측 dot 네비게이션 -->
      <div class="fixed right-8 top-1/2 transform -translate-y-1/2 z-10">
        <div class="flex flex-col items-center space-y-4">
          <button
            v-for="(_, idx) in 3"
            :key="idx"
            @click="scrollToSection(idx)"
            class="w-3 h-3 rounded-full transition-all duration-300 cursor-pointer"
            :class="activeSection === idx ? 'bg-blue-600 scale-125' : 'bg-gray-400 hover:bg-gray-600'"
            aria-label="네비게이션 도트"
          ></button>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, onUnmounted, computed, watch } from 'vue'
import { useAiStore } from '../../stores/ai'
import { useRouter } from 'vue-router'

// 섹션 refs
const section1 = ref<HTMLElement|null>(null)
const section2 = ref<HTMLElement|null>(null)
const section3 = ref<HTMLElement|null>(null)
const activeSection = ref(0)
const selectedPlaceIndex = ref<number|null>(null)

const aiStore = useAiStore()
const router = useRouter()

// store 상태
const originalDrawingUrl = computed(() => aiStore.originalDataUrl)
const aiTransformedUrl  = computed(() => aiStore.resultUrl)
const placeObjects = computed(() => aiStore.recommendedPlaces)

// ▶ AI 변환 이미지가 준비되면 백엔드에 추천 요청을 날립니다.
watch(aiTransformedUrl, (url) => {
  if (url) {
    aiStore.fetchRecommendedPlaces()
  }
})

// 메서드
function selectPlace(idx: number) { selectedPlaceIndex.value = idx }
function redrawImage() { router.push('/drawpage') }
function goToPlanning() {
  if (selectedPlaceIndex.value !== null) {
    // 1) 선택된 place 객체 꺼내기
    const place = placeObjects.value[selectedPlaceIndex.value]
    const name = place.name

    // 2) Pinia에 저장
    aiStore.setSelectedPlaceName(place)

    router.push(`/tripplanpage`)
  }
}
function scrollToSection(idx: number) {
  const secs = [section1.value, section2.value, section3.value]
  if (secs[idx]) { secs[idx]!.scrollIntoView({ behavior: 'smooth' }); activeSection.value = idx }
}
function handleScroll() {
  const pos = window.scrollY, h = window.innerHeight
  ;[section1,section2,section3].forEach((secRef,i) => {
    const sec = secRef.value
    if (!sec) return
    const top = sec.offsetTop, bot = top + sec.offsetHeight
    if (pos >= top - h/3 && pos < bot - h/3) activeSection.value = i
  })
}
function enableScrollSnap() {
  document.body.style.scrollSnapType = 'y mandatory'
  document.querySelectorAll('section').forEach(s => s.classList.add('snap-start'))
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll)
  enableScrollSnap()
  handleScroll()
})
onUnmounted(() => window.removeEventListener('scroll', handleScroll))
</script>

<style scoped>
body { scroll-behavior: smooth; }
section { scroll-snap-align: start; transition: opacity 0.3s ease; }
.active-section { opacity: 1; }
::-webkit-scrollbar { width: 8px; }
::-webkit-scrollbar-track { background: #f1f1f1; }
::-webkit-scrollbar-thumb { background: #888; border-radius: 4px; }
::-webkit-scrollbar-thumb:hover { background: #555; }
@keyframes fadeIn { from { opacity:0; transform:translateY(20px);} to{ opacity:1; transform:translateY(0);} }
.section-content { animation: fadeIn 0.8s ease-out; }
</style>
