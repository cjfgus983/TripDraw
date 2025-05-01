<!-- The exported code uses Tailwind CSS. Install Tailwind CSS in your dev environment to ensure all styles work. -->
<template>
  
      <!-- 메인 콘텐츠 영역 -->
      <main class="flex-grow bg-gray-50">
        <!-- 게시판 헤더 섹션 -->
        <section class="relative h-[200px] overflow-hidden">
          <div
            class="absolute inset-0 bg-gradient-to-r from-[#4A90E2] to-[#357ABD] opacity-90 z-10"
          ></div>
          <img
            src="https://readdy.ai/api/search-image?query=beautiful%2520travel%2520destinations%2520collage%2520with%2520iconic%2520landmarks%2520from%2520around%2520the%2520world%2520professional%2520photography%2520with%2520vibrant%2520colors%2520and%2520clean%2520composition%2520perfect%2520for%2520travel%2520forum%2520header%2520section%2520high%2520quality%2520digital%2520art&width=1440&height=200&seq=board_header&orientation=landscape"
            alt="여행 게시판 배경"
            class="absolute inset-0 w-full h-full object-cover object-top"
          />
          <div
            class="container mx-auto px-4 h-full relative z-20 flex flex-col justify-center"
          >
            <h1 class="text-3xl font-bold text-white mb-4">자유 게시판</h1>
            <p class="text-white text-lg max-w-2xl">
              다양한 여행 경험과 정보를 공유하고 소통하는 공간입니다.
            </p>
          </div>
        </section>
  
        <!-- 게시판 기능 영역 -->
        <section class="py-6 bg-white border-b">
          <div class="container mx-auto px-4">
            <div
              class="flex flex-col md:flex-row justify-between items-center gap-4"
            >
              <!-- 검색바 -->
              <div class="relative w-full md:w-1/3">
                <input
                  type="text"
                  placeholder="게시글 검색"
                  class="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#4A90E2] text-sm"
                  v-model="searchQuery"
                />
                <i
                  class="fas fa-search absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"
                ></i>
              </div>
  
              <!-- 카테고리 탭 -->
              <div
                class="flex space-x-1 overflow-x-auto w-full md:w-auto py-2 md:py-0"
              >
                <button
                  v-for="category in categories"
                  :key="category.id"
                  @click="selectCategory(category.id)"
                  :class="[
                    'px-4 py-2 text-sm font-medium rounded-lg transition-all whitespace-nowrap !rounded-button cursor-pointer',
                    selectedCategory === category.id 
                      ? 'bg-[#4A90E2] text-white' 
                      : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
                  ]"
                >
                  {{ category.name }}
                </button>
              </div>
  
              <!-- 글쓰기 버튼 -->
              <button
                class="bg-[#4A90E2] text-white px-5 py-2 rounded-lg font-medium hover:bg-[#357ABD] transition-all flex items-center whitespace-nowrap !rounded-button cursor-pointer"
              >
                <i class="fas fa-pen-to-square mr-2"></i> 글쓰기
              </button>
            </div>
          </div>
        </section>
  
        <!-- 인기 게시글 섹션 -->
        <section class="py-8 bg-gray-50">
          <div class="container mx-auto px-4">
            <h2 class="text-2xl font-bold mb-6 flex items-center">
              <i class="fas fa-fire text-[#FF6B6B] mr-2"></i> 인기 게시글
            </h2>
  
            <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
              <div
                v-for="post in popularPosts"
                :key="post.id"
                class="bg-white rounded-lg shadow-sm overflow-hidden hover:shadow-md transition-all cursor-pointer"
              >
                <div class="h-48 overflow-hidden">
                  <img
                    :src="post.image"
                    :alt="post.title"
                    class="w-full h-full object-cover object-top"
                  />
                </div>
                <div class="p-4">
                  <div class="flex items-center mb-2">
                    <span
                      class="text-xs font-medium px-2 py-1 rounded-full"
                      :class="getCategoryClass(post.category)"
                    >
                      {{ getCategoryName(post.category) }}
                    </span>
                  </div>
                  <h3 class="text-lg font-bold mb-2 line-clamp-1">
                    {{ post.title }}
                  </h3>
                  <p class="text-gray-600 text-sm mb-3 line-clamp-2">
                    {{ post.content }}
                  </p>
                  <div
                    class="flex justify-between items-center text-sm text-gray-500"
                  >
                    <div class="flex items-center">
                      <img
                        :src="post.authorAvatar"
                        :alt="post.author"
                        class="w-6 h-6 rounded-full mr-2"
                      />
                      <span>{{ post.author }}</span>
                    </div>
                    <div class="flex space-x-3">
                      <span class="flex items-center"
                        ><i class="far fa-eye mr-1"></i> {{ post.views }}</span
                      >
                      <span class="flex items-center"
                        ><i class="far fa-heart mr-1"></i> {{ post.likes }}</span
                      >
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </section>
  
        <!-- 최신 게시글 목록 -->
        <section class="py-8 bg-white">
          <div class="container mx-auto px-4">
            <h2 class="text-2xl font-bold mb-6 flex items-center">
              <i class="fas fa-clock text-[#4A90E2] mr-2"></i> 최신 게시글
            </h2>
  
            <!-- 테이블 형태 (데스크톱) -->
            <div
              class="hidden md:block overflow-hidden rounded-lg border border-gray-200 mb-6"
            >
              <table class="w-full">
                <thead class="bg-gray-50">
                  <tr>
                    <th
                      class="px-4 py-3 text-left text-sm font-medium text-gray-500 w-12"
                    >
                      번호
                    </th>
                    <th
                      class="px-4 py-3 text-left text-sm font-medium text-gray-500 w-24"
                    >
                      카테고리
                    </th>
                    <th
                      class="px-4 py-3 text-left text-sm font-medium text-gray-500"
                    >
                      제목
                    </th>
                    <th
                      class="px-4 py-3 text-left text-sm font-medium text-gray-500 w-28"
                    >
                      작성자
                    </th>
                    <th
                      class="px-4 py-3 text-center text-sm font-medium text-gray-500 w-28"
                    >
                      작성일
                    </th>
                    <th
                      class="px-4 py-3 text-center text-sm font-medium text-gray-500 w-20"
                    >
                      조회수
                    </th>
                    <th
                      class="px-4 py-3 text-center text-sm font-medium text-gray-500 w-20"
                    >
                      좋아요
                    </th>
                  </tr>
                </thead>
                <tbody class="divide-y divide-gray-200">
                  <tr
                    v-for="post in filteredPosts"
                    :key="post.id"
                    class="hover:bg-gray-50 cursor-pointer"
                  >
                    <td class="px-4 py-4 text-sm text-gray-500">{{ post.id }}</td>
                    <td class="px-4 py-4">
                      <span
                        class="text-xs font-medium px-2 py-1 rounded-full"
                        :class="getCategoryClass(post.category)"
                      >
                        {{ getCategoryName(post.category) }}
                      </span>
                    </td>
                    <td class="px-4 py-4 font-medium">
                      <a
                        href="https://readdy.ai/home/c35feb0c-4e46-4d7a-be66-c91e9b24361e/6ef85fde-3aa6-4c31-bf25-4b3469df5ede"
                        data-readdy="true"
                        class="hover:text-[#4A90E2]"
                      >
                        {{ post.title }}
                        <span
                          v-if="post.commentCount > 0"
                          class="text-[#4A90E2] ml-2"
                          >[{{ post.commentCount }}]</span
                        >
                        <span
                          v-if="isNew(post.date)"
                          class="ml-2 text-xs font-medium text-red-500"
                          >NEW</span
                        >
                      </a>
                    </td>
                    <td class="px-4 py-4 text-sm">
                      <div class="flex items-center">
                        <img
                          :src="post.authorAvatar"
                          :alt="post.author"
                          class="w-6 h-6 rounded-full mr-2"
                        />
                        <span>{{ post.author }}</span>
                      </div>
                    </td>
                    <td class="px-4 py-4 text-sm text-gray-500 text-center">
                      {{ formatDate(post.date) }}
                    </td>
                    <td class="px-4 py-4 text-sm text-gray-500 text-center">
                      {{ post.views }}
                    </td>
                    <td class="px-4 py-4 text-sm text-gray-500 text-center">
                      {{ post.likes }}
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
  
            <!-- 카드 형태 (모바일) -->
            <div class="md:hidden space-y-4">
              <div
                v-for="post in filteredPosts"
                :key="post.id"
                class="bg-white rounded-lg border border-gray-200 p-4 hover:shadow-md transition-all cursor-pointer"
              >
                <div class="flex justify-between items-start mb-2">
                  <span
                    class="text-xs font-medium px-2 py-1 rounded-full"
                    :class="getCategoryClass(post.category)"
                  >
                    {{ getCategoryName(post.category) }}
                  </span>
                  <span class="text-xs text-gray-500"
                    >{{ formatDate(post.date) }}</span
                  >
                </div>
                <a
                  href="https://readdy.ai/home/c35feb0c-4e46-4d7a-be66-c91e9b24361e/6ef85fde-3aa6-4c31-bf25-4b3469df5ede"
                  data-readdy="true"
                  class="block"
                >
                  <h3 class="text-lg font-medium mb-2">
                    {{ post.title }}
                    <span v-if="post.commentCount > 0" class="text-[#4A90E2] ml-1"
                      >[{{ post.commentCount }}]</span
                    >
                    <span
                      v-if="isNew(post.date)"
                      class="ml-1 text-xs font-medium text-red-500"
                      >NEW</span
                    >
                  </h3>
                </a>
                <p class="text-gray-600 text-sm mb-3 line-clamp-2">
                  {{ post.content }}
                </p>
                <div
                  class="flex justify-between items-center text-sm text-gray-500"
                >
                  <div class="flex items-center">
                    <img
                      :src="post.authorAvatar"
                      :alt="post.author"
                      class="w-6 h-6 rounded-full mr-2"
                    />
                    <span>{{ post.author }}</span>
                  </div>
                  <div class="flex space-x-3">
                    <span class="flex items-center"
                      ><i class="far fa-eye mr-1"></i> {{ post.views }}</span
                    >
                    <span class="flex items-center"
                      ><i class="far fa-heart mr-1"></i> {{ post.likes }}</span
                    >
                  </div>
                </div>
              </div>
            </div>
  
            <!-- 페이지네이션 -->
            <div class="flex justify-center mt-8">
              <nav class="flex items-center space-x-1">
                <button
                  @click="changePage(currentPage - 1)"
                  :disabled="currentPage === 1"
                  :class="[
                    'px-3 py-2 rounded-md !rounded-button whitespace-nowrap cursor-pointer',
                    currentPage === 1 ? 'text-gray-400 cursor-not-allowed' : 'text-gray-700 hover:bg-gray-100'
                  ]"
                >
                  <i class="fas fa-chevron-left"></i>
                </button>
  
                <button
                  v-for="page in displayedPages"
                  :key="page"
                  @click="changePage(page)"
                  :class="[
                    'px-3 py-2 rounded-md !rounded-button whitespace-nowrap cursor-pointer',
                    currentPage === page 
                      ? 'bg-[#4A90E2] text-white' 
                      : 'text-gray-700 hover:bg-gray-100'
                  ]"
                >
                  {{ page }}
                </button>
  
                <button
                  @click="changePage(currentPage + 1)"
                  :disabled="currentPage === totalPages"
                  :class="[
                    'px-3 py-2 rounded-md !rounded-button whitespace-nowrap cursor-pointer',
                    currentPage === totalPages ? 'text-gray-400 cursor-not-allowed' : 'text-gray-700 hover:bg-gray-100'
                  ]"
                >
                  <i class="fas fa-chevron-right"></i>
                </button>
              </nav>
            </div>
          </div>
        </section>
      </main>
  </template>
  
  <script lang="ts" setup>
  import { ref, computed, onMounted } from "vue";
  
  const mobileMenuOpen = ref(false);
  const searchQuery = ref("");
  const selectedCategory = ref("all");
  const currentPage = ref(1);
  const postsPerPage = 10;
  
  const toggleMobileMenu = () => {
    mobileMenuOpen.value = !mobileMenuOpen.value;
  };
  
  const categories = [
    { id: "all", name: "전체" },
    { id: "review", name: "여행 후기" },
    { id: "tips", name: "여행 팁" },
    { id: "companion", name: "동행 구하기" },
    { id: "question", name: "질문/답변" },
    { id: "info", name: "여행 정보" },
  ];
  
  const popularPosts = [
    {
      id: 1,
      title: "제주도 3박 4일 여행 후기 - 꼭 가봐야 할 숨은 명소",
      content:
        "제주도를 3박 4일 동안 여행하면서 발견한 관광객이 잘 모르는 숨은 명소들을 공유합니다. 렌트카 없이도 대중교통으로 충분히 갈 수 있는 곳들이에요.",
      author: "여행좋아",
      authorAvatar:
        "https://readdy.ai/api/search-image?query=professional%2520headshot%2520of%2520young%2520korean%2520woman%2520in%2520casual%2520attire%2520with%2520neutral%2520background%2520clean%2520portrait%2520style&width=100&height=100&seq=avatar1&orientation=squarish",
      date: "2025-04-28T14:30:00",
      views: 1245,
      likes: 89,
      category: "review",
      commentCount: 23,
      image:
        "https://readdy.ai/api/search-image?query=beautiful%2520jeju%2520island%2520korea%2520scenic%2520landscape%2520with%2520volcanic%2520rocks%2520and%2520ocean%2520view%2520professional%2520travel%2520photography%2520with%2520vibrant%2520colors%2520and%2520perfect%2520lighting%2520high%2520quality%2520digital%2520art&width=400&height=250&seq=post1&orientation=landscape",
    },
    {
      id: 2,
      title: "유럽 배낭여행 준비물 체크리스트 & 꿀팁 모음",
      content:
        "3개월간의 유럽 배낭여행을 준비하면서 알게 된 모든 팁과 필수 준비물 목록을 정리했습니다. 처음 유럽 여행을 계획하시는 분들에게 도움이 될 거예요.",
      author: "세계여행러",
      authorAvatar:
        "https://readdy.ai/api/search-image?query=professional%2520headshot%2520of%2520young%2520korean%2520man%2520in%2520casual%2520attire%2520with%2520neutral%2520background%2520clean%2520portrait%2520style&width=100&height=100&seq=avatar2&orientation=squarish",
      date: "2025-04-25T09:15:00",
      views: 2367,
      likes: 152,
      category: "tips",
      commentCount: 45,
      image:
        "https://readdy.ai/api/search-image?query=european%2520travel%2520essentials%2520flatlay%2520with%2520passport%2520camera%2520map%2520and%2520travel%2520items%2520on%2520wooden%2520background%2520professional%2520photography%2520with%2520clean%2520composition%2520high%2520quality%2520digital%2520art&width=400&height=250&seq=post2&orientation=landscape",
    },
    {
      id: 3,
      title: "5월 태국 치앙마이 동행 구합니다 (2명)",
      content:
        "5월 15일부터 20일까지 태국 치앙마이 여행을 계획 중인데, 함께 여행할 동행 2명을 구합니다. 20-30대 남녀 무관이며, 맛집 탐방과 문화 체험에 관심 있으신 분이면 좋겠습니다.",
      author: "여행친구",
      authorAvatar:
        "https://readdy.ai/api/search-image?query=professional%2520headshot%2520of%2520middle%2520aged%2520korean%2520woman%2520in%2520casual%2520attire%2520with%2520neutral%2520background%2520clean%2520portrait%2520style&width=100&height=100&seq=avatar3&orientation=squarish",
      date: "2025-05-01T10:45:00",
      views: 876,
      likes: 42,
      category: "companion",
      commentCount: 18,
      image:
        "https://readdy.ai/api/search-image?query=beautiful%2520chiang%2520mai%2520thailand%2520with%2520traditional%2520temples%2520and%2520lantern%2520festival%2520professional%2520travel%2520photography%2520with%2520vibrant%2520colors%2520and%2520perfect%2520lighting%2520high%2520quality%2520digital%2520art&width=400&height=250&seq=post3&orientation=landscape",
    },
  ];
  
  const posts = [
    ...popularPosts,
    {
      id: 4,
      title: "일본 오사카 맛집 베스트 10 - 현지인 추천 장소",
      content:
        "오사카 현지인 친구에게 추천받은 진짜 맛집 10곳을 소개합니다. 관광객이 잘 모르는 숨은 맛집부터 인기 있는 맛집까지 다양하게 정리했어요.",
      author: "맛집탐험가",
      authorAvatar:
        "https://readdy.ai/api/search-image?query=professional%2520headshot%2520of%2520young%2520korean%2520man%2520in%2520casual%2520attire%2520with%2520neutral%2520background%2520clean%2520portrait%2520style&width=100&height=100&seq=avatar4&orientation=squarish",
      date: "2025-04-20T16:20:00",
      views: 1876,
      likes: 134,
      category: "info",
      commentCount: 27,
      image:
        "https://readdy.ai/api/search-image?query=delicious%2520japanese%2520street%2520food%2520in%2520osaka%2520with%2520takoyaki%2520and%2520okonomiyaki%2520professional%2520food%2520photography%2520with%2520vibrant%2520colors%2520and%2520perfect%2520lighting%2520high%2520quality%2520digital%2520art&width=400&height=250&seq=post4&orientation=landscape",
    },
    {
      id: 5,
      title: "혼자 떠난 베트남 다낭 4박 5일 여행기",
      content:
        "처음으로 혼자 떠난 해외여행, 베트남 다낭에서의 4박 5일 여행 경험을 공유합니다. 혼자 여행하시는 분들을 위한 팁도 함께 알려드려요.",
      author: "솔로트래블러",
      authorAvatar:
        "https://readdy.ai/api/search-image?query=professional%2520headshot%2520of%2520young%2520korean%2520woman%2520in%2520casual%2520attire%2520with%2520neutral%2520background%2520clean%2520portrait%2520style&width=100&height=100&seq=avatar5&orientation=squarish",
      date: "2025-04-15T11:30:00",
      views: 1543,
      likes: 98,
      category: "review",
      commentCount: 31,
      image:
        "https://readdy.ai/api/search-image?query=beautiful%2520golden%2520bridge%2520in%2520ba%2520na%2520hills%2520danang%2520vietnam%2520with%2520mountain%2520view%2520professional%2520travel%2520photography%2520with%2520vibrant%2520colors%2520and%2520perfect%2520lighting%2520high%2520quality%2520digital%2520art&width=400&height=250&seq=post5&orientation=landscape",
    },
    {
      id: 6,
      title: "항공권 싸게 구매하는 꿀팁 총정리",
      content:
        "10년간 여행을 다니며 알게 된 항공권 저렴하게 구매하는 방법을 모두 공유합니다. 시즌별, 노선별 특징과 예약 타이밍까지 상세히 알려드려요.",
      author: "여행의신",
      authorAvatar:
        "https://readdy.ai/api/search-image?query=professional%2520headshot%2520of%2520middle%2520aged%2520korean%2520man%2520in%2520casual%2520attire%2520with%2520neutral%2520background%2520clean%2520portrait%2520style&width=100&height=100&seq=avatar6&orientation=squarish",
      date: "2025-04-10T13:45:00",
      views: 3254,
      likes: 276,
      category: "tips",
      commentCount: 52,
      image:
        "https://readdy.ai/api/search-image?query=airplane%2520flying%2520above%2520clouds%2520with%2520beautiful%2520sunset%2520view%2520from%2520window%2520professional%2520travel%2520photography%2520with%2520vibrant%2520colors%2520and%2520perfect%2520lighting%2520high%2520quality%2520digital%2520art&width=400&height=250&seq=post6&orientation=landscape",
    },
    {
      id: 7,
      title: "6월 중순 북유럽 3개국 함께 여행하실 분 찾습니다",
      content:
        "6월 15일부터 30일까지 스웨덴, 노르웨이, 핀란드 3개국 여행을 계획 중입니다. 함께 여행할 동행을 구해요. 20-30대 남녀 무관, 사진 찍는 것을 좋아하시는 분이면 더 좋아요.",
      author: "북극성",
      authorAvatar:
        "https://readdy.ai/api/search-image?query=professional%2520headshot%2520of%2520young%2520korean%2520man%2520in%2520casual%2520attire%2520with%2520neutral%2520background%2520clean%2520portrait%2520style&width=100&height=100&seq=avatar7&orientation=squarish",
      date: "2025-04-05T17:20:00",
      views: 754,
      likes: 38,
      category: "companion",
      commentCount: 14,
      image:
        "https://readdy.ai/api/search-image?query=beautiful%2520northern%2520lights%2520aurora%2520borealis%2520in%2520norway%2520with%2520snow%2520covered%2520landscape%2520professional%2520travel%2520photography%2520with%2520vibrant%2520colors%2520and%2520perfect%2520lighting%2520high%2520quality%2520digital%2520art&width=400&height=250&seq=post7&orientation=landscape",
    },
    {
      id: 8,
      title: "국내 여행 시 꼭 챙겨야 할 필수 앱 10가지",
      content:
        "국내 여행을 더 편리하고 알차게 만들어주는 필수 앱 10가지를 소개합니다. 교통, 숙소, 맛집, 관광지 정보 등 카테고리별로 정리했어요.",
      author: "앱리뷰어",
      authorAvatar:
        "https://readdy.ai/api/search-image?query=professional%2520headshot%2520of%2520young%2520korean%2520woman%2520in%2520casual%2520attire%2520with%2520neutral%2520background%2520clean%2520portrait%2520style&width=100&height=100&seq=avatar8&orientation=squarish",
      date: "2025-04-01T09:10:00",
      views: 2165,
      likes: 187,
      category: "tips",
      commentCount: 35,
      image:
        "https://readdy.ai/api/search-image?query=smartphone%2520with%2520travel%2520apps%2520on%2520screen%2520with%2520map%2520and%2520travel%2520items%2520around%2520professional%2520photography%2520with%2520clean%2520composition%2520high%2520quality%2520digital%2520art&width=400&height=250&seq=post8&orientation=landscape",
    },
    {
      id: 9,
      title: "태국 방콕 여행 시 주의해야 할 사기 수법 5가지",
      content:
        "방콕 여행 중 당할 수 있는 흔한 사기 수법 5가지와 대처 방법을 알려드립니다. 실제 경험담을 바탕으로 작성했어요.",
      author: "여행안전지킴이",
      authorAvatar:
        "https://readdy.ai/api/search-image?query=professional%2520headshot%2520of%2520middle%2520aged%2520korean%2520man%2520in%2520casual%2520attire%2520with%2520neutral%2520background%2520clean%2520portrait%2520style&width=100&height=100&seq=avatar9&orientation=squarish",
      date: "2025-03-25T14:50:00",
      views: 1876,
      likes: 142,
      category: "tips",
      commentCount: 47,
      image:
        "https://readdy.ai/api/search-image?query=busy%2520street%2520in%2520bangkok%2520thailand%2520with%2520tuk%2520tuk%2520and%2520street%2520vendors%2520professional%2520travel%2520photography%2520with%2520vibrant%2520colors%2520and%2520perfect%2520lighting%2520high%2520quality%2520digital%2520art&width=400&height=250&seq=post9&orientation=landscape",
    },
    {
      id: 10,
      title: "4월 말 제주도 렌트카 동행 구합니다",
      content:
        "4월 28일부터 5월 2일까지 제주도 여행 예정인데, 렌트카 비용 나눠서 함께 여행하실 분을 찾습니다. 2-3명 정도 모집 중이에요.",
      author: "제주사랑",
      authorAvatar:
        "https://readdy.ai/api/search-image?query=professional%2520headshot%2520of%2520young%2520korean%2520woman%2520in%2520casual%2520attire%2520with%2520neutral%2520background%2520clean%2520portrait%2520style&width=100&height=100&seq=avatar10&orientation=squarish",
      date: "2025-03-20T10:30:00",
      views: 687,
      likes: 29,
      category: "companion",
      commentCount: 8,
      image:
        "https://readdy.ai/api/search-image?query=car%2520driving%2520on%2520coastal%2520road%2520in%2520jeju%2520island%2520korea%2520with%2520ocean%2520view%2520professional%2520travel%2520photography%2520with%2520vibrant%2520colors%2520and%2520perfect%2520lighting%2520high%2520quality%2520digital%2520art&width=400&height=250&seq=post10&orientation=landscape",
    },
    {
      id: 11,
      title: "여름 휴가 추천! 동남아 비수기 여행지 BEST 5",
      content:
        "여름에 가면 비수기라 저렴하고 한적하게 즐길 수 있는 동남아 여행지 5곳을 소개합니다. 각 지역별 장단점과 추천 일정도 함께 알려드려요.",
      author: "여행플래너",
      authorAvatar:
        "https://readdy.ai/api/search-image?query=professional%2520headshot%2520of%2520middle%2520aged%2520korean%2520woman%2520in%2520casual%2520attire%2520with%2520neutral%2520background%2520clean%2520portrait%2520style&width=100&height=100&seq=avatar11&orientation=squarish",
      date: "2025-03-15T15:40:00",
      views: 1543,
      likes: 112,
      category: "info",
      commentCount: 23,
      image:
        "https://readdy.ai/api/search-image?query=beautiful%2520empty%2520tropical%2520beach%2520in%2520southeast%2520asia%2520with%2520turquoise%2520water%2520and%2520palm%2520trees%2520professional%2520travel%2520photography%2520with%2520vibrant%2520colors%2520and%2520perfect%2520lighting%2520high%2520quality%2520digital%2520art&width=400&height=250&seq=post11&orientation=landscape",
    },
    {
      id: 12,
      title: "호주 워킹홀리데이 1년 생활 총정리 및 팁",
      content:
        "호주에서 1년간 워킹홀리데이를 하며 경험한 모든 것들을 정리했습니다. 비자 신청부터 일자리 구하기, 생활비 관리까지 상세히 공유해요.",
      author: "세계여행러",
      authorAvatar:
        "https://readdy.ai/api/search-image?query=professional%2520headshot%2520of%2520young%2520korean%2520man%2520in%2520casual%2520attire%2520with%2520neutral%2520background%2520clean%2520portrait%2520style&width=100&height=100&seq=avatar2&orientation=squarish",
      date: "2025-03-10T11:25:00",
      views: 2876,
      likes: 231,
      category: "review",
      commentCount: 64,
      image:
        "https://readdy.ai/api/search-image?query=sydney%2520opera%2520house%2520and%2520harbour%2520bridge%2520at%2520sunset%2520australia%2520professional%2520travel%2520photography%2520with%2520vibrant%2520colors%2520and%2520perfect%2520lighting%2520high%2520quality%2520digital%2520art&width=400&height=250&seq=post12&orientation=landscape",
    },
    {
      id: 13,
      title: "해외여행 시 꼭 챙겨야 할 여행 필수품 리스트",
      content:
        "해외여행을 준비할 때 꼭 챙겨야 할 필수품 목록을 국가/지역별로 정리했습니다. 많은 여행 경험을 바탕으로 실용적인 팁만 모았어요.",
      author: "여행의신",
      authorAvatar:
        "https://readdy.ai/api/search-image?query=professional%2520headshot%2520of%2520middle%2520aged%2520korean%2520man%2520in%2520casual%2520attire%2520with%2520neutral%2520background%2520clean%2520portrait%2520style&width=100&height=100&seq=avatar6&orientation=squarish",
      date: "2025-03-05T13:15:00",
      views: 3421,
      likes: 289,
      category: "tips",
      commentCount: 41,
      image:
        "https://readdy.ai/api/search-image?query=travel%2520essentials%2520flatlay%2520with%2520passport%2520camera%2520adapter%2520and%2520toiletries%2520on%2520light%2520background%2520professional%2520photography%2520with%2520clean%2520composition%2520high%2520quality%2520digital%2520art&width=400&height=250&seq=post13&orientation=landscape",
    },
    {
      id: 14,
      title: "질문: 스위스 융프라우 지역 대중교통으로 여행 가능한가요?",
      content:
        "다음 달에 스위스 여행을 계획 중인데, 융프라우 지역을 렌트카 없이 대중교통만으로 여행이 가능할지 궁금합니다. 경험자분들의 조언 부탁드려요.",
      author: "여행초보",
      authorAvatar:
        "https://readdy.ai/api/search-image?query=professional%2520headshot%2520of%2520young%2520korean%2520woman%2520in%2520casual%2520attire%2520with%2520neutral%2520background%2520clean%2520portrait%2520style&width=100&height=100&seq=avatar14&orientation=squarish",
      date: "2025-03-01T09:50:00",
      views: 865,
      likes: 42,
      category: "question",
      commentCount: 17,
      image:
        "https://readdy.ai/api/search-image?query=beautiful%2520jungfrau%2520region%2520in%2520switzerland%2520with%2520snow%2520capped%2520mountains%2520and%2520green%2520valleys%2520professional%2520travel%2520photography%2520with%2520vibrant%2520colors%2520and%2520perfect%2520lighting%2520high%2520quality%2520digital%2520art&width=400&height=250&seq=post14&orientation=landscape",
    },
    {
      id: 15,
      title: "5월 황금연휴 국내 여행지 추천 (혼잡도 낮은 곳)",
      content:
        "다가오는 5월 황금연휴에 사람 많은 유명 관광지 대신 한적하게 즐길 수 있는 국내 여행지를 추천합니다. 지역별로 정리했어요.",
      author: "조용한여행자",
      authorAvatar:
        "https://readdy.ai/api/search-image?query=professional%2520headshot%2520of%2520middle%2520aged%2520korean%2520man%2520in%2520casual%2520attire%2520with%2520neutral%2520background%2520clean%2520portrait%2520style&width=100&height=100&seq=avatar15&orientation=squarish",
      date: "2025-02-25T14:30:00",
      views: 1987,
      likes: 156,
      category: "info",
      commentCount: 29,
      image:
        "https://readdy.ai/api/search-image?query=peaceful%2520korean%2520countryside%2520landscape%2520with%2520traditional%2520houses%2520and%2520mountains%2520in%2520spring%2520professional%2520travel%2520photography%2520with%2520vibrant%2520colors%2520and%2520perfect%2520lighting%2520high%2520quality%2520digital%2520art&width=400&height=250&seq=post15&orientation=landscape",
    },
    {
      id: 16,
      title: "일본 오키나와 4박 5일 자유여행 후기",
      content:
        "일본 오키나와를 4박 5일 동안 자유여행으로 다녀온 경험을 공유합니다. 렌트카로 섬 구석구석을 돌아다니며 발견한 숨은 명소들을 알려드려요.",
      author: "맛집탐험가",
      authorAvatar:
        "https://readdy.ai/api/search-image?query=professional%2520headshot%2520of%2520young%2520korean%2520man%2520in%2520casual%2520attire%2520with%2520neutral%2520background%2520clean%2520portrait%2520style&width=100&height=100&seq=avatar4&orientation=squarish",
      date: "2025-02-20T16:45:00",
      views: 1432,
      likes: 98,
      category: "review",
      commentCount: 22,
      image:
        "https://readdy.ai/api/search-image?query=beautiful%2520okinawa%2520beach%2520with%2520crystal%2520clear%2520turquoise%2520water%2520and%2520white%2520sand%2520professional%2520travel%2520photography%2520with%2520vibrant%2520colors%2520and%2520perfect%2520lighting%2520high%2520quality%2520digital%2520art&width=400&height=250&seq=post16&orientation=landscape",
    },
    {
      id: 17,
      title: "질문: 유럽 여행 시 심카드 vs 포켓와이파이 어떤 게 좋을까요?",
      content:
        "다음 달 유럽 3개국(프랑스, 이탈리아, 스페인) 2주 여행 예정인데, 현지 심카드와 포켓와이파이 중 어떤 것이 더 경제적이고 편리할지 조언 부탁드립니다.",
      author: "인터넷중독",
      authorAvatar:
        "https://readdy.ai/api/search-image?query=professional%2520headshot%2520of%2520young%2520korean%2520woman%2520in%2520casual%2520attire%2520with%2520neutral%2520background%2520clean%2520portrait%2520style&width=100&height=100&seq=avatar17&orientation=squarish",
      date: "2025-02-15T10:20:00",
      views: 1254,
      likes: 67,
      category: "question",
      commentCount: 31,
      image:
        "https://readdy.ai/api/search-image?query=smartphone%2520with%2520map%2520app%2520and%2520sim%2520card%2520on%2520european%2520map%2520background%2520professional%2520photography%2520with%2520clean%2520composition%2520high%2520quality%2520digital%2520art&width=400&height=250&seq=post17&orientation=landscape",
    },
    {
      id: 18,
      title: "여름 휴가 때 가기 좋은 동남아 비수기 여행지 추천",
      content:
        "여름에 동남아 여행을 계획 중이신 분들을 위해 비수기라 저렴하고 한적하게 즐길 수 있는 여행지를 추천합니다. 각 지역별 특징과 장단점도 함께 알려드려요.",
      author: "여행플래너",
      authorAvatar:
        "https://readdy.ai/api/search-image?query=professional%2520headshot%2520of%2520middle%2520aged%2520korean%2520woman%2520in%2520casual%2520attire%2520with%2520neutral%2520background%2520clean%2520portrait%2520style&width=100&height=100&seq=avatar11&orientation=squarish",
      date: "2025-02-10T13:40:00",
      views: 1765,
      likes: 124,
      category: "info",
      commentCount: 19,
      image:
        "https://readdy.ai/api/search-image?query=beautiful%2520tropical%2520beach%2520in%2520southeast%2520asia%2520with%2520longtail%2520boats%2520and%2520limestone%2520cliffs%2520professional%2520travel%2520photography%2520with%2520vibrant%2520colors%2520and%2520perfect%2520lighting%2520high%2520quality%2520digital%2520art&width=400&height=250&seq=post18&orientation=landscape",
    },
    {
      id: 19,
      title: "7월 초 발리 우붓 지역 동행 구합니다",
      content:
        "7월 1일부터 7일까지 발리 우붓 지역 여행 예정인데, 함께 여행할 동행을 구합니다. 20-30대 여성분들 환영해요. 편안한 분위기에서 힐링하는 여행을 원합니다.",
      author: "발리러버",
      authorAvatar:
        "https://readdy.ai/api/search-image?query=professional%2520headshot%2520of%2520young%2520korean%2520woman%2520in%2520casual%2520attire%2520with%2520neutral%2520background%2520clean%2520portrait%2520style&width=100&height=100&seq=avatar19&orientation=squarish",
      date: "2025-02-05T11:30:00",
      views: 643,
      likes: 31,
      category: "companion",
      commentCount: 12,
      image:
        "https://readdy.ai/api/search-image?query=beautiful%2520rice%2520terraces%2520in%2520ubud%2520bali%2520with%2520palm%2520trees%2520and%2520traditional%2520balinese%2520architecture%2520professional%2520travel%2520photography%2520with%2520vibrant%2520colors%2520and%2520perfect%2520lighting%2520high%2520quality%2520digital%2520art&width=400&height=250&seq=post19&orientation=landscape",
    },
    {
      id: 20,
      title: "여행 사진 잘 찍는 팁 (스마트폰으로도 가능)",
      content:
        "전문 장비 없이 스마트폰만으로도 인생 여행 사진을 찍을 수 있는 팁을 공유합니다. 구도, 조명, 편집까지 단계별로 알려드려요.",
      author: "여행사진작가",
      authorAvatar:
        "https://readdy.ai/api/search-image?query=professional%2520headshot%2520of%2520young%2520korean%2520man%2520in%2520casual%2520attire%2520with%2520neutral%2520background%2520clean%2520portrait%2520style&width=100&height=100&seq=avatar20&orientation=squarish",
      date: "2025-02-01T15:15:00",
      views: 2543,
      likes: 198,
      category: "tips",
      commentCount: 37,
      image:
        "https://readdy.ai/api/search-image?query=person%2520taking%2520photo%2520with%2520smartphone%2520of%2520beautiful%2520sunset%2520at%2520beach%2520silhouette%2520style%2520professional%2520photography%2520with%2520vibrant%2520colors%2520and%2520perfect%2520lighting%2520high%2520quality%2520digital%2520art&width=400&height=250&seq=post20&orientation=landscape",
    },
  ];
  
  const filteredPosts = computed(() => {
    let result = [...posts];
  
    // 카테고리 필터링
    if (selectedCategory.value !== "all") {
      result = result.filter((post) => post.category === selectedCategory.value);
    }
  
    // 검색어 필터링
    if (searchQuery.value.trim()) {
      const query = searchQuery.value.toLowerCase();
      result = result.filter(
        (post) =>
          post.title.toLowerCase().includes(query) ||
          post.content.toLowerCase().includes(query) ||
          post.author.toLowerCase().includes(query),
      );
    }
  
    // 페이지네이션
    const startIndex = (currentPage.value - 1) * postsPerPage;
    const endIndex = startIndex + postsPerPage;
  
    return result.slice(startIndex, endIndex);
  });
  
  const totalPages = computed(() => {
    let filteredCount = posts.length;
  
    if (selectedCategory.value !== "all") {
      filteredCount = posts.filter(
        (post) => post.category === selectedCategory.value,
      ).length;
    }
  
    if (searchQuery.value.trim()) {
      const query = searchQuery.value.toLowerCase();
      filteredCount = posts.filter(
        (post) =>
          post.title.toLowerCase().includes(query) ||
          post.content.toLowerCase().includes(query) ||
          post.author.toLowerCase().includes(query),
      ).length;
    }
  
    return Math.ceil(filteredCount / postsPerPage);
  });
  
  const displayedPages = computed(() => {
    const pages = [];
    const maxVisiblePages = 5;
  
    if (totalPages.value <= maxVisiblePages) {
      for (let i = 1; i <= totalPages.value; i++) {
        pages.push(i);
      }
    } else {
      let startPage = Math.max(
        1,
        currentPage.value - Math.floor(maxVisiblePages / 2),
      );
      let endPage = startPage + maxVisiblePages - 1;
  
      if (endPage > totalPages.value) {
        endPage = totalPages.value;
        startPage = Math.max(1, endPage - maxVisiblePages + 1);
      }
  
      for (let i = startPage; i <= endPage; i++) {
        pages.push(i);
      }
    }
  
    return pages;
  });
  
  const selectCategory = (categoryId: string) => {
    selectedCategory.value = categoryId;
    currentPage.value = 1;
  };
  
  const changePage = (page: number) => {
    if (page >= 1 && page <= totalPages.value) {
      currentPage.value = page;
      window.scrollTo({ top: 0, behavior: "smooth" });
    }
  };
  
  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    const now = new Date();
    const diffTime = Math.abs(now.getTime() - date.getTime());
    const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24));
  
    if (diffDays === 0) {
      return "오늘";
    } else if (diffDays === 1) {
      return "어제";
    } else if (diffDays < 7) {
      return `${diffDays}일 전`;
    } else {
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, "0")}-${String(date.getDate()).padStart(2, "0")}`;
    }
  };
  
  const isNew = (dateString: string) => {
    const date = new Date(dateString);
    const now = new Date();
    const diffTime = Math.abs(now.getTime() - date.getTime());
    const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24));
  
    return diffDays <= 3;
  };
  
  const getCategoryName = (categoryId: string) => {
    const category = categories.find((c) => c.id === categoryId);
    return category ? category.name : "";
  };
  
  const getCategoryClass = (categoryId: string) => {
    switch (categoryId) {
      case "review":
        return "bg-blue-100 text-blue-800";
      case "tips":
        return "bg-green-100 text-green-800";
      case "companion":
        return "bg-purple-100 text-purple-800";
      case "question":
        return "bg-orange-100 text-orange-800";
      case "info":
        return "bg-teal-100 text-teal-800";
      default:
        return "bg-gray-100 text-gray-800";
    }
  };
  </script>
  
  <style scoped>
  @import url("https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@300;400;500;700&display=swap");
  
  body {
    font-family: "Noto Sans KR", sans-serif;
    min-height: 1024px;
  }
  
  .nav-link {
    position: relative;
    padding: 0.5rem 0;
    transition: all 0.3s ease;
  }
  
  .nav-link:hover {
    color: #9ec6f3;
  }
  
  .nav-link::after {
    content: "";
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
  
  .mobile-menu {
    max-height: 0;
    overflow: hidden;
    transition: max-height 0.3s ease;
  }
  
  .mobile-menu.open {
    max-height: 500px;
  }
  
  .line-clamp-1 {
    display: -webkit-box;
    -webkit-line-clamp: 1;
    -webkit-box-orient: vertical;
    overflow: hidden;
  }
  
  .line-clamp-2 {
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
  }
  
  input[type="number"]::-webkit-inner-spin-button,
  input[type="number"]::-webkit-outer-spin-button {
    -webkit-appearance: none;
    margin: 0;
  }
  </style>
  