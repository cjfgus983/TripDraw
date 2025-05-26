<template>
    <div class="min-h-screen bg-gradient-to-b from-[#9FB3DF] to-[#BDDDE4] bg-opacity-10">
      <main class="container mx-auto px-6 py-10">
        <div class="max-w-4xl mx-auto bg-white rounded-lg shadow-lg p-8">
          <h2 class="text-3xl font-bold text-soft-purple mb-8 text-center">
            그림 등록
          </h2>
  
          <!-- 그림 선택 영역 -->
          <div class="mb-8">
            <div class="relative">
              <img
                v-if="selectedImageUrl"
                :src="selectedImageUrl"
                class="max-h-80 mx-auto rounded-lg"
              />
              <div
                v-else
                class="h-80 bg-gray-100 rounded-lg flex items-center justify-center"
              >
                <p class="text-gray-500">등록할 그림을 선택해주세요</p>
              </div>
            </div>
            <button
              @click="openImageSelector"
              class="mt-4 w-full px-6 py-3 bg-soft-blue text-white rounded-lg hover:bg-soft-blue-dark transition shadow-md cursor-pointer !rounded-button whitespace-nowrap"
            >
              <i class="fas fa-image mr-2"></i>
              내 그림 선택하기
            </button>
          </div>
  
          <!-- 그림 선택 모달 -->
          <div
            v-if="showSelector"
            class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
          >
            <div class="bg-white rounded-lg shadow-lg p-6 max-w-3xl w-full">
              <h3 class="text-xl font-semibold mb-4">등록할 그림을 선택하세요</h3>
              <div class="grid grid-cols-3 gap-4 overflow-auto max-h-96">
                <!-- 오직 원본 그림만 -->
                <div
                  v-for="d in userDrawings"
                  :key="d.drawingId"
                  class="cursor-pointer border rounded hover:shadow-md"
                  @click="selectImage(d)"
                >
                  <img
                    :src="d.originalUrl"
                    class="w-full h-32 object-cover rounded-t-lg"
                  />
                  <div class="p-2 text-sm text-center">
                    내가 그린 그림 — {{ new Date(d.createdAt).toLocaleString() }}
                  </div>
                </div>
              </div>
              <button
                @click="showSelector = false"
                class="mt-4 px-4 py-2 bg-gray-300 rounded"
              >
                취소
              </button>
            </div>
          </div>
  
          <!-- 입력 폼 -->
          <form @submit.prevent="submitForm" class="space-y-6">
            <div>
              <label for="title" class="block text-gray-700 font-medium mb-2"
                >작품 제목</label
              >
              <input
                type="text"
                id="title"
                v-model="formData.title"
                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-soft-blue"
                placeholder="작품의 제목을 입력하세요"
                required
              />
            </div>
  
            <div>
              <label
                for="description"
                class="block text-gray-700 font-medium mb-2"
                >작품 설명</label
              >
              <textarea
                id="description"
                v-model="formData.description"
                rows="4"
                class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-soft-blue"
                placeholder="작품에 대한 설명을 입력하세요"
              ></textarea>
            </div>
  
            <!-- 버튼 영역 -->
            <div class="flex justify-end space-x-4 mt-8">
              <button
                type="button"
                @click="cancel"
                class="px-6 py-3 border border-gray-300 rounded-lg text-gray-700 hover:bg-gray-100 transition cursor-pointer !rounded-button whitespace-nowrap"
              >
                취소
              </button>
              <button
                type="submit"
                class="px-8 py-3 bg-soft-blue text-white rounded-lg hover:bg-soft-blue-dark transition shadow-md cursor-pointer !rounded-button whitespace-nowrap"
                :disabled="isSubmitting"
              >
                <i class="fas fa-check mr-2"></i>
                {{ isSubmitting ? '등록 중...' : '그림 등록' }}
              </button>
            </div>
          </form>
        </div>
      </main>
    </div>
  </template>
  
  <script lang="ts" setup>
  import { ref, reactive, onMounted } from "vue";
  import { useRouter } from "vue-router";
  import axios from "axios";
  
  interface MyDrawing {
    drawingId: number;
    originalUrl: string;
    createdAt: string;
  }
  
  const router = useRouter();
  const userDrawings = ref<MyDrawing[]>([]);
  const showSelector = ref(false);
  const selectedDrawingId = ref<number | null>(null);
  const selectedImageUrl = ref<string | null>(null);
  
  const isSubmitting = ref(false);
  const formData = reactive({
    title: "",
    description: "",
    tags: [] as string[],
  });
  
  const openImageSelector = () => {
    showSelector.value = true;
  };
  
  const submitForm = async () => {
    if (!selectedImageUrl.value || !selectedDrawingId.value) {
      alert("!! 먼저 등록할 그림을 선택해주세요.");
      return;
    }
  
    isSubmitting.value = true;
    try {
      await axios.post("/api/drawingboard", {
        drawingId: selectedDrawingId.value,
        title: formData.title,
        content: formData.description,
      });
      alert("✅ 게시판에 그림을 등록했습니다!");
      router.push("/drawingboard");
      resetForm();
    } catch (error) {
      alert("등록 중 오류가 발생했습니다. 다시 시도해주세요.");
    } finally {
      isSubmitting.value = false;
    }
  };
  
  const cancel = () => {
    if (confirm("작성 중인 내용이 모두 사라집니다. 정말 취소하시겠습니까?")) {
      resetForm();
    }
  };
  
  const resetForm = () => {
    formData.title = "";
    formData.description = "";
    formData.tags = [];
    selectedDrawingId.value = null;
    selectedImageUrl.value = null;
  };
  
  onMounted(async () => {
    try {
      const { data } = await axios.get<MyDrawing[]>("/api/mypage/drawings");
      userDrawings.value = data;
    } catch (e) {
      console.error("내 그림 불러오기 실패", e);
    }
  });
  
  function selectImage(d: MyDrawing) {
    selectedDrawingId.value = d.drawingId;
    selectedImageUrl.value = d.originalUrl;
    showSelector.value = false;
  }
  </script>
  
  <style scoped>
  .bg-soft-blue {
    background-color: #9ec6f3;
  }
  .bg-soft-blue-dark {
    background-color: #8ab6e3;
  }
  .text-soft-purple {
    color: #9fb3df;
  }
  .focus\:ring-soft-blue:focus {
    --tw-ring-color: #9ec6f3;
  }
  </style>
  