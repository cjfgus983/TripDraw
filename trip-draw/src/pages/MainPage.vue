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
          <!-- 여행지 카드 1 -->
          <div class="destination-card">
            <div class="h-64 rounded-lg overflow-hidden mb-4">
              <img
                src="https://readdy.ai/api/search-image?query=beautiful%20jeju%20island%20korea%20with%20volcanic%20landscape%20and%20blue%20ocean%2C%20professional%20travel%20photography%20with%20vibrant%20colors%20and%20perfect%20lighting%2C%20clear%20blue%20sky%20with%20soft%20clouds&width=400&height=256&seq=dest1&orientation=landscape"
                alt="제주도"
                class="w-full h-full object-cover object-top"
              />
            </div>
            <h3 class="text-xl font-bold mb-2">제주도</h3>
            <p class="text-gray-600 mb-4">
              푸른 바다와 아름다운 자연이 어우러진 한국의 대표 휴양지
            </p>
            <button
              class="text-[#4A90E2] font-medium flex items-center whitespace-nowrap cursor-pointer"
            >
              자세히 보기<i class="fas fa-arrow-right ml-2"></i>
            </button>
          </div>
          <!-- 여행지 카드 2 -->
          <div class="destination-card">
            <div class="h-64 rounded-lg overflow-hidden mb-4">
              <img
                src="https://readdy.ai/api/search-image?query=beautiful%20gyeongju%20korea%20with%20traditional%20korean%20architecture%20and%20cherry%20blossoms%2C%20historic%20temples%20and%20palaces%2C%20professional%20travel%20photography%20with%20perfect%20lighting%20and%20composition&width=400&height=256&seq=dest2&orientation=landscape"
                alt="경주"
                class="w-full h-full object-cover object-top"
              />
            </div>
            <h3 class="text-xl font-bold mb-2">경주</h3>
            <p class="text-gray-600 mb-4">
              천년 고도의 역사와 문화가 살아 숨쉬는 도시
            </p>
            <button
              class="text-[#4A90E2] font-medium flex items-center whitespace-nowrap cursor-pointer"
            >
              자세히 보기<i class="fas fa-arrow-right ml-2"></i>
            </button>
          </div>
          <!-- 여행지 카드 3 -->
          <div class="destination-card">
            <div class="h-64 rounded-lg overflow-hidden mb-4">
              <img
                src="https://readdy.ai/api/search-image?query=beautiful%20busan%20korea%20with%20haeundae%20beach%20and%20city%20skyline%20at%20sunset%2C%20coastal%20cityscape%20with%20modern%20architecture%20and%20ocean%20view%2C%20professional%20travel%20photography%20with%20warm%20lighting&width=400&height=256&seq=dest3&orientation=landscape"
                alt="부산"
                class="w-full h-full object-cover object-top"
              />
            </div>
            <h3 class="text-xl font-bold mb-2">부산</h3>
            <p class="text-gray-600 mb-4">
              해변과 산, 도시가 어우러진 매력적인 항구도시
            </p>
            <button
              class="text-[#4A90E2] font-medium flex items-center whitespace-nowrap cursor-pointer"
            >
              자세히 보기<i class="fas fa-arrow-right ml-2"></i>
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
          <div class="feature-card bg-white p-8 rounded-lg shadow-md text-center">
            <div class="w-16 h-16 mx-auto mb-6 flex items-center justify-center bg-[#9EC6F3] rounded-full text-white">
              <i class="fas fa-map-marked-alt text-2xl"></i>
            </div>
            <h3 class="text-xl font-bold mb-3">맞춤형 여행 계획</h3>
            <p class="text-gray-600">
              당신의 취향과 예산에 맞는 완벽한 여행 일정을 쉽게 계획할 수 있습니다.
            </p>
          </div>
          <!-- 특징 2 -->
          <div class="feature-card bg-white p-8 rounded-lg shadow-md text-center">
            <div class="w-16 h-16 mx-auto mb-6 flex items-center justify-center bg-[#9FB3DF] rounded-full text-white">
              <i class="fas fa-users text-2xl"></i>
            </div>
            <h3 class="text-xl font-bold mb-3">실시간 커뮤니티</h3>
            <p class="text-gray-600">
              다른 여행자들과 경험을 공유하고 실시간으로 소통할 수 있습니다.
            </p>
          </div>
          <!-- 특징 3 -->
          <div class="feature-card bg-white p-8 rounded-lg shadow-md text-center">
            <div class="w-16 h-16 mx-auto mb-6 flex items-center justify-center bg-[#4A90E2] rounded-full text-white">
              <i class="fas fa-percentage text-2xl"></i>
            </div>
            <h3 class="text-xl font-bold mb-3">특별 할인 혜택</h3>
            <p class="text-gray-600">
              회원만을 위한 특별 할인과 프로모션을 통해 더 경제적인 여행을 즐기세요.
            </p>
          </div>
        </div>
      </div>
    </section>

    <!-- 사용자 후기 섹션 -->
    <section class="py-16 bg-white">
      <div class="container mx-auto px-4">
        <h2 class="text-3xl font-bold text-center mb-12">사용자 후기</h2>

        <!-- Swiper 컴포넌트 -->
        <Swiper
          :modules="swiperModules"
          :slides-per-view="1"
          :space-between="20"
          pagination
          autoplay
          :breakpoints="{ 640: { slidesPerView: 2 }, 1024: { slidesPerView: 3 } }"
          class="my-8"
        >
          <SwiperSlide
            v-for="(t, idx) in testimonials"
            :key="idx"
            class="p-4"
          >
            <div class="bg-gray-50 p-6 rounded-lg shadow-sm">
              <div class="flex items-center mb-4">
                <div class="text-yellow-400 flex">
                  <i class="fas fa-star" v-for="star in 5" :key="star" />
                </div>
              </div>
              <p class="text-gray-600 mb-4">{{ t.comment }}</p>
              <div class="flex items-center">
                <div class="w-12 h-12 rounded-full overflow-hidden mr-4">
                  <img
                    :src="t.avatar"
                    alt="사용자 아바타"
                    class="w-full h-full object-cover"
                  />
                </div>
                <div>
                  <h4 class="font-bold">{{ t.name }}</h4>
                  <p class="text-sm text-gray-500">{{ t.location }}</p>
                </div>
              </div>
            </div>
          </SwiperSlide>
          <!-- pagination 엘리먼트 -->
          <div class="swiper-pagination mt-8"></div>
        </Swiper>
      </div>
    </section>

    <!-- 뉴스레터 구독 섹션 -->
    <section class="py-16 bg-[#9FB3DF] text-white">
      <div class="container mx-auto px-4 text-center">
        <h2 class="text-3xl font-bold mb-6">여행 소식 받아보기</h2>
        <p class="text-lg mb-8 max-w-2xl mx-auto">
          최신 여행 정보와 특별 할인 혜택을 이메일로 받아보세요.
        </p>
        <div class="max-w-md mx-auto flex">
          <input
            type="email"
            placeholder="이메일 주소를 입력하세요"
            class="flex-grow px-4 py-3 rounded-l-lg border-none text-gray-800 text-sm focus:outline-none"
          />
          <button
            class="bg-[#4A90E2] px-6 py-3 rounded-r-lg font-medium hover:bg-[#357ABD] transition-all whitespace-nowrap cursor-pointer"
          >
            구독하기
          </button>
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

// 더미 리뷰 데이터
const testimonials = ref([
  {
    name: '김민준',
    location: '서울',
    comment:
      '이 서비스를 통해 제주도 여행을 계획했는데, 정말 완벽했습니다. 모든 일정이 체계적으로 잘 짜여져 있어서 스트레스 없이 여행을 즐길 수 있었어요.',
    avatar:
      'https://readdy.ai/api/search-image?seq=avatar1&width=100&height=100',
  },
  {
    name: '이지은',
    location: '부산',
    comment:
      '친구들과 부산 여행을 갔는데, 이 앱 덕분에 시간과 비용을 많이 절약했어요. 특히 숨겨진 맛집 추천이 정말 좋았습니다!',
    avatar:
      'https://readdy.ai/api/search-image?seq=avatar2&width=100&height=100',
  },
  {
    name: '박준호',
    location: '대구',
    comment:
      '여행 계획을 세우는 것이 항상 어려웠는데, 이 서비스를 사용하고 나서는 정말 쉬워졌어요. 다음 여행도 여기서 계획할 예정입니다.',
    avatar:
      'https://readdy.ai/api/search-image?seq=avatar3&width=100&height=100',
  },
  {
    name: '최서연',
    location: '인천',
    comment:
      '커뮤니티를 통해 다른 여행자들의 경험을 참고할 수 있어서 좋았어요. 실제 경험담이 정말 유용했습니다.',
    avatar:
      'https://readdy.ai/api/search-image?seq=avatar4&width=100&height=100',
  },
])

function goToDrawPage() {
  router.push('/drawpage')
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