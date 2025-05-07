<!-- The exported code uses Tailwind CSS. Install Tailwind CSS in your dev environment to ensure all styles work. -->
<template>
  <div class="min-h-screen bg-gray-50 flex justify-center py-12 px-4">
    <div class="w-full max-w-4xl bg-white rounded-lg shadow-lg overflow-hidden">
      <!-- 게시글 헤더 -->
      <div class="bg-[#9EC6F3] py-6 px-8">
        <h1 class="text-2xl font-bold text-white">
          문의사항 제목이 여기에 표시됩니다
        </h1>
      </div>
      <!-- 작성자 정보 -->
      <div
        class="bg-[#BDDDE4] bg-opacity-40 py-3 px-8 flex justify-between items-center"
      >
        <div class="flex items-center">
          <i class="fas fa-user-circle text-gray-600 mr-2"></i>
          <span class="text-gray-700">홍길동</span>
        </div>
        <div class="flex items-center">
          <i class="far fa-calendar-alt text-gray-600 mr-2"></i>
          <span class="text-gray-700">2025년 5월 2일</span>
        </div>
      </div>
      <!-- 게시글 내용 -->
      <div class="bg-[#FFF1D5] bg-opacity-30 p-8">
        <!-- 이미지 영역 -->
        <div v-if="hasImage" class="mb-8 rounded-lg overflow-hidden">
          <img
            :src="postImage"
            alt="문의 이미지"
            class="w-full h-auto object-cover"
          />
        </div>
        <!-- 텍스트 내용 -->
        <div class="text-gray-800 leading-relaxed">
          <p class="mb-4">
            안녕하세요, 문의사항이 있어 글을 남깁니다. 최근에 구매한 제품에 대해
            몇 가지 질문이 있습니다.
          </p>
          <p class="mb-4">
            첫째, 제품 사용 설명서에 나와있는 A 기능이 작동하지 않는 것
            같습니다. 정확한 사용 방법을 알려주실 수 있을까요?
          </p>
          <p class="mb-4">
            둘째, 보증 기간은 얼마나 되는지 확인하고 싶습니다. 구매 영수증은
            보관하고 있습니다.
          </p>
          <p>빠른 답변 부탁드립니다. 감사합니다.</p>
        </div>
      </div>
      <!-- 관리자 답변 영역 -->
      <div v-if="hasAdminReply" class="border-t-2 border-gray-200">
        <div class="bg-[#9EC6F3] py-4 px-8">
          <div class="flex items-center mb-3">
            <div
              class="bg-white px-3 py-1 rounded-full text-[#9EC6F3] font-bold text-sm mr-3"
            >
              관리자 답변
            </div>
            <span class="text-sm text-gray-700">2025년 5월 3일</span>
          </div>
          <div class="bg-white p-6 rounded-lg text-gray-800 leading-relaxed">
            <p class="mb-4">
              안녕하세요, 홍길동 고객님. 문의 주셔서 감사합니다.
            </p>
            <p class="mb-4">
              A 기능 관련하여 설명드리자면, 전원 버튼을 3초간 길게 누른 후
              사용하셔야 합니다. 설명서에 이 부분이 명확하지 않아 불편을 드려
              죄송합니다.
            </p>
            <p class="mb-4">
              보증 기간은 구매일로부터 1년입니다. 추가 문의사항이 있으시면
              언제든지 문의해 주세요.
            </p>
            <p>감사합니다.</p>
          </div>
        </div>
      </div>
      <!-- 버튼 영역 -->
      <div class="p-8 flex justify-end space-x-4">
        <button
          @click="handleEdit"
          class="bg-[#9EC6F3] text-white py-3 px-6 rounded-lg shadow-md hover:bg-opacity-90 transition duration-300 ease-in-out cursor-pointer whitespace-nowrap !rounded-button"
        >
          <i class="fas fa-edit mr-2"></i>수정하기
        </button>
        <button
          @click="handleDelete"
          class="bg-[#BDDDE4] bg-opacity-70 text-gray-700 py-3 px-6 rounded-lg shadow-md hover:bg-opacity-90 transition duration-300 ease-in-out cursor-pointer whitespace-nowrap !rounded-button"
        >
          <i class="fas fa-trash-alt mr-2"></i>삭제하기
        </button>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref } from "vue";
// 게시글 이미지 상태
const hasImage = ref(true);
const postImage = ref(
  "https://readdy.ai/api/search-image?query=A%20modern%20customer%20service%20inquiry%20image%20showing%20a%20desk%20with%20a%20laptop%2C%20notebook%2C%20and%20coffee%20cup%20from%20above%2C%20clean%20and%20professional%20setup%2C%20soft%20natural%20lighting%2C%20minimalist%20composition%20with%20subtle%20details&width=800&height=500&seq=12346&orientation=landscape",
);
// 관리자 답변 상태
const hasAdminReply = ref(true);
// 이벤트 핸들러
const handleEdit = () => {
  alert("게시글 수정 페이지로 이동합니다.");
  // 실제 구현에서는 라우터를 사용하여 수정 페이지로 이동
  // router.push('/edit/게시글ID');
};
const handleDelete = () => {
  if (confirm("정말로 이 게시글을 삭제하시겠습니까?")) {
    alert("게시글이 삭제되었습니다.");
    // 실제 구현에서는 API 호출 후 목록 페이지로 이동
    // await deletePost(게시글ID);
    // router.push('/list');
  }
};
</script>

<style scoped>
/* 추가 스타일링이 필요한 경우 여기에 작성 */
</style>
