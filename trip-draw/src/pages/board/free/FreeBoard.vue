<!-- The exported code uses Tailwind CSS. Install Tailwind CSS in your dev environment to ensure all styles work. -->
<template>
<div class="min-h-screen flex flex-col">
<!-- Import Swiper styles -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/swiper@8/swiper-bundle.min.css" />

<!-- 메인 콘텐츠 영역 -->
<main class="flex-grow bg-gray-50">
<!-- 게시판 헤더 섹션 -->
<section class="relative h-[200px] overflow-hidden">
<div class="absolute inset-0 bg-gradient-to-r from-[#9FB3DF] to-[#BDDDE4] opacity-90 z-10"></div>
<img
src="https://readdy.ai/api/search-image?query=modern%2520abstract%2520background%2520with%2520soft%2520flowing%2520shapes%2520and%2520gentle%2520curves%2520in%2520pastel%2520colors%2520perfect%2520for%2520community%2520forum%2520header%2520professional%2520clean%2520design&width=1440&height=200&seq=board_header&orientation=landscape"
alt="자유 게시판 배경"
class="absolute inset-0 w-full h-full object-cover object-top"
/>
<div class="container mx-auto px-4 h-full relative z-20 flex flex-col justify-center">
<h1 class="text-3xl font-bold text-white mb-4">자유 게시판</h1>
<p class="text-white text-lg max-w-2xl">자유롭게 이야기를 나누고 소통하는 공간입니다.</p>
</div>
</section>
<!-- 게시판 기능 영역 -->
<section class="py-6 bg-white border-b">
<div class="container mx-auto px-4">
<div class="flex justify-between items-center gap-4">
<!-- 검색바 -->
<div class="relative w-full md:w-1/3">
<input
type="text"
placeholder="게시글 검색"
class="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#9FB3DF] text-sm"
v-model="searchQuery"
/>
<i class="fas fa-search absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"></i>
</div>
<router-link
  to="/freeregist"
  class="bg-[#9FB3DF] text-white px-5 py-2 rounded-lg font-medium hover:bg-[#8CA3D0] transition-all flex items-center whitespace-nowrap !rounded-button cursor-pointer"
>
  <i class="fas fa-pen-to-square mr-2"></i> 글쓰기
</router-link>
</div>
</div>
</section>
<!-- 인기 게시글 섹션 -->
<section class="py-8 bg-[#FFF1D5]">
<div class="container mx-auto px-4">
<h2 class="text-2xl font-bold mb-6 flex items-center">
<i class="fas fa-fire text-[#9FB3DF] mr-2"></i> 인기 게시글
</h2>
<swiper
:modules="swiperModules"
:slides-per-view="1"
:space-between="20"
:breakpoints="{
'640': {
slidesPerView: 2,
spaceBetween: 20
},
'768': {
slidesPerView: 3,
spaceBetween: 30
},
'1024': {
slidesPerView: 4,
spaceBetween: 30
}
}"
:pagination="{ clickable: true }"
:grabCursor="true"
class="mySwiper"
>
<swiper-slide
v-for="post in popularPosts"
:key="post.freeId"
class="bg-white rounded-lg shadow-sm overflow-hidden hover:shadow-md transition-all cursor-pointer"
>
<div class="h-48 overflow-hidden">
<img
   :src="(post.imageUrls && post.imageUrls.length)
           ? post.imageUrls[0]
          : logo"
        @error="onImageError"
        :alt="post.title"
        class="w-full h-full object-cover object-top"
      />
</div>
<div class="p-4">
<div class="flex items-center mb-2">
<span class="text-xs font-medium px-2 py-1 rounded-full" :class="getCategoryClass(post.category)">
{{ getCategoryName(post.category) }}
</span>
</div>
<h3 class="text-lg font-bold mb-2 line-clamp-1">{{ post.title }}</h3>
<p class="text-gray-600 text-sm mb-3 line-clamp-2">{{ post.content }}</p>
<div class="flex justify-between items-center text-sm text-gray-500">
<div class="flex items-center">
<!-- <img :src="post.authorAvatar" :alt="post.nickName" class="w-6 h-6 rounded-full mr-2" /> -->
<span>{{ post.nickName }}</span>
</div>
<div class="flex space-x-3">
<span class="flex items-center"><i class="far fa-eye mr-1"></i> {{ post.viewCount }}</span>
<span class="flex items-center"><i class="far fa-heart mr-1"></i> {{ post.likeCount }}</span>
</div>
</div>
</div>
</swiper-slide>
</swiper>
</div>
</section>
<!-- 최신 게시글 목록 -->
<section class="py-8 bg-white">
<div class="container mx-auto px-4">
<h2 class="text-2xl font-bold mb-6 flex items-center">
<i class="fas fa-clock text-[#4A90E2] mr-2"></i> 최신 게시글
</h2>
<!-- 테이블 형태 (데스크톱) -->
<div class="hidden md:block overflow-hidden rounded-lg border border-gray-200 mb-6">
<table class="w-full">
<thead class="bg-gray-50">
<tr>
<th class="px-4 py-3 text-left text-sm font-medium text-gray-500 w-15">번호</th>
<th class="px-4 py-3 text-left text-sm font-medium text-gray-500">제목</th>
<th class="px-4 py-3 text-left text-sm font-medium text-gray-500 w-28">작성자</th>
<th class="px-4 py-3 text-center text-sm font-medium text-gray-500 w-28">작성일</th>
<th class="px-4 py-3 text-center text-sm font-medium text-gray-500 w-20">조회수</th>
<th class="px-4 py-3 text-center text-sm font-medium text-gray-500 w-20">좋아요</th>
</tr>
</thead>
<tbody class="divide-y divide-gray-200">
<tr
v-for="post in filteredPosts"
:key="post.freeId"
class="hover:bg-gray-50 cursor-pointer"
>
<td class="px-4 py-4 text-sm text-gray-500">{{ post.freeId }}</td>
<td class="px-4 py-4 font-medium">
  <router-link
    :to="{ name: 'FreeDetail', params: { freeId: post.freeId } }"
    class="hover:text-[#4A90E2]"
  >
    {{ post.title }}
  </router-link>
<span v-if="post.createdAt > 0" class="text-[#4A90E2] ml-2">[{{ post.createdAt }}]</span>
<span v-if="isNew(post.createdAt )" class="ml-2 text-xs font-medium text-red-500">NEW</span>
</td>
<td class="px-4 py-4 text-sm">
<div class="flex items-center">
<!-- <img :src="post.authorAvatar" :alt="post.author" class="w-6 h-6 rounded-full mr-2" /> -->
<span>{{ post.nickName }}</span>
</div>
</td>
<td class="px-4 py-4 text-sm text-gray-500 text-center">{{ formatDate(post.createdAt) }}</td>
<td class="px-4 py-4 text-sm text-gray-500 text-center">{{ post.viewCount }}</td>
<td class="px-4 py-4 text-sm text-gray-500 text-center">{{ post.likeCount }}</td>
</tr>
</tbody>
</table>
</div>
<!-- 카드 형태 (모바일) -->
<div class="md:hidden space-y-4">
<div
v-for="post in filteredPosts"
:key="post.freeId"
class="bg-white rounded-lg border border-gray-200 p-4 hover:shadow-md transition-all cursor-pointer"
>
<div class="flex justify-between items-start mb-2">
<span class="text-xs font-medium px-2 py-1 rounded-full" :class="getCategoryClass(post.category)">
{{ getCategoryName(post.category) }}
</span>
<span class="text-xs text-gray-500">{{ formatDate(post.date) }}</span>
<td class="px-4 py-4 font-medium">
  <router-link
    :to="{ name: 'FreeDetail', params: { freeId: post.freeId } }"
    class="hover:text-[#4A90E2]"
  >
    {{ post.title }}
  </router-link>
  </td>
<span v-if="post.commentCount > 0" class="text-[#4A90E2] ml-1">[{{ post.commentCount }}]</span>
<span v-if="isNew(post.createdAt )" class="ml-1 text-xs font-medium text-red-500">NEW</span>
<p class="text-gray-600 text-sm mb-3 line-clamp-2">{{ post.content }}</p>
<div class="flex justify-between items-center text-sm text-gray-500">
<div class="flex items-center">
<img :src="post.authorAvatar" :alt="post.nickName" class="w-6 h-6 rounded-full mr-2" />
<span>{{ post.nickName }}</span>
</div>
<div class="flex space-x-3">
<span class="flex items-center"><i class="far fa-eye mr-1"></i> {{ post.viewCount }}</span>
<span class="flex items-center"><i class="far fa-heart mr-1"></i> {{ post.likeCount }}</span>
</div>
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

</div>
</template>
<script lang="ts" setup>
import axios from 'axios';
import logo from '@/assets/logo.png';
import { ref, computed, onMounted } from 'vue';
import { Swiper, SwiperSlide } from 'swiper/vue';
import { Pagination, Autoplay } from 'swiper/modules';
const swiperModules = [Pagination, Autoplay];
const posts = ref<any[]>([]);

// 이미지 로딩 실패 시 대체
function onImageError(e: Event) {
  (e.target as HTMLImageElement).src = logo;
}


onMounted(async () => {
  try {
    const token = localStorage.getItem('accessToken');
    const { data } = await axios.get('http://localhost:8080/api/free', {
      headers: { Authorization: `Bearer ${token}` }
    });
    posts.value = data;
  } catch (err) {
    console.error('게시글을 불러오는 데 실패했습니다:', err);
  }
});
const mobileMenuOpen = ref(false);
const searchQuery = ref('');
const selectedCategory = ref('all');
const currentPage = ref(1);
const postsPerPage = 10;
const toggleMobileMenu = () => {
mobileMenuOpen.value = !mobileMenuOpen.value;
};
const categories = [
{ id: 'all', name: '전체' },
{ id: 'review', name: '여행 후기' },
{ id: 'tips', name: '여행 팁' },
{ id: 'companion', name: '동행 구하기' },
{ id: 'question', name: '질문/답변' },
{ id: 'info', name: '여행 정보' }
];

// 좋아요 많은 순으로 정렬한 인기 게시글(computed)
const popularPosts = computed(() => {
  return [...posts.value]                   // 원본 건드리지 않도록 복사
    .sort((a, b) => b.likeCount - a.likeCount) // likeCount 내림차순
    .slice(0, 10);                          // 상위 10개만
});

const filteredPosts = computed(() => {
let result = [...posts.value];
// 카테고리 필터링
if (selectedCategory.value !== 'all') {
result = result.filter(post => post.category === selectedCategory.value);
}
// 검색어 필터링
if (searchQuery.value.trim()) {
const query = searchQuery.value.toLowerCase();
result = result.filter(post =>
post.title.toLowerCase().includes(query) ||
post.content.toLowerCase().includes(query) ||
post.nickName.toLowerCase().includes(query)
);
}
// 페이지네이션
const startIndex = (currentPage.value - 1) * postsPerPage;
const endIndex = startIndex + postsPerPage;
return result.slice(startIndex, endIndex);
});
const totalPages = computed(() => {
let filteredCount = posts.value.length;
if (selectedCategory.value !== 'all') {
filteredCount = posts.value.filter(post => post.category === selectedCategory.value).length;
}
if (searchQuery.value.trim()) {
const query = searchQuery.value.toLowerCase();
filteredCount = posts.value.filter(post =>
post.title.toLowerCase().includes(query) ||
post.content.toLowerCase().includes(query) ||
post.nickName.toLowerCase().includes(query)
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
let startPage = Math.max(1, currentPage.value - Math.floor(maxVisiblePages / 2));
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
window.scrollTo({ top: 0, behavior: 'smooth' });
}
};
const formatDate = (dateString: string) => {
const date = new Date(dateString);
const now = new Date();
const diffTime = Math.abs(now.getTime() - date.getTime());
const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24));
if (diffDays === 0) {
return '오늘';
} else if (diffDays === 1) {
return '어제';
} else if (diffDays < 7) {
return `${diffDays}일 전`;
} else {
return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
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
const category = categories.find(c => c.id === categoryId);
return category ? category.name : '';
};
const getCategoryClass = (categoryId: string) => {
switch (categoryId) {
case 'review':
return 'bg-blue-100 text-blue-800';
case 'tips':
return 'bg-green-100 text-green-800';
case 'companion':
return 'bg-purple-100 text-purple-800';
case 'question':
return 'bg-orange-100 text-orange-800';
case 'info':
return 'bg-teal-100 text-teal-800';
default:
return 'bg-gray-100 text-gray-800';
}
};
</script>
<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@300;400;500;700&display=swap');
body {
font-family: 'Noto Sans KR', sans-serif;
min-height: 1024px;
}
.nav-link {
position: relative;
padding: 0.5rem 0;
transition: all 0.3s ease;
}
.nav-link:hover {
color: #9EC6F3;
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
