<!-- src/App.vue -->
<template>
  <div class="min-h-screen flex flex-col">
    <Header />

    <!-- sticky nav: 뷰포트를 기준으로 항상 상단에 붙습니다 -->
    <nav
      class="h-12 bg-gradient-to-r from-[#9FB3DF] to-[#BDDDE4]
             text-white shadow-md sticky top-0 z-50"
    >
      <div class="container mx-auto px-4 h-full flex items-center justify-between">
        <div class="flex space-x-6">
          <router-link to="/freeboard"    class="nav-link">게시판</router-link>
          <router-link to="/tripplanpage" class="nav-link">계획짜기</router-link>
          <router-link to="/noticeboard"  class="nav-link">공지</router-link>
          <router-link to="/contactboard" class="nav-link">문의</router-link>
        </div>
        <div class="flex space-x-4">
          <router-link to="/login" class="nav-link">로그인</router-link>
          <router-link to="/signup" class="nav-link">회원가입</router-link>
        </div>
      </div>
    </nav>

    <!-- main 콘텐츠: overflow-auto 제거하여 window 스크롤 사용 -->
    <main class="flex-grow">
      <router-view />
    </main>

    <Footer />
  </div>
</template>

<script setup lang="ts">
import Header from '@/components/Header.vue'
import Footer from '@/components/Footer.vue'
</script>

<style>
@import url("https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@300;400;500;700&display=swap");

body {
  font-family: "Noto Sans KR", sans-serif;
}

.nav-link {
  position: relative;
  padding: 0.5rem 0;
  transition: all 0.3s ease;
  color: inherit;
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
</style>
