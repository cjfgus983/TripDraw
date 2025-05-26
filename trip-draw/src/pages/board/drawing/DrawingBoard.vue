<template>
  <div class="min-h-screen bg-gray-50">
    <div class="max-w-7xl mx-auto px-4 py-8">
      <!-- 상단: 등록하기 버튼 -->
      <div class="flex justify-end mb-6">
        <button
          @click="goCreate"
          class="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 transition !rounded-button whitespace-nowrap"
        >
          등록하기
        </button>
      </div>

      <!-- 이미지 모달 -->
      <div
        v-if="showImageModal"
        class="fixed inset-0 bg-black bg-opacity-75 flex items-center justify-center z-50"
        @click="closeImageModal"
      >
        <div
          class="relative max-w-3xl w-full mx-4 bg-white rounded-lg overflow-hidden flex flex-col md:flex-row max-h-[80vh] overflow-auto"
          @click.stop
        >
          <!-- 좌: 이미지 -->
          <div class="md:w-1/2 w-full bg-black relative">
            <img
              :src="selectedImage"
              alt="확대된 이미지"
              class="w-full h-full object-contain"
            />
          </div>
          <!-- 우: 메타정보 -->
          <div class="md:w-1/2 w-full p-6 overflow-auto flex flex-col justify-between">
            <div>
              <h3 class="text-2xl font-bold mb-4">{{ selectedArtwork?.title }}</h3>
              <p class="text-gray-600 mb-6 whitespace-pre-line">
                {{ selectedArtwork?.content }}
              </p>
            </div>
            <div class="mt-auto space-y-4">
              <div class="text-gray-600">
                <p class="font-medium">작성자: {{ selectedArtwork?.nickname }}</p>
                <p class="text-sm text-gray-500">{{ formatDate(selectedArtwork?.createdAt) }}</p>
              </div>
              <div class="flex space-x-4">
                <button
                  @click="toggleModalLike"
                  class="flex items-center space-x-2 focus:outline-none !rounded-button whitespace-nowrap"
                >
                  <i
                    :class="[
                      selectedArtwork?.isLiked
                        ? 'fas fa-heart text-red-500'
                        : 'far fa-heart text-gray-400',
                      'text-2xl'
                    ]"
                  ></i>
                  <span class="text-lg font-medium">{{ selectedArtwork?.likeCount }}</span>
                </button>
                <button
                  v-if="selectedArtwork?.userId === currentUserId"
                  @click="deleteArtwork(selectedArtwork.drawingBoardId)"
                  class="flex items-center space-x-2 text-red-600 hover:text-red-800 !rounded-button whitespace-nowrap"
                >
                  <i class="fas fa-trash-alt text-2xl"></i>
                  <span class="text-lg font-medium">삭제</span>
                </button>
              </div>
            </div>
          </div>
          <!-- 닫기 버튼 -->
          <button
            @click="closeImageModal"
            class="absolute top-4 right-4 text-white bg-black bg-opacity-50 rounded-full p-2 hover:bg-opacity-70 transition-all duration-300 !rounded-button whitespace-nowrap"
          >
            <i class="fas fa-times text-xl"></i>
          </button>
        </div>
      </div>

      <!-- 인기 그림 섹션 -->
      <section class="mb-16">
        <h2 class="text-3xl font-bold mb-8 text-center py-2 bg-[#9FB3DF] text-white rounded-lg shadow-md">
          이번 주 인기 작품
        </h2>
        <div class="grid grid-cols-5 gap-6">
          <div
            v-for="item in boardStore.popular"
            :key="item.drawingBoardId"
            class="bg-[#BDDDE4] rounded-lg overflow-hidden shadow-md transition-transform duration-300 hover:scale-105 cursor-pointer relative"
            @click="openImageModal(item.imageUrl, item)"
          >
            <div class="relative h-64 overflow-hidden">
              <img :src="item.imageUrl" class="w-full h-full object-cover object-top" />
              <div class="absolute bottom-2 right-2 bg-white bg-opacity-80 px-2 py-1 rounded-full flex items-center">
                <i :class="[item.isLiked ? 'fas fa-heart text-red-500' : 'far fa-heart text-gray-400', 'mr-1']"></i>
                <span class="text-sm font-medium">{{ item.likeCount }}</span>
              </div>
            </div>
            <button
              v-if="item.userId === currentUserId"
              @click.stop="deleteArtwork(item.drawingBoardId)"
              class="absolute top-2 right-2 bg-white p-2 rounded-full shadow hover:bg-gray-100 cursor-pointer !rounded-button whitespace-nowrap"
            >
              <i class="fas fa-trash-alt text-gray-600"></i>
            </button>
          </div>
        </div>
      </section>

      <!-- 일반 그림 게시판 -->
      <section>
        <h2 class="text-3xl font-bold mb-8 text-center py-2 bg-[#9EC6F3] text-white rounded-lg shadow-md">
          모든 작품
        </h2>
        <div class="grid grid-cols-3 gap-8">
          <div
            v-for="(item, index) in boardStore.artworks"
            :key="item.drawingBoardId"
            class="bg-[#FFF1D5] rounded-lg overflow-hidden shadow-md hover:shadow-lg transition-all duration-300 relative"
          >
            <div class="h-80 overflow-hidden cursor-pointer" @click="openImageModal(item.imageUrl, item)">
              <img :src="item.imageUrl" class="w-full h-full object-cover object-top" />
            </div>
            <div class="p-4 flex justify-between items-center">
              <div>
                <p class="text-gray-700 font-medium">{{ item.title }}</p>
                <p class="text-sm text-gray-500">작성자: {{ item.nickname }}</p>
              </div>
              <div class="flex items-center space-x-2">
                <button
                  @click.stop="onListLike(index)"
                  class="flex items-center space-x-1 focus:outline-none !rounded-button whitespace-nowrap cursor-pointer"
                >
                  <i :class="[item.isLiked ? 'fas fa-heart text-red-500' : 'far fa-heart text-gray-400', 'text-lg']"></i>
                  <span class="text-sm">{{ item.likeCount }}</span>
                </button>
                <button
                  v-if="item.userId === currentUserId"
                  @click.stop="deleteArtwork(item.drawingBoardId)"
                  class="text-red-600 hover:text-red-800 focus:outline-none !rounded-button whitespace-nowrap"
                >
                  <i class="fas fa-trash-alt"></i>
                </button>
              </div>
            </div>
          </div>
        </div>
        <div class="text-center mt-10">
          <button
            @click="loadMore()"
            class="bg-[#9EC6F3] text-white px-6 py-2 rounded-full hover:bg-opacity-90 transition-all duration-300 !rounded-button whitespace-nowrap cursor-pointer"
          >
            더 보기
          </button>
        </div>
      </section>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useDrawingBoardStore } from '@/stores/drawingboard'
import type { BoardItem } from '@/stores/drawingboard'
import { useUserStore } from '@/stores/user'

const router        = useRouter()
const boardStore    = useDrawingBoardStore()
const userStore     = useUserStore()
const currentUserId = computed<number | null>(() => userStore.id ? Number(userStore.id) : null)

// 모달 상태
const showImageModal  = ref(false)
const selectedImage   = ref<string>('')
const selectedArtwork = ref<BoardItem | null>(null)

onMounted(() => {
  boardStore.fetchAll(1)
})

function goCreate() {
  router.push('/drawingregist')
}

function openImageModal(url: string, item: BoardItem) {
  selectedImage.value   = url
  selectedArtwork.value = item
  showImageModal.value  = true
}

function closeImageModal() {
  showImageModal.value  = false
  selectedImage.value   = ''
  selectedArtwork.value = null
}

function loadMore() {
  boardStore.loadMore()
}

function onListLike(idx: number) {
  const item = boardStore.artworks[idx]
  boardStore.toggleLike(item.drawingBoardId)
}

function toggleModalLike() {
  if (!selectedArtwork.value) return
  boardStore.toggleLike(selectedArtwork.value.drawingBoardId)
}

function deleteArtwork(boardId: number) {
  boardStore.deleteArtwork(boardId)
}

function formatDate(dateStr: string | null | undefined): string {
  if (!dateStr) return ''
  return dateStr.replace('T', ' ').substring(0, 16)
}
</script>

<style scoped>
.bg-soft-blue { background-color: #9ec6f3; }
.bg-soft-blue-dark { background-color: #8ab6e3; }
.text-soft-purple { color: #9fb3df; }
.focus\:ring-soft-blue:focus { --tw-ring-color: #9ec6f3; }
input[type="number"]::-webkit-inner-spin-button,
input[type="number"]::-webkit-outer-spin-button {
  -webkit-appearance: none;
  margin: 0;
}
</style>
