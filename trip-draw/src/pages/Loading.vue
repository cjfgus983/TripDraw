<!-- The exported code uses Tailwind CSS. Install Tailwind CSS in your dev environment to ensure all styles work. -->
<template>
    <div
      class="loading-container min-h-screen flex flex-col items-center justify-center relative overflow-hidden"
    >
      <!-- Background gradient -->
      <div
        class="absolute inset-0 bg-gradient-to-br from-[#FFF1D5] via-[#BDDDE4]/20 to-[#9FB3DF]/30 -z-10"
      ></div>
  
      <!-- Floating elements -->
      <div
        v-for="(bubble, index) in floatingBubbles"
        :key="index"
        class="absolute rounded-full opacity-70"
        :class="bubble.class"
        :style="{
             width: `${bubble.size}px`,
             height: `${bubble.size}px`,
             left: `${bubble.x}%`,
             top: `${bubble.y}%`,
             animationDelay: `${bubble.delay}s`,
             backgroundColor: bubble.color
           }"
      ></div>
  
      <!-- Cancel button -->
      <button
        class="absolute top-6 right-6 px-4 py-2 bg-white/80 text-[#9FB3DF] rounded-button hover:bg-white transition-all duration-300 shadow-md cursor-pointer whitespace-nowrap font-medium"
      >
        <i class="fas fa-times mr-2"></i>취소하기
      </button>
  
      <!-- Main content -->
      <div class="flex flex-col items-center justify-center z-10 px-4">
        <!-- Status message -->
        <h1
          class="text-2xl md:text-3xl font-semibold text-[#4A5568] mb-12 text-center"
        >
          AI가 당신의 작품을 개선하고 있습니다
        </h1>
  
        <!-- Main loading animation -->
        <div class="loading-animation mb-16 relative">
          <div class="spinner-outer"></div>
          <div class="spinner-middle"></div>
          <div class="spinner-inner"></div>
  
          <!-- Center icon -->
          <div
            class="absolute inset-0 flex items-center justify-center text-white"
          >
            <i class="fas fa-paint-brush text-3xl"></i>
          </div>
        </div>
  
        <!-- Status indicators -->
        <div class="status-container w-full max-w-md mb-8">
          <div class="flex justify-between mb-2">
            <span class="text-[#4A5568] font-medium">{{ currentStage }}</span>
            <span class="text-[#9FB3DF] font-medium">{{ progressPercent }}%</span>
          </div>
  
          <!-- Progress bar -->
          <div
            class="h-3 w-full bg-white/50 rounded-full overflow-hidden shadow-inner"
          >
            <div
              class="progress-bar h-full rounded-full"
              :style="{ width: `${progressPercent}%` }"
            ></div>
          </div>
        </div>
  
        <!-- Status message -->
        <p class="text-[#4A5568] text-center max-w-md">{{ statusMessage }}</p>
  
        <!-- Estimated time -->
        <p class="text-[#9FB3DF] mt-4 font-medium">
          예상 소요 시간: {{ remainingTime }}초
        </p>
      </div>
    </div>
  </template>
  
  <script lang="ts" setup>
  import { ref, onMounted, onUnmounted, computed } from "vue";
  
  // Progress state
  const progress = ref(0);
  const maxProgress = 100;
  const progressSpeed = ref(0.5); // Progress points per second
  const remainingTime = computed(() =>
    Math.ceil((maxProgress - progress.value) / progressSpeed.value),
  );
  const progressPercent = computed(() =>
    Math.min(Math.round(progress.value), 100),
  );
  
  // Status messages based on progress
  const currentStage = computed(() => {
    if (progress.value < 25) return "이미지 분석 중";
    if (progress.value < 50) return "스타일 적용 중";
    if (progress.value < 75) return "세부 요소 개선 중";
    return "최종 이미지 생성 중";
  });
  
  const statusMessage = computed(() => {
    if (progress.value < 25) {
      return "당신의 작품을 분석하고 있습니다. 잠시만 기다려주세요.";
    } else if (progress.value < 50) {
      return "작품에 최적화된 스타일을 적용하고 있습니다.";
    } else if (progress.value < 75) {
      return "세부 요소를 개선하고 색상을 조정하고 있습니다.";
    } else {
      return "최종 이미지를 생성하고 있습니다. 곧 완료됩니다!";
    }
  });
  
  // Floating bubbles
  const floatingBubbles = ref([
    {
      x: 15,
      y: 20,
      size: 40,
      color: "#9FB3DF",
      delay: 0,
      class: "float-animation-1",
    },
    {
      x: 80,
      y: 15,
      size: 25,
      color: "#9EC6F3",
      delay: 1.5,
      class: "float-animation-2",
    },
    {
      x: 25,
      y: 80,
      size: 35,
      color: "#BDDDE4",
      delay: 1,
      class: "float-animation-3",
    },
    {
      x: 70,
      y: 75,
      size: 30,
      color: "#FFF1D5",
      delay: 0.5,
      class: "float-animation-1",
    },
    {
      x: 40,
      y: 30,
      size: 20,
      color: "#9EC6F3",
      delay: 2,
      class: "float-animation-2",
    },
    {
      x: 60,
      y: 40,
      size: 15,
      color: "#9FB3DF",
      delay: 1.2,
      class: "float-animation-3",
    },
    {
      x: 85,
      y: 60,
      size: 18,
      color: "#BDDDE4",
      delay: 0.8,
      class: "float-animation-1",
    },
    {
      x: 10,
      y: 50,
      size: 22,
      color: "#FFF1D5",
      delay: 1.7,
      class: "float-animation-2",
    },
    {
      x: 50,
      y: 85,
      size: 28,
      color: "#9FB3DF",
      delay: 0.3,
      class: "float-animation-3",
    },
  ]);
  
  // Progress timer
  let progressInterval: number | null = null;
  
  onMounted(() => {
    progressInterval = window.setInterval(() => {
      if (progress.value < maxProgress) {
        progress.value += progressSpeed.value / 10;
  
        // Randomly vary the speed a little to make it feel more natural
        progressSpeed.value = 0.3 + Math.random() * 0.4;
      } else {
        if (progressInterval) {
          clearInterval(progressInterval);
        }
      }
    }, 100);
  });
  
  onUnmounted(() => {
    if (progressInterval) {
      clearInterval(progressInterval);
    }
  });
  </script>
  
  <style scoped>
  .loading-container {
    font-family: "Noto Sans KR", sans-serif;
  }
  
  .loading-animation {
    width: 180px;
    height: 180px;
    position: relative;
  }
  
  .spinner-outer,
  .spinner-middle,
  .spinner-inner {
    position: absolute;
    border-radius: 50%;
  }
  
  .spinner-outer {
    width: 100%;
    height: 100%;
    border: 8px solid transparent;
    border-top-color: #9fb3df;
    border-right-color: #9ec6f3;
    border-bottom-color: #bddde4;
    border-left-color: #fff1d5;
    animation: spin 3s linear infinite;
  }
  
  .spinner-middle {
    width: 75%;
    height: 75%;
    top: 12.5%;
    left: 12.5%;
    border: 6px solid transparent;
    border-top-color: #bddde4;
    border-right-color: #fff1d5;
    border-bottom-color: #9fb3df;
    border-left-color: #9ec6f3;
    animation: spin 2.5s linear infinite reverse;
  }
  
  .spinner-inner {
    width: 50%;
    height: 50%;
    top: 25%;
    left: 25%;
    border: 4px solid transparent;
    border-top-color: #fff1d5;
    border-right-color: #9fb3df;
    border-bottom-color: #9ec6f3;
    border-left-color: #bddde4;
    animation: spin 2s linear infinite;
    background: linear-gradient(135deg, #9fb3df, #9ec6f3);
    display: flex;
    align-items: center;
    justify-content: center;
  }
  
  .progress-bar {
    background: linear-gradient(to right, #9fb3df, #9ec6f3, #bddde4, #fff1d5);
    transition: width 0.3s ease;
  }
  
  @keyframes spin {
    0% {
      transform: rotate(0deg);
    }
    100% {
      transform: rotate(360deg);
    }
  }
  
  .float-animation-1 {
    animation: float1 15s infinite ease-in-out;
  }
  
  .float-animation-2 {
    animation: float2 18s infinite ease-in-out;
  }
  
  .float-animation-3 {
    animation: float3 20s infinite ease-in-out;
  }
  
  @keyframes float1 {
    0%,
    100% {
      transform: translate(0, 0);
    }
    25% {
      transform: translate(15px, -15px);
    }
    50% {
      transform: translate(0, -30px);
    }
    75% {
      transform: translate(-15px, -15px);
    }
  }
  
  @keyframes float2 {
    0%,
    100% {
      transform: translate(0, 0);
    }
    25% {
      transform: translate(-20px, 10px);
    }
    50% {
      transform: translate(0, 20px);
    }
    75% {
      transform: translate(20px, 10px);
    }
  }
  
  @keyframes float3 {
    0%,
    100% {
      transform: translate(0, 0);
    }
    33% {
      transform: translate(15px, 15px);
    }
    66% {
      transform: translate(-15px, 15px);
    }
  }
  </style>
  