<!-- The exported code uses Tailwind CSS. Install Tailwind CSS in your dev environment to ensure all styles work. -->
<template>
    <div class="min-h-screen bg-[#FFF1D5] bg-opacity-30 py-10">
      <div class="max-w-6xl mx-auto px-4">
        <h1 class="text-3xl font-bold text-center mb-8 text-[#9FB3DF]">
          마이페이지
        </h1>
        <!-- 탭 메뉴 -->
      <div class="bg-white rounded-lg shadow-sm mb-6">
        <div class="flex border-b">
          <button
            v-for="(tab, index) in tabs"
            :key="index"
            @click="activeTab = tab.id"
            :class="[
              'flex-1 py-4 px-4 text-center font-medium transition-colors duration-200 cursor-pointer whitespace-nowrap',
              activeTab === tab.id ? 'text-[#9FB3DF] border-b-2 border-[#9FB3DF]' : 'text-gray-600 hover:text-[#9EC6F3]'
            ]"
          >
            {{ tab.name }}
          </button>
        </div>
      </div>
      <!-- 탭 콘텐츠 -->
      <div class="bg-white rounded-lg shadow-sm p-6 min-h-[600px]">
        <!-- 내 그림 -->
        <div v-if="activeTab === 'myDrawings'" class="fade-in">
          <div class="flex justify-between items-center mb-6">
            <h2 class="text-xl font-semibold text-gray-800">내 그림</h2>
          </div>
          <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            <div
              v-for="(drawing, index) in myDrawings"
              :key="index"
              class="relative border rounded-lg overflow-hidden bg-white shadow-sm hover:shadow-md transition cursor-pointer"
              @click="openImageModal(drawing)"
            >
            <img
                :src="drawing.originalUrl"
                alt="그림"
                class="w-full h-48 object-cover object-top cursor-pointer"
                @click="openImageModal(drawing)"
              />
              <!-- 삭제 버튼 -->
              <div class="absolute top-2 right-2 flex space-x-1">
                <button
                  @click.stop="deleteDrawing(drawing.drawingId, index)"
                  class="bg-white p-2 rounded-full shadow hover:bg-gray-100 cursor-pointer !rounded-button"
                >
                  <i class="fas fa-trash-alt text-gray-600"></i>
                </button>
              </div>
              <div class="p-4">
                <h3 class="font-medium text-gray-800">내가 그린 그림</h3>
                <p class="text-sm text-gray-500 mt-1">{{ drawing.date }}</p>
              </div>
            </div>
          </div>
          <div v-if="myDrawings.length === 0" class="text-center py-12">
            <i class="fas fa-paint-brush text-gray-300 text-5xl mb-4"></i>
            <p class="text-gray-500">아직 업로드한 그림이 없습니다.</p>
          </div>
        </div>
          <!-- 내 여행계획 -->
        <div v-if="activeTab === 'myTravelPlans'" class="fade-in">
          <div class="flex justify-between items-center mb-6">
            <h2 class="text-xl font-semibold text-gray-800">내 여행계획</h2>
            <button
              class="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 transition cursor-pointer !rounded-button whitespace-nowrap"
            >
              새 여행 계획 추가
            </button>
          </div>

          <div class="space-y-4">
            <div
              v-for="plan in filteredPlans"
              :key="plan.id"
              class="bg-white rounded-lg shadow-md p-4 flex items-center gap-4 transition-all duration-300 hover:shadow-lg w-4/5 mx-auto"
            >
              <!-- Plan Content -->
              <div class="flex-grow flex justify-between items-center">
                <div>
                  <div class="flex items-center gap-3 mb-2">
                    <h3 class="text-lg font-semibold">{{ plan.title }}</h3>
                    <span
                      class="bg-[#9FB3DF] text-white px-2 py-1 rounded-md text-xs"
                      >{{ plan.region }}</span
                    >
                  </div>
                </div>
                <!-- Route Display -->
                <div class="flex flex-wrap items-center gap-3 ml-4">
                  <template
                    v-for="(location, locIndex) in plan.route"
                    :key="locIndex"
                  >
                    <span
                      class="bg-[#FFF1D5] px-3 py-2 rounded text-base font-medium whitespace-nowrap"
                      >{{ location }}</span
                    >
                    <i
                      v-if="locIndex < plan.route.length - 1"
                      class="fas fa-arrow-right text-[#9EC6F3] text-lg flex-shrink-0"
                    ></i>
                  </template>
                </div>
              </div>
            </div>

            <div v-if="filteredPlans.length === 0" class="text-center py-12">
              <i class="fas fa-plane text-gray-300 text-5xl mb-4"></i>
              <p class="text-gray-500">아직 등록된 여행 계획이 없습니다.</p>
              <button
                class="mt-4 bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 transition cursor-pointer !rounded-button whitespace-nowrap"
              >
                첫 여행 계획 만들기
              </button>
            </div>
          </div>
        </div>
          <!-- 회원정보수정 -->
          <div v-if="activeTab === 'editProfile'" class="fade-in">
            <h2 class="text-xl font-semibold text-gray-800 mb-6">
              회원정보 수정
            </h2>
            <div
              class="max-w-md mx-auto bg-gray-50 p-6 rounded-lg"
            >
              <p class="text-gray-700 mb-4">
                회원정보를 수정하기 위해 비밀번호를 입력해주세요.
              </p>
              <div class="mb-4">
                <label
                  for="password"
                  class="block text-sm font-medium text-gray-700 mb-1"
                  >비밀번호</label
                >
                <input
                  type="password"
                  id="password"
                  v-model="password"
                  class="w-full px-3 py-2 border border-[#BDDDE4] rounded-md focus:outline-none focus:ring-2 focus:ring-[#9EC6F3]"
                  placeholder="비밀번호를 입력하세요"
                />
              </div>
              <div class="flex justify-end">
                <button
                  @click="verifyPassword"
                  class="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 transition cursor-pointer !rounded-button whitespace-nowrap"
                >
                  확인
                </button>
              </div>
            </div>
          </div>
          <!-- 회원탈퇴 -->
          <div v-if="activeTab === 'deleteAccount'" class="fade-in">
            <h2 class="text-xl font-semibold text-gray-800 mb-6">회원탈퇴</h2>
            <div class="max-w-md mx-auto">
              <div class="bg-red-50 border border-red-200 rounded-lg p-4 mb-6">
                <div class="flex items-start">
                  <i
                    class="fas fa-exclamation-triangle text-red-500 mt-1 mr-3"
                  ></i>
                  <div>
                    <h3 class="font-medium text-red-800">
                      회원 탈퇴 전 주의사항
                    </h3>
                    <ul
                      class="mt-2 text-sm text-red-700 list-disc pl-5 space-y-1"
                    >
                      <li>
                        계정을 삭제하면 모든 데이터가 영구적으로 삭제됩니다.
                      </li>
                      <li>업로드한 모든 그림과 여행 계획이 삭제됩니다.</li>
                      <li>삭제된 계정은 복구할 수 없습니다.</li>
                    </ul>
                  </div>
                </div>
              </div>
              <div class="bg-[#BDDDE4] bg-opacity-10 p-6 rounded-lg">
                <p class="text-gray-700 mb-4">
                  회원 탈퇴를 진행하려면 비밀번호를 입력해주세요.
                </p>
                <div class="mb-4">
                  <label
                    for="deletePassword"
                    class="block text-sm font-medium text-gray-700 mb-1"
                    >비밀번호</label
                  >
                  <input
                    type="password"
                    id="deletePassword"
                    v-model="deletePassword"
                    class="w-full px-3 py-2 border border-[#BDDDE4] rounded-md focus:outline-none focus:ring-2 focus:ring-[#9EC6F3]"
                    placeholder="비밀번호를 입력하세요"
                  />
                </div>
                <div class="flex justify-end">
                  <button
                    @click="confirmDeleteAccount"
                    class="bg-red-600 text-white px-4 py-2 rounded-md hover:bg-red-700 transition cursor-pointer !rounded-button whitespace-nowrap"
                  >
                    탈퇴하기
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- 확인 모달 -->
      <div
        v-if="showModal"
        class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
      >
        <div class="bg-white rounded-lg p-6 max-w-md w-full">
          <h3 class="text-lg font-medium text-gray-900 mb-4">{{ modalTitle }}</h3>
          <p class="text-gray-700">{{ modalMessage }}</p>
          <div class="mt-6 flex justify-end space-x-3">
            <button
              v-if="modalType === 'confirm'"
              @click="showModal = false"
              class="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50 transition cursor-pointer !rounded-button whitespace-nowrap"
            >
              취소
            </button>
            <button
              @click="handleModalConfirm"
              :class="[
  'px-4 py-2 rounded-md text-white transition cursor-pointer !rounded-button whitespace-nowrap',
  modalType === 'confirm' ? 'bg-red-600 hover:bg-red-700' : 'bg-[#9EC6F3] hover:bg-[#9FB3DF]'
  ]"
            >
              {{ modalType === 'confirm' ? '확인' : '확인' }}
            </button>
          </div>
        </div>
      </div>
    </div>
      <!-- 상세 이미지 모달 -->
    <div
      v-if="showImageModal"
      class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
    >
      <div class="bg-white rounded-lg p-4 max-w-2xl w-full relative overflow-auto max-h-[80vh]">
        <button
          @click="closeImageModal"
          class="absolute top-2 right-2 text-gray-600"
        >
          <i class="fas fa-times"></i>
        </button>
        <div class="flex flex-col md:flex-row gap-4 p-2">
          <div class="flex-1 overflow-auto">
            <h4 class="font-medium mb-2">내가 그린 그림</h4>
            <img :src="selectedDrawing?.originalUrl" alt="Original" class="w-full h-auto object-contain" />
          </div>
          <div class="flex-1 overflow-auto">
            <h4 class="font-medium mb-2">AI 변환 그림</h4>
            <img :src="selectedDrawing?.gptUrl" alt="GPT" class="w-full h-auto object-contain" />
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script lang="ts" setup>
  import { ref, computed, onMounted } from "vue";
  import { useRouter } from 'vue-router'
  

  import axios from "axios";

  import { useUserStore } from '@/stores/user';
  const userStore = useUserStore();


  const router = useRouter();

  // 탭 상태 관리
  // 필터된 여행 계획 (필요 시 favorite 필터링 로직 추가 가능)
const filteredPlans = computed(() => travelPlans.value);
  const tabs = [
    { id: "myDrawings", name: "내 그림" },
    { id: "myTravelPlans", name: "내 여행계획" },
    { id: "editProfile", name: "회원정보수정" },
    { id: "deleteAccount", name: "회원탈퇴" },
  ];
  const activeTab = ref("myDrawings");

  interface DrawingItem {
    drawingId: number;
    originalUrl: string;
    gptUrl: string;
    date: string;
}


const myDrawings = ref<DrawingItem[]>([]);
const selectedDrawing = ref<DrawingItem | null>(null);
const showImageModal = ref(false);

function openImageModal(drawing: DrawingItem) {
  selectedDrawing.value = drawing;
  showImageModal.value = true;
}
function closeImageModal() {
  showImageModal.value = false;
  selectedDrawing.value = null;
}


onMounted(async () => {
  if (activeTab.value !== 'myDrawings') return;
  try {
    const { data } = await axios.get<{
      drawingId: number;
      originalUrl: string;
      gptUrl: string;
      createdAt: string;
    }[]>('/api/mypage/drawings');

    myDrawings.value = data.map(item => {
      const dt = new Date(item.createdAt.replace(' ', 'T'));
      return {
        drawingId: item.drawingId,
        originalUrl: item.originalUrl,
        gptUrl: item.gptUrl,
        date: dt.toLocaleString(),
      };
    });
  } catch (err) {
    console.error('내 그림 불러오기 실패', err);
  }
});

  // 여행 계획 데이터
  const travelPlans = ref([
  {
    id: 1,
    title: "제주도 4박 5일 여행",
    region: "제주도",
    author: "김여행",
    route: ["성산일출봉", "우도", "한라산", "카페 투어", "협재 해수욕장"],
    favorite: false,
  },
  {
    id: 2,
    title: "부산 주말 여행",
    region: "부산",
    author: "홍길동",
    route: ["해운대", "광안리", "감천문화마을", "자갈치시장"],
    favorite: false,
  },
  {
    id: 3,
    title: "강원도 스키 여행",
    region: "평창",
    author: "이동수",
    route: ["용평 리조트", "알펜시아", "평창 송어 축제"],
    favorite: false,
  },
  ]);
  // 회원정보 수정 관련
  const password = ref("");
  
  // 회원탈퇴 관련
  const deletePassword = ref("");
  // 모달 관련
  const showModal = ref(false);
  const modalType = ref<"alert" | "confirm">("alert");
  const modalTitle = ref("");
  const modalMessage = ref("");
  const modalCallback = ref<() => void>(() => {});

  const verifyPassword = async () => {
  try {
    // 1) DB 검증
    await axios.post("/api/users/verify-password", { password: password.value });
    userStore.confirmPassword();
    router.push("/editprofile");
  } catch {
    showAlertModal("비밀번호 오류", "비밀번호가 일치하지 않습니다.");
  }
};
  // 회원탈퇴 확인 함수
  const confirmDeleteAccount = () => {
    if (deletePassword.value === "") {
      showAlertModal("입력 오류", "비밀번호를 입력해주세요.");
      return;
    }
    showConfirmModal(
      "회원탈퇴 확인",
      "정말로 탈퇴하시겠습니까? 이 작업은 되돌릴 수 없습니다.",
      deleteAccount,
    );
  };
  // 실제 회원탈퇴 처리 함수
  async function deleteAccount() {
    try {
      // POST로 비밀번호 검증 + 삭제 엔드포인트 호출
      await axios.post("/api/users/delete-user", {
        password: deletePassword.value
      });

      // 성공
      showAlertModal(
        "탈퇴 완료",
        "회원탈퇴가 완료되었습니다. 이용해주셔서 감사합니다.",
      );

      // 원하시면 userStore.logout() 같은 로그아웃 처리 후 리다이렉트
      localStorage.removeItem('accessToken')
      userStore.clearProfile()
      userStore.logout();
      router.push("/");

    } catch (error: any) {
      // 401 Unauthorized → 비밀번호 불일치
      if (error.response?.status === 401) {
        showAlertModal("비밀번호 오류", "비밀번호가 일치하지 않습니다.");
      } else {
        showAlertModal("오류", "문제가 발생했습니다. 다시 시도해주세요.");
      }
    }
  }
  // 알림 모달 표시 함수
  const showAlertModal = (title: string, message: string) => {
    modalType.value = "alert";
    modalTitle.value = title;
    modalMessage.value = message;
    showModal.value = true;
  };
  // 확인 모달 표시 함수
  const showConfirmModal = (
    title: string,
    message: string,
    callback: () => void,
  ) => {
    modalType.value = "confirm";
    modalTitle.value = title;
    modalMessage.value = message;
    modalCallback.value = callback;
    showModal.value = true;
  };
  // 모달 확인 버튼 처리 함수
  const handleModalConfirm = () => {
    if (modalType.value === "confirm") {
      modalCallback.value();
    }
    showModal.value = false;
  };

  async function deleteDrawing(drawingId: number, index: number) {
  try {
    await axios.delete(`/api/mypage/drawings/${drawingId}`);
    // 성공 시 화면에서 바로 제거
    myDrawings.value.splice(index, 1);
  } catch (err) {
    console.error('삭제 실패', err);
  }
}
  </script>
  
  <style scoped>
  .fade-in {
    animation: fadeIn 0.3s ease-in-out;
  }
  @keyframes fadeIn {
    from {
      opacity: 0;
      transform: translateY(10px);
    }
    to {
      opacity: 1;
      transform: translateY(0);
    }
  }
  input[type="number"]::-webkit-inner-spin-button,
  input[type="number"]::-webkit-outer-spin-button {
    -webkit-appearance: none;
    margin: 0;
  }
  </style>
  