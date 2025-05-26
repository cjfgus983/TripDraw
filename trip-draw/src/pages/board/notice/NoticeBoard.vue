<template>
  <div class="min-h-screen bg-gray-50 py-8 px-4">
    <div class="max-w-6xl mx-auto bg-white rounded-lg shadow-md p-6">
      <h1 class="text-3xl font-bold text-center text-gray-800 mb-8">공지사항</h1>

      <!-- 검색 -->
      <div class="mb-8 flex justify-end">
        <div class="relative w-80">
          <input
            type="text"
            v-model="searchQuery"
            placeholder="검색어를 입력하세요"
            class="w-full pl-10 pr-4 py-2 border border-primary rounded-lg focus:outline-none focus:ring-2 focus:ring-primary/50"
          />
          <i
            class="fas fa-search absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"
          />
          <button
            @click="searchNotices"
            class="absolute right-2 top-1/2 transform -translate-y-1/2 bg-primary text-white px-3 py-1 rounded-button whitespace-nowrap text-sm"
          >
            검색
          </button>
        </div>
      </div>

      <!-- 테이블 -->
      <div class="overflow-x-auto">
        <table class="w-full border-collapse">
          <thead>
            <tr class="bg-primary text-white">
              <th class="py-3 px-4 text-left w-[8%]">번호</th>
              <th class="py-3 px-4 text-left w-[60%]">제목</th>
              <th class="py-3 px-4 text-left w-[20%]">작성자</th>
              <th class="py-3 px-4 text-right w-[12%]">조회수</th>
            </tr>
          </thead>
          <tbody>
            <!-- 중요 공지 -->
            <tr
              v-for="notice in pinnedNotices"
              :key="notice.noticeId"
              class="bg-important font-bold border-b border-gray-200 hover:bg-secondary transition-colors cursor-pointer"
              @click="goToDetail(notice.noticeId)"
            >
              <td class="py-4 px-4 text-left">
                <i class="fas fa-thumbtack text-primary"></i>
              </td>
              <td class="py-4 px-4 text-left">
                <span class="text-primary font-bold mr-2">[중요]</span>
                {{ notice.title }}
              </td>
              <td class="py-4 px-4 text-left">
                {{ notice.nickname }}
              </td>
              <td class="py-4 px-4 text-right">
                {{ notice.viewCount.toLocaleString() }}
              </td>
            </tr>

            <!-- 일반 공지 -->
            <tr
              v-for="(notice, idx) in regularNotices"
              :key="notice.noticeId"
              :class="[
                idx % 2 === 0 ? 'bg-gray-50' : 'bg-white',
                'border-b border-gray-200 hover:bg-hover transition-colors cursor-pointer'
              ]"
              @click="goToDetail(notice.noticeId)"
            >
              <td class="py-4 px-4 text-left">{{ notice.noticeId }}</td>
              <td class="py-4 px-4 text-left">{{ notice.title }}</td>
              <td class="py-4 px-4 text-left">{{ notice.nickname }}</td>
              <td class="py-4 px-4 text-right">{{ notice.viewCount.toLocaleString() }}</td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- 페이지네이션 & 등록 버튼 -->
      <div class="mt-8 flex justify-between items-center">
        <nav class="flex items-center space-x-1">
          <button
            class="px-3 py-1 rounded-button bg-gray-100 hover:bg-gray-200 text-gray-600"
            @click="prevPage"
            :disabled="currentPage===1"
          ><i class="fas fa-chevron-left"></i></button>
          <button
            v-for="page in totalPages"
            :key="page"
            @click="currentPage=page"
            :class="[
              'px-3 py-1 rounded-button whitespace-nowrap cursor-pointer',
              currentPage===page
                ? 'bg-primary text-white'
                : 'bg-gray-100 hover:bg-gray-200 text-gray-600'
            ]"
          >{{ page }}</button>
          <button
            class="px-3 py-1 rounded-button bg-gray-100 hover:bg-gray-200 text-gray-600"
            @click="nextPage"
            :disabled="currentPage===totalPages"
          ><i class="fas fa-chevron-right"></i></button>
        </nav>
        <button
          v-if="isAdmin"
          @click="handleRegister"
          class="bg-primary text-white px-4 py-2 rounded-button hover:bg-primary/90 transition-colors flex items-center gap-2"
        >
          <i class="fas fa-plus"></i> 등록하기
        </button>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

interface NoticeDto {
  noticeId:    number
  userId:      number
  title:       string
  content:     string
  createdAt:   string
  viewCount:   number
  isPinned:    boolean
  nickname:    string // 작성자 닉네임
}

const router = useRouter()

// 사용자 정보
const userId   = ref<number|null>(null)
const nickname = ref<string>('')
const role     = ref<string>('')
// "ADMIN" 권한 여부
const isAdmin = computed(() => role.value === 'ADMIN')
const searchQuery = ref('')
const currentPage = ref(1)
const pageSize    = 10

// 전체 공지 스토어
const notices = ref<NoticeDto[]>([])

// 로그인 토큰
const token = localStorage.getItem('accessToken')

// API 호출: 전체 공지
async function fetchNotices() {
  try {
    const { data } = await axios.get<NoticeDto[]>('/api/notice')
    notices.value = data
  } catch (err) {
    console.error('공지 로드 실패', err)
    alert('공지사항을 불러오는 데 실패했습니다.')
  }
}

// 검색 (클라이언트 필터)
function searchNotices() {
  currentPage.value = 1
}

// 중요/일반 공지 필터
const filtered = computed(() =>
  notices.value.filter(n =>
    !searchQuery.value ||
    n.title.includes(searchQuery.value) ||
    n.content.includes(searchQuery.value)
  )
)
const pinnedNotices  = computed(() => filtered.value.filter(n => n.isPinned))
const regularNotices = computed(() => filtered.value.filter(n => !n.isPinned))

// 페이징 계산
const totalPages = computed(() =>
  Math.ceil(regularNotices.value.length / pageSize)
)
function prevPage() { if (currentPage.value>1) currentPage.value-- }
function nextPage() { if (currentPage.value<totalPages.value) currentPage.value++ }

// 상세 페이지 이동
function goToDetail(id: number) {
  router.push({ name: 'NoticeDetail', params: { noticeId: id } })
}

// 등록 페이지 이동
function handleRegister() {
  router.push({ name: 'NoticeRegist' })
}

// 컴포넌트 로드 시
onMounted(async () => {
  if (!token) return

  try {
    // 1) 내 정보 받아오기 (userId, nickname, role)
    const me = await axios.get<{
      userId: number,
      nickname: string,
      role: string
    }>(
      '/api/users/me',
      { headers: { Authorization: `Bearer ${token}` } }
    )
    userId.value   = me.data.userId
    nickname.value = me.data.nickname
    role.value     = me.data.role
    await fetchNotices()
  } catch (err) {
    console.error('사용자 정보 로드 실패', err)
    alert('사용자 정보를 불러오는 데 실패했습니다.')
  }
})
</script>

<style scoped>
.bg-primary { background-color: #9fb3df }
.text-primary { color: #9fb3df }
.border-primary { border-color: #9fb3df }
.bg-secondary { background-color: #bddde4 }
.bg-important { background-color: #faf5e9 }
.bg-hover { background-color: #e8f0f8 }
</style>
