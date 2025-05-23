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
            <h1 class="text-3xl font-bold mb-2">{{ selectedPlan.boardTitle }}</h1>
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
          <div @click="toggleFavorite" class="cursor-pointer">
            <i
              class="fas fa-star text-2xl"
              :class="selectedPlan.favorite ? 'text-yellow-400' : 'text-gray-300'"
            />
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
          </template>
        </div>
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

interface PlanDetail {
  boardTitle: string;
  boardCategoryList: string[];
  nickname: string;
  region: string;
  route: string[];
  favorite: boolean;
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

const selectedPlan = ref<PlanDetail>({
  boardTitle: "",
  boardCategoryList: [],
  nickname: "",
  region: "",
  route: [],
  favorite: false,
});
const comments      = ref<CommentDto[]>([]);
const newComment    = ref("");
const editingId     = ref<number|null>(null);
const editCommentText = ref("");

// 한 번에 내 정보, 상세, 댓글 불러오기
async function loadAll() {
  if (!token) {
    alert("로그인이 필요합니다.");
    router.push("/login");
    return;
  }

  // 1) 내 정보
  const me = await axios.get<{ userId: number }>(
    "/api/users/me",
    { headers: { Authorization: `Bearer ${token}` } }
  );
  currentUserId.value = me.data.userId;

  // 2) 보드 상세
  const bd = await axios.get<PlanDetail>(
    `/api/trip/boards/${planBoardId}`,
    {
      headers: { Authorization: `Bearer ${token}` },
      params: { userId: currentUserId.value }
    }
  );
  selectedPlan.value = bd.data;

  // 3) 댓글 목록
  const cm = await axios.get<CommentDto[]>(
    `/api/trip/boards/${planBoardId}/comments`,
    { headers: { Authorization: `Bearer ${token}` } }
  );
  comments.value = cm.data;
}

onMounted(loadAll);

// 뒤로가기
function goBackToList() {
  router.push({ name: "TripPlanList" });
}

// 즐겨찾기 toggle
async function toggleFavorite() {
  const url = `/api/trip/boards/${planBoardId}/favorite`;
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

// 댓글 작성
async function addComment() {
  if (!newComment.value.trim()) return;
  await axios.post(
    `/api/trip/boards/${planBoardId}/comments`,
    {
      userId: currentUserId.value,
      content: newComment.value
    },
    { headers:{ Authorization: `Bearer ${token}` } }
  );
  newComment.value = "";
  await loadAll();
}

// 댓글 수정
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
    `/api/trip/boards/${planBoardId}/comments/${editingId.value}`,
    {
      userId: currentUserId.value,
      content: editCommentText.value
    },
    { headers:{ Authorization: `Bearer ${token}` } }
  );
  cancelEdit();
  await loadAll();
}

// 댓글 삭제
async function deleteComment(commentId: number) {
  if (!confirm("정말 삭제하시겠습니까?")) return;
  await axios.delete(
    `/api/trip/boards/${planBoardId}/comments/${commentId}`,
    { headers:{ Authorization: `Bearer ${token}` } }
  );
  await loadAll();
}
</script>

<style scoped>
/* Hide number input arrows */
input[type="number"]::-webkit-inner-spin-button,
input[type="number"]::-webkit-outer-spin-button {
  -webkit-appearance: none;
  margin: 0;
}
input[type="number"] {
  -moz-appearance: textfield;
}

/* Custom scrollbar */
::-webkit-scrollbar {
  width: 8px;
}
::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 10px;
}
::-webkit-scrollbar-thumb {
  background: #9FB3DF;
  border-radius: 10px;
}
::-webkit-scrollbar-thumb:hover {
  background: #9EC6F3;
}
</style>