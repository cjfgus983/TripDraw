<!-- The exported code uses Tailwind CSS. Install Tailwind CSS in your dev environment to ensure all styles work. -->
<template>
    <div class="min-h-screen bg-gray-50 py-10 px-4 sm:px-6">
      <div
        class="max-w-5xl mx-auto bg-white rounded-xl shadow-md overflow-hidden"
      >
        <div class="p-8">
          <h1 class="text-2xl font-bold text-gray-800 mb-6">공지사항 등록</h1>
  
          <!-- 제목 입력 영역 -->
          <div class="mb-6">
            <label
              for="title"
              class="block text-base font-medium text-gray-700 mb-2"
              >제목</label
            >
            <input
              type="text"
              id="title"
              v-model="title"
              placeholder="제목을 입력해주세요"
              class="w-full px-4 py-3 border-2 border-[#9FB3DF] rounded-lg focus:outline-none focus:border-[#9EC6F3] text-gray-700"
            />
          </div>
  
          <!-- 내용 작성 영역 -->
          <div class="mb-8">
            <label
              for="content"
              class="block text-base font-medium text-gray-700 mb-2"
              >내용</label
            >
  
            <!-- 에디터 툴바 -->
            <div
              class="flex flex-wrap items-center gap-2 p-3 bg-gray-50 border-2 border-b-0 border-[#BDDDE4] rounded-t-lg"
            >
              <!-- 텍스트 스타일 -->
              <div class="flex border-r border-gray-300 pr-2 mr-2">
                <button
                  @click="applyFormat('bold')"
                  :class="{ 'bg-gray-200': formats.bold }"
                  class="p-2 rounded-lg hover:bg-gray-200 cursor-pointer !rounded-button whitespace-nowrap"
                >
                  <i class="fas fa-bold"></i>
                </button>
                <button
                  @click="applyFormat('italic')"
                  :class="{ 'bg-gray-200': formats.italic }"
                  class="p-2 rounded-lg hover:bg-gray-200 cursor-pointer !rounded-button whitespace-nowrap"
                >
                  <i class="fas fa-italic"></i>
                </button>
                <button
                  @click="applyFormat('underline')"
                  :class="{ 'bg-gray-200': formats.underline }"
                  class="p-2 rounded-lg hover:bg-gray-200 cursor-pointer !rounded-button whitespace-nowrap"
                >
                  <i class="fas fa-underline"></i>
                </button>
              </div>
  
              <!-- 색상 선택 -->
              <div class="flex border-r border-gray-300 pr-2 mr-2">
                <div class="relative">
                  <button
                    @click="toggleColorPicker('text')"
                    class="p-2 rounded-lg hover:bg-gray-200 cursor-pointer !rounded-button whitespace-nowrap"
                  >
                    <i class="fas fa-font" :style="{ color: textColor }"></i>
                  </button>
                  <div
                    v-if="colorPickers.text"
                    class="absolute z-10 mt-1 bg-white shadow-lg rounded-lg p-2 grid grid-cols-5 gap-1"
                  >
                    <div
                      v-for="color in colors"
                      :key="color"
                      :style="{ backgroundColor: color }"
                      @click="selectColor('text', color)"
                      class="w-6 h-6 rounded-full cursor-pointer hover:opacity-80"
                    ></div>
                  </div>
                </div>
  
                <div class="relative">
                  <button
                    @click="toggleColorPicker('background')"
                    class="p-2 rounded-lg hover:bg-gray-200 cursor-pointer !rounded-button whitespace-nowrap"
                  >
                    <i
                      class="fas fa-fill-drip"
                      :style="{ color: backgroundColor }"
                    ></i>
                  </button>
                  <div
                    v-if="colorPickers.background"
                    class="absolute z-10 mt-1 bg-white shadow-lg rounded-lg p-2 grid grid-cols-5 gap-1"
                  >
                    <div
                      v-for="color in colors"
                      :key="color"
                      :style="{ backgroundColor: color }"
                      @click="selectColor('background', color)"
                      class="w-6 h-6 rounded-full cursor-pointer hover:opacity-80"
                    ></div>
                  </div>
                </div>
              </div>
  
              <!-- 정렬 -->
              <div class="flex border-r border-gray-300 pr-2 mr-2">
                <button
                  @click="applyAlignment('left')"
                  :class="{ 'bg-gray-200': alignment === 'left' }"
                  class="p-2 rounded-lg hover:bg-gray-200 cursor-pointer !rounded-button whitespace-nowrap"
                >
                  <i class="fas fa-align-left"></i>
                </button>
                <button
                  @click="applyAlignment('center')"
                  :class="{ 'bg-gray-200': alignment === 'center' }"
                  class="p-2 rounded-lg hover:bg-gray-200 cursor-pointer !rounded-button whitespace-nowrap"
                >
                  <i class="fas fa-align-center"></i>
                </button>
                <button
                  @click="applyAlignment('right')"
                  :class="{ 'bg-gray-200': alignment === 'right' }"
                  class="p-2 rounded-lg hover:bg-gray-200 cursor-pointer !rounded-button whitespace-nowrap"
                >
                  <i class="fas fa-align-right"></i>
                </button>
              </div>
  
              <!-- 이미지 업로드 -->
              <div>
                <label
                  class="p-2 rounded-lg hover:bg-gray-200 cursor-pointer !rounded-button whitespace-nowrap"
                >
                  <i class="fas fa-image"></i>
                  <input
                    type="file"
                    accept="image/*"
                    @change="uploadImage"
                    class="hidden"
                  />
                </label>
              </div>
            </div>
  
            <!-- 에디터 본문 -->
            <div
              ref="editorRef"
              contenteditable="true"
              class="w-full min-h-[400px] p-4 border-2 border-[#BDDDE4] rounded-b-lg focus:outline-none"
              :style="`text-align: ${alignment}`"
              @input="handleInput"
              placeholder="내용을 입력해주세요"
            ></div>
          </div>
           <!-- 고정 여부 체크박스 -->
         <div class="mb-6 flex items-center">
           <input
             id="isPinned"
             type="checkbox"
             v-model="isPinned"
             class="h-4 w-4 text-[#9FB3DF] border-gray-300 rounded focus:ring-[#9FB3DF]"
           />
           <label for="isPinned" class="ml-2 text-gray-700 font-medium">
             중요 공지로 고정
           </label>
         </div>
  
          <!-- 버튼 영역 -->
          <div class="flex justify-end">
            <button
              @click="submitForm"
              class="px-8 py-3 bg-[#9EC6F3] text-white font-medium rounded-lg hover:bg-[#8ab3e0] transition-colors cursor-pointer !rounded-button whitespace-nowrap"
            >
              등록하기
            </button>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script lang="ts" setup>
  import { ref, reactive, onMounted } from "vue";
  import axios from "axios";
  import { useRouter } from "vue-router";

  const router = useRouter();

  
  // 제목
  const title = ref("");
  
  // 에디터 참조
  const editorRef = ref<HTMLElement | null>(null);
  
  // 에디터 컨텐츠
  const content = ref("");
  
  // 포맷 상태
  const formats = reactive({
    bold: false,
    italic: false,
    underline: false,
  });
  
  // 정렬 상태
  const alignment = ref("left");
  
  // 색상 선택기 상태
  const colorPickers = reactive({
    text: false,
    background: false,
  });
  
  // 현재 선택된 색상
  const textColor = ref("#000000");
  const backgroundColor = ref("#000000");
  
  // 색상 팔레트
  const colors = [
    "#9FB3DF",
    "#9EC6F3",
    "#BDDDE4",
    "#FFF1D5",
    "#000000",
    "#FF5252",
    "#4CAF50",
    "#2196F3",
    "#FFC107",
    "#9C27B0",
    "#795548",
    "#607D8B",
    "#E91E63",
    "#3F51B5",
    "#009688",
    "#FF9800",
  ];
  
// 현재 로그인한 유저 ID
const userId = ref<number|null>(null);
// 중요 공지 여부 (원하시면 UI에 토글 추가)
const isPinned = ref(false);

// 토큰
const token = localStorage.getItem("accessToken");

  // 에디터 초기화
  onMounted(() => {
    if (editorRef.value) editorRef.value.focus();

  if (!token) return;
  axios.get<{ userId: number }>(
    "http://localhost:8080/api/users/me",
    { headers: { Authorization: `Bearer ${token}` } }
  )
  .then(r => { userId.value = r.data.userId })
  .catch(err => console.error("내 정보 로드 실패", err))
  });
  
  // 텍스트 포맷 적용
  const applyFormat = (format: "bold" | "italic" | "underline") => {
    document.execCommand(format, false);
    formats[format] = !formats[format];
  };
  
  // 정렬 적용
  const applyAlignment = (align: string) => {
    alignment.value = align;
    if (editorRef.value) {
      document.execCommand(
        "justify" + align.charAt(0).toUpperCase() + align.slice(1),
        false,
      );
    }
  };
  
  // 색상 선택기 토글
  const toggleColorPicker = (type: "text" | "background") => {
    colorPickers[type] = !colorPickers[type];
    // 다른 색상 선택기 닫기
    if (type === "text") {
      colorPickers.background = false;
    } else {
      colorPickers.text = false;
    }
  };
  
  // 색상 선택
  const selectColor = (type: "text" | "background", color: string) => {
    if (type === "text") {
      textColor.value = color;
      document.execCommand("foreColor", false, color);
    } else {
      backgroundColor.value = color;
      document.execCommand("hiliteColor", false, color);
    }
    colorPickers[type] = false;
  };
  
  // 이미지 업로드
  const uploadImage = (event: Event) => {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      const reader = new FileReader();
  
      reader.onload = (e) => {
        const img = document.createElement("img");
        img.src = e.target?.result as string;
        img.style.maxWidth = "100%";
        img.style.height = "auto";
        img.style.margin = "10px 0";
  
        if (editorRef.value) {
          // 현재 선택 위치에 이미지 삽입
          document.execCommand("insertHTML", false, img.outerHTML);
        }
      };
  
      reader.readAsDataURL(input.files[0]);
    }
  };
  
  // 에디터 내용 변경 처리
  const handleInput = () => {
    if (editorRef.value) {
      content.value = editorRef.value.innerHTML;
    }
  };
  
  // 폼 제출
  async function submitForm() {
    if (!title.value.trim()) {
      alert("제목을 입력해주세요.");
      return;
    }
  
    if (!content.value.trim()) {
      alert("내용을 입력해주세요.");
      return;
    }
if (!userId.value) {
  alert("로그인이 필요합니다.");
  return router.push("/login");
}

try {
  await axios.post(
    "http://localhost:8080/api/notice",
    {
      userId: userId.value,
      title: title.value,
      content: content.value,
      isPinned: isPinned.value
    },
    { headers: { Authorization: `Bearer ${token}` } }
  );
  alert("공지사항이 등록되었습니다.");
  router.push({ name: "NoticeBoard" });
} catch (err) {
  console.error("등록 실패", err);
  alert("등록 중 오류가 발생했습니다.");
}
  }
  </script>
  
  <style scoped>
  [contenteditable="true"]:empty:before {
    content: attr(placeholder);
    color: #aaa;
    font-style: italic;
  }
  
  [contenteditable="true"]:focus {
    outline: none;
  }
  
  /* 에디터 내부 이미지 스타일 */
  [contenteditable="true"] img {
    max-width: 100%;
    height: auto;
    margin: 10px 0;
  }
  
  /* 에디터 내부 텍스트 스타일 */
  [contenteditable="true"] p {
    margin-bottom: 10px;
  }
  </style>
  