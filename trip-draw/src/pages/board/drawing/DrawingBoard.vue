<template>
  <div class="min-h-screen bg-gray-50">
    <!-- 메인 컨테이너 -->
    <div class="max-w-7xl mx-auto px-4 py-8">
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
                <p class="text-sm text-gray-500">{{ selectedArtwork?.createdAt }}</p>
              </div>
              <div class="flex space-x-4">
                <button
                  @click="toggleModalLike"
                  :class="['flex items-center space-x-2 focus:outline-none !rounded-button whitespace-nowrap', selectedArtwork?.isLiked ? 'opacity-50 cursor-not-allowed' : '']"
                  :disabled="selectedArtwork?.isLiked"
                >
                  <i
                    class="fas fa-heart text-2xl"
                    :class="selectedArtwork?.isLiked ? 'text-red-500' : 'text-gray-400'"
                  ></i>
                  <span class="text-lg font-medium">{{ selectedArtwork?.likeCount || 0 }}</span>
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
        <h2
          class="text-3xl font-bold mb-8 text-center py-2 bg-[#9FB3DF] text-white rounded-lg shadow-md"
        >
          이번 주 인기 작품
        </h2>
        <div class="grid grid-cols-5 gap-6">
          <div
            v-for="(item, idx) in popularArtworks"
            :key="item.drawingBoardId"
            class="bg-[#BDDDE4] rounded-lg overflow-hidden shadow-md transition-transform duration-300 hover:scale-105 cursor-pointer relative"
            @click="openImageModal(item.imageUrl, item)"
          >
            <div class="relative h-64 overflow-hidden">
              <img
                :src="item.imageUrl"
                class="w-full h-full object-cover object-top"
              />
              <div
                class="absolute bottom-2 right-2 bg-white bg-opacity-80 px-2 py-1 rounded-full flex items-center"
              >
                <i class="fas fa-heart text-red-500 mr-1"></i>
                <span class="text-sm font-medium">{{ item.likeCount }}</span>
              </div>
            </div>
            <button
              v-if="item.userId === currentUserId"
              @click.stop="deleteArtwork(item.drawingBoardId, idx, 'popular')"
              class="absolute top-2 right-2 bg-white p-2 rounded-full shadow hover:bg-gray-100 cursor-pointer !rounded-button whitespace-nowrap"
            >
              <i class="fas fa-trash-alt text-gray-600"></i>
            </button>
          </div>
        </div>
      </section>

      <!-- 일반 그림 게시판 -->
      <section>
        <h2
          class="text-3xl font-bold mb-8 text-center py-2 bg-[#9EC6F3] text-white rounded-lg shadow-md"
        >
          모든 작품
        </h2>
        <div class="grid grid-cols-3 gap-8">
          <div
            v-for="(item, index) in artworks"
            :key="item.drawingBoardId"
            class="bg-[#FFF1D5] rounded-lg overflow-hidden shadow-md hover:shadow-lg transition-all duration-300 relative"
          >
            <div
              class="h-80 overflow-hidden cursor-pointer"
              @click="openImageModal(item.imageUrl, item)"
            >
              <img
                :src="item.imageUrl"
                class="w-full h-full object-cover object-top"
              />
            </div>
            <div class="p-4 flex justify-between items-center">
              <div>
                <p class="text-gray-700 font-medium">{{ item.title }}</p>
                <p class="text-sm text-gray-500">작성자: {{ item.nickname }}</p>
              </div>
              <div class="flex items-center space-x-2">
                <button
                  @click.stop="onListLike(index)"
                  :class="['flex items-center space-x-1 focus:outline-none !rounded-button whitespace-nowrap cursor-pointer', item.isLiked ? 'opacity-50 cursor-not-allowed' : '']"
                  :disabled="item.isLiked"
                >
                  <i
                    class="fas fa-heart text-lg"
                    :class="item.isLiked ? 'text-red-500' : 'text-gray-400'"
                  ></i>
                  <span class="text-sm">{{ item.likeCount }}</span>
                </button>
                <button
                  v-if="item.userId === currentUserId"
                  @click.stop="deleteArtwork(item.drawingBoardId, index)"
                  class="text-red-600 hover:text-red-800 focus:outline-none !rounded-button whitespace-nowrap"
                >
                  <i class="fas fa-trash-alt"></i>
                </button>
              </div>
            </div>
          </div>
        </div>
        <!-- 더 보기 버튼 -->
        <div class="text-center mt-10">
          <button
            @click="loadMore()"
            class="bg-[#9EC6F3] text-white px-6 py-2 rounded-full hover:bg-opacity-90 transition-all duration-300 !rounded-button whitespace-nowrap cursor-pointer"
          >
            더 보기
          </button>
        </div>
      </section>

      <!-- 그림 업로드 플로팅 버튼 -->
      <button
        @click="openUploadModal"
        class="fixed bottom-8 right-8 bg-[#9FB3DF] text-white flex items-center justify-center p-4 rounded-full shadow-lg hover:scale-110 transition-transform duration-300 !rounded-button whitespace-nowrap cursor-pointer"
      >
        <i class="fas fa-plus mr-2"></i>
        <span>그림 올리기</span>
      </button>

      <!-- 업로드 모달 (기존 로직 유지) -->
      <div
        v-if="showUploadModal"
        class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
      >
        <div class="bg-white rounded-lg p-6 w-full max-w-lg">
          <!-- ... 업로드 폼 생략 ... -->
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, computed } from "vue";
import axios from "axios";
import { useUserStore } from "@/stores/user";

interface BoardItem {
  drawingBoardId: number;
  drawingId:      number;
  userId:         number;
  nickname:       string;
  title:          string;
  content:        string | null;
  viewCount:      number;
  likeCount:      number;
  createdAt:      string;
  imageUrl:       string;
  isLiked?:       boolean;
}

// Pinia 스토어에서 로그인된 유저 정보
const userStore = useUserStore();
const currentUserId = computed<number | null>(() => userStore.id ? Number(userStore.id) : null);

const popularArtworks = ref<BoardItem[]>([]);
const artworks        = ref<BoardItem[]>([]);

// 이미지 모달
const showImageModal  = ref(false);
const selectedImage   = ref("");
const selectedArtwork = ref<BoardItem|null>(null);

// 업로드 모달
const showUploadModal = ref(false);
const fileInput       = ref<HTMLInputElement|null>(null);
const previewImage    = ref("");
const newArtwork      = ref({ title: "", likes: 0, isLiked: false });

const PAGE_SIZE = 9;

onMounted(async () => {
  try {
    const popRes = await axios.get<BoardItem[]>("/api/drawingboard/popular");
    popularArtworks.value = popRes.data.map(i => ({ ...i, isLiked: i.isLiked ?? false }));

    const allRes = await axios.get<BoardItem[]>(`/api/drawingboard?page=1`);
    artworks.value = allRes.data.map(i => ({ ...i, isLiked: i.isLiked ?? false }));
  } catch (e) {
    console.error("게시판 불러오기 실패", e);
  }
});

function openImageModal(url: string, item?: BoardItem) {
  selectedImage.value   = url;
  selectedArtwork.value = item || null;
  showImageModal.value  = true;
}
function closeImageModal() {
  showImageModal.value  = false;
  selectedImage.value   = "";
  selectedArtwork.value = null;
}

async function loadMore() {
  const nextPage = Math.floor(artworks.value.length / PAGE_SIZE) + 1;
  const { data } = await axios.get<BoardItem[]>(`/api/drawingboard?page=${nextPage}`);
  artworks.value.push(...data.map(i => ({ ...i, isLiked: i.isLiked ?? false })));
}

async function toggleLike(boardId: number, localRef: BoardItem) {
  try {
    if (!localRef.isLiked) {
      await axios.post(`/api/drawingboard/${boardId}/like`);
      localRef.isLiked = true;
      localRef.likeCount += 1;
    } else {
      await axios.delete(`/api/drawingboard/${boardId}/like`);
      localRef.isLiked = false;
      localRef.likeCount -= 1;
    }
  } catch (e) {
    console.error('좋아요 처리 실패', e);
  }
}

function onListLike(idx: number) {
  const art = artworks.value[idx];
  toggleLike(art.drawingBoardId, art);
}

async function toggleModalLike() {
  if (!selectedArtwork.value) return;
  toggleLike(selectedArtwork.value.drawingBoardId, selectedArtwork.value);
}

function openUploadModal() {
  showUploadModal.value = true;
  newArtwork.value      = { title: "", likes: 0, isLiked: false };
  previewImage.value    = "";
}
function triggerFileInput() { fileInput.value?.click(); }
function handleFileUpload(e: Event) {
  const files = (e.target as HTMLInputElement).files;
  if (files && files[0]) previewImage.value = URL.createObjectURL(files[0]);
}
function removeImage() {
  previewImage.value = "";
  if (fileInput.value) fileInput.value.value = "";
}
function uploadArtwork() {
  if (newArtwork.value.title && previewImage.value) {
    artworks.value.unshift({
      drawingBoardId: Date.now(),
      drawingId:      0,
      userId:         currentUserId.value || 0,
      nickname:       userStore.nickname || '',
      title:          newArtwork.value.title,
      content:        null,
      viewCount:      0,
      likeCount:      0,
      createdAt:      new Date().toISOString(),
      imageUrl:       previewImage.value,
      isLiked:        false,
    });
    showUploadModal.value = false;
    newArtwork.value      = { title: "", likes: 0, isLiked: false };
    previewImage.value    = "";
  }
}

async function deleteArtwork(boardId: number, idx?: number, section?: 'popular' | 'all') {
  try {
    await axios.delete(`/api/drawingboard/${boardId}`);
    if (section === 'popular') {
      popularArtworks.value.splice(idx!, 1);
    } else {
      artworks.value.splice(idx!, 1);
    }
    closeImageModal();
  } catch (e) {
    console.error('삭제 실패', e);
  }
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
