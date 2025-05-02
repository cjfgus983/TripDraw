<!-- The exported code uses Tailwind CSS. Install Tailwind CSS in your dev environment to ensure all styles work. -->
<template>
    <div class="container mx-auto px-4 py-8 min-h-screen">
      <!-- 게시글 상세 영역 -->
      <div class="bg-white rounded-lg shadow-lg p-6 mb-8">
        <!-- 게시글 제목 -->
        <h1 class="text-3xl font-bold mb-4">
          자연 속에서 힐링하는 방법: 숲 테라피의 효과
        </h1>
  
        <!-- 작성자 정보 -->
        <div
          class="flex flex-wrap items-center text-gray-600 mb-6 pb-4 border-b border-gray-200"
        >
          <div class="flex items-center mr-6">
            <i class="fas fa-user-circle text-xl mr-2 text-[#9FB3DF]"></i>
            <span>자연사랑</span>
          </div>
          <div class="flex items-center mr-6">
            <i class="far fa-calendar-alt mr-2 text-[#9FB3DF]"></i>
            <span>2025-05-01</span>
          </div>
          <div class="flex items-center mr-6">
            <i class="far fa-eye mr-2 text-[#9FB3DF]"></i>
            <span>조회수 238</span>
          </div>
        </div>
  
        <!-- 게시글 대표 이미지 -->
        <div class="mb-8 overflow-hidden rounded-lg">
          <img
            :src="postImage"
            alt="자연 속에서 힐링하는 방법"
            class="w-full h-[400px] object-cover object-top cursor-pointer"
            @click="showFullImage = true"
          />
        </div>
  
        <!-- AI 요약 -->
        <div class="bg-[#FFF1D5] p-4 rounded-lg mb-8">
          <h3 class="font-bold mb-2 flex items-center">
            <i class="fas fa-robot mr-2"></i>
            AI 요약
          </h3>
          <p class="text-gray-700">
            이 글은 숲 테라피의 효과와 방법에 대해 설명합니다. 자연 속에서 시간을
            보내는 것이 스트레스 감소, 면역력 향상, 집중력 개선에 도움이 된다는
            연구 결과를 소개하고, 일상에서 자연과 연결되는 방법을 제안합니다. 주
            2-3회 30분 이상의 숲 산책이 정신 건강에 큰 도움이 된다고 강조합니다.
          </p>
        </div>
  
        <!-- 게시글 본문 -->
        <div class="mb-8 leading-relaxed">
          <p class="mb-4">
            현대인들은 도시의 빠른 생활 속에서 많은 스트레스를 받고 있습니다.
            이러한 스트레스를 해소하고 심신의 건강을 회복하는 방법 중 하나로 '숲
            테라피'가 주목받고 있습니다.
          </p>
          <p class="mb-4">
            숲 테라피란 숲이 가진 치유력을 활용해 심신의 건강을 증진시키는 활동을
            말합니다. 나무에서 발산되는 피톤치드, 맑은 공기, 자연의 소리와 색감이
            우리 몸과 마음에 긍정적인 영향을 미친다는 것이 여러 연구를 통해
            입증되었습니다.
          </p>
          <p class="mb-4">
            일본과 한국에서 진행된 연구에 따르면, 숲에서 보내는 시간이
            코르티솔(스트레스 호르몬) 수치를 낮추고, 혈압을 안정시키며, 면역
            세포의 활동을 활성화시키는 것으로 나타났습니다. 또한 자연 속에서의
            활동은 주의력 결핍 및 우울증 증상 완화에도 효과적입니다.
          </p>
          <p class="mb-4">
            숲 테라피를 실천하는 방법은 간단합니다. 가까운 공원이나 숲을 찾아
            천천히 걷기, 나무 관찰하기, 자연의 소리 듣기, 깊게 호흡하기 등의
            활동을 할 수 있습니다. 주 2-3회, 한 번에 30분 이상 숲에서 시간을
            보내는 것이 이상적입니다.
          </p>
          <p class="mb-4">
            도시에 살고 있어 숲을 자주 방문하기 어렵다면, 실내 식물을 키우거나
            자연 소리를 들으며 명상하는 것도 대안이 될 수 있습니다. 중요한 것은
            자연과의 연결감을 유지하는 것입니다.
          </p>
          <p>
            바쁜 일상 속에서도 자연과 함께하는 시간을 의식적으로 만들어보세요.
            작은 변화가 여러분의 건강과 행복에 큰 차이를 가져올 수 있습니다.
          </p>
        </div>
  
        <!-- 추천/비추천 버튼 -->
        <div class="flex justify-center space-x-4 mb-8">
          <button
            class="!rounded-button whitespace-nowrap flex items-center px-6 py-3 rounded-full text-white transition-all duration-300 cursor-pointer"
            :class="isLiked ? 'bg-[#BDDDE4]' : 'bg-[#9FB3DF]'"
            @click="toggleLike"
          >
            <i class="fas fa-thumbs-up mr-2"></i>
            추천 {{ likeCount }}
          </button>
          <button
            class="!rounded-button whitespace-nowrap flex items-center px-6 py-3 rounded-full text-white transition-all duration-300 cursor-pointer"
            :class="isDisliked ? 'bg-[#BDDDE4]' : 'bg-[#9EC6F3]'"
            @click="toggleDislike"
          >
            <i class="fas fa-thumbs-down mr-2"></i>
            비추천 {{ dislikeCount }}
          </button>
        </div>
      </div>
  
      <!-- 댓글 영역 -->
      <div class="bg-white rounded-lg shadow-lg p-6">
        <h2 class="text-xl font-bold mb-6 flex items-center">
          <i class="far fa-comments mr-2 text-[#9FB3DF]"></i>
          댓글 {{ comments.length }}개
        </h2>
  
        <!-- 댓글 작성 폼 -->
        <div class="mb-8">
          <textarea
            v-model="newComment"
            placeholder="댓글을 작성해주세요"
            class="w-full p-4 border border-[#9FB3DF] rounded-lg mb-3 min-h-[100px] focus:outline-none focus:ring-2 focus:ring-[#9FB3DF]"
          ></textarea>
          <div class="flex justify-end">
            <button
              @click="addComment"
              class="!rounded-button whitespace-nowrap bg-[#9FB3DF] text-white px-6 py-2 rounded-full transition-all hover:bg-opacity-90 cursor-pointer"
            >
              댓글 등록
            </button>
          </div>
        </div>
  
        <!-- 댓글 목록 -->
        <div v-if="comments.length > 0">
          <div
            v-for="(comment, index) in comments"
            :key="index"
            class="py-4"
            :class="{'border-b border-[#BDDDE4]': index !== comments.length - 1}"
          >
            <div class="flex items-center justify-between mb-2">
              <div class="flex items-center">
                <i class="fas fa-user-circle text-xl mr-2 text-[#9FB3DF]"></i>
                <span class="font-medium">{{ comment.author }}</span>
              </div>
              <span class="text-sm text-gray-500">{{ comment.date }}</span>
            </div>
            <p class="text-gray-700">{{ comment.content }}</p>
          </div>
        </div>
        <div v-else class="text-center py-8 text-gray-500">
          아직 댓글이 없습니다. 첫 댓글을 작성해보세요!
        </div>
      </div>
  
      <!-- 이미지 전체화면 모달 -->
      <div
        v-if="showFullImage"
        class="fixed inset-0 bg-black bg-opacity-90 flex items-center justify-center z-50"
        @click="showFullImage = false"
      >
        <div class="relative max-w-4xl max-h-screen p-4">
          <img
            :src="postImage"
            alt="자연 속에서 힐링하는 방법"
            class="max-w-full max-h-[90vh] object-contain"
          />
          <button
            class="absolute top-4 right-4 text-white text-2xl cursor-pointer"
            @click.stop="showFullImage = false"
          >
            <i class="fas fa-times"></i>
          </button>
        </div>
      </div>
    </div>
  </template>
  
  <script lang="ts" setup>
  import { ref } from "vue";
  
  // 게시글 이미지
  const postImage =
    "https://readdy.ai/api/search-image?query=A%20serene%20forest%20scene%20with%20sunlight%20filtering%20through%20tall%20trees%2C%20creating%20a%20peaceful%20atmosphere.%20The%20image%20shows%20a%20lush%20green%20forest%20with%20a%20small%20path%20winding%20through%20it%2C%20perfect%20for%20forest%20therapy.%20Natural%20light%20creates%20a%20calming%20ambiance%20with%20rays%20of%20sunshine%20illuminating%20the%20forest%20floor%20covered%20in%20moss%20and%20ferns&width=1200&height=800&seq=12345&orientation=landscape";
  
  // 좋아요/싫어요 상태
  const isLiked = ref(false);
  const isDisliked = ref(false);
  const likeCount = ref(42);
  const dislikeCount = ref(5);
  
  // 좋아요 토글
  const toggleLike = () => {
    if (isLiked.value) {
      likeCount.value--;
      isLiked.value = false;
    } else {
      if (isDisliked.value) {
        isDisliked.value = false;
        dislikeCount.value--;
      }
      likeCount.value++;
      isLiked.value = true;
    }
  };
  
  // 싫어요 토글
  const toggleDislike = () => {
    if (isDisliked.value) {
      dislikeCount.value--;
      isDisliked.value = false;
    } else {
      if (isLiked.value) {
        isLiked.value = false;
        likeCount.value--;
      }
      dislikeCount.value++;
      isDisliked.value = true;
    }
  };
  
  // 댓글 관련
  const newComment = ref("");
  const comments = ref([
    {
      author: "산책러버",
      date: "2025-05-02",
      content:
        "요즘 주말마다 근처 공원에 가고 있는데, 확실히 기분이 좋아지는 것 같아요. 이 글 보고 더 자주 가야겠다고 생각했습니다!",
    },
    {
      author: "도시인",
      date: "2025-05-01",
      content:
        "도시에 살다보니 자연을 접하기 어려운데, 집에 식물을 들이는 것부터 시작해볼까 합니다. 좋은 정보 감사합니다.",
    },
    {
      author: "건강지킴이",
      date: "2025-05-01",
      content:
        "숲 테라피의 효과에 대한 연구 결과가 흥미롭네요. 혹시 관련 연구 자료를 더 볼 수 있는 링크가 있을까요?",
    },
  ]);
  
  // 댓글 추가
  const addComment = () => {
    if (newComment.value.trim()) {
      const today = new Date();
      const formattedDate = `${today.getFullYear()}-${String(today.getMonth() + 1).padStart(2, "0")}-${String(today.getDate()).padStart(2, "0")}`;
  
      comments.value.unshift({
        author: "사용자",
        date: formattedDate,
        content: newComment.value,
      });
  
      newComment.value = "";
    }
  };
  
  // 이미지 전체화면 보기
  const showFullImage = ref(false);
  </script>
  
  <style scoped>
  .container {
    max-width: 1000px;
  }
  
  textarea {
    resize: vertical;
  }
  
  /* 버튼 호버 효과 */
  button:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  }
  
  /* 버튼 활성화 효과 */
  button:active {
    transform: translateY(0);
    box-shadow: none;
  }
  </style>
  