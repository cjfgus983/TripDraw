<template>
  <div>
    <header
      class="h-[55px] bg-white shadow-sm flex items-center justify-center transition-all duration-300"
      :class="{ 'opacity-0': !isHeaderVisible }"
    >
      <div class="container mx-auto px-4 flex items-center justify-center">
        <div class="logo-container text-center">
          <!-- router-link 로 감싸서 클릭 시 "/" 로 이동 -->
          <router-link to="/">
            <img :src="logo" alt="로고" class="h-[150px] cursor-pointer" />
          </router-link>
        </div>
      </div>
    </header>
  </div>
  
</template>

<script setup lang="ts">
import logo from '@/assets/logo.png';
import { ref, onMounted } from 'vue';

const isHeaderVisible = ref(true);
let lastScrollPosition = 0;
onMounted(() => {
  window.addEventListener('scroll', handleScroll);
});
const handleScroll = () => {
  const currentScrollPosition = window.scrollY;
  isHeaderVisible.value =
    currentScrollPosition < lastScrollPosition || currentScrollPosition < 60;
  lastScrollPosition = currentScrollPosition;
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
  

  