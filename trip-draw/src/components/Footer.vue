<template>
    <footer class="bg-gray-800 text-white py-12">
      <div class="container mx-auto px-4">
        <div class="grid grid-cols-1 md:grid-cols-4 gap-8">
          <!-- 회사 정보 -->
          <div>
            <h3 class="text-lg font-bold mb-4">팀 소개</h3>
            <p class="text-gray-400">김철현</p>
            <p class="text-gray-400 mb-4">최유리</p>

          </div>
          <!-- 빠른 링크, 고객 지원, 연락처 생략 (원하면 추가해 드림) -->
        </div>
        <div class="border-t border-gray-700 mt-8 pt-8 flex flex-col md:flex-row justify-between items-center">
          <p class="text-gray-400 text-sm">© HyeonRi</p>
        </div>
      </div>
    </footer>
  </template>
  
  <script setup lang="ts"></script>
  
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
  .feature-card {
    transition:
      transform 0.3s ease,
      box-shadow 0.3s ease;
  }
  .feature-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
  }
  .destination-card {
    transition: transform 0.3s ease;
  }
  .destination-card:hover {
    transform: translateY(-5px);
  }
  input[type="number"]::-webkit-inner-spin-button,
  input[type="number"]::-webkit-outer-spin-button {
    -webkit-appearance: none;
    margin: 0;
  }
  </style>
  