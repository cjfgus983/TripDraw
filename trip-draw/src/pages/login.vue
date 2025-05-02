<!-- The exported code uses Tailwind CSS. Install Tailwind CSS in your dev environment to ensure all styles work. -->
<template>
    <div
      class="min-h-screen flex items-center justify-center bg-gradient-to-br from-[#9FB3DF] via-[#9EC6F3] to-[#BDDDE4] py-12 px-4 sm:px-6 lg:px-8"
    >
      <div
        class="max-w-md w-full space-y-8 bg-white/90 backdrop-blur-sm p-10 rounded-lg shadow-lg border border-[#FFF1D5]/20"
      >
        <div class="text-center">
            <h1 class="text-2xl font-bold text-[#9FB3DF] mb-8 text-center">
                로그인
            </h1>
        </div>
        <form class="mt-8 space-y-6" @submit.prevent="handleLogin">
          <div class="rounded-md shadow-sm -space-y-px">
            <div class="mb-4">
              <label for="email-address" class="sr-only">이메일</label>
              <div class="relative">
                <div
                  class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none"
                >
                  <i class="fas fa-envelope text-gray-400"></i>
                </div>
                <input
                  id="email-address"
                  v-model="email"
                  name="email"
                  type="email"
                  autocomplete="email"
                  required
                  class="appearance-none rounded-md relative block w-full px-3 py-3 pl-10 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-blue-500 focus:border-blue-500 focus:z-10 sm:text-sm"
                  placeholder="이메일을 입력해주세요"
                />
              </div>
              <p v-if="emailError" class="mt-1 text-sm text-red-600">
                {{ emailError }}
              </p>
            </div>
            <div class="mb-4">
              <label for="password" class="sr-only">비밀번호</label>
              <div class="relative">
                <div
                  class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none"
                >
                  <i class="fas fa-lock text-gray-400"></i>
                </div>
                <input
                  id="password"
                  v-model="password"
                  :type="showPassword ? 'text' : 'password'"
                  name="password"
                  autocomplete="current-password"
                  required
                  class="appearance-none rounded-md relative block w-full px-3 py-3 pl-10 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-blue-500 focus:border-blue-500 focus:z-10 sm:text-sm"
                  placeholder="비밀번호를 입력해주세요"
                />
                <div class="absolute inset-y-0 right-0 pr-3 flex items-center">
                  <button
                    type="button"
                    @click="togglePasswordVisibility"
                    class="text-gray-400 hover:text-gray-500 cursor-pointer !rounded-button whitespace-nowrap"
                  >
                    <i
                      :class="showPassword ? 'fas fa-eye-slash' : 'fas fa-eye'"
                    ></i>
                  </button>
                </div>
              </div>
            </div>
          </div>
          <div>
            <button
              type="submit"
              class="group relative w-full flex justify-center py-3 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-[#9FB3DF] hover:bg-[#8CA4D0] focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-[#9FB3DF] cursor-pointer !rounded-button whitespace-nowrap transition-all duration-200"
            >
              로그인
            </button>
          </div>
        </form>
        <div class="mt-6">
          <div class="relative">
            <div class="absolute inset-0 flex items-center">
              <div class="w-full border-t border-gray-300"></div>
            </div>
            <div class="relative flex justify-center text-sm">
              <span class="px-2 bg-white text-gray-500">또는</span>
            </div>
          </div>
          <div class="mt-4">
            <button
              @click="handleKakaoLogin"
              class="w-full flex items-center justify-center px-4 py-3 border border-transparent text-sm font-medium rounded-md text-gray-700 bg-[#FEE500] hover:bg-[#FDD700] focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-[#FEE500] !rounded-button whitespace-nowrap transition-all duration-200"
            >
              <i class="fas fa-comment text-gray-700 mr-2"></i>
              카카오톡으로 계속하기
            </button>
          </div>
        </div>
        <div class="mt-6 text-center">
          <p class="text-sm text-gray-600">
            아직 회원이 아니신가요?
            <button
              @click="goToSignup"
              class="font-medium text-[#9EC6F3] hover:text-[#8CA4D0] cursor-pointer !rounded-button whitespace-nowrap transition-all duration-200"
            >
              회원가입
            </button>
          </p>
        </div>
      </div>
    </div>
  </template>
  
  <script lang="ts" setup>
  import { ref } from "vue";
  // 로고 이미지
  import logo from '@/assets/logo.png';
  // 폼 상태 관리
  const email = ref("");
  const password = ref("");
  const showPassword = ref(false);
  const emailError = ref("");
  // 비밀번호 표시/숨김 토글
  const togglePasswordVisibility = () => {
    showPassword.value = !showPassword.value;
  };
  // 이메일 유효성 검사
  const validateEmail = () => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email.value)) {
      emailError.value = "유효한 이메일 주소를 입력해주세요";
      return false;
    }
    emailError.value = "";
    return true;
  };
  // 로그인 처리
  const handleLogin = () => {
    if (validateEmail()) {
      console.log("로그인 시도:", {
        email: email.value,
        password: password.value,
      });
      // 실제 로그인 로직 구현 부분
      alert("로그인 기능이 구현되었습니다!");
    }
  };
  // 카카오 로그인 처리
  const handleKakaoLogin = () => {
    console.log("카카오톡 로그인 시도");
    // 실제 카카오 로그인 로직 구현 부분
    alert("카카오톡 로그인 기능이 구현되었습니다!");
  };
  // 회원가입 페이지로 이동
  const goToSignup = () => {
    console.log("회원가입 페이지로 이동");
    // 실제 페이지 이동 로직 구현 부분
    alert("회원가입 페이지로 이동합니다!");
  };
  </script>
  
  <style scoped>
  /* 추가 스타일 */
  .bg-gradient-to-br {
    background-size: 400% 400%;
    animation: gradient 15s ease infinite;
  }
  @keyframes gradient {
    0% {
      background-position: 0% 50%;
    }
    50% {
      background-position: 100% 50%;
    }
    100% {
      background-position: 0% 50%;
    }
  }
  input:-webkit-autofill,
  input:-webkit-autofill:hover,
  input:-webkit-autofill:focus {
    -webkit-box-shadow: 0 0 0px 1000px white inset;
    transition: background-color 5000s ease-in-out 0s;
  }
  input:focus {
    outline: none;
  }
  </style>
  