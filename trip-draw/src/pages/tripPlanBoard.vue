<!-- The exported code uses Tailwind CSS. Install Tailwind CSS in your dev environment to ensure all styles work. -->
<template>
    <div class="min-h-screen bg-white">
      <!-- Fullpage container -->
      <div class="relative">
        <!-- Page dots navigation -->
        <div
          class="fixed right-6 top-1/2 transform -translate-y-1/2 z-50 flex flex-col items-center space-y-4"
        >
          <button
            v-for="(_, index) in 3"
            :key="index"
            @click="goToPage(index)"
            :class="[
  'w-3 h-3 rounded-full cursor-pointer transition-all duration-300',
  currentPage === index ? 'bg-[#9FB3DF] w-4 h-4' : 'bg-gray-300'
  ]"
          ></button>
        </div>
        <!-- Pages -->
        <div
          class="transition-transform duration-700 ease-in-out"
          :style="{ transform: `translateY(-${currentPage * 100}vh)` }"
        >
          <!-- Page 1 -->
          <div class="h-screen w-full flex flex-col p-8">
            <div
              class="filter-section bg-[#BDDDE4] rounded-lg p-4 mb-6 shadow-md"
            >
              <div class="flex flex-wrap items-center gap-4">
                <!-- Category Toggle -->
                <div class="relative">
                  <button
                    @click="toggleCategoryDropdown"
                    class="bg-[#9EC6F3] text-white px-4 py-2 rounded-button flex items-center gap-2 cursor-pointer whitespace-nowrap"
                  >
                    <span>카테고리</span>
                    <i
                      :class="['fas', categoryDropdownOpen ? 'fa-chevron-up' : 'fa-chevron-down']"
                    ></i>
                  </button>
                  <div
                    v-if="categoryDropdownOpen"
                    class="absolute top-full left-0 mt-1 bg-white shadow-lg rounded-md z-10 w-48"
                  >
                    <ul>
                      <li
                        v-for="category in categories"
                        :key="category"
                        @click="selectCategory(category)"
                        class="px-4 py-2 hover:bg-[#FFF1D5] cursor-pointer"
                      >
                        {{ category }}
                      </li>
                    </ul>
                  </div>
                </div>
                <!-- Region Toggle -->
                <div class="relative">
                  <button
                    @click="toggleRegionDropdown"
                    class="bg-[#9EC6F3] text-white px-4 py-2 rounded-button flex items-center gap-2 cursor-pointer whitespace-nowrap"
                  >
                    <span>지역</span>
                    <i
                      :class="['fas', regionDropdownOpen ? 'fa-chevron-up' : 'fa-chevron-down']"
                    ></i>
                  </button>
                  <div
                    v-if="regionDropdownOpen"
                    class="absolute top-full left-0 mt-1 bg-white shadow-lg rounded-md z-10 w-48"
                  >
                    <ul>
                      <li
                        v-for="region in regions"
                        :key="region"
                        @click="selectRegion(region)"
                        class="px-4 py-2 hover:bg-[#FFF1D5] cursor-pointer"
                      >
                        {{ region }}
                      </li>
                    </ul>
                  </div>
                </div>
                <!-- Search Input -->
                <div class="flex-grow max-w-md">
                  <div class="relative">
                    <input
                      type="text"
                      v-model="searchQuery"
                      placeholder="여행 계획 제목 검색"
                      class="w-full px-4 py-2 pl-10 border-none rounded-md bg-white text-sm focus:outline-none focus:ring-2 focus:ring-[#9FB3DF]"
                    />
                    <i
                      class="fas fa-search absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"
                    ></i>
                  </div>
                </div>
                <!-- Active Filters -->
                <div
                  class="flex items-center gap-2 flex-wrap"
                  v-if="selectedCategory || selectedRegion"
                >
                  <div
                    v-if="selectedCategory"
                    class="bg-[#9FB3DF] text-white px-3 py-1 rounded-full text-sm flex items-center gap-1"
                  >
                    <span>{{ selectedCategory }}</span>
                    <i
                      @click="clearCategory"
                      class="fas fa-times cursor-pointer"
                    ></i>
                  </div>
                  <div
                    v-if="selectedRegion"
                    class="bg-[#9FB3DF] text-white px-3 py-1 rounded-full text-sm flex items-center gap-1"
                  >
                    <span>{{ selectedRegion }}</span>
                    <i
                      @click="clearRegion"
                      class="fas fa-times cursor-pointer"
                    ></i>
                  </div>
                </div>
              </div>
            </div>
            <div class="flex-grow overflow-auto pb-6">
              <h2 class="text-2xl font-bold mb-6 text-[#9FB3DF]">여행 계획</h2>
              <div class="space-y-4">
                <div
                  v-for="(plan, index) in filteredPlans"
                  :key="plan.id"
                  class="bg-white rounded-lg shadow-md p-4 flex items-center gap-4 transition-all duration-300 hover:shadow-lg w-4/5 mx-auto"
                  :class="{'border-l-4 border-[#FFF1D5]': plan.favorite}"
                >
                  <!-- Favorite Star -->
                  <div @click="toggleFavorite(plan.id)" class="cursor-pointer">
                    <i
                      class="fas fa-star text-xl"
                      :class="plan.favorite ? 'text-yellow-400' : 'text-gray-300'"
                    ></i>
                  </div>
                  <!-- Plan Content -->
                  <div class="flex-grow flex justify-between items-center">
                    <div>
                      <div class="flex items-center gap-3 mb-2">
                        <h3 class="text-lg font-semibold">{{ plan.title }}</h3>
                        <span
                          class="bg-[#9FB3DF] text-white px-2 py-1 rounded-md text-xs"
                          >{{ plan.region }}</span
                        >
                        <span class="text-gray-500 text-sm"
                          >작성자: {{ plan.author }}</span
                        >
                      </div>
                    </div>
                    <!-- Route Display -->
                    <div class="flex items-center gap-3 ml-4 overflow-hidden">
                      <template
                        v-for="(location, locIndex) in displayRoute(plan.route)"
                        :key="locIndex"
                      >
                        <span
                          class="bg-[#FFF1D5] px-3 py-2 rounded text-base font-medium whitespace-nowrap"
                          >{{ location }}</span
                        >
                        <i
                          v-if="locIndex < displayRoute(plan.route).length - 1"
                          class="fas fa-arrow-right text-[#9EC6F3] text-lg flex-shrink-0"
                        ></i>
                      </template>
                      <span
                        v-if="plan.route.length > displayRoute(plan.route).length"
                        class="text-gray-500 ml-2"
                        >...</span
                      >
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <!-- Page 2 -->
          <div
            class="h-screen w-full flex items-center justify-center bg-[#FFF1D5]"
          >
            <div class="text-center">
              <h2 class="text-3xl font-bold text-[#9FB3DF] mb-4">
                두 번째 페이지
              </h2>
              <p class="text-lg text-gray-700">
                스크롤을 내리거나 오른쪽 점을 클릭하여 페이지를 이동하세요.
              </p>
              <img
                :src="page2Image"
                alt="여행 이미지"
                class="mt-8 rounded-lg shadow-lg max-w-2xl mx-auto object-cover h-96 w-full"
              />
            </div>
          </div>
          <!-- Page 3 -->
          <div
            class="h-screen w-full flex items-center justify-center bg-[#BDDDE4]"
          >
            <div class="text-center">
              <h2 class="text-3xl font-bold text-[#9FB3DF] mb-4">
                세 번째 페이지
              </h2>
              <p class="text-lg text-gray-700">
                스크롤을 내리거나 오른쪽 점을 클릭하여 페이지를 이동하세요.
              </p>
              <img
                :src="page3Image"
                alt="여행 이미지"
                class="mt-8 rounded-lg shadow-lg max-w-2xl mx-auto object-cover h-96 w-full"
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script lang="ts" setup>
  import { ref, computed, onMounted, onBeforeUnmount } from "vue";
  // Page navigation
  const currentPage = ref(0);
  const goToPage = (pageIndex: number) => {
    currentPage.value = pageIndex;
  };
  // Handle wheel events for fullpage scrolling
  const handleWheel = (event: WheelEvent) => {
    // Prevent default scrolling
    event.preventDefault();
    // Debounce scroll events
    if (scrollTimeout.value) return;
    scrollTimeout.value = setTimeout(() => {
      if (event.deltaY > 0 && currentPage.value < 2) {
        // Scroll down
        currentPage.value++;
      } else if (event.deltaY < 0 && currentPage.value > 0) {
        // Scroll up
        currentPage.value--;
      }
      scrollTimeout.value = null;
    }, 500);
  };
  const scrollTimeout = ref<number | null>(null);
  onMounted(() => {
    window.addEventListener("wheel", handleWheel, { passive: false });
  });
  onBeforeUnmount(() => {
    window.removeEventListener("wheel", handleWheel);
    if (scrollTimeout.value) {
      clearTimeout(scrollTimeout.value);
    }
  });
  // Filter dropdowns
  const categoryDropdownOpen = ref(false);
  const regionDropdownOpen = ref(false);
  const selectedCategory = ref("");
  const selectedRegion = ref("");
  const searchQuery = ref("");
  const toggleCategoryDropdown = () => {
    categoryDropdownOpen.value = !categoryDropdownOpen.value;
    if (categoryDropdownOpen.value) {
      regionDropdownOpen.value = false;
    }
  };
  const toggleRegionDropdown = () => {
    regionDropdownOpen.value = !regionDropdownOpen.value;
    if (regionDropdownOpen.value) {
      categoryDropdownOpen.value = false;
    }
  };
  const selectCategory = (category: string) => {
    selectedCategory.value = category;
    categoryDropdownOpen.value = false;
  };
  const selectRegion = (region: string) => {
    selectedRegion.value = region;
    regionDropdownOpen.value = false;
  };
  const clearCategory = () => {
    selectedCategory.value = "";
  };
  const clearRegion = () => {
    selectedRegion.value = "";
  };
  // Close dropdowns when clicking outside
  onMounted(() => {
    document.addEventListener("click", (event) => {
      const target = event.target as HTMLElement;
      if (!target.closest(".relative")) {
        categoryDropdownOpen.value = false;
        regionDropdownOpen.value = false;
      }
    });
  });
  // Sample data
  const categories = [
    "가족 여행",
    "커플 여행",
    "친구 여행",
    "혼자 여행",
    "비즈니스 여행",
  ];
  const regions = [
    "서울",
    "부산",
    "제주도",
    "강원도",
    "경기도",
    "전라도",
    "경상도",
  ];
  const travelPlans = ref([
    {
      id: 1,
      title: "제주도 3박 4일 여행",
      region: "제주도",
      author: "여행좋아",
      route: ["제주공항", "성산일출봉", "우도", "한라산", "서귀포"],
      category: "가족 여행",
      favorite: false,
    },
    {
      id: 2,
      title: "서울 도심 데이트 코스",
      region: "서울",
      author: "도시여행자",
      route: ["명동", "남산타워", "이태원", "홍대", "한강공원"],
      category: "커플 여행",
      favorite: false,
    },
    {
      id: 3,
      title: "부산 해변 여행",
      region: "부산",
      author: "바다사랑",
      route: ["해운대", "광안리", "태종대", "감천문화마을", "자갈치시장"],
      category: "친구 여행",
      favorite: true,
    },
    {
      id: 4,
      title: "강원도 힐링 여행",
      region: "강원도",
      author: "산속여행",
      route: ["춘천", "속초", "양양", "강릉", "평창"],
      category: "혼자 여행",
      favorite: false,
    },
    {
      id: 5,
      title: "경주 역사 탐방",
      region: "경상도",
      author: "역사탐험가",
      route: ["불국사", "석굴암", "첨성대", "안압지", "경주월드"],
      category: "가족 여행",
      favorite: false,
    },
    {
      id: 6,
      title: "전주 맛집 투어",
      region: "전라도",
      author: "맛집탐험가",
      route: ["전주한옥마을", "남부시장", "경기전", "오목대", "한옥레스토랑"],
      category: "친구 여행",
      favorite: false,
    },
  ]);
  // Filter travel plans
  const filteredPlans = computed(() => {
    let result = [...travelPlans.value];
    // Sort by favorite
    result.sort((a, b) => {
      if (a.favorite && !b.favorite) return -1;
      if (!a.favorite && b.favorite) return 1;
      return 0;
    });
    // Apply category filter
    if (selectedCategory.value) {
      result = result.filter((plan) => plan.category === selectedCategory.value);
    }
    // Apply region filter
    if (selectedRegion.value) {
      result = result.filter((plan) => plan.region === selectedRegion.value);
    }
    // Apply search query
    if (searchQuery.value.trim()) {
      const query = searchQuery.value.toLowerCase();
      result = result.filter((plan) => plan.title.toLowerCase().includes(query));
    }
    return result;
  });
  // Toggle favorite with max 3 limit
  const toggleFavorite = (id: number) => {
    const planIndex = travelPlans.value.findIndex((plan) => plan.id === id);
    if (planIndex !== -1) {
      const currentFavorites = travelPlans.value.filter(
        (plan) => plan.favorite,
      ).length;
      const isCurrentlyFavorite = travelPlans.value[planIndex].favorite;
  
      if (!isCurrentlyFavorite && currentFavorites >= 3) {
        alert("즐겨찾기는 최대 3개까지만 가능합니다.");
        return;
      }
  
      travelPlans.value[planIndex].favorite = !isCurrentlyFavorite;
    }
  };
  // Images for pages 2 and 3
  const page2Image =
    "https://readdy.ai/api/search-image?query=Beautiful%20Korean%20travel%20destination%20with%20mountains%20and%20traditional%20temples%2C%20serene%20landscape%20with%20cherry%20blossoms%20and%20clear%20blue%20sky%2C%20high%20quality%20professional%20photography%20with%20soft%20natural%20lighting%20and%20vibrant%20colors%2C%20perfect%20for%20travel%20inspiration&width=800&height=600&seq=1&orientation=landscape";
  const page3Image =
    "https://readdy.ai/api/search-image?query=Scenic%20coastal%20view%20of%20Busan%20Korea%20with%20beach%20and%20city%20skyline%2C%20beautiful%20sunset%20with%20golden%20light%20reflecting%20on%20ocean%20waves%2C%20high%20quality%20professional%20photography%20with%20dramatic%20sky%20and%20peaceful%20atmosphere%2C%20perfect%20travel%20destination&width=800&height=600&seq=2&orientation=landscape";
  
  // Function to handle route display
  const displayRoute = (route: string[]) => {
    const containerWidth = window.innerWidth * 0.3; // Approximate width for route display
    const locationWidth = 100; // Approximate width for each location
    const arrowWidth = 20; // Approximate width for arrow
    const maxItems = Math.floor(containerWidth / (locationWidth + arrowWidth));
    return route.slice(0, Math.max(2, maxItems));
  };
  </script>
  
  <style scoped>
  .filter-section {
    backdrop-filter: blur(5px);
  }
  /* Hide number input arrows */
  input[type="number"]::-webkit-inner-spin-button,
  input[type="number"]::-webkit-outer-spin-button {
    -webkit-appearance: none;
    margin: 0;
  }
  input[type="number"] {
    -moz-appearance: textfield;
  }
  /* Custom scrollbar */
  ::-webkit-scrollbar {
    width: 8px;
  }
  ::-webkit-scrollbar-track {
    background: #f1f1f1;
    border-radius: 10px;
  }
  ::-webkit-scrollbar-thumb {
    background: #9fb3df;
    border-radius: 10px;
  }
  ::-webkit-scrollbar-thumb:hover {
    background: #9ec6f3;
  }
  </style>
  