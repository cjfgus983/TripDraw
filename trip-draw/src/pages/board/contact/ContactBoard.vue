<!-- The exported code uses Tailwind CSS. Install Tailwind CSS in your dev environment to ensure all styles work. -->
<template>
    <div class="min-h-screen bg-gray-50 py-8 px-4">
      <div class="max-w-6xl mx-auto">
        <div class="mb-8">
          <h1 class="text-3xl font-bold text-gray-800 mb-6">문의사항</h1>
  
          <!-- 검색 및 등록 영역 -->
          <div
            class="flex flex-col sm:flex-row justify-between items-center mb-6 gap-4"
          >
            <div class="relative w-full sm:w-96">
              <input
                type="text"
                v-model="searchQuery"
                placeholder="검색어를 입력하세요"
                class="w-full pl-10 pr-4 py-2 rounded-lg border border-gray-200 focus:outline-none focus:ring-2 focus:ring-blue-300"
              />
              <i
                class="fas fa-search absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"
              ></i>
            </div>
  
            <button
              @click="openPostForm"
              class="!rounded-button whitespace-nowrap bg-[#9FB3DF] hover:bg-[#8CA2CE] text-white px-6 py-2 rounded-lg transition duration-200 cursor-pointer flex items-center"
            >
              <i class="fas fa-pen-to-square mr-2"></i>
              등록하기
            </button>
          </div>
  
          <!-- 게시판 테이블 -->
          <div class="bg-white rounded-lg shadow-sm overflow-hidden">
            <table class="w-full">
              <thead>
                <tr class="bg-gray-50 border-b border-gray-200">
                  <th
                    class="px-4 py-3 text-left text-sm font-medium text-gray-600 w-16"
                  >
                    번호
                  </th>
                  <th
                    class="px-4 py-3 text-left text-sm font-medium text-gray-600"
                  >
                    제목
                  </th>
                  <th
                    class="px-4 py-3 text-left text-sm font-medium text-gray-600 w-32"
                  >
                    작성자
                  </th>
                  <th
                    class="px-4 py-3 text-left text-sm font-medium text-gray-600 w-24"
                  >
                    조회수
                  </th>
                </tr>
              </thead>
              <tbody>
                <tr
                  v-for="post in displayedPosts"
                  :key="post.id"
                  class="border-b border-gray-100 hover:bg-gray-50"
                >
                  <td class="px-4 py-4 text-sm text-gray-600">{{ post.id }}</td>
                  <td class="px-4 py-4">
                    <div
                      @click="handlePostClick(post)"
                      class="cursor-pointer flex items-center"
                    >
                      <div v-if="post.isReply" class="text-[#9FB3DF] mr-2">
                        <i class="fas fa-reply transform rotate-180"></i>
                      </div>
                      <div v-if="post.isPrivate" class="text-[#9FB3DF] mr-2">
                        <i class="fas fa-lock"></i>
                      </div>
                      <span
                        :class="{ 
                          'text-gray-800 font-medium': !post.isPrivate, 
                          'text-gray-500': post.isPrivate,
                          'ml-6': post.isReply
                        }"
                      >
                        {{ post.isPrivate ? '비밀글입니다.' : post.title }}
                      </span>
                      <span
                        v-if="post.hasNewComment"
                        class="ml-2 px-2 py-0.5 bg-[#FFF1D5] text-[#E6A23C] text-xs rounded-full"
                      >
                        New
                      </span>
                    </div>
                  </td>
                  <td class="px-4 py-4 text-sm text-gray-600">
                    {{ post.author }}
                  </td>
                  <td class="px-4 py-4 text-sm text-gray-600">
                    {{ post.views }}
                  </td>
                </tr>
                <tr v-if="displayedPosts.length === 0">
                  <td colspan="4" class="px-4 py-8 text-center text-gray-500">
                    등록된 문의사항이 없습니다.
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
  
          <!-- 페이지네이션 -->
          <div class="flex justify-center mt-6">
            <div class="flex space-x-1">
              <button
                @click="changePage(currentPage - 1)"
                :disabled="currentPage === 1"
                class="!rounded-button whitespace-nowrap px-3 py-1 rounded-md border border-gray-300 cursor-pointer"
                :class="{ 'opacity-50 cursor-not-allowed': currentPage === 1 }"
              >
                <i class="fas fa-chevron-left"></i>
              </button>
  
              <button
                v-for="page in totalPages"
                :key="page"
                @click="changePage(page)"
                class="!rounded-button whitespace-nowrap px-3 py-1 rounded-md cursor-pointer"
                :class="currentPage === page ? 'bg-[#9FB3DF] text-white' : 'border border-gray-300'"
              >
                {{ page }}
              </button>
  
              <button
                @click="changePage(currentPage + 1)"
                :disabled="currentPage === totalPages"
                class="!rounded-button whitespace-nowrap px-3 py-1 rounded-md border border-gray-300 cursor-pointer"
                :class="{ 'opacity-50 cursor-not-allowed': currentPage === totalPages }"
              >
                <i class="fas fa-chevron-right"></i>
              </button>
            </div>
          </div>
        </div>
      </div>
  
      <!-- 비밀글 비밀번호 모달 -->
      <div
        v-if="showPasswordModal"
        class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
      >
        <div class="bg-white rounded-lg p-6 w-full max-w-md">
          <h3 class="text-xl font-bold mb-4">비밀글 확인</h3>
          <p class="mb-4 text-gray-600">
            이 글은 비밀글입니다. 비밀번호를 입력해주세요.
          </p>
  
          <div class="mb-4">
            <input
              type="password"
              v-model="inputPassword"
              placeholder="비밀번호"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-300"
            />
          </div>
  
          <div class="flex justify-end space-x-3">
            <button
              @click="closePasswordModal"
              class="!rounded-button whitespace-nowrap px-4 py-2 border border-gray-300 rounded-lg cursor-pointer"
            >
              취소
            </button>
            <button
              @click="checkPassword"
              class="!rounded-button whitespace-nowrap bg-[#9FB3DF] hover:bg-[#8CA2CE] text-white px-4 py-2 rounded-lg cursor-pointer"
            >
              확인
            </button>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script lang="ts" setup>
  import { ref, computed } from "vue";
  
  interface Post {
    id: number;
    title: string;
    author: string;
    views: number;
    isPrivate: boolean;
    password?: string;
    isReply: boolean;
    hasNewComment: boolean;
  }
  
  // 게시글 데이터
  const posts = ref<Post[]>([
    {
      id: 10,
      title: "상품 배송 관련 문의드립니다",
      author: "김민준",
      views: 45,
      isPrivate: false,
      isReply: false,
      hasNewComment: false,
    },
    {
      id: 9,
      title: "[답변] 상품 배송 관련 문의드립니다",
      author: "관리자",
      views: 32,
      isPrivate: false,
      isReply: true,
      hasNewComment: false,
    },
    {
      id: 8,
      title: "교환 절차에 대해 알고 싶습니다",
      author: "이서연",
      views: 67,
      isPrivate: true,
      password: "1234",
      isReply: false,
      hasNewComment: false,
    },
    {
      id: 7,
      title: "[답변] 교환 절차에 대해 알고 싶습니다",
      author: "관리자",
      views: 54,
      isPrivate: true,
      password: "1234",
      isReply: true,
      hasNewComment: false,
    },
    {
      id: 6,
      title: "결제 오류가 발생했어요",
      author: "박지훈",
      views: 89,
      isPrivate: false,
      isReply: false,
      hasNewComment: true,
    },
    {
      id: 5,
      title: "상품 재입고 문의",
      author: "최수아",
      views: 112,
      isPrivate: false,
      isReply: false,
      hasNewComment: false,
    },
    {
      id: 4,
      title: "[답변] 상품 재입고 문의",
      author: "관리자",
      views: 98,
      isPrivate: false,
      isReply: true,
      hasNewComment: true,
    },
    {
      id: 3,
      title: "회원 정보 변경 방법",
      author: "정도윤",
      views: 76,
      isPrivate: true,
      password: "5678",
      isReply: false,
      hasNewComment: false,
    },
    {
      id: 2,
      title: "쿠폰 사용 기간 연장 가능한가요?",
      author: "한소율",
      views: 134,
      isPrivate: false,
      isReply: false,
      hasNewComment: false,
    },
    {
      id: 1,
      title: "[답변] 쿠폰 사용 기간 연장 가능한가요?",
      author: "관리자",
      views: 121,
      isPrivate: false,
      isReply: true,
      hasNewComment: false,
    },
  ]);
  
  // 페이지네이션
  const currentPage = ref(1);
  const postsPerPage = 10;
  const totalPages = computed(() =>
    Math.ceil(filteredPosts.value.length / postsPerPage),
  );
  
  // 검색 기능
  const searchQuery = ref("");
  const filteredPosts = computed(() => {
    if (!searchQuery.value) return posts.value;
  
    const query = searchQuery.value.toLowerCase();
    return posts.value.filter((post) => {
      // 비밀글은 제목이 '비밀글입니다'로 표시되지만, 실제 제목으로 검색 가능하게 함
      return (
        post.title.toLowerCase().includes(query) ||
        post.author.toLowerCase().includes(query)
      );
    });
  });
  
  // 현재 페이지에 표시할 게시글
  const displayedPosts = computed(() => {
    const startIndex = (currentPage.value - 1) * postsPerPage;
    const endIndex = startIndex + postsPerPage;
    return filteredPosts.value.slice(startIndex, endIndex);
  });
  
  // 페이지 변경
  const changePage = (page: number) => {
    if (page < 1 || page > totalPages.value) return;
    currentPage.value = page;
  };
  
  // 비밀글 관련
  const showPasswordModal = ref(false);
  const inputPassword = ref("");
  const selectedPost = ref<Post | null>(null);
  
  // 게시글 클릭 처리
  const handlePostClick = (post: Post) => {
    if (post.isPrivate) {
      selectedPost.value = post;
      showPasswordModal.value = true;
      inputPassword.value = "";
    } else {
      goToPostDetail(post);
    }
  };
  
  // 비밀번호 확인
  const checkPassword = () => {
    if (
      selectedPost.value &&
      inputPassword.value === selectedPost.value.password
    ) {
      goToPostDetail(selectedPost.value);
      closePasswordModal();
    } else {
      alert("비밀번호가 일치하지 않습니다.");
    }
  };
  
  // 모달 닫기
  const closePasswordModal = () => {
    showPasswordModal.value = false;
    selectedPost.value = null;
    inputPassword.value = "";
  };
  
  // 게시글 상세 페이지로 이동 (실제 구현 시 라우팅 처리)
  const goToPostDetail = (post: Post) => {
    console.log("게시글 상세 페이지로 이동:", post);
    // 실제 구현 시 라우터를 사용하여 상세 페이지로 이동
    // router.push({ name: 'PostDetail', params: { id: post.id } });
  };
  
  // 게시글 등록 폼으로 이동 (실제 구현 시 라우팅 처리)
  const openPostForm = () => {
    console.log("게시글 등록 폼으로 이동");
    // 실제 구현 시 라우터를 사용하여 등록 페이지로 이동
    // router.push({ name: 'PostForm' });
  };
  </script>
  
  <style scoped>
  /* 추가 스타일링 */
  input[type="number"]::-webkit-inner-spin-button,
  input[type="number"]::-webkit-outer-spin-button {
    -webkit-appearance: none;
    margin: 0;
  }
  
  input[type="number"] {
    -moz-appearance: textfield;
  }
  </style>
  