<!-- The exported code uses Tailwind CSS. Install Tailwind CSS in your dev environment to ensure all styles work. -->
<template>
    <div class="min-h-screen bg-gray-50 font-sans">
      <!-- 섹션 컨테이너 -->
      <div class="relative">
        <!-- 첫 번째 섹션: 원본 그림 -->
        <section
          ref="section1"
          class="h-screen w-full flex flex-col items-center justify-center px-6 snap-start"
          :class="{ 'active-section': activeSection === 0 }"
        >
          <div class="text-center mb-8">
            <h2 class="text-3xl font-bold text-gray-800 mb-2">내가 그린 그림</h2>
            <p class="text-gray-600">당신이 직접 그린 원본 그림입니다.</p>
          </div>
          <div
            class="relative w-full max-w-3xl h-[500px] rounded-lg overflow-hidden shadow-xl"
          >
            <img
              :src="originalDrawingUrl"
              alt="원본 그림"
              class="w-full h-full object-cover object-top"
            />
          </div>
        </section>
  
        <!-- 두 번째 섹션: AI 변환 그림 -->
        <section
          ref="section2"
          class="h-screen w-full flex flex-col items-center justify-center px-6 snap-start"
          :class="{ 'active-section': activeSection === 1 }"
        >
          <div class="text-center mb-8">
            <h2 class="text-3xl font-bold text-gray-800 mb-2">
              AI가 변환한 그림
            </h2>
            <p class="text-gray-600">AI가 당신의 그림을 아름답게 변환했습니다.</p>
          </div>
          <div
            class="relative w-full max-w-3xl h-[500px] rounded-lg overflow-hidden shadow-xl"
          >
            <img
              :src="aiTransformedUrl"
              alt="AI 변환 그림"
              class="w-full h-full object-cover object-top"
            />
          </div>
        </section>
  
        <!-- 세 번째 섹션: AI 추천 여행지 -->
        <section
          ref="section3"
          class="h-screen w-full flex flex-col items-center justify-center px-6 snap-start"
          :class="{ 'active-section': activeSection === 2 }"
        >
          <div class="text-center mb-8">
            <h2 class="text-3xl font-bold text-gray-800 mb-2">AI 추천 여행지</h2>
            <p class="text-gray-600 max-w-2xl">
              그림을 분석한 결과, 다음과 같은 여행지를 추천합니다. 마음에 드는
              여행지를 선택해주세요.
            </p>
          </div>
  
          <!-- 추천 여행지 카드 -->
          <div
            class="grid grid-cols-1 md:grid-cols-3 gap-6 w-full max-w-5xl mb-12"
          >
            <div
              v-for="(place, index) in recommendedPlaces"
              :key="index"
              @click="selectPlace(index)"
              class="relative bg-white rounded-lg overflow-hidden shadow-lg transition-all duration-300 cursor-pointer"
              :class="{ 'ring-4 ring-blue-500 transform scale-105': selectedPlaceIndex === index }"
            >
              <div class="h-48 overflow-hidden">
                <img
                  :src="place.imageUrl"
                  :alt="place.name"
                  class="w-full h-full object-cover object-top"
                />
              </div>
              <div class="p-4">
                <h3 class="text-xl font-bold text-gray-800 mb-2">
                  {{ place.name }}
                </h3>
                <p class="text-gray-600 text-sm">{{ place.description }}</p>
              </div>
            </div>
          </div>
  
          <!-- 하단 버튼 영역 -->
          <div class="flex justify-between w-full max-w-2xl">
            <button
              class="px-6 py-3 bg-gray-600 text-white rounded-lg shadow-md hover:bg-gray-700 transition-colors duration-300 cursor-pointer !rounded-button whitespace-nowrap"
              @click="redrawImage"
            >
              <i class="fas fa-paint-brush mr-2"></i> 다시 그리기
            </button>
            <button
              class="px-6 py-3 rounded-lg shadow-md transition-colors duration-300 cursor-pointer !rounded-button whitespace-nowrap"
              :class="selectedPlaceIndex !== null ? 'bg-blue-600 text-white hover:bg-blue-700' : 'bg-gray-300 text-gray-500 cursor-not-allowed'"
              :disabled="selectedPlaceIndex === null"
              @click="goToPlanning"
            >
              <i class="fas fa-map-marked-alt mr-2"></i> 계획 짜러 가기
            </button>
          </div>
        </section>
  
        <!-- 우측 dot 네비게이션 -->
        <div class="fixed right-8 top-1/2 transform -translate-y-1/2 z-10">
          <div class="flex flex-col items-center space-y-4">
            <button
              v-for="(_, index) in 3"
              :key="index"
              @click="scrollToSection(index)"
              class="w-3 h-3 rounded-full transition-all duration-300 cursor-pointer"
              :class="activeSection === index ? 'bg-blue-600 scale-125' : 'bg-gray-400 hover:bg-gray-600'"
              aria-label="네비게이션 도트"
            ></button>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script lang="ts" setup>
  import { ref, onMounted, onUnmounted } from "vue";
  
  // 섹션 참조
  const section1 = ref<HTMLElement | null>(null);
  const section2 = ref<HTMLElement | null>(null);
  const section3 = ref<HTMLElement | null>(null);
  const activeSection = ref(0);
  const selectedPlaceIndex = ref<number | null>(null);
  
  // 이미지 URL
  const originalDrawingUrl =
    "https://readdy.ai/api/search-image?query=A%20simple%20hand-drawn%20sketch%20of%20a%20beach%20scene%20with%20palm%20trees%20and%20waves%2C%20drawn%20with%20simple%20lines%20like%20a%20child%20drawing%2C%20on%20white%20paper%20with%20visible%20pencil%20or%20pen%20strokes%2C%20simple%20and%20minimalistic%20style&width=800&height=500&seq=1001&orientation=landscape";
  
  const aiTransformedUrl =
    "https://readdy.ai/api/search-image?query=A%20beautiful%20tropical%20beach%20paradise%20with%20crystal%20clear%20turquoise%20water%2C%20white%20sandy%20beaches%2C%20swaying%20palm%20trees%2C%20and%20a%20stunning%20sunset%20with%20vibrant%20orange%20and%20pink%20hues%20in%20the%20sky%2C%20photorealistic%20style%20with%20high%20detail&width=800&height=500&seq=1002&orientation=landscape";
  
  // 추천 여행지 데이터
  const recommendedPlaces = ref([
    {
      name: "발리, 인도네시아",
      description:
        "아름다운 해변, 푸른 바다, 그리고 독특한 문화가 어우러진 열대 천국입니다. 서핑, 다이빙, 사원 방문 등 다양한 활동을 즐길 수 있습니다.",
      imageUrl:
        "https://readdy.ai/api/search-image?query=Beautiful%20Bali%20beach%20with%20traditional%20Balinese%20umbrellas%20and%20loungers%2C%20crystal%20clear%20turquoise%20water%2C%20white%20sand%2C%20palm%20trees%2C%20and%20traditional%20boats%20in%20distance%2C%20sunny%20day%20with%20blue%20sky%2C%20tropical%20paradise&width=400&height=300&seq=1003&orientation=landscape",
    },
    {
      name: "산토리니, 그리스",
      description:
        "에게해의 푸른 바다와 하얀 건물이 조화를 이루는 낭만적인 섬입니다. 환상적인 일몰과 독특한 건축물을 감상할 수 있습니다.",
      imageUrl:
        "https://readdy.ai/api/search-image?query=Santorini%20Greece%20with%20iconic%20white%20buildings%20with%20blue%20domes%20overlooking%20the%20crystal%20clear%20Aegean%20Sea%2C%20stunning%20sunset%20with%20orange%20and%20pink%20sky%2C%20beautiful%20cliffside%20views%2C%20traditional%20Greek%20architecture&width=400&height=300&seq=1004&orientation=landscape",
    },
    {
      name: "몰디브",
      description:
        "투명한 바다와 하얀 모래사장, 수상 방갈로가 특징인 꿈같은 휴양지입니다. 스노클링과 다이빙을 통해 아름다운 해양 생물을 만날 수 있습니다.",
      imageUrl:
        "https://readdy.ai/api/search-image?query=Maldives%20luxury%20water%20bungalows%20over%20crystal%20clear%20turquoise%20lagoon%2C%20white%20sandy%20beaches%2C%20palm%20trees%2C%20coral%20reefs%20visible%20from%20above%2C%20perfect%20tropical%20paradise%20with%20overwater%20villas&width=400&height=300&seq=1005&orientation=landscape",
    },
  ]);
  
  // 여행지 선택 함수
  const selectPlace = (index: number) => {
    selectedPlaceIndex.value = index;
  };
  
  // 다시 그리기 함수
  const redrawImage = () => {
    // 다시 그리기 페이지로 이동하는 로직
    console.log("다시 그리기 페이지로 이동");
  };
  
  // 계획 짜러 가기 함수
  const goToPlanning = () => {
    if (selectedPlaceIndex.value !== null) {
      // 계획 짜기 페이지로 이동하는 로직
      console.log(
        `${recommendedPlaces.value[selectedPlaceIndex.value].name}에 대한 계획 짜기 페이지로 이동`,
      );
    }
  };
  
  // 섹션으로 스크롤하는 함수
  const scrollToSection = (index: number) => {
    const sections = [section1.value, section2.value, section3.value];
    if (sections[index]) {
      sections[index]?.scrollIntoView({ behavior: "smooth" });
      activeSection.value = index;
    }
  };
  
  // 스크롤 이벤트 핸들러
  const handleScroll = () => {
    const scrollPosition = window.scrollY;
    const windowHeight = window.innerHeight;
  
    const sections = [section1.value, section2.value, section3.value];
  
    sections.forEach((section, index) => {
      if (!section) return;
  
      const sectionTop = section.offsetTop;
      const sectionBottom = sectionTop + section.offsetHeight;
  
      if (
        scrollPosition >= sectionTop - windowHeight / 3 &&
        scrollPosition < sectionBottom - windowHeight / 3
      ) {
        activeSection.value = index;
      }
    });
  };
  
  // 스크롤 스냅 기능
  const enableScrollSnap = () => {
    document.body.style.scrollSnapType = "y mandatory";
    const sections = document.querySelectorAll("section");
    sections.forEach((section) => {
      section.classList.add("snap-start");
    });
  };
  
  onMounted(() => {
    window.addEventListener("scroll", handleScroll);
    enableScrollSnap();
    // 초기 활성 섹션 설정
    handleScroll();
  });
  
  onUnmounted(() => {
    window.removeEventListener("scroll", handleScroll);
  });
  </script>
  
  <style scoped>
  body {
    scroll-behavior: smooth;
    scroll-snap-type: y mandatory;
  }
  
  section {
    scroll-snap-align: start;
    transition: opacity 0.3s ease;
  }
  
  .active-section {
    opacity: 1;
  }
  
  /* 스크롤바 스타일링 */
  ::-webkit-scrollbar {
    width: 8px;
  }
  
  ::-webkit-scrollbar-track {
    background: #f1f1f1;
  }
  
  ::-webkit-scrollbar-thumb {
    background: #888;
    border-radius: 4px;
  }
  
  ::-webkit-scrollbar-thumb:hover {
    background: #555;
  }
  
  /* 추가 애니메이션 */
  @keyframes fadeIn {
    from {
      opacity: 0;
      transform: translateY(20px);
    }
    to {
      opacity: 1;
      transform: translateY(0);
    }
  }
  
  .section-content {
    animation: fadeIn 0.8s ease-out;
  }
  </style>
  