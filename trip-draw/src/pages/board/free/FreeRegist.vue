<!-- The exported code uses Tailwind CSS. Install Tailwind CSS in your dev environment to ensure all styles work. -->
<template>
    <div class="min-h-screen bg-gray-50 flex justify-center py-12">
      <div class="w-full max-w-4xl bg-white shadow-lg rounded-lg overflow-hidden">
        <!-- 헤더 섹션 -->
        <div class="bg-[#9FB3DF] py-6 px-8">
          <h1 class="text-2xl font-bold text-white text-center">
            소통 게시판 글 작성
          </h1>
        </div>
  
        <!-- 입력 폼 섹션 -->
        <div class="p-8">
          <!-- 제목 입력 -->
          <div class="mb-6">
            <label for="title" class="block text-gray-700 font-medium mb-2"
              >제목</label
            >
            <input
              type="text"
              id="title"
              v-model="title"
              placeholder="제목을 입력해주세요"
              class="w-full px-4 py-3 border border-[#BDDDE4] rounded-md focus:outline-none focus:ring-2 focus:ring-[#9EC6F3] text-gray-700"
            />
          </div>
  
          <!-- 사진 업로드 -->
          <div class="mb-6">
            <label class="block text-gray-700 font-medium mb-2">사진</label>
            <div
              class="border-2 border-dashed border-[#9EC6F3] rounded-lg p-8 text-center cursor-pointer"
              @click="triggerFileInput"
            >
              <input
                type="file"
                ref="fileInput"
                @change="handleFileUpload"
                multiple
                accept="image/*"
                class="hidden"
              />
              <i class="fas fa-camera text-4xl text-[#9EC6F3] mb-3"></i>
              <p class="text-gray-500">클릭하여 사진 추가</p>
            </div>
  
            <!-- 이미지 미리보기 -->
            <div
              v-if="previewImages.length > 0"
              class="mt-4 grid grid-cols-3 gap-4"
            >
              <div
                v-for="(image, index) in previewImages"
                :key="index"
                class="relative"
              >
                <img :src="image" class="w-full h-32 object-cover rounded-md" />
                <button
                  @click.stop="removeImage(index)"
                  class="absolute top-1 right-1 bg-red-500 text-white rounded-full w-6 h-6 flex items-center justify-center !rounded-button whitespace-nowrap cursor-pointer"
                >
                  <i class="fas fa-times text-xs"></i>
                </button>
              </div>
            </div>
          </div>
  
          <!-- 내용 입력 -->
          <div class="mb-8">
            <label for="content" class="block text-gray-700 font-medium mb-2"
              >내용</label
            >
            <textarea
              id="content"
              v-model="content"
              placeholder="내용을 입력해주세요"
              rows="12"
              class="w-full px-4 py-3 border border-[#BDDDE4] rounded-md focus:outline-none focus:ring-2 focus:ring-[#9EC6F3] text-gray-700 resize-none"
            ></textarea>
          </div>
  
          <!-- 등록 버튼 -->
          <button
            @click="submitPost"
            class="w-full bg-[#9FB3DF] text-white py-4 px-6 rounded-md hover:bg-opacity-90 transition-all font-medium !rounded-button whitespace-nowrap cursor-pointer"
          >
            등록하기
          </button>
        </div>
      </div>
    </div>
  </template>
  
  <script lang="ts" setup>
  import { ref } from "vue";
  
  // 상태 관리
  const title = ref("");
  const content = ref("");
  const previewImages = ref<string[]>([]);
  const fileInput = ref<HTMLInputElement | null>(null);
  const files = ref<File[]>([]);
  
  // 파일 입력 트리거
  const triggerFileInput = () => {
    if (fileInput.value) {
      fileInput.value.click();
    }
  };
  
  // 파일 업로드 처리
  const handleFileUpload = (event: Event) => {
    const target = event.target as HTMLInputElement;
    if (!target.files) return;
  
    const newFiles = Array.from(target.files);
  
    newFiles.forEach((file) => {
      // 이미지 파일만 허용
      if (!file.type.match("image.*")) return;
  
      // 파일 저장
      files.value.push(file);
  
      // 이미지 미리보기 생성
      const reader = new FileReader();
      reader.onload = (e) => {
        if (e.target?.result) {
          previewImages.value.push(e.target.result as string);
        }
      };
      reader.readAsDataURL(file);
    });
  
    // 파일 입력 초기화
    if (target) {
      target.value = "";
    }
  };
  
  // 이미지 제거
  const removeImage = (index: number) => {
    previewImages.value.splice(index, 1);
    files.value.splice(index, 1);
  };
  
  // 게시글 등록
  const submitPost = () => {
    // 유효성 검사
    if (!title.value.trim()) {
      alert("제목을 입력해주세요.");
      return;
    }
  
    if (!content.value.trim()) {
      alert("내용을 입력해주세요.");
      return;
    }
  
    // 실제 서버에 전송하는 로직 (여기서는 예시만 제공)
    const formData = new FormData();
    formData.append("title", title.value);
    formData.append("content", content.value);
  
    files.value.forEach((file, index) => {
      formData.append(`image_${index}`, file);
    });
  
    // API 호출 코드가 여기 들어갈 수 있음
    // 예: axios.post('/api/posts', formData)
  
    // 임시 성공 메시지
    alert("게시글이 성공적으로 등록되었습니다.");
  
    // 폼 초기화
    resetForm();
  };
  
  // 폼 초기화
  const resetForm = () => {
    title.value = "";
    content.value = "";
    previewImages.value = [];
    files.value = [];
  };
  </script>
  
  <style scoped>
  /* 스타일 커스터마이징 */
  textarea:focus,
  input:focus {
    outline: none;
  }
  
  /* 파일 입력 숨기기 */
  input[type="file"] {
    display: none;
  }
  
  /* 스크롤바 스타일링 */
  textarea::-webkit-scrollbar {
    width: 8px;
  }
  
  textarea::-webkit-scrollbar-track {
    background: #f1f1f1;
    border-radius: 4px;
  }
  
  textarea::-webkit-scrollbar-thumb {
    background: #bddde4;
    border-radius: 4px;
  }
  
  textarea::-webkit-scrollbar-thumb:hover {
    background: #9ec6f3;
  }
  </style>
  