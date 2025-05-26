<template>
  <div class="min-h-screen bg-gray-50 flex justify-center py-12">
    <div class="w-full max-w-4xl bg-white shadow-lg rounded-lg overflow-hidden">
      <!-- 헤더 -->
      <div class="bg-[#9FB3DF] py-6 px-8">
        <h1 class="text-2xl font-bold text-white text-center">소통 게시판 글 작성</h1>
      </div>

      <!-- 폼 -->
      <div class="p-8">
        <!-- 제목 -->
        <div class="mb-6">
          <label for="title" class="block text-gray-700 font-medium mb-2">제목</label>
          <input
            id="title"
            v-model="title"
            type="text"
            placeholder="제목을 입력해주세요"
            class="w-full px-4 py-3 border border-[#BDDDE4] rounded-md focus:outline-none focus:ring-2 focus:ring-[#9EC6F3]"
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
              ref="fileInput"
              type="file"
              multiple
              accept="image/*"
              class="hidden"
              @change="handleFileUpload"
            />
            <i class="fas fa-camera text-4xl text-[#9EC6F3] mb-3"></i>
            <p class="text-gray-500">클릭하여 사진 추가</p>
          </div>

          <!-- 미리보기 -->
          <div v-if="previewImages.length" class="mt-4 grid grid-cols-3 gap-4">
            <div v-for="(img, i) in previewImages" :key="i" class="relative">
              <img :src="img" class="w-full h-32 object-cover rounded-md" />
              <button
                @click.stop="removeImage(i)"
                class="absolute top-1 right-1 bg-red-500 text-white rounded-full w-6 h-6 flex items-center justify-center"
              >
                <i class="fas fa-times text-xs"></i>
              </button>
            </div>
          </div>
        </div>

        <!-- 내용 -->
        <div class="mb-8">
          <label for="content" class="block text-gray-700 font-medium mb-2">내용</label>
          <textarea
            id="content"
            v-model="content"
            rows="12"
            placeholder="내용을 입력해주세요"
            class="w-full px-4 py-3 border border-[#BDDDE4] rounded-md focus:outline-none focus:ring-2 focus:ring-[#9EC6F3] resize-none"
          ></textarea>
        </div>

        <!-- 전송 -->
        <button
          @click="submitPost"
          :disabled="submitting"
          class="w-full bg-[#9FB3DF] text-white py-4 rounded-md hover:bg-opacity-90 transition-all"
        >
          {{ submitting ? '등록 중…' : '등록하기' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const router = useRouter();
const title = ref('');
const content = ref('');
const files = ref<File[]>([]);
const previewImages = ref<string[]>([]);
const fileInput = ref<HTMLInputElement | null>(null);
const submitting = ref(false);

const userId = ref<number | null>(null);

onMounted(async () => {
  const token = localStorage.getItem('accessToken')
  if (!token) return
  try {
    const { data } = await axios.get(
      '/api/users/me',
      { headers: { Authorization: `Bearer ${token}` } }
    )
    userId.value = data.userId
  } catch {
    // 토큰 만료 등 에러 처리
    //localStorage.removeItem('accessToken')
    //nickname.value = null
  }
});

function triggerFileInput() {
  fileInput.value?.click();
}

function handleFileUpload(e: Event) {
  const target = e.target as HTMLInputElement;
  if (!target.files) return;
  Array.from(target.files).forEach(file => {
    if (!file.type.startsWith('image/')) return;
    files.value.push(file);
    const reader = new FileReader();
    reader.onload = () => {
      if (reader.result) previewImages.value.push(reader.result as string);
    };
    reader.readAsDataURL(file);
  });
  target.value = '';
}

function removeImage(idx: number) {
  files.value.splice(idx, 1);
  previewImages.value.splice(idx, 1);
}

async function submitPost() {
  // (생략) 유효성 검사

  const formData = new FormData();
  // FormData에 각각의 필드를 RequestParam 으로 추가
  formData.append('userId', userId.value!.toString());                // 실제 userId 로 바꿔주세요
  formData.append('title', title.value);
  formData.append('content', content.value);

  // images 파트는 @RequestPart("images") 와 매칭
  files.value.forEach(file => {
    formData.append('images', file);
  });

  try {
    submitting.value = true;
    await axios.post(
      '/api/free/createFree',
      formData,
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('accessToken')}`,
          // multipart/form-data 는 브라우저가 Content-Type: boundary=... 을 자동 설정해 줍니다.
        }
      }
    );
    alert('등록되었습니다!');
    router.push('/freeboard');
  } finally {
    submitting.value = false;
  }
}
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
  