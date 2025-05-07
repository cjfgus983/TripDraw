<!-- The exported code uses Tailwind CSS. Install Tailwind CSS in your dev environment to ensure all styles work. -->
<template>
    <div class="min-h-screen bg-gray-50">
      <div class="max-w-5xl mx-auto px-4 py-8">
        <!-- 상단 네비게이션 -->
        <div class="mb-8">
          <a
            href="https://readdy.ai/home/c35feb0c-4e46-4d7a-be66-c91e9b24361e/fb46eb57-06bb-44c0-b376-1af6e2b92609"
            data-readdy="true"
            class="inline-flex items-center text-gray-600 hover:text-blue-600 transition-colors cursor-pointer !rounded-button whitespace-nowrap"
          >
            <i class="fas fa-arrow-left mr-2"></i>
            목록으로 돌아가기
          </a>
        </div>
        <!-- 공지사항 헤더 -->
        <div class="bg-white rounded-lg shadow-md p-6 mb-6">
          <div class="border-b border-gray-200 pb-5">
            <h1 class="text-2xl font-bold mb-4">
              <span class="text-red-600 font-bold">[중요]</span> 사이트 개편 안내
              및 서비스 일시 중단 안내
            </h1>
            <div class="flex flex-wrap gap-6 text-sm text-gray-600">
              <div class="flex items-center">
                <i class="fas fa-user-circle mr-2"></i>
                <span>작성자: 시스템 관리자</span>
              </div>
              <div class="flex items-center">
                <i class="fas fa-calendar-alt mr-2"></i>
                <span>작성일: 2024.02.15</span>
              </div>
              <div class="flex items-center">
                <i class="fas fa-eye mr-2"></i>
                <span>조회수: 1,254회</span>
              </div>
            </div>
          </div>
          <!-- 공지사항 본문 -->
          <div class="py-6 text-gray-700 leading-relaxed">
            <section class="mb-8">
              <h2 class="text-xl font-bold mb-4">개편 목적 및 개요</h2>
              <p class="mb-4">
                안녕하세요, 고객 여러분. 저희 서비스를 이용해 주셔서 감사합니다.
                더 나은 서비스 제공을 위해 사이트 전반에 걸친 개편 작업을 진행할
                예정입니다. 이번 개편은 사용자 경험 향상과 시스템 안정성 강화를
                목표로 하고 있습니다.
              </p>
              <p>
                개편을 통해 더욱 직관적인 인터페이스와 빠른 응답 속도, 그리고
                새로운 기능들이 추가될 예정입니다. 이에 따라 아래와 같이 일시적인
                서비스 중단이 있을 예정이오니 양해 부탁드립니다.
              </p>
            </section>
            <section class="mb-8">
              <h2 class="text-xl font-bold mb-4">서비스 중단 일정</h2>
              <div class="bg-blue-50 border-l-4 border-blue-500 p-4 mb-4">
                <p class="font-bold">서비스 중단 시간</p>
                <p>
                  2024년 5월 10일(금) 오후 11:00 ~ 2024년 5월 11일(토) 오전 06:00
                  (총 7시간)
                </p>
              </div>
              <p>
                위 시간 동안 모든 서비스 이용이 불가능하며, 작업이 예상보다 일찍
                완료될 경우 서비스가 조기에 재개될 수 있습니다. 작업 진행 상황은
                공식 SNS 채널을 통해 실시간으로 안내해 드릴 예정입니다.
              </p>
            </section>
            <section class="mb-8">
              <h2 class="text-xl font-bold mb-4">영향 받는 서비스 목록</h2>
              <ul class="list-disc pl-5 mb-4 space-y-2">
                <li>웹사이트 및 모바일 웹 서비스 전체</li>
                <li>모바일 애플리케이션 (iOS, Android)</li>
                <li>API 연동 서비스</li>
                <li>회원 관리 시스템</li>
                <li>결제 시스템</li>
              </ul>
              <p>
                서비스 중단 기간 동안 진행 중이던 작업은 자동 저장되지 않을 수
                있으니, 중요한 데이터는 미리 백업해 주시기 바랍니다.
              </p>
            </section>
            <section class="mb-8">
              <h2 class="text-xl font-bold mb-4">개편 후 변경사항</h2>
              <div class="grid md:grid-cols-2 gap-4 mb-4">
                <div class="bg-white border border-gray-200 rounded-lg p-4">
                  <h3 class="font-bold mb-2">UI/UX 개선</h3>
                  <p>
                    더욱 직관적이고 사용하기 쉬운 인터페이스로 개선되며, 모바일
                    환경에서의 사용성이 크게 향상됩니다.
                  </p>
                </div>
                <div class="bg-white border border-gray-200 rounded-lg p-4">
                  <h3 class="font-bold mb-2">성능 최적화</h3>
                  <p>
                    페이지 로딩 속도가 평균 40% 향상되며, 대용량 데이터 처리
                    능력이 강화됩니다.
                  </p>
                </div>
                <div class="bg-white border border-gray-200 rounded-lg p-4">
                  <h3 class="font-bold mb-2">신규 기능 추가</h3>
                  <p>
                    실시간 협업 기능, 고급 분석 도구, 개인화된 대시보드 등 다양한
                    신규 기능이 추가됩니다.
                  </p>
                </div>
                <div class="bg-white border border-gray-200 rounded-lg p-4">
                  <h3 class="font-bold mb-2">보안 강화</h3>
                  <p>
                    최신 보안 프로토콜 적용으로 더욱 안전한 서비스 이용이
                    가능해집니다.
                  </p>
                </div>
              </div>
              <p>
                자세한 변경사항은 개편 완료 후 별도의 안내 공지를 통해 상세히
                안내드릴 예정입니다.
              </p>
            </section>
            <section class="mb-4">
              <h2 class="text-xl font-bold mb-4">사용자 유의사항</h2>
              <div class="bg-yellow-50 border-l-4 border-yellow-500 p-4 mb-4">
                <p class="font-bold mb-2">서비스 이용 전 확인사항</p>
                <ul class="list-disc pl-5 space-y-1">
                  <li>
                    개편 후 첫 접속 시 캐시 삭제 및 새로고침이 필요할 수 있습니다.
                  </li>
                  <li>모바일 앱은 최신 버전으로 업데이트가 필요합니다.</li>
                  <li>
                    일부 기존 URL이 변경될 수 있으니 북마크는 개편 후 다시 설정해
                    주세요.
                  </li>
                </ul>
              </div>
              <p>
                개편 후 서비스 이용에 어려움이 있으시거나 문의사항이 있으신 경우,
                고객센터(1588-0000)로 연락 주시거나 홈페이지 내 문의하기를 통해
                문의해 주시기 바랍니다. 24시간 내에 답변 드리겠습니다.
              </p>
            </section>
            <div class="mt-8 pt-6 border-t border-gray-200">
              <p class="text-gray-600">
                저희 서비스를 이용해 주시는 고객 여러분께 다시 한번 감사드리며, 더
                나은 서비스로 보답하겠습니다.
              </p>
              <p class="font-bold mt-2">감사합니다.</p>
            </div>
          </div>
        </div>
        <!-- 관리자 기능 -->
        <div v-if="isAdmin" class="flex justify-end space-x-4 mt-6">
          <button
            class="px-6 py-2 bg-blue-300 text-white rounded-md hover:bg-blue-400 transition-colors cursor-pointer !rounded-button whitespace-nowrap"
          >
            <i class="fas fa-edit mr-2"></i>수정
          </button>
          <button
            class="px-6 py-2 bg-red-300 text-white rounded-md hover:bg-red-400 transition-colors cursor-pointer !rounded-button whitespace-nowrap"
          >
            <i class="fas fa-trash-alt mr-2"></i>삭제
          </button>
        </div>
      </div>
    </div>
  </template>
  
  <script lang="ts" setup>
  import { ref } from "vue";
  // 관리자 권한 여부 (실제로는 로그인 상태와 권한에 따라 결정됨)
  const isAdmin = ref(true);
  </script>
  
  <style scoped>
  .notice-content p {
    margin-bottom: 1rem;
  }
  </style>
  