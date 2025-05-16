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
                  <!--
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
                  </tr>--> 
                  <tr
  v-for="post in posts"
  :key="post.freeId"
  class="hover:bg-gray-50 cursor-pointer"
>
  <!-- 번호 -->
  <td class="px-4 py-4 text-sm text-gray-500">{{ post.freeId }}</td>

  <!-- 제목 -->
  <td class="px-4 py-4 font-medium">
    <a href="#" class="hover:text-[#4A90E2]">
      {{ post.title }}
    </a>
  </td>

  <!-- 작성자 닉네임 -->
  <td class="px-4 py-4 text-sm">
    <div class="flex items-center">
      <!-- <img
        src="https://via.placeholder.com/24" 
        alt="작성자"
        class="w-6 h-6 rounded-full mr-2"
      /> -->
      <span>{{ post.nickName }}</span>
    </div>
  </td>

  <!-- 작성일 -->
  <td class="px-4 py-4 text-sm text-gray-500 text-center">
    {{ formatDate(post.createdAt) }}
  </td>

  <!-- 조회수 -->
  <td class="px-4 py-4 text-sm text-gray-500 text-center">
    {{ post.viewCount }}
  </td>
  <td class="px-4 py-4 text-sm text-gray-500 text-center">
    {{ post.likeCount }}
  </td>
</tr>


                </tbody>
              </table>
            </div>
  
            <!-- 카드 형태 (모바일) -->
            <div class="md:hidden space-y-4">
              <div
                v-for="post in posts"
                :key="post.id"
                class="bg-white rounded-lg border border-gray-200 p-4 hover:shadow-md transition-all cursor-pointer"
              >
                <div class="flex justify-between items-start mb-2">
                  <!-- <span
                    class="text-xs font-medium px-2 py-1 rounded-full"
                    :class="getCategoryClass(post.category)"
                  >
                    {{ getCategoryName(post.category) }}
                  </span> -->
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
                    <!-- <img
                      :src="post.authorAvatar"
                      :alt="post.author"
                      class="w-6 h-6 rounded-full mr-2"
                    /> -->
                    <span>{{ post.nickName }}</span>
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
  import axios from 'axios';
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
  
  const posts = ref([]);
const token = localStorage.getItem("accessToken");

onMounted(async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/free', {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    posts.value = response.data;
  } catch (error) {
    console.error("게시글을 불러오는 데 실패했습니다:", error);
  }
});
  
  const filteredPosts = computed(() => {
    let result = [...posts];
  
    // // 카테고리 필터링
    // if (selectedCategory.value !== "all") {
    //   result = result.filter((post) => post.category === selectedCategory.value);
    // }
  
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
  