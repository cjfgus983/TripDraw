The exported code uses Tailwind CSS. Install Tailwind CSS in your dev environment to ensure all styles work.
<template>
    <div class="min-h-screen">
      <!-- 상단 로고 네비게이션 바 -->
      <div
        class="top-nav flex items-center justify-center h-16 bg-white shadow-sm"
      >
        <div class="logo-container">
          <h1 class="text-2xl font-bold text-blue-700">LOGO</h1>
        </div>
      </div>
      <!-- 하단 메뉴 네비게이션 바 -->
      <div
        class="bottom-nav h-12 bg-gradient-to-r from-blue-500 to-blue-700 shadow-md"
      >
        <div class="container mx-auto px-4 h-full">
          <div class="flex justify-between items-center h-full">
            <!-- 왼쪽 메뉴 항목 -->
            <div class="flex space-x-6">
              <a href="#" class="nav-item">게시판</a>
              <a href="#" class="nav-item">계획짜기</a>
              <a href="#" class="nav-item">공지</a>
              <a href="#" class="nav-item">문의</a>
            </div>
            <!-- 오른쪽 메뉴 항목 -->
            <div class="flex space-x-4">
              <a href="#" class="nav-item">로그인</a>
              <a href="#" class="nav-item">회원가입</a>
            </div>
          </div>
        </div>
      </div>
      <!-- 모바일 메뉴 버튼 (기본적으로 숨김) -->
      <div class="mobile-menu-button hidden fixed top-4 right-4 z-50 md:hidden">
        <button
          @click="toggleMobileMenu"
          class="text-blue-700 focus:outline-none !rounded-button whitespace-nowrap cursor-pointer"
        >
          <i class="fas fa-bars text-2xl"></i>
        </button>
      </div>
      <!-- 모바일 메뉴 (기본적으로 숨김) -->
      <div
        :class="['mobile-menu fixed inset-0 bg-white z-40 transform transition-transform duration-300 ease-in-out', isMobileMenuOpen ? 'translate-x-0' : 'translate-x-full']"
      >
        <div class="flex justify-end p-4">
          <button
            @click="toggleMobileMenu"
            class="text-blue-700 focus:outline-none !rounded-button whitespace-nowrap cursor-pointer"
          >
            <i class="fas fa-times text-2xl"></i>
          </button>
        </div>
        <div class="flex flex-col items-center space-y-4 mt-8">
          <a href="#" class="mobile-nav-item">게시판</a>
          <a href="#" class="mobile-nav-item">계획짜기</a>
          <a href="#" class="mobile-nav-item">공지</a>
          <a href="#" class="mobile-nav-item">문의</a>
          <div class="border-t border-gray-200 w-3/4 my-4"></div>
          <a href="#" class="mobile-nav-item">로그인</a>
          <a href="#" class="mobile-nav-item">회원가입</a>
        </div>
      </div>
      <!-- 페이지 콘텐츠 영역 -->
      <div class="container mx-auto px-4 py-8">
        <div
          class="bg-white rounded-lg shadow-md p-6 min-h-[800px] flex flex-col items-center justify-center"
        >
          <div class="text-center mb-8">
            <h2 class="text-2xl font-bold text-gray-800">
              이미지를 변환중입니다
            </h2>
          </div>
          <div
            class="w-full max-w-md bg-gray-200 rounded-full h-4 overflow-hidden"
          >
            <div
              ref="progressBar"
              class="h-full w-0 bg-gradient-to-r from-blue-500 to-blue-700 transition-all duration-[10000ms] ease-linear"
            ></div>
          </div>
        </div>
      </div>
      <!-- 푸터 영역 -->
      <footer class="bg-gray-800 text-white py-8 mt-12">
        <div class="container mx-auto px-4">
          <div class="flex flex-col md:flex-row justify-between">
            <div class="mb-6 md:mb-0">
              <h3 class="text-xl font-bold mb-4">LOGO</h3>
              <p class="text-gray-400">© 2025 모든 권리 보유</p>
            </div>
            <div class="grid grid-cols-2 gap-8">
              <div>
                <h4 class="text-lg font-semibold mb-3">바로가기</h4>
                <ul class="space-y-2">
                  <li>
                    <a href="#" class="text-gray-400 hover:text-white transition"
                      >게시판</a
                    >
                  </li>
                  <li>
                    <a href="#" class="text-gray-400 hover:text-white transition"
                      >계획짜기</a
                    >
                  </li>
                  <li>
                    <a href="#" class="text-gray-400 hover:text-white transition"
                      >공지</a
                    >
                  </li>
                  <li>
                    <a href="#" class="text-gray-400 hover:text-white transition"
                      >문의</a
                    >
                  </li>
                </ul>
              </div>
              <div>
                <h4 class="text-lg font-semibold mb-3">연락처</h4>
                <ul class="space-y-2">
                  <li class="text-gray-400">
                    <i class="fas fa-envelope mr-2"></i> info@example.com
                  </li>
                  <li class="text-gray-400">
                    <i class="fas fa-phone mr-2"></i> 02-123-4567
                  </li>
                  <li class="text-gray-400">
                    <i class="fas fa-map-marker-alt mr-2"></i> 서울특별시 강남구
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>
      </footer>
    </div>
  </template>
  
  <script lang="ts" setup>
  import { ref, onMounted } from "vue";
  
  const isMobileMenuOpen = ref(false);
  const progressBar = ref<HTMLElement | null>(null);
  
  const toggleMobileMenu = () => {
    isMobileMenuOpen.value = !isMobileMenuOpen.value;
  };
  
  onMounted(() => {
    if (progressBar.value) {
      progressBar.value.style.width = "0%";
      setTimeout(() => {
        if (progressBar.value) {
          progressBar.value.style.width = "100%";
        }
      }, 100);
    }
  });
  </script>
  
  <style scoped>
  @import url("https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@300;400;500;700&display=swap");
  body {
    font-family: "Noto Sans KR", sans-serif;
    min-height: 1024px;
  }
  .nav-item {
    color: white;
    font-weight: 500;
    padding: 0.5rem 1rem;
    border-radius: 4px;
    transition: background-color 0.2s;
    cursor: pointer;
    white-space: nowrap;
  }
  .nav-item:hover {
    background-color: rgba(255, 255, 255, 0.15);
  }
  .mobile-nav-item {
    color: #3b82f6;
    font-weight: 500;
    font-size: 1.25rem;
    padding: 0.75rem 1.5rem;
    border-radius: 4px;
    transition: background-color 0.2s;
    cursor: pointer;
  }
  .mobile-nav-item:hover {
    background-color: #f0f9ff;
  }
  @media (max-width: 768px) {
    .bottom-nav {
      display: none;
    }
    .mobile-menu-button {
      display: block;
    }
    .top-nav {
      justify-content: center;
      position: relative;
    }
  }
  </style>
  