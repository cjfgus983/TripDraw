<template>
  <div class="min-h-screen bg-white">
    <div class="h-screen w-full flex flex-col p-8">
      <!-- Filters -->
      <div class="filter-section bg-[#BDDDE4] rounded-lg p-4 mb-6 shadow-md w-4/5 mx-auto">
        <div class="flex flex-wrap items-center gap-4">
          <!-- Category Toggle -->
          <div class="relative">
            <button
              @click="toggleCategoryDropdown"
              class="bg-[#9EC6F3] text-white px-4 py-2 rounded-button flex items-center gap-2 cursor-pointer whitespace-nowrap"
            >
              <span>카테고리</span>
              <i :class="['fas', categoryDropdownOpen ? 'fa-chevron-up' : 'fa-chevron-down']" />
            </button>
            <div
              v-if="categoryDropdownOpen"
              class="absolute top-full left-0 mt-1 bg-white shadow-lg rounded-md z-10 w-48"
            >
              <ul>
                <li
                  v-for="c in categories"
                  :key="c"
                  @click="selectCategory(c)"
                  class="px-4 py-2 hover:bg-[#FFF1D5] cursor-pointer"
                >
                  {{ c }}
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
              <i :class="['fas', regionDropdownOpen ? 'fa-chevron-up' : 'fa-chevron-down']" />
            </button>
            <div
              v-if="regionDropdownOpen"
              class="absolute top-full left-0 mt-1 bg-white shadow-lg rounded-md z-10 w-48 max-h-60 overflow-y-auto"
            >
              <ul>
                <li
                  v-for="r in regions"
                  :key="r"
                  @click="selectRegion(r)"
                  class="px-4 py-2 hover:bg-[#FFF1D5] cursor-pointer"
                >
                  {{ r }}
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
              <i class="fas fa-search absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" />
            </div>
          </div>

          <!-- Active Filters -->
          <div class="flex items-center gap-2 flex-wrap" v-if="selectedCategory || selectedRegion">
            <div
              v-if="selectedCategory"
              class="bg-[#9FB3DF] text-white px-3 py-1 rounded-full text-sm flex items-center gap-1"
            >
              <span>{{ selectedCategory }}</span>
              <i @click="clearCategory" class="fas fa-times cursor-pointer" />
            </div>
            <div
              v-if="selectedRegion"
              class="bg-[#9FB3DF] text-white px-3 py-1 rounded-full text-sm flex items-center gap-1"
            >
              <span>{{ selectedRegion }}</span>
              <i @click="clearRegion" class="fas fa-times cursor-pointer" />
            </div>
          </div>
        </div>
      </div>

      <!-- Plan List -->
      <div class="flex-grow overflow-auto pb-6">
        <h2 class="text-2xl font-bold mb-6 text-[#9FB3DF] w-4/5 mx-auto">여행 계획</h2>
        <div class="space-y-4">
          <div
            v-for="plan in paginatedPlans"
            :key="plan.planBoardId"
            @click="goToDetail(plan.planBoardId)"
            class="cursor-pointer bg-white rounded-lg shadow-md p-4 flex items-center gap-4 transition-all duration-300 hover:shadow-lg w-4/5 mx-auto"
            :class="{ 'border-l-4 border-[#FFF1D5]': plan.favorite }"
          >
            <!-- Favorite Star -->
            <div @click.stop="toggleFavorite(plan.planBoardId)" class="cursor-pointer">
              <i class="fas fa-star text-xl" :class="plan.favorite ? 'text-yellow-400' : 'text-gray-300'" />
            </div>
            <!-- Plan Content -->
            <div class="flex-grow flex justify-between items-center">
              <div>
                <div class="flex items-center gap-3 mb-2">
                  <h3 class="text-lg font-semibold">{{ plan.boardTitle }}</h3>
                  <span
                    v-for="cat in plan.boardCategoryList"
                    :key="cat"
                    class="bg-[#9FB3DF] text-white px-2 py-1 rounded-md text-xs"
                  >
                    {{ cat }}
                  </span>
                  <span class="text-gray-500 text-sm">작성자: {{ plan.nickname }}</span>
                </div>
              </div>
              <!-- Route Display (최대 5개) -->
              <div class="flex items-center gap-3 ml-4">
                <template v-for="(loc, idx) in plan.route" :key="idx">
                  <span class="bg-[#FFF1D5] px-3 py-2 rounded text-base font-medium whitespace-nowrap">
                    {{ loc }}
                  </span>
                  <i v-if="idx < plan.route.length - 1" class="fas fa-arrow-right text-[#9EC6F3] text-lg flex-shrink-0" />
                </template>
              </div>
            </div>
          </div>
        </div>

        <!-- Pagination -->
        <div class="flex justify-center items-center gap-2 mt-8">
          <button
            @click="prevPage"
            :disabled="currentPage === 1"
            class="px-4 py-2 rounded-button bg-[#9FB3DF] text-white disabled:opacity-50 disabled:cursor-not-allowed whitespace-nowrap"
          >
            <i class="fas fa-chevron-left mr-1" /> 이전
          </button>
          <div class="flex items-center gap-2">
            <button
              v-for="page in totalPages"
              :key="page"
              @click="goToPage(page)"
              class="w-10 h-10 rounded-full flex items-center justify-center whitespace-nowrap"
              :class="currentPage === page ? 'bg-[#9FB3DF] text-white' : 'bg-gray-100 text-gray-600 hover:bg-gray-200'"
            >
              {{ page }}
            </button>
          </div>
          <button
            @click="nextPage"
            :disabled="currentPage === totalPages"
            class="px-4 py-2 rounded-button bg-[#9FB3DF] text-white disabled:opacity-50 disabled:cursor-not-allowed whitespace-nowrap"
          >
            다음 <i class="fas fa-chevron-right ml-1" />
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, onMounted } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";

// --- 필터 상태 ---
const categoryDropdownOpen = ref(false);
const regionDropdownOpen = ref(false);
const selectedCategory = ref("");
const selectedRegion = ref("");
const searchQuery = ref("");

const toggleCategoryDropdown = () => {
  categoryDropdownOpen.value = !categoryDropdownOpen.value;
  if (categoryDropdownOpen.value) regionDropdownOpen.value = false;
};
const toggleRegionDropdown = () => {
  regionDropdownOpen.value = !regionDropdownOpen.value;
  if (regionDropdownOpen.value) categoryDropdownOpen.value = false;
};
const selectCategory = (c: string) => {
  selectedCategory.value = c;
  categoryDropdownOpen.value = false;
};
const selectRegion = (r: string) => {
  selectedRegion.value = r;
  regionDropdownOpen.value = false;
};
const clearCategory = () => { selectedCategory.value = ""; };
const clearRegion = () => { selectedRegion.value = ""; };

const router = useRouter();

// 카드 클릭 핸들러
function goToDetail(planBoardId: number) {
  router.push({ name: "TripPlanDetail", params: { id: planBoardId } });
}


// 드롭다운 외부 클릭시 닫기
onMounted(() => {
  document.addEventListener("click", (e) => {
    if (!(e.target as HTMLElement).closest(".relative")) {
      categoryDropdownOpen.value = false;
      regionDropdownOpen.value = false;
    }
  });
});

// 필터 옵션
const categories = [
  '힐링',
  '액티비티',
  '데이트',
  '먹부림',
  '자연',
  '도심',
  '가족',
  '친구',
  '혼자',
  '비즈니스'
]
const regions = [
  // 아시아 – 국가
  "대한민국", "일본", "중국", "태국", "싱가포르", "베트남", "인도", "말레이시아",
  // 유럽 – 국가
  "프랑스", "이탈리아", "스페인", "영국", "독일", "스위스", "네덜란드", "그리스",
  // 북미 – 국가
  "미국", "캐나다", "멕시코",
  // 오세아니아 – 국가
  "호주", "뉴질랜드",
  // 아프리카 – 국가
  "이집트", "남아프리카공화국", "모로코",
  // 중동
  "아랍에미리트", "이스라엘",

  // 도시(인기 여행지)
  "파리", "런던", "뉴욕", "로마", "바르셀로나",
  "베니스", "도쿄", "오사카", "방콕", "시드니"
]

// --- 데이터 타입 ---
interface TravelPlan {
  planBoardId: number;
  planCode: string;
  boardTitle: string;
  boardContent: string;
  boardCategory: string;
  boardCategoryList: string[];
  createdAt: string;
  routeConcat: string;
  route: string[];
  userId: number;
  region: string;
  nickname: string;
  favorite: boolean;
}
interface APIPlan {
  favorite: boolean;
  nickname: string;
  planBoardId: number;
  planCode: string;
  boardTitle: string;
  boardContent: string;
  boardCategoryConcat: string;
  boardCategoryList: string[];
  createdAt: string;
  routeConcat: string;
  route: string[];
  userId?: number;
  region?: string;
}

// 여행 계획 데이터
const travelPlans = ref<TravelPlan[]>([]);

const userId = ref<number>(0);

// API 호출하여 데이터 로드
onMounted(async () => {
  try {
    const token = localStorage.getItem("accessToken");
    if (!token) {
      alert("로그인이 필요합니다.");
      return;
    }

    const { data: me } = await axios.get<{ userId: number }>(
      '/test/api/users/me',
      { headers: { Authorization: `Bearer ${token}` } }
    )
    userId.value = me.userId

    const { data } = await axios.get<APIPlan[]>(
      "/test/api/trip/boards",
      { headers: { Authorization: `Bearer ${token}` }, params: { userId: userId.value } }
    );
    travelPlans.value = data.map(p => ({
      planBoardId:       p.planBoardId,
      planCode:          p.planCode,
      boardTitle:        p.boardTitle,
      boardContent:      p.boardContent,
      boardCategory:     p.boardCategoryConcat,
      boardCategoryList: p.boardCategoryList,
      createdAt:         p.createdAt,
      routeConcat:       p.routeConcat,
      route:             p.route.map(s => s.trim()).slice(0, 5),
      userId:            p.userId ?? 0,
      region:            p.region ?? "미정",
      nickname:          p.nickname?.toString() ?? "알 수 없음",
      favorite:          p.favorite ?? false
    }));
  } catch (err) {
    console.error("API 호출 실패", err);
    alert("여행 계획 목록 로드에 실패했습니다.");
  }
});

// --- 페이징 & 필터링 ---
const itemsPerPage = 5;
const currentPage  = ref(1);

const filteredPlans = computed(() => {
  let list = [...travelPlans.value];
  // 즐겨찾기 먼저
  list.sort((a, b) => (b.favorite ? 1 : 0) - (a.favorite ? 1 : 0));
  // 카테고리 필터
  if (selectedCategory.value) {
    list = list.filter(p => p.boardCategoryList.includes(selectedCategory.value));
  }
  // 지역 필터
  if (selectedRegion.value) {
    list = list.filter(p => p.region === selectedRegion.value);
  }
  // 제목 검색
  if (searchQuery.value.trim()) {
    const q = searchQuery.value.toLowerCase();
    list = list.filter(p => p.boardTitle.toLowerCase().includes(q));
  }
  return list;
});

const totalPages = computed(() => Math.ceil(filteredPlans.value.length / itemsPerPage));
const paginatedPlans = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  return filteredPlans.value.slice(start, start + itemsPerPage);
});

function prevPage()    { if (currentPage.value > 1) currentPage.value--; }
function nextPage()    { if (currentPage.value < totalPages.value) currentPage.value++; }
function goToPage(n:number) { currentPage.value = n; }

// 즐겨찾기 토글 (최대 3개)
async function toggleFavorite(id: number) {
  const idx = travelPlans.value.findIndex(p => p.planBoardId === id);
  if (idx === -1) return;
  // 즐겨찾기 제한
  const favCount = travelPlans.value.filter(p => p.favorite).length;
  if (!travelPlans.value[idx].favorite && favCount >= 3) {
    alert("즐겨찾기는 최대 3개까지만 가능합니다.");
    return;
  }

  const token = localStorage.getItem("accessToken");
  if (!token) {
    alert("로그인이 필요합니다.");
    return;
  }

  try {
    if (!travelPlans.value[idx].favorite) {
      // 즐겨찾기 추가
      await axios.post(
        `/test/api/trip/boards/${id}/favorite`,
        null,
        {
          headers: { Authorization: `Bearer ${token}` },
          params: { userId: userId.value }
        }
      );
      travelPlans.value[idx].favorite = true;
    } else {
      // 즐겨찾기 해제
      await axios.delete(
        `/test/api/trip/boards/${id}/favorite`,
        {
          headers: { Authorization: `Bearer ${token}` },
          params: { userId: userId.value }
        }
      );
      travelPlans.value[idx].favorite = false;
    }
  } catch (e) {
    console.error("즐겨찾기 API 오류", e);
    alert("즐겨찾기 처리에 실패했습니다.");
  }
}
</script>

<style scoped>
.filter-section {
  backdrop-filter: blur(5px);
}
/* hide number input arrows */
input::-webkit-inner-spin-button,
input::-webkit-outer-spin-button {
  -webkit-appearance: none;
  margin: 0;
}
input[type="number"] { -moz-appearance: textfield; }
/* scrollbar */
::-webkit-scrollbar { width: 8px; }
::-webkit-scrollbar-track { background: #f1f1f1; border-radius: 10px; }
::-webkit-scrollbar-thumb { background: #9fb3df; border-radius: 10px; }
::-webkit-scrollbar-thumb:hover { background: #9ec6f3; }
</style>
