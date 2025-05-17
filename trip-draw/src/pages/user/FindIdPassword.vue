<!-- The exported code uses Tailwind CSS. Install Tailwind CSS in your dev environment to ensure all styles work. -->
<template>
  <div
    class="min-h-screen flex flex-col items-center justify-center bg-gradient-to-b from-blue-50 to-white py-12 px-4"
  >
    <div class="w-full max-w-md bg-white rounded-xl shadow-lg overflow-hidden">
      <!-- 탭 네비게이션 -->
      <div class="flex w-full">
        <button
          @click="activeTab = 'findId'"
          :class="{
'bg-[#FFF1D5]': activeTab === 'findId',
'bg-[#9FB3DF] bg-opacity-70': activeTab !== 'findId'
}"
          class="flex-1 py-4 text-[#333333] font-medium text-center transition-all duration-300 cursor-pointer !rounded-button whitespace-nowrap"
        >
          아이디 찾기
        </button>
        <button
          @click="activeTab = 'findPassword'"
          :class="{
'bg-[#FFF1D5]': activeTab === 'findPassword',
'bg-[#9FB3DF] bg-opacity-70': activeTab !== 'findPassword'
}"
          class="flex-1 py-4 text-[#333333] font-medium text-center transition-all duration-300 cursor-pointer !rounded-button whitespace-nowrap"
        >
          비밀번호 찾기
        </button>
      </div>
      <!-- 아이디 찾기 폼 -->
      <div v-if="activeTab === 'findId'" class="p-8">
        <h2 class="text-2xl font-bold text-center text-gray-800 mb-8">
          아이디 찾기
        </h2>
        <div class="space-y-6">
          <div class="space-y-2">
            <label for="name" class="block text-sm font-medium text-gray-700"
              >이름</label
            >
            <input
              type="text"
              id="name"
              v-model="findIdForm.name"
              class="w-full px-4 py-3 border border-[#BDDDE4] rounded-lg focus:outline-none focus:ring-2 focus:ring-[#9FB3DF] transition-all"
              placeholder="이름을 입력해주세요"
            />
          </div>
          <div class="space-y-2">
            <label for="phoneNumber" class="block text-sm font-medium text-gray-700"
              >전화번호</label
            >
            <input
              type="text"
              id="phoneNumber"
              v-model="findIdForm.phoneNumber"
              @input="onPhoneInput"
              class="w-full px-4 py-3 border border-[#BDDDE4] rounded-lg focus:outline-none focus:ring-2 focus:ring-[#9FB3DF] transition-all"
              placeholder="전화번호를 입력해주세요"
            />
          </div>
          <div class="space-y-2">
            <label for="email" class="block text-sm font-medium text-gray-700"
              >이메일</label
            >
            <div class="flex space-x-2">
              <input
                type="email"
                id="email"
                v-model="findIdForm.email"
                class="flex-1 px-4 py-3 border border-[#BDDDE4] rounded-lg focus:outline-none focus:ring-2 focus:ring-[#9FB3DF] transition-all"
                placeholder="인증번호를 받을 이메일"
              />
              <button
                @click="sendVerificationCode"
                class="px-4 py-3 bg-[#9EC6F3] text-white font-medium rounded-lg hover:bg-[#8ab6e3] transition-colors cursor-pointer !rounded-button whitespace-nowrap"
              >
                인증번호 받기
              </button>
            </div>
          </div>
          <div v-if="isSendingCode" class="text-sm text-gray-500 mt-2">
              인증번호가 도착할 때까지 잠시 기다려주세요.
          </div>
          <div v-if="showVerificationField" class="space-y-2">
            <label
              for="verification"
              class="block text-sm font-medium text-gray-700"
              >인증번호</label
            >
            <input
              type="text"
              id="verification"
              v-model="findIdForm.verificationCode"
              class="w-full px-4 py-3 border border-[#BDDDE4] rounded-lg focus:outline-none focus:ring-2 focus:ring-[#9FB3DF] transition-all"
              placeholder="인증번호 6자리를 입력해주세요"
            />
          </div>
          <button
            @click="findId"
            class="w-full py-4 bg-[#9FB3DF] text-white font-medium rounded-lg hover:bg-[#8da2cc] transition-colors cursor-pointer !rounded-button whitespace-nowrap"
          >
            아이디 찾기
          </button>
        </div>
      </div>
      <!-- 비밀번호 찾기 폼 -->
      <div v-if="activeTab === 'findPassword'" class="p-8">
        <h2 class="text-2xl font-bold text-center text-gray-800 mb-8">
          비밀번호 찾기
        </h2>
        <div class="space-y-6">
          <div class="space-y-2">
            <label for="userId" class="block text-sm font-medium text-gray-700"
              >아이디(이메일)</label
            >
            <input
              type="email"
              id="userId"
              v-model="findPasswordForm.email"
              class="w-full px-4 py-3 border border-[#BDDDE4] rounded-lg focus:outline-none focus:ring-2 focus:ring-[#9FB3DF] transition-all"
              placeholder="가입한 이메일을 입력해주세요"
            />
          </div>
          <!-- 인증번호 입력 필드 제거 -->
          <button
            @click="immediatePasswordReset"
            class="w-full py-4 bg-[#9EC6F3] text-white font-medium rounded-lg hover:bg-[#8ab6e3] transition-colors cursor-pointer !rounded-button whitespace-nowrap"
          >
            비밀번호 찾기
          </button>
        </div>
      </div>
    </div>
    <!-- 결과 모달 -->
    <div
      v-if="showResultModal"
      class="fixed inset-0 flex items-center justify-center z-50 bg-black bg-opacity-50"
    >
      <div class="bg-white rounded-xl p-8 max-w-md w-full shadow-xl">
        <h3 class="text-xl font-bold text-gray-800 mb-4">{{ resultTitle }}</h3>
        <p class="text-gray-600 mb-6">{{ resultMessage }}</p>
        <div class="flex justify-end">
          <button
            @click="closeModal"
            class="px-6 py-2 bg-[#9FB3DF] text-white font-medium rounded-lg hover:bg-[#8da2cc] transition-colors cursor-pointer !rounded-button whitespace-nowrap"
          >
            확인
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref } from "vue";
import axios from "axios";

// API 기본 주소 설정
axios.defaults.baseURL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080";

// 활성 탭 상태
const activeTab = ref<"findId"|"findPassword">("findId");
const isSendingCode = ref(false);

// 아이디 찾기 폼
const findIdForm = ref({ name: "", phoneNumber: "", email: "", verificationCode: "", });

// 비밀번호 찾기 폼
const findPasswordForm = ref({ email: "" });

// 인증번호 필드 표시 여부
const showVerificationField = ref(false);

// 결과 모달 상태
const showResultModal = ref(false);
const resultTitle = ref("");
const resultMessage = ref("");

// 아이디 찾기
async function sendVerificationCode() {
if (!findIdForm.value.name || !findIdForm.value.email) {
  resultTitle.value = "입력 오류";
  resultMessage.value = "이름과 이메일을 모두 입력해주세요.";
  showResultModal.value = true;
  return;
}
try {
  isSendingCode.value = true;
  await axios.post("/api/users/find-email/send", {
    name: findIdForm.value.name,
    phoneNumber: findIdForm.value.phoneNumber,
    email: findIdForm.value.email,
  });
  showVerificationField.value = true;
  resultTitle.value = "인증번호 발송";
  resultMessage.value = "입력하신 이메일로 인증번호가 발송되었습니다.";
} catch (e: any) {
  resultTitle.value = "요청 실패";
  resultMessage.value = e.response?.data?.message || "인증번호 발송에 실패했습니다.";
} finally {
  isSendingCode.value = false;
  showResultModal.value = true;
}
}

async function findId() {
if (!findIdForm.value.name || !findIdForm.value.email) {
  resultTitle.value = "입력 오류";
  resultMessage.value = "이름과 이메일을 모두 입력해주세요.";
  showResultModal.value = true;
  return;
}
if (!showVerificationField.value) {
  resultTitle.value = "인증 필요";
  resultMessage.value = "먼저 인증번호를 요청해주세요.";
  showResultModal.value = true;
  return;
}
if (!findIdForm.value.verificationCode) {
  resultTitle.value = "입력 오류";
  resultMessage.value = "인증번호를 입력해주세요.";
  showResultModal.value = true;
  return;
}

try {
  const { data } = await axios.post("/api/users/find-email/verify", {
    name: findIdForm.value.name,
    email: findIdForm.value.email,
    code: findIdForm.value.verificationCode,
  });
  if (data.found) {
    alert(`아이디 찾기 성공! 회원님의 아이디(이메일)는 ${data.email} 입니다.`);
    resultTitle.value = "아이디 찾기 성공";
    resultMessage.value = `회원님의 아이디(이메일)는 ${data.email} 입니다.`;
  } else {
    resultTitle.value = "인증 실패";
    resultMessage.value = "입력하신 정보가 일치하지 않거나 인증번호가 틀렸습니다.";
  }
} catch (e: any) {
  resultTitle.value = "요청 실패";
  resultMessage.value = e.response?.data?.message || "아이디 찾기에 실패했습니다.";
} finally {
  showResultModal.value = true;
}
}

// 비밀번호 즉시 처리 함수
function immediatePasswordReset() {
// 모달 바로 띄우기
alert("임시 비밀번호를 전송했습니다.");
resultTitle.value = "임시 비밀번호 발송";
resultMessage.value = "해당 이메일로 임시 비밀번호가 발송되었습니다.";
showResultModal.value = true;
// 비동기로 서버 호출은 뒤에서 처리
axios.post("/api/users/find-password", { email: findPasswordForm.value.email });
}

// 모달 닫기
function closeModal() {
showResultModal.value = false;
}

// 전화번호 입력 포맷팅
const onPhoneInput = (e: Event) => {
const input = e.target as HTMLInputElement;
let digits = input.value.replace(/\D/g, "").slice(0, 11);
if (digits.length > 3 && digits.length <= 7) {
  digits = digits.replace(/(\d{3})(\d+)/, "$1-$2");
} else if (digits.length > 7) {
  digits = digits.replace(/(\d{3})(\d{4})(\d+)/, "$1-$2-$3");
}
findIdForm.value.phoneNumber = digits;
};
</script>

<style scoped>
/* 입력 필드에서 숫자 화살표 제거 */
input[type="number"]::-webkit-inner-spin-button,
input[type="number"]::-webkit-outer-spin-button,
input[type="number"] {
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
  margin: 0;
}

/* 포커스 시 아웃라인 제거 */
input:focus {
  outline: none;
}
</style>
