<template>
  <div class="min-h-screen bg-gray-50 py-8 px-4">
    <div class="max-w-5xl mx-auto bg-white rounded-lg shadow-md p-6">
      <!-- 상단 네비게이션 -->
      <div class="mb-8">
        <button @click="$router.back()"
                class="inline-flex items-center text-gray-600 hover:text-blue-600 transition-colors rounded-button whitespace-nowrap">
          <i class="fas fa-arrow-left mr-2"></i>
          목록으로 돌아가기
        </button>
      </div>

      <!-- 공지사항 헤더 -->
      <div v-if="notice" class="bg-white rounded-lg shadow-md p-6 mb-6">
        <div class="border-b border-gray-200 pb-5 flex justify-between items-start">
          <div>
            <h1 class="text-2xl font-bold mb-2">
              <span v-if="notice.isPinned" class="text-red-600 font-bold mr-2">[중요]</span>
              <!-- 뷰 모드 -->
              <span v-if="!isEditing">{{ notice.title }}</span>
              <!-- 편집 모드 -->
              <input
                v-else
                v-model="editTitle"
                class="w-full px-3 py-1 border rounded"
                placeholder="제목을 입력하세요"
              />
            </h1>
            <div class="flex flex-wrap gap-6 text-sm text-gray-600">
              <div class="flex items-center">
                <i class="fas fa-user-circle mr-2"></i>
                <span>작성자: {{ notice.nickname }}</span>
              </div>
              <div class="flex items-center">
                <i class="fas fa-calendar-alt mr-2"></i>
                <span>작성일: {{ formatDate(notice.createdAt) }}</span>
              </div>
              <div class="flex items-center">
                <i class="fas fa-eye mr-2"></i>
                <span>조회수: {{ notice.viewCount.toLocaleString() }}회</span>
              </div>
            </div>
          </div>

          <!-- 관리자 기능: 수정/저장/취소/삭제 버튼 -->
      <div v-if="isAdmin" class="flex items-center space-x-2">
        <!-- 뷰 모드 -->
        <button
          v-if="!isEditing"
          @click="goToEdit"
          class="px-4 py-2 bg-blue-300 text-white rounded hover:bg-blue-400 transition-colors"
        >
          <i class="fas fa-edit mr-1"></i>수정
        </button>
        <button
          v-if="!isEditing"
          @click="deleteNotice"
          class="px-4 py-2 bg-red-400 text-white rounded hover:bg-red-500 transition-colors"
        >
          <i class="fas fa-trash mr-1"></i>삭제
        </button>

        <!-- 편집 모드 -->
        <button
          v-if="isEditing"
          @click="saveEdit"
          class="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600 transition-colors"
        >
          저장
        </button>
        <button
          v-if="isEditing"
          @click="cancelEdit"
          class="px-4 py-2 bg-gray-300 rounded hover:bg-gray-400 transition-colors"
        >
          취소
        </button>
      </div>
    </div>

        <!-- 공지사항 본문 -->
        <div class="notice-content text-gray-700 leading-relaxed mt-4">
          <!-- 뷰 모드 -->
           <div v-if="!isEditing" v-html="notice.content"></div>
          <!-- 편집 모드 -->
          <div
            v-else
            ref="editRef"
            contenteditable="true"
            class="w-full min-h-[200px] p-4 border rounded"
            @input="onEditInput"
            v-html="editContent"
          ></div>
        </div>
      </div>

      <div v-else class="text-center py-16 text-gray-500">
        로딩 중...
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'
import { useRoute, useRouter } from 'vue-router'

// 인터페이스
interface NoticeDto {
  noticeId:   number
  userId:     number
  nickname:   string
  title:      string
  content:    string    // stored raw HTML or markdown
  createdAt:  string    // ISO 문자열
  viewCount:  number
  isPinned:   boolean
}

// 라우터, 토큰
const route = useRoute()
const router = useRouter()
const noticeId = Number(route.params.noticeId)
const token    = localStorage.getItem('accessToken')

// 공지와 편집 상태
const notice      = ref<NoticeDto|null>(null)
const isEditing   = ref(false)
const editTitle   = ref('')
const editContent = ref('')
const editRef     = ref<HTMLElement|null>(null)

// 사용자 정보
const userId   = ref<number|null>(null)
const nickname = ref<string>('')
const role     = ref<string>('')

// ADMIN 여부
const isAdmin = computed(() => role.value === 'ADMIN')

function onEditInput(e: Event) {
  const html = (e.target as HTMLElement).innerHTML;
  editContent.value = html;
}
// 컴포넌트 마운트 시
onMounted(async () => {
  if (!token) return

  try {
    // 1) 내 정보 (userId, nickname, role) 가져오기
    const me = await axios.get<{
      userId: number,
      nickname: string,
      role: string
    }>(
      '/test/api/users/me',
      { headers: { Authorization: `Bearer ${token}` } }
    )
    userId.value   = me.data.userId
    nickname.value = me.data.nickname
    role.value     = me.data.role

    // 2) 조회수 1 증가 (PATCH 엔드포인트 추가 필요)
    // await axios.patch(
    //   `/test/api/notice/${noticeId}/views`,
    //   null,
    //   { headers: { Authorization: `Bearer ${token}` } }
    // )

    // 3) 공지 상세 가져오기
    const { data } = await axios.get<NoticeDto>(
      `/test/api/notice/${noticeId}`,
      { headers: { Authorization: `Bearer ${token}` } }
    )
    notice.value = data
  } catch (err) {
    console.error('공지 상세 로드 실패', err)
    alert('공지사항을 불러오지 못했습니다.')
  }
})

// 날짜 포맷 헬퍼 (YYYY.MM.DD)
function formatDate(iso: string) {
  const d  = new Date(iso)
  const yy = d.getFullYear()
  const mm = String(d.getMonth()+1).padStart(2,'0')
  const dd = String(d.getDate()).padStart(2,'0')
  return `${yy}.${mm}.${dd}`
}

// “수정” 눌렀을 때 편집 모드로 전환
function goToEdit() {
  if (!notice.value) return
  isEditing.value   = true
  editTitle.value   = notice.value.title
  editContent.value = notice.value.content
  // 포커스 주기
  setTimeout(() => editRef.value?.focus(), 0)
}

// “취소” 눌렀을 때 원복
function cancelEdit() {
  isEditing.value = false
}

// “저장” 눌렀을 때 API 호출
async function saveEdit() {
  if (!notice.value) return
  try {
    await axios.put(
      `/test/api/notice/${noticeId}`,
      {
        title:       editTitle.value,
        content:     editContent.value,
      },
      { headers: { Authorization: `Bearer ${token}` } }
    )
    // 업데이트 후 다시 상세 조회
    const { data } = await axios.get<NoticeDto>(
      `/test/api/notice/${noticeId}`,
      { headers: { Authorization: `Bearer ${token}` } }
    )
    notice.value    = data
    isEditing.value = false
    alert('수정되었습니다.')
  } catch (e) {
    console.error('수정 실패', e)
    alert('수정에 실패했습니다.')
  }
}
async function deleteNotice() {
  if (!confirm('정말 삭제하시겠습니까?')) return
  if (!token) return
  try {
    await axios.delete(
      `/api/notice/${noticeId}`,
      { headers:{ Authorization:`Bearer ${token}` } }
    )
    alert('삭제되었습니다.')
    router.push({ name: 'NoticeBoard' })
  } catch (e) {
    console.error(e)
    alert('삭제에 실패했습니다.')
  }
}
</script>

<style scoped>
.notice-content p {
  margin-bottom: 1rem;
}
/* Tailwind 커스텀 색상 */
.border-gray-200 { border-color: #e5e7eb; }
.text-gray-500   { color: #6b7280; }
.text-gray-600   { color: #4b5563; }
.text-gray-700   { color: #374151; }
.bg-gray-50      { background-color: #f9fafb; }
.bg-white        { background-color: #ffffff; }
.shadow-md       { box-shadow: 0 4px 6px rgba(0,0,0,0.1); }
.rounded-lg      { border-radius: 0.5rem; }
.p-6             { padding: 1.5rem; }
.mb-6            { margin-bottom: 1.5rem; }
.mb-8            { margin-bottom: 2rem; }
input {
  outline: none;
}
</style>
