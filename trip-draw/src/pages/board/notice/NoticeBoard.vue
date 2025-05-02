<!-- The exported code uses Tailwind CSS. Install Tailwind CSS in your dev environment to ensure all styles work. -->
<template>
    <div class="min-h-screen bg-gray-50 py-8 px-4">
      <div class="max-w-6xl mx-auto bg-white rounded-lg shadow-md p-6">
        <h1 class="text-3xl font-bold text-center text-gray-800 mb-8">
          공지사항
        </h1>
        <!-- 검색 영역 -->
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
            ></i>
            <button
              @click="searchNotices"
              class="absolute right-2 top-1/2 transform -translate-y-1/2 bg-primary text-white px-3 py-1 rounded-button cursor-pointer whitespace-nowrap text-sm"
            >
              검색
            </button>
          </div>
        </div>
        <!-- 공지사항 테이블 -->
        <div class="overflow-x-auto">
          <table class="w-full border-collapse">
            <thead>
              <tr class="bg-primary text-white">
                <th class="py-3 px-4 text-left w-[8%]">번호</th>
                <th class="py-3 px-4 text-left w-[60%]">제목</th>
                <th class="py-3 px-4 text-left w-[20%]">관리자</th>
                <th class="py-3 px-4 text-right w-[12%]">조회수</th>
              </tr>
            </thead>
            <tbody>
              <!-- 중요 공지사항 -->
              <tr
                v-for="notice in importantNotices"
                :key="notice.id"
                class="bg-important font-bold border-b border-gray-200 hover:bg-secondary transition-colors"
              >
                <td class="py-4 px-4 text-left">
                  <i class="fas fa-thumbtack text-primary"></i>
                </td>
                <td class="py-4 px-4 text-left">
                  <a
                    :href="`https://readdy.ai/home/c35feb0c-4e46-4d7a-be66-c91e9b24361e/56faafa6-22e5-4529-9263-87f13e511f01`"
                    data-readdy="true"
                    class="flex items-center hover:underline"
                  >
                    <span class="text-primary font-bold mr-2">[중요]</span>
                    {{ notice.title }}
                  </a>
                </td>
                <td class="py-4 px-4 text-left">{{ notice.admin }}</td>
                <td class="py-4 px-4 text-right">
                  {{ notice.views.toLocaleString() }}
                </td>
              </tr>
              <!-- 일반 공지사항 -->
              <tr
                v-for="(notice, index) in regularNotices"
                :key="notice.id"
                :class="{'bg-gray-50': index % 2 === 0, 'bg-white': index % 2 !== 0, 'hover:bg-hover transition-colors': true}"
                class="border-b border-gray-200"
              >
                <td class="py-4 px-4 text-left">{{ notice.id }}</td>
                <td class="py-4 px-4 text-left">
                  <a
                    :href="`https://readdy.ai/home/c35feb0c-4e46-4d7a-be66-c91e9b24361e/56faafa6-22e5-4529-9263-87f13e511f01`"
                    data-readdy="true"
                    class="hover:underline"
                  >
                    {{ notice.title }}
                  </a>
                </td>
                <td class="py-4 px-4 text-left">{{ notice.admin }}</td>
                <td class="py-4 px-4 text-right">
                  {{ notice.views.toLocaleString() }}
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <!-- 페이지네이션 -->
        <div class="mt-8 flex justify-between items-center">
          <div></div>
          <nav class="flex items-center space-x-1">
            <button
              class="px-3 py-1 rounded-button cursor-pointer whitespace-nowrap bg-gray-100 hover:bg-gray-200 text-gray-600"
            >
              <i class="fas fa-chevron-left"></i>
            </button>
            <button
              v-for="page in 5"
              :key="page"
              :class="[
  'px-3 py-1 rounded-button cursor-pointer whitespace-nowrap',
  page === currentPage ? 'bg-primary text-white' : 'bg-gray-100 hover:bg-gray-200 text-gray-600'
  ]"
            >
              {{ page }}
            </button>
            <button
              class="px-3 py-1 rounded-button cursor-pointer whitespace-nowrap bg-gray-100 hover:bg-gray-200 text-gray-600"
            >
              <i class="fas fa-chevron-right"></i>
            </button>
          </nav>
          <button
            v-if="isAdmin"
            @click="handleRegister"
            class="bg-primary text-white px-4 py-2 rounded-button hover:bg-primary/90 transition-colors flex items-center gap-2"
          >
            <i class="fas fa-plus"></i>
            등록하기
          </button>
        </div>
      </div>
    </div>
  </template>
  
  <script lang="ts" setup>
  import { ref } from "vue";
  // 관리자 여부
  const isAdmin = ref(true); // Set this based on your authentication logic
  // 현재 페이지
  const currentPage = ref(1);
  // 검색어
  const searchQuery = ref("");
  // 중요 공지사항
  const importantNotices = ref([
    {
      id: 1,
      title: "사이트 개편 안내 및 서비스 일시 중단 안내",
      admin: "시스템 관리자",
      views: 1254,
      important: true,
    },
    {
      id: 2,
      title: "2025년 5월 개인정보 처리방침 변경 안내",
      admin: "개인정보 관리자",
      views: 986,
      important: true,
    },
    {
      id: 3,
      title: "서비스 이용약관 개정 안내 (2025.05.15 시행)",
      admin: "법무팀",
      views: 872,
      important: true,
    },
  ]);
  // 일반 공지사항
  const regularNotices = ref([
    {
      id: 25,
      title: "2025년 여름 이벤트 안내",
      admin: "마케팅팀",
      views: 421,
      important: false,
    },
    {
      id: 24,
      title: "모바일 앱 업데이트 안내 (v3.2.1)",
      admin: "개발팀",
      views: 356,
      important: false,
    },
    {
      id: 23,
      title: "고객센터 운영시간 변경 안내",
      admin: "고객지원팀",
      views: 289,
      important: false,
    },
    {
      id: 22,
      title: "신규 서비스 출시 안내",
      admin: "서비스기획팀",
      views: 412,
      important: false,
    },
    {
      id: 21,
      title: "시스템 정기 점검 안내 (5월 10일)",
      admin: "시스템 관리자",
      views: 198,
      important: false,
    },
    {
      id: 20,
      title: "회원 등급 시스템 개편 안내",
      admin: "회원관리팀",
      views: 367,
      important: false,
    },
    {
      id: 19,
      title: "결제 시스템 업데이트 안내",
      admin: "결제시스템팀",
      views: 245,
      important: false,
    },
    {
      id: 18,
      title: "신규 파트너사 협업 안내",
      admin: "제휴마케팅팀",
      views: 178,
      important: false,
    },
  ]);
  // 검색 기능
  const searchNotices = () => {
    // 실제 구현에서는 API 호출 등으로 검색 기능을 구현할 수 있습니다.
    console.log("검색어:", searchQuery.value);
  };
  // 상세 페이지 이동
  const goToDetail = (noticeId: number) => {
    // 실제 구현시에는 라우터를 사용하여 페이지 이동
    console.log(`공지사항 ${noticeId}번 상세페이지로 이동`);
  };
  
  // 등록하기 버튼 클릭 핸들러
  const handleRegister = () => {
    // Implement the registration logic here
    console.log("공지사항 등록 페이지로 이동");
  };
  </script>
  
  <style scoped>
  .bg-primary {
    background-color: #9fb3df;
  }
  .text-primary {
    color: #9fb3df;
  }
  .border-primary {
    border-color: #9fb3df;
  }
  .bg-secondary {
    background-color: #bddde4;
  }
  .bg-important {
    background-color: #faf5e9;
  }
  .bg-hover {
    background-color: #e8f0f8;
  }
  /* 페이지네이션 활성 버튼 스타일 */
  .pagination-active {
    background-color: #9fb3df;
    color: white;
  }
  /* 테이블 행 호버 효과 */
  tbody tr:hover {
    background-color: #e8f0f8;
  }
  /* 중요 공지사항 스타일 */
  .important-notice {
    font-weight: bold;
  }
  </style>
  