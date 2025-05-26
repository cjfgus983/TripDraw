<!-- src/pages/MainPage.vue -->
<template>
  <!-- 메인 콘텐츠 영역 -->
  <main class="flex-grow bg-gray-50">
    <!-- 히어로 섹션 -->
    <section class="relative h-[500px] overflow-hidden">
      <div class="absolute inset-0 bg-gradient-to-r from-[#9FB3DF] to-transparent z-10"></div>
      <img
        src="https://readdy.ai/api/search-image?query=beautiful%20scenic%20travel%20destination%20with%20mountains%20and%20ocean%20in%20Korea%2C%20peaceful%20landscape%20with%20soft%20gradient%20sky%20in%20blue%20and%20pink%20tones%2C%20professional%20photography%20with%20depth%20and%20atmosphere%2C%20perfect%20for%20travel%20planning%20website%20hero%20section&width=1440&height=500&seq=hero1&orientation=landscape"
        alt="여행 배경"
        class="absolute inset-0 w-full h-full object-cover object-top"
      />
      <div class="container mx-auto px-4 h-full relative z-20 flex items-center">
        <div class="max-w-lg text-white">
          <h1 class="text-4xl font-bold mb-4">
            당신의 완벽한 여행을 계획하세요
          </h1>
          <p class="text-lg mb-6">
            지금 바로 시작하여 잊지 못할 추억을 만드세요. 전 세계 최고의
            여행지를 발견하고 여행 계획을 세워보세요.
          </p>
          <button
            @click="goToDrawPage"
            class="bg-white text-[#4A90E2] px-6 py-3 rounded-lg font-medium hover:bg-opacity-90 transition-all shadow-lg whitespace-nowrap cursor-pointer"
          >
            지금 시작하기
          </button>
        </div>
      </div>
    </section>

    <!-- 인기 여행지 섹션 -->
    <section class="py-16 bg-white">
        <div class="container mx-auto px-4">
          <h2 class="text-3xl font-bold text-center mb-12">인기 여행지</h2>
          <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
              <!-- 카드 1: 오사카 -->
          <div class="destination-card">
            <div class="h-64 rounded-lg overflow-hidden mb-4">
              <img
                src="https://readdy.ai/api/search-image?query=beautiful%20osaka%20japan%20with%20traditional%20architecture%20and%20modern%20cityscape%2C%20cherry%20blossoms%20and%20vibrant%20street%20scenes%2C%20professional%20travel%20photography%20with%20perfect%20lighting%20and%20composition&width=400&height=256&seq=dest1&orientation=landscape"
                alt="일본 오사카"
                class="w-full h-full object-cover object-top"
              />
            </div>
            <h3 class="text-xl font-bold mb-2">{{ osaka.name }}</h3>
            <p class="text-gray-600 mb-4">{{ osaka.description }}</p>
            <button
              @click="selectAndGo(osaka)"
              class="text-[#4A90E2] font-medium flex items-center !rounded-button cursor-pointer"
            >
              자세히 보기 <i class="fas fa-arrow-right ml-2"></i>
            </button>
          </div>

          <!-- 카드 2: 부산 -->
          <div class="destination-card">
            <div class="h-64 rounded-lg overflow-hidden mb-4">
              <img
                src="https://readdy.ai/api/search-image?query=beautiful%20busan%20korea%20with%20haeundae%20beach%20and%20city%20skyline%20at%20sunset%2C%20coastal%20cityscape%20with%20modern%20architecture%20and%20ocean%20view%2C%20professional%20travel%20photography%20with%20warm%20lighting&width=400&height=256&seq=dest2&orientation=landscape"
                alt="한국 부산"
                class="w-full h-full object-cover object-top"
              />
            </div>
            <h3 class="text-xl font-bold mb-2">{{ busan.name }}</h3>
            <p class="text-gray-600 mb-4">{{ busan.description }}</p>
            <button
              @click="selectAndGo(busan)"
              class="text-[#4A90E2] font-medium flex items-center !rounded-button cursor-pointer"
            >
              자세히 보기 <i class="fas fa-arrow-right ml-2"></i>
            </button>
          </div>

          <!-- 카드 3: 파리 -->
          <div class="destination-card">
            <div class="h-64 rounded-lg overflow-hidden mb-4">
              <img
                src="https://readdy.ai/api/search-image?query=beautiful%20paris%20france%20with%20eiffel%20tower%20and%20seine%20river%2C%20historic%20architecture%20and%20romantic%20atmosphere%2C%20professional%20travel%20photography%20with%20perfect%20lighting%20and%20composition&width=400&height=256&seq=dest3&orientation=landscape"
                alt="프랑스 파리"
                class="w-full h-full object-cover object-top"
              />
            </div>
            <h3 class="text-xl font-bold mb-2">{{ paris.name }}</h3>
            <p class="text-gray-600 mb-4">{{ paris.description }}</p>
            <button
              @click="selectAndGo(paris)"
              class="text-[#4A90E2] font-medium flex items-center !rounded-button cursor-pointer"
            >
              자세히 보기 <i class="fas fa-arrow-right ml-2"></i>
            </button>
          </div>
          </div>
        </div>
      </section>
      <!-- 서비스 특징 섹션 -->
      <section class="py-16 bg-gradient-to-b from-[#BDDDE4] to-[#FFF1D5]">
        <div class="container mx-auto px-4">
          <h2 class="text-3xl font-bold text-center mb-12">
            왜 저희 서비스를 이용해야 할까요?
          </h2>
          <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
            <!-- 특징 1 -->
            <div
              class="feature-card bg-white p-8 rounded-lg shadow-md text-center"
            >
              <div
                class="w-16 h-16 mx-auto mb-6 flex items-center justify-center bg-[#9EC6F3] rounded-full text-white"
              >
                <i class="fas fa-map-marked-alt text-2xl"></i>
              </div>
              <h3 class="text-xl font-bold mb-3">맞춤형 여행 계획</h3>
              <p class="text-gray-600">
                당신의 취향에 맞는 완벽한 여행 일정을 쉽게 계획하고 공유할 수 있습니다.
              </p>
            </div>
            <!-- 특징 2 -->
            <div
              class="feature-card bg-white p-8 rounded-lg shadow-md text-center"
            >
              <div
                class="w-16 h-16 mx-auto mb-6 flex items-center justify-center bg-[#9FB3DF] rounded-full text-white"
              >
                <i class="fas fa-users text-2xl"></i>
              </div>
              <h3 class="text-xl font-bold mb-3">실시간 커뮤니티</h3>
              <p class="text-gray-600">
                다른 여행자들과 경험을 공유하고 실시간으로 소통할 수 있습니다.
              </p>
            </div>
            <!-- 특징 3 -->
            <div
              class="feature-card bg-white p-8 rounded-lg shadow-md text-center"
            >
              <div
                class="w-16 h-16 mx-auto mb-6 flex items-center justify-center bg-[#4A90E2] rounded-full text-white"
              >
                <i class="fas fa-robot text-2xl"></i>
              </div>
              <h3 class="text-xl font-bold mb-3">AI 여행지 추천</h3>
              <p class="text-gray-600">
                회원님만의 그림으로 AI에게 여행지 추천을 받을 수 있어요.
              </p>
            </div>
          </div>
        </div>
      </section>
  </main>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
const router = useRouter()



// swiper/vue 컴포넌트와 모듈
import { Swiper, SwiperSlide } from 'swiper/vue'
import { Pagination, Autoplay } from 'swiper/modules'


// 사용할 모듈 배열
const swiperModules = [Pagination, Autoplay]

function goToDrawPage() {
  router.push('/drawpage')
}

import { useAiStore } from '@/stores/ai'

// PlaceInfo 타입
interface PlaceInfo {
  name: string
  description: string
  imageUrl: string
  latitude: number
  longitude: number
}

// 1) 각 카드에 대응하는 객체를 상수로 선언
const osaka: PlaceInfo = {
  name: '일본 오사카',
  description: '전통과 현대가 공존하는 일본의 매력적인 도시',
  imageUrl:
    'https://readdy.ai/api/search-image?query=osaka&width=400&height=256&seq=dest1',
  latitude: 34.6937,
  longitude: 135.5023,
}

const busan: PlaceInfo = {
  name: '한국 부산',
  description: '해변과 산, 도시가 어우러진 매력적인 항구도시',
  imageUrl:
    'https://readdy.ai/api/search-image?query=busan&width=400&height=256&seq=dest2',
  latitude: 35.1796,
  longitude: 129.0756,
}

const paris: PlaceInfo = {
  name: '프랑스 파리',
  description: '예술과 로맨스의 도시, 세계적인 관광 명소',
  imageUrl:
    'https://readdy.ai/api/search-image?query=paris&width=400&height=256&seq=dest3',
  latitude: 48.8566,
  longitude: 2.3522,
}

const aiStore = useAiStore()

// 2) 클릭 시 호출되는 함수
function selectAndGo(place: PlaceInfo) {
  aiStore.setSelectedPlaceName(place)       // Pinia에 저장
  router.push({ name: 'TripPlanPage' })          // 이동할 라우트 이름으로 변경
}

</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@300;400;500;700&display=swap');

body {
  font-family: 'Noto Sans KR', sans-serif;
  min-height: 1024px;
}

/* 카드들 호버 애니메이션 */
.feature-card,
.destination-card {
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}
.feature-card:hover,
.destination-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
}

/* nav-link underlining (만약 App.vue에 필요하다면) */
.nav-link {
  position: relative;
  padding: 0.5rem 0;
  transition: all 0.3s ease;
}
.nav-link:hover {
  color: #9ec6f3;
}
.nav-link::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 0;
  width: 0;
  height: 2px;
  background-color: white;
  transition: width 0.3s ease;
}
.nav-link:hover::after {
  width: 100%;
}
</style>