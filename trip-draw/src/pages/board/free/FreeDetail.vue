<template>
  <div class="container mx-auto px-4 py-8 min-h-screen">
    <!-- 게시글 상세 영역 -->
    <div v-if="post" class="bg-white rounded-lg shadow-lg p-6 mb-8">
      <!-- 게시글 제목 -->
      <h1 class="text-3xl font-bold mb-4">{{ post.title }}</h1>

      <!-- 작성자 정보 -->
      <div
        class="flex flex-wrap items-center text-gray-600 mb-6 pb-4 border-b border-gray-200"
      >
        <div class="flex items-center mr-6">
          <i class="fas fa-user-circle text-xl mr-2 text-[#9FB3DF]"></i>
          <span>{{ post.nickName }}</span>
        </div>
        <div class="flex items-center mr-6">
          <i class="far fa-calendar-alt mr-2 text-[#9FB3DF]"></i>
          <span>{{ formatDate(post.createdAt) }}</span>
        </div>
        <div class="flex items-center mr-6">
          <i class="far fa-eye mr-2 text-[#9FB3DF]"></i>
          <span>조회수 {{ post.viewCount }}</span>
        </div>
      </div>

      <!-- 게시글 대표 이미지 -->
      <div v-if="post.imageUrls && post.imageUrls.length" class="mb-8 overflow-hidden rounded-lg">
        <img
          :src="post.imageUrls[0]"
          alt="대표 이미지"
          class="w-full h-[400px] object-cover object-top cursor-pointer"
          @click="showFullImage = true"
        />
      </div>

      <!-- AI 요약 (예시로 post.summary 가 있다고 가정) -->
      <div v-if="post.summary" class="bg-[#FFF1D5] p-4 rounded-lg mb-8">
        <h3 class="font-bold mb-2 flex items-center">
          <i class="fas fa-robot mr-2"></i> AI 요약
        </h3>
        <p class="text-gray-700">{{ post.summary }}</p>
      </div>

      <!-- 게시글 본문 -->
      <div class="mb-8 leading-relaxed" v-html="post.content"></div>

      <!-- 추천/비추천 버튼 -->
      <div class="flex justify-center space-x-4 mb-8">
        <button
          @click="toggleLike"
          class="!rounded-button whitespace-nowrap flex items-center px-6 py-3 rounded-full text-white transition-all duration-300 cursor-pointer"
          :class="isLiked
            ? 'bg-[#6C8EBF]'   /* 눌러진(active) 상태일 때 더 진한 파란색 */
            : 'bg-[#9FB3DF]'   /* 눌리지 않은(inactive) 상태일 때 연한 파란색 */">
          <i class="fas fa-thumbs-up mr-2"></i>
          추천 {{ likeCount }}
        </button>
        <button
          @click="toggleDislike"
          class="!rounded-button whitespace-nowrap flex items-center px-6 py-3 rounded-full text-white transition-all duration-300 cursor-pointer"
          :class="isDisliked
            ? 'bg-[#5A8CCB]'   /* 눌린 상태에 더 진한 색 */
            : 'bg-[#9EC6F3]'   /* 눌리지 않은 상태에 연한 색 */">
          <i class="fas fa-thumbs-down mr-2"></i>
          비추천 {{ dislikeCount }}
        </button>
      </div>
    </div>

    <!-- 댓글 영역 -->
    <div class="bg-white rounded-lg shadow-lg p-6" v-if="post">
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
          v-for="(comment, idx) in comments"
          :key="comment.commentId"
          class="py-4"
          :class="{'border-b border-[#BDDDE4]': idx !== comments.length - 1}"
        >
          <div class="flex items-center justify-between mb-2">
            <div class="flex items-center">
              <i class="fas fa-user-circle text-xl mr-2 text-[#9FB3DF]"></i>
              <span class="font-medium">{{ comment.nickName }}</span>
            </div>
            <span class="text-sm text-gray-500">{{ formatDate(comment.createdAt) }}</span>
          </div>

          <!-- 수정 모드 -->
          <div v-if="editingCommentId === comment.commentId" class="mb-2">
            <textarea
              v-model="editingContent"
              class="w-full p-3 border border-gray-300 rounded-lg resize-none"
              rows="3"
            ></textarea>
            <div class="flex justify-end mt-2 space-x-2">
              <button
                @click="saveEdit(comment.commentId)"
                class="px-4 py-2 bg-[#9FB3DF] text-white rounded hover:bg-blue-600"
              >
                저장
              </button>
              <button
                @click="cancelEdit()"
                class="px-4 py-2 bg-gray-300 text-gray-700 rounded hover:bg-gray-400"
              >
                취소
              </button>
            </div>
          </div>

          <!-- 일반 모드 -->
          <div v-else class="mb-2">
            <p class="text-gray-700">{{ comment.content }}</p>
          </div>

          <!-- 본인 댓글만 수정/삭제 -->
          <div v-if="comment.userId === userId" class="flex space-x-2 text-sm">
            <button
              @click="startEdit(comment)"
              class="text-blue-500 hover:underline"
            >
              수정
            </button>
            <button
              @click="deleteComment(comment.commentId)"
              class="text-red-500 hover:underline"
            >
              삭제
            </button>
          </div>
        </div>
      </div>
      <div v-else class="text-center py-8 text-gray-500">
        아직 댓글이 없습니다. 첫 댓글을 작성해보세요!
      </div>
    </div>

    <!-- 전체화면 이미지 모달 -->
    <div
      v-if="showFullImage"
      class="fixed inset-0 bg-black bg-opacity-90 flex items-center justify-center z-50"
      @click="showFullImage = false"
    >
      <div class="relative max-w-4xl max-h-screen p-4">
        <img
          :src="post.value.imageUrls[0]"
          alt="전체화면 이미지"
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
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'

const route = useRoute()
const freeId = Number(route.params.freeId)

const post = ref<any>(null)
const comments = ref<any[]>([])
const newComment = ref('')
const showFullImage = ref(false)

// 추천/비추천
const isLiked = ref(false)
const isDisliked = ref(false)
const likeCount = ref(0)
const dislikeCount = ref(0)

// 날짜 포맷 함수
function formatDate(iso: string) {
  const d = new Date(iso)
  return d.toISOString().slice(0, 10)
}

// 댓글 편집용
const editingCommentId = ref<number|null>(null)
const editingContent = ref('')

const userId = ref<number | null>(null);

// 게시글 + 댓글 불러오기
onMounted(async () => {
  const token = localStorage.getItem('accessToken')
  const headers = token ? { Authorization: `Bearer ${token}` } : {}

  // 상세 게시글
  const { data: dto } = await axios.get(
    `http://localhost:8080/api/free/${freeId}`,
    { headers }
  )
  post.value = dto
  console.log(post.value.imageUrls[0])
  likeCount.value = dto.likeCount
  dislikeCount.value = dto.dislikeCount ?? 0

  // 댓글 목록
  const { data: cmts } = await axios.get(
    `http://localhost:8080/api/free/${freeId}/comment`,
    { headers }
  )
  comments.value = cmts

  // 추천/비추천 상태
  if (!token) return
  try {
    const { data } = await axios.get(
      'http://localhost:8080/api/users/me',
      { headers: { Authorization: `Bearer ${token}` } }
    )
    userId.value = data.userId
  } catch {
    // 토큰 만료 등 에러 처리
    //localStorage.removeItem('accessToken')
    //nickname.value = null
  }
})

// 댓글 등록
async function addComment() {
  if (!newComment.value.trim()) return;
  if (userId.value == null) {
    alert('로그인이 필요합니다.');
    return;
  }

  const token = localStorage.getItem('accessToken');
  const headers = token ? { Authorization: `Bearer ${token}` } : {};

  // freeId 는 path, userId 는 query parameter 로 전달
  await axios.post(
    `http://localhost:8080/api/free/${freeId}/comment?userId=${userId.value}`,
    { content: newComment.value },   // CommentRequest DTO 에 맞춘 body
    { headers }
  );

  // 등록 후 다시 불러오기
  const { data: cmts } = await axios.get(
    `http://localhost:8080/api/free/${freeId}/comment`,
    { headers }
  );
  comments.value = cmts;
  newComment.value = '';
}


// 수정 시작
function startEdit(comment: any) {
  editingCommentId.value = comment.commentId
  editingContent.value = comment.content
}

// 수정 취소
function cancelEdit() {
  editingCommentId.value = null
  editingContent.value = ''
}

// 수정 저장
async function saveEdit(commentId: number) {
  if (!editingContent.value.trim()) return
  const token = localStorage.getItem('accessToken')
  const headers = token ? { Authorization: `Bearer ${token}` } : {}
  await axios.put(
    `http://localhost:8080/api/free/${freeId}/comment/${commentId}`,
    { content: editingContent.value },
    { headers }
  )
  // 다시 로드
  const { data: cmts } = await axios.get(
    `http://localhost:8080/api/free/${freeId}/comment`, { headers }
  )
  comments.value = cmts
  cancelEdit()
}

// 댓글 삭제
async function deleteComment(commentId: number) {
  if (!confirm('정말 삭제하시겠습니까?')) return
  const token = localStorage.getItem('accessToken')
  const headers = token ? { Authorization: `Bearer ${token}` } : {}
  await axios.delete(
    `http://localhost:8080/api/free/${freeId}/comment/${commentId}`, { headers }
  )
  // 로컬에서 바로 제거
  comments.value = comments.value.filter(c => c.commentId !== commentId)
}



// 공통 반응 전송 메서드
async function sendReaction(type: 'LIKE' | 'DISLIKE') {
  if (userId.value == null) {
    alert('로그인이 필요합니다.')
    return
  }

  const token = localStorage.getItem('accessToken')
  const headers = token ? { Authorization: `Bearer ${token}` } : {}

  const url =
    `http://localhost:8080/api/free/${freeId}/reaction` +
    `?type=${type}` +
    `&userId=${userId.value}`

  const { data } = await axios.post<{
    nowActive: boolean,
    type: 'LIKE'|'DISLIKE',
    likeCount: number,
    dislikeCount: number
  }>(
    url,
    {},   // Body 없음
    { headers }
  )

  // 응답 반영
  likeCount.value     = data.likeCount
  dislikeCount.value  = data.dislikeCount
  if (data.type === 'LIKE') {
    isLiked.value    = data.nowActive
    if (data.nowActive) isDisliked.value = false
  } else {
    isDisliked.value = data.nowActive
    if (data.nowActive)  isLiked.value    = false
  }
}

function toggleLike()    { sendReaction('LIKE') }
function toggleDislike() { sendReaction('DISLIKE') }
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
  