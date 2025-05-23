<template>
  <div class="min-h-screen bg-white">
    <div class="w-full flex flex-col p-8">
      <!-- Back Button -->
      <div class="w-4/5 mx-auto mb-4">
        <button
          @click="goBackToList"
          class="bg-[#9FB3DF] text-white px-4 py-2 rounded-button flex items-center gap-2 whitespace-nowrap"
        >
          <i class="fas fa-arrow-left"></i> 목록으로 돌아가기
        </button>
      </div>

      <!-- Plan Header -->
      <div class="bg-[#FFF1D5] rounded-lg p-6 mb-6 shadow-md w-4/5 mx-auto">
        <div class="flex justify-between items-start">
          <div>
            <!-- 제목 -->
            <h1 class="text-3xl font-bold mb-1">{{ selectedPlan.boardTitle }}</h1>
            <!-- 작성일 -->
            <div class="text-xs text-gray-500 mb-3">{{ selectedPlan.createdAt }}</div>
            <!-- 카테고리 · 작성자 · 지역 -->
            <div class="flex items-center gap-3 mb-4">
              <span
                v-for="cat in selectedPlan.boardCategoryList"
                :key="cat"
                class="bg-[#9FB3DF] text-white px-3 py-1 rounded-md text-xs"
              >{{ cat }}</span>
              <span class="text-gray-600">작성자: {{ selectedPlan.nickname }}</span>
              <span class="text-gray-600">지역: {{ selectedPlan.region }}</span>
            </div>
          </div>
          <div class="flex items-center gap-3">
            <button
              @click="copyToClipboard"
              class="bg-[#9FB3DF] text-white px-4 py-2 rounded-button flex items-center gap-2 cursor-pointer whitespace-nowrap"
            >
              <i class="fas fa-copy"></i>
              <span>공유하기</span>
            </button>
          <!-- 즐겨찾기 -->
          <div @click="toggleFavorite" class="cursor-pointer">
            <i
              class="fas fa-star text-2xl"
              :class="selectedPlan.favorite ? 'text-yellow-400' : 'text-gray-300'"
            />
          </div>
        </div>
        </div>

        <!-- Route Overview -->
        <div class="flex items-center gap-3 flex-wrap mt-2">
          <template v-for="(loc, idx) in selectedPlan.route" :key="idx">
            <span class="bg-white px-3 py-2 rounded text-base font-medium whitespace-nowrap">
              {{ loc }}
            </span>
            <i
              v-if="idx < selectedPlan.route.length - 1"
              class="fas fa-arrow-right text-[#9EC6F3] text-lg"
            />
            <!-- Copy Success Modal -->
            <div
              v-if="showCopyModal"
              class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
            >
              <div class="bg-white rounded-lg p-8 max-w-2xl w-full mx-4">
                <div class="text-center mb-6">
                  <h2 class="text-2xl font-bold mb-2">
                    경로코드가 복사되었습니다!
                  </h2>
                  <p class="text-gray-600">
                    계획짜기에서 가져오기 버튼을 통해 경로를 수정할 수 있어요!
                  </p>
                </div>

                <button
                  @click="goToPlanPage"
                  class="w-full bg-[#9FB3DF] text-white py-3 rounded-button mb-6 hover:bg-[#8FA3CF] transition-colors whitespace-nowrap"
                >
                  계획짜기
                </button>

                <div class="bg-gray-50 p-4 rounded-lg relative">
                  <div class="max-h-40 overflow-y-auto mb-2 text-sm">
                    <pre class="whitespace-pre-wrap">{{ copiedText }}</pre>
                  </div>
                  <div
                    class="absolute top-4 right-4 text-green-500 flex items-center"
                  >
                    <i class="fas fa-check mr-2"></i>
                    <span>복사됨</span>
                  </div>
                </div>

                <button
                  @click="showCopyModal = false"
                  class="mt-6 w-full border border-gray-300 text-gray-700 py-2 rounded-button hover:bg-gray-50 transition-colors whitespace-nowrap"
                >
                  닫기
                </button>
              </div>
            </div>
          </template>
        </div>
      </div>
      <!-- Daily Plans -->
<div class="w-4/5 mx-auto pb-6">
  <h2 class="text-2xl font-bold mb-4 text-[#9FB3DF]">일차별 여행 계획</h2>
  <div class="space-y-6">
    <div
      v-for="day in dailyPlans"
      :key="day.dayNo"
      class="bg-white rounded-lg shadow-md p-6"
    >
      <h3 class="text-xl font-semibold mb-4 text-[#9FB3DF] border-b pb-2">
        Day {{ day.dayNo }}
      </h3>
      <div class="space-y-4">
        <div
          v-for="activity in day.activities"
          :key="activity.locationId"
          class="flex gap-4 p-3 rounded-md hover:bg-gray-50"
        >
          <div class="w-1/6 text-sm text-gray-500">
            {{ formatTime(activity.startTime) }} – {{ formatTime(activity.endTime) }}
          </div>
          <div class="flex-grow">
            <div class="flex items-center gap-2 mb-1">
              <h4 class="font-medium text-lg">{{ activity.addressName }}</h4>
              <span
                class="bg-[#BDDDE4] text-gray-700 px-2 py-1 rounded text-xs"
              >{{ activity.addressCategory }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

      <!-- 게시글 본문 -->
      <div class="bg-white rounded-lg shadow-md p-6 mb-6 w-4/5 mx-auto">
        <h3 class="text-xl font-semibold mb-4 text-[#9FB3DF]">여행 설명</h3>
        <p class="text-gray-700 whitespace-pre-line">{{ selectedPlan.boardContent }}</p>
      </div>

      <!-- Comments Section -->
      <div class="bg-white rounded-lg shadow-md p-6 mt-6 w-4/5 mx-auto">
        <h3 class="text-xl font-semibold mb-4 text-[#9FB3DF]">
          댓글 ({{ comments.length }})
        </h3>

        <!-- New Comment -->
        <div class="mb-6 bg-gray-50 p-4 rounded-md">
          <textarea
            v-model="newComment"
            placeholder="댓글을 작성해주세요..."
            class="w-full p-3 rounded-md bg-white text-sm focus:outline-none focus:ring-2 focus:ring-[#9FB3DF] min-h-[100px]"
          />
          <div class="flex justify-end mt-2">
            <button
              @click="addComment"
              class="bg-[#9FB3DF] text-white px-4 py-2 rounded-button"
            >댓글 작성</button>
          </div>
        </div>

        <!-- Comment List -->
        <div class="space-y-4">
          <div v-for="c in comments" :key="c.commentId" class="border-b pb-4">
            <div class="flex justify-between items-start">
              <div class="flex items-center gap-3">
                <div class="w-10 h-10 bg-[#FFF1D5] rounded-full flex items-center justify-center">
                  <i class="fas fa-user text-[#9FB3DF]" />
                </div>
                <div>
                  <div class="font-medium">{{ c.nickname }}</div>
                  <div class="text-xs text-gray-500">{{ c.createdAt }}</div>
                </div>
              </div>
              <div v-if="c.userId === currentUserId" class="flex gap-2">
                <button
                  v-if="editingId !== c.commentId"
                  @click="startEdit(c)"
                  class="text-[#9FB3DF]"
                >수정</button>
                <button @click="deleteComment(c.commentId)" class="text-red-500">삭제</button>
              </div>
            </div>
            <div class="mt-2 pl-12">
              <div v-if="editingId === c.commentId">
                <textarea
                  v-model="editCommentText"
                  class="w-full p-3 rounded-md bg-white"
                />
                <div class="flex justify-end gap-2 mt-2">
                  <button @click="cancelEdit" class="bg-gray-300 px-3 rounded">취소</button>
                  <button @click="saveEdit" class="bg-[#9FB3DF] px-3 rounded text-white">저장</button>
                </div>
              </div>
              <p v-else class="text-gray-700">{{ c.content }}</p>
            </div>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import axios from "axios";

interface DayPlan {
  dayNo: number;
  activities: {
    locationId: number;
    planCode: string;
    dayNo: number;
    addressName: string;
    addressCategory: string;
    startTime: string;
    endTime: string;
  }[];
}

interface PlanDetailWithDays {
  board: PlanDetail;      // 기존 board 메타
  dailyPlans: DayPlan[];  // 방금 추가된 일차별 리스트
}


// API에서 내려주는 DTO 구조에 맞춘 타입
interface PlanDetail {
  planBoardId: number;
  planCode: string;
  boardTitle: string;
  boardContent: string;
  boardCategory: string;
  boardCategoryList: string[];
  createdAt: string;
  routeConcat: string;
  route: string[];
  userId: number;
  region: string;
  nickname: string;
  favorite: boolean;
  dailyPlans: number;
}

interface CommentDto {
  commentId: number;
  userId: number;
  nickname: string;
  content: string;
  createdAt: string;
}

const route = useRoute();
const router = useRouter();
const planBoardId = Number(route.params.id);
const token = localStorage.getItem("accessToken");
const currentUserId = ref<number>(0);

// 상세와 댓글 상태
const selectedPlan = ref<PlanDetail>({
  planBoardId: 0,
  planCode: "",
  boardTitle: "",
  boardContent: "",
  boardCategory: "",
  boardCategoryList: [],
  createdAt: "",
  routeConcat: "",
  route: [],
  userId: 0,
  region: "",
  nickname: "",
  favorite: false,
  dailyPlans: 0,
});

const dailyPlans   = ref<DayPlan[]>([]);
const comments      = ref<CommentDto[]>([]);
const newComment    = ref("");
const editingId     = ref<number|null>(null);
const editCommentText = ref("");

// 1) 내 정보, 2) 상세, 3) 댓글 불러오기
async function loadAll() {
  if (!token) {
    alert("로그인이 필요합니다.");
    return router.push("/login");
  }

  // 내 정보
  const me = await axios.get<{ userId: number }>(
    "http://localhost:8080/api/users/me",
    { headers: { Authorization: `Bearer ${token}` } }
  );
  currentUserId.value = me.data.userId;

  // 게시글 상세
  const bd = await axios.get<PlanDetail>(
    `http://localhost:8080/api/trip/boards/${planBoardId}`,
    {
      headers: { Authorization: `Bearer ${token}` },
      params:  { userId: currentUserId.value }
    }
  );
  selectedPlan.value = bd.data;

  // 댓글 목록
  const cm = await axios.get<CommentDto[]>(
    `http://localhost:8080/api/trip/boards/${planBoardId}/comments`,
    { headers: { Authorization: `Bearer ${token}` } }
  );
  comments.value = cm.data;
  
  // 일차별 계획
  const resp = await axios.get<PlanDetailWithDays>(
    `http://localhost:8080/api/trip/boards/${planBoardId}/detail-with-locations`,
    {
      headers: { Authorization: `Bearer ${token}` },
      params:  { userId: currentUserId.value }
    }
  );
  selectedPlan.value = resp.data.board;
  dailyPlans.value   = resp.data.dailyPlans;
}

onMounted(loadAll);

// 뒤로 가기
function goBackToList() {
  router.push({ name: "TripPlanBoard" });
}

// 계획짜기 페이지로 이동
function goToPlanPage() {
  router.push({ name: "TripPlanPage" });
}

// 즐겨찾기 토글
async function toggleFavorite() {
  const url = `http://localhost:8080/api/trip/boards/${planBoardId}/favorite`;
  const cfg = {
    headers: { Authorization: `Bearer ${token}` },
    params:  { userId: currentUserId.value }
  };
  if (!selectedPlan.value.favorite) {
    await axios.post(url, null, cfg);
  } else {
    await axios.delete(url, cfg);
  }
  selectedPlan.value.favorite = !selectedPlan.value.favorite;
}

// 댓글 CRUD
async function addComment() {
  if (!newComment.value.trim()) return;
  await axios.post(
    `http://localhost:8080/api/trip/boards/${planBoardId}/comments`,
    { userId: currentUserId.value, content: newComment.value },
    { headers: { Authorization: `Bearer ${token}` } }
  );
  newComment.value = "";
  await loadAll();
}

function startEdit(c: CommentDto) {
  editingId.value = c.commentId;
  editCommentText.value = c.content;
}
function cancelEdit() {
  editingId.value = null;
  editCommentText.value = "";
}
async function saveEdit() {
  if (editingId.value === null || !editCommentText.value.trim()) return;
  await axios.put(
    `http://localhost:8080/api/trip/boards/${planBoardId}/comments/${editingId.value}`,
    { userId: currentUserId.value, content: editCommentText.value },
    { headers: { Authorization: `Bearer ${token}` } }
  );
  cancelEdit();
  await loadAll();
}

async function deleteComment(commentId: number) {
  if (!confirm("정말 삭제하시겠습니까?")) return;
  await axios.delete(
    `http://localhost:8080/api/trip/boards/${planBoardId}/comments/${commentId}`,
    { headers: { Authorization: `Bearer ${token}` } }
  );
  await loadAll();
}
// Format time helper
const formatTime = (timeString: string) => {
  const [hours, minutes] = timeString.split(":");
  return `${hours}:${minutes}`;
};

// Clipboard copy functionality
// Add new refs for modal control and copied text
const showCopyModal = ref(false);
const copiedText = ref("");

const copyToClipboard = () => {
  const code = selectedPlan.value.planCode;
  navigator.clipboard.writeText(code)
    .then(() => {
      copiedText.value = code;
      showCopyModal.value = true;
    })
    .catch((err) => {
      console.error("클립보드 복사 실패:", err);
      alert("클립보드 복사에 실패했습니다. 다시 시도해주세요.");
    });
};
</script>

<style scoped>
/* Hide number input arrows */
input::-webkit-inner-spin-button,
input::-webkit-outer-spin-button {
  -webkit-appearance: none; margin: 0;
}
input[type="number"] { -moz-appearance: textfield; }

/* Custom scrollbar */
::-webkit-scrollbar { width: 8px; }
::-webkit-scrollbar-track { background: #f1f1f1; border-radius:10px; }
::-webkit-scrollbar-thumb { background: #9FB3DF; border-radius:10px; }
::-webkit-scrollbar-thumb:hover { background: #9EC6F3; }
</style>