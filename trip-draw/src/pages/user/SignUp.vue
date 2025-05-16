<!-- The exported code uses Tailwind CSS. Install Tailwind CSS in your dev environment to ensure all styles work. -->
<template>
    <div
      class="min-h-screen flex items-center justify-center bg-gradient-to-br from-[#9FB3DF] via-[#9EC6F3] to-[#BDDDE4] py-10"
    >
      <div class="max-w-md w-full space-y-8 bg-white/90 backdrop-blur-sm p-10 rounded-lg shadow-lg border border-[#FFF1D5]/20">
        <div class="p-10">
          <!-- 헤더 섹션 -->
          <h1 class="text-2xl font-bold text-[#9FB3DF] mb-8 text-center">
            회원가입
          </h1>
          <!-- 소셜 로그인 섹션 제거 -->
          <!-- 입력 폼 섹션 -->
          <form @submit.prevent="handleSubmit">
            <div class="space-y-5">
              <!-- 이메일 주소 -->
              <div>
                <label class="block mb-1 text-sm font-medium">
                  이메일 주소 <span class="text-[#9EC6F3]">*</span>
                </label>
                <div class="flex gap-2">
                  <input
                    type="email"
                    v-model="email"
                    class="flex-1 px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#BDDDE4]"
                    placeholder="example@email.com"
                    required
                    @input="onEmailInput"
                  />
                  <button
                    type="button"
                    @click="sendVerificationEmail"
                    :disabled="!emailValid || isEmailVerified"
                    class="px-4 py-3 text-sm font-medium text-white bg-[#9FB3DF] rounded-button whitespace-nowrap disabled:bg-gray-300 disabled:cursor-not-allowed"
                  >
                    {{ isEmailVerified ? '인증완료' : '인증하기' }}
                  </button>
                </div>
                <p v-if="emailError" class="mt-1 text-xs text-[#FF6B6B]">
                  {{ emailError }}
                </p>
                <p v-else-if="checkingEmail" class="mt-1 text-xs text-[#999]">
                  중복 검사 중...
                </p>
                <p v-else-if="emailAvailable === false" class="mt-1 text-xs text-[#FF6B6B]">
                  이미 등록된 이메일입니다.
                </p>
                <p v-else-if="emailAvailable === true" class="mt-1 text-xs text-[#4CAF50]">
                  사용 가능한 이메일입니다.
                </p>
                <p
                  v-else-if="emailValid && !isEmailVerified"
                  class="mt-1 text-xs text-[#4CAF50]"
                >
                  유효한 이메일 형식입니다
                </p>
                <p
                  v-else-if="isEmailVerified"
                  class="mt-1 text-xs text-[#4CAF50]"
                >
                  이메일 인증이 완료되었습니다
                </p>
  
                <!-- 이메일 인증 코드 입력 -->
                <div v-if="showVerificationInput" class="mt-3">
                  <div class="flex gap-2">
                    <input
                      type="text"
                      v-model="verificationCode"
                      class="flex-1 px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#BDDDE4]"
                      placeholder="인증번호 6자리 입력"
                      maxlength="6"
                    />
                    <button
                      type="button"
                      @click="verifyCode"
                      :disabled="verificationCode.length !== 6"
                      class="px-4 py-3 text-sm font-medium text-white bg-[#9FB3DF] rounded-button whitespace-nowrap disabled:bg-gray-300 disabled:cursor-not-allowed"
                    >
                      확인
                    </button>
                  </div>
                  <p class="mt-1 text-xs text-gray-500">
                    인증번호 만료까지
                    <span class="text-[#9FB3DF]">{{ verificationTimer }}</span>
                  </p>
                </div>
              </div>
  
              <!-- 닉네임 -->
              <div class="mt-5">
                <label class="block mb-1 text-sm font-medium">
                  닉네임 <span class="text-[#9EC6F3]">*</span>
                </label>
                <div class="flex gap-2">
                  <input
                    type="text"
                    v-model="nickname"
                    class="flex-1 px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#BDDDE4]"
                    placeholder="닉네임을 입력해주세요"
                    required
                    @blur="validateNickname"
                  />
                  <button
                    type="button"
                    @click="checkNicknameDuplicate"
                    :disabled="!nickname || !!nicknameError"
                    class="px-4 py-3 text-sm font-medium text-white bg-[#9FB3DF] rounded-button whitespace-nowrap disabled:bg-gray-300 disabled:cursor-not-allowed"
                  >
                    중복확인
                  </button>
                </div>
                <p v-if="nicknameError" class="mt-1 text-xs text-[#FF6B6B]">
                  {{ nicknameError }}
                </p>
                <p
                  v-else-if="isNicknameVerified"
                  class="mt-1 text-xs text-[#4CAF50]"
                >
                  사용 가능한 닉네임입니다
                </p>
              </div>
              <!-- 비밀번호 -->
              <div>
                <label class="block mb-1 text-sm font-medium">
                  비밀번호 <span class="text-[#9EC6F3]">*</span>
                </label>
                <div class="relative">
                  <input
                    :type="showPassword ? 'text' : 'password'"
                    v-model="password"
                    class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#BDDDE4]"
                    placeholder="8자 이상, 영문/숫자/특수문자 조합"
                    required
                    @input="validatePassword"   
                    @blur="validatePassword"
                  />
                  <button
                    type="button"
                    @click="showPassword = !showPassword"
                    class="absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-500 cursor-pointer whitespace-nowrap !rounded-button"
                  >
                    <i
                      :class="showPassword ? 'far fa-eye-slash' : 'far fa-eye'"
                    ></i>
                  </button>
                </div>
                <p v-if="passwordError" class="mt-1 text-xs text-[#FF6B6B]">
                  {{ passwordError }}
                </p>
                <p v-else-if="passwordValid" class="mt-1 text-xs text-[#4CAF50]">
                  안전한 비밀번호입니다
                </p>
              </div>
              <!-- 비밀번호 확인 -->
              <div>
                <label class="block mb-1 text-sm font-medium">
                  비밀번호 확인 <span class="text-[#9EC6F3]">*</span>
                </label>
                <div class="relative">
                  <input
                    :type="showConfirmPassword ? 'text' : 'password'"
                    v-model="confirmPassword"
                    class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#BDDDE4]"
                    placeholder="비밀번호를 다시 입력해주세요"
                    required
                    @input="validateConfirmPassword"
                    @blur="validateConfirmPassword"
                  />
                  <button
                    type="button"
                    @click="showConfirmPassword = !showConfirmPassword"
                    class="absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-500 cursor-pointer whitespace-nowrap !rounded-button"
                  >
                    <i
                      :class="showConfirmPassword ? 'far fa-eye-slash' : 'far fa-eye'"
                    ></i>
                  </button>
                </div>
                <p
                  v-if="confirmPasswordError"
                  class="mt-1 text-xs text-[#FF6B6B]"
                >
                  {{ confirmPasswordError }}
                </p>
                <p
                  v-else-if="confirmPasswordValid"
                  class="mt-1 text-xs text-[#4CAF50]"
                >
                  비밀번호가 일치합니다
                </p>
              </div>
              <!-- 이름 -->
              <div>
                <label class="block mb-1 text-sm font-medium">
                  이름 <span class="text-[#9EC6F3]">*</span>
                </label>
                <input
                  type="text"
                  v-model="name"
                  class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#BDDDE4]"
                  placeholder="실명을 입력해주세요"
                  required
                  @blur="validateName"
                />
                <p v-if="nameError" class="mt-1 text-xs text-[#FF6B6B]">
                  {{ nameError }}
                </p>
              </div>
              <!-- 휴대폰 번호 -->
              <div>
                <label class="block mb-1 text-sm font-medium">
                  휴대폰 번호 <span class="text-gray-400"></span>
                </label>
                <input
                  type="tel"
                  v-model="phone"
                  @input="onPhoneInput"
                  class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#BDDDE4]"
                  placeholder="010-0000-0000"
                  @blur="validatePhone"
                />
                <p v-if="phoneError" class="mt-1 text-xs text-[#FF6B6B]">
                  {{ phoneError }}
                </p>
              </div>
  
              <!-- 주소 -->
              <div class="mt-5">
                <label class="block mb-1 text-sm font-medium">
                  주소 <span class="text-[#9EC6F3]">*</span>
                </label>
                <div class="flex gap-2 mb-2">
                  <input
                    type="text"
                    v-model="address"
                    class="flex-1 px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#BDDDE4]"
                    placeholder="주소 검색"
                    readonly
                    required
                  />
                  <button
                    type="button"
                    @click="openAddressSearch"
                    class="px-4 py-3 text-sm font-medium text-white bg-[#9FB3DF] rounded-button whitespace-nowrap"
                  >
                    주소 검색
                  </button>
                </div>
                <input
                  type="text"
                  v-model="addressDetail"
                  class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#BDDDE4]"
                  placeholder="상세주소를 입력해주세요"
                  required
                />
              </div>
            </div>
            <!-- 약관 동의 섹션 -->
            <div class="mt-8 p-4 bg-[#FFF1D5] rounded-lg">
              <div class="flex items-center mb-3">
                <input
                  type="checkbox"
                  id="agreeAll"
                  v-model="agreeAll"
                  @change="toggleAllAgreements"
                  class="w-5 h-5 rounded text-[#9FB3DF] focus:ring-[#9EC6F3]"
                />
                <label
                  for="agreeAll"
                  class="ml-2 text-sm font-medium cursor-pointer"
                  >모든 약관에 동의합니다</label
                >
              </div>
              <div class="ml-1 space-y-2 border-t border-[#BDDDE4] pt-3">
                <div class="flex items-center">
                  <input
                    type="checkbox"
                    id="agreeService"
                    v-model="agreements.service"
                    @change="checkAgreements"
                    class="w-4 h-4 rounded text-[#9FB3DF] focus:ring-[#9EC6F3]"
                    required
                  />
                  <label for="agreeService" class="ml-2 text-sm cursor-pointer"
                    >서비스 이용약관 동의
                    <span class="text-[#9EC6F3]">*</span></label
                  >
                  <button
                    type="button"
                    class="ml-auto text-xs text-gray-500 underline cursor-pointer whitespace-nowrap !rounded-button"
                  >
                    보기
                  </button>
                </div>
                <div class="flex items-center">
                  <input
                    type="checkbox"
                    id="agreePrivacy"
                    v-model="agreements.privacy"
                    @change="checkAgreements"
                    class="w-4 h-4 rounded text-[#9FB3DF] focus:ring-[#9EC6F3]"
                    required
                  />
                  <label for="agreePrivacy" class="ml-2 text-sm cursor-pointer"
                    >개인정보 처리방침 동의
                    <span class="text-[#9EC6F3]">*</span></label
                  >
                  <button
                    type="button"
                    class="ml-auto text-xs text-gray-500 underline cursor-pointer whitespace-nowrap !rounded-button"
                  >
                    보기
                  </button>
                </div>
                <div class="flex items-center">
                  <input
                    type="checkbox"
                    id="agreeMarketing"
                    v-model="agreements.marketing"
                    @change="checkAgreements"
                    class="w-4 h-4 rounded text-[#9FB3DF] focus:ring-[#9EC6F3]"
                  />
                  <label for="agreeMarketing" class="ml-2 text-sm cursor-pointer"
                    >마케팅 정보 수신 동의
                    <span class="text-gray-400">(선택)</span></label
                  >
                  <button
                    type="button"
                    class="ml-auto text-xs text-gray-500 underline cursor-pointer whitespace-nowrap !rounded-button"
                  >
                    보기
                  </button>
                </div>
              </div>
            </div>
            <!-- 가입하기 버튼 -->
            <button
              type="submit"
              :disabled="!isFormValid"
              :class="[
  'w-full py-4 mt-6 text-white font-medium rounded-button cursor-pointer whitespace-nowrap transition-all',
  isFormValid
  ? 'bg-gradient-to-r from-[#9FB3DF] to-[#9EC6F3] hover:opacity-90'
  : 'bg-gray-300 cursor-not-allowed'
  ]"
            >
              가입하기
            </button>
            <!-- 로그인 링크 -->
            <div class="mt-6 text-center">
              <p class="text-sm text-gray-600">
                이미 계정이 있으신가요?
                <a href="#" class="text-[#9FB3DF] font-medium cursor-pointer"
                  >로그인</a
                >
              </p>
            </div>
          </form>
        </div>
      </div>
    </div>
  </template>
  
  <script lang="ts" setup>
  import { ref, computed, onMounted, watch } from "vue";
  import axios from "axios";
  import { useRouter } from "vue-router";
  import debounce from "lodash/debounce";

  const isDaumLoaded = ref(false);

  const router = useRouter();

  const loading = ref(false);
  const error = ref<string | null>(null);
  

  // 폼 데이터
  const email = ref("");
  const emailAvailable = ref<boolean|null>(null);
  const checkingEmail = ref(false);

  const password = ref("");
  const confirmPassword = ref("");
  const name = ref("");
  const phone = ref("");
  const nickname = ref("");
  const address = ref("");
  const addressDetail = ref("");
  
  // 이메일 인증 관련
  const isEmailVerified = ref(false);
  const showVerificationInput = ref(false);
  const verificationCode = ref("");
  const verificationTimer = ref("3:00");
  let timerInterval: number;
  
  // 닉네임 인증 관련
  const isNicknameVerified = ref(false);
  const nicknameError = ref("");


  const onPhoneInput = (e: Event) => {
  const input = e.target as HTMLInputElement
  // 숫자만 남기고 최대 11자리
  let digits = input.value.replace(/\D/g, '').slice(0, 11)

  if (digits.length > 3 && digits.length <= 7) {
    // 010 1234 → 010-1234
    digits = digits.replace(/(\d{3})(\d+)/, '$1-$2')
  } else if (digits.length > 7) {
    // 010 1234 5678 → 010-1234-5678
    digits = digits.replace(/(\d{3})(\d{4})(\d+)/, '$1-$2-$3')
  }

  phone.value = digits
}

  
  // Daum 주소 검색 스크립트 로드
  onMounted(() => {
  const script = document.createElement("script");
  script.src = "//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js";
  script.onload = () => {
    isDaumLoaded.value = true;
  };
  document.head.appendChild(script);
});
  
  // 이메일 인증 메서드
  // 1) 이메일 중복 조회 + 인증번호 전송
const sendVerificationEmail = async () => {
  // 1-1) 형식 검사
  validateEmail();
  if (!emailValid.value) return;

  showVerificationInput.value = true;
  startVerificationTimer();

  try {
    // 1-2) 중복 확인
    const { data: { available } } = await axios.get(
      "http://localhost:8080/api/users/check-email",
      { params: { email: email.value } }
    );
    if (!available) {
      emailError.value = "이미 등록된 이메일입니다.";
      return;
    }

    // 1-3) 인증번호 요청 (백엔드에 send-email 엔드포인트를 만들어 두세요)
    await axios.post(
      "http://localhost:8080/api/users/email/send",
      { email: email.value }
    );

    // 입력 UI 노출 & 타이머 시작


  } catch (e: any) {
    emailError.value = e.response?.data?.message || "인증번호 전송에 실패했습니다.";
  }
};


  
  const startVerificationTimer = () => {
    let timeLeft = 180; // 3분
    clearInterval(timerInterval);
  
    timerInterval = setInterval(() => {
      const minutes = Math.floor(timeLeft / 60);
      const seconds = timeLeft % 60;
      verificationTimer.value = `${minutes}:${seconds.toString().padStart(2, "0")}`;
  
      if (timeLeft === 0) {
        clearInterval(timerInterval);
        showVerificationInput.value = false;
      }
      timeLeft--;
    }, 1000);
  };
  
  // 2) 입력된 코드 검증
const verifyCode = async () => {
  if (verificationCode.value.length !== 6) return;

  try {
    const { data: { verified } } = await axios.post(
      "http://localhost:8080/api/users/email/verify",
      {
        email: email.value,
        code:  verificationCode.value
      }
    );

    if (verified) {
      isEmailVerified.value = true;
      showVerificationInput.value = false;
      clearInterval(timerInterval);
    } else {
      emailError.value = "인증번호가 올바르지 않습니다.";
    }

  } catch (e: any) {
    emailError.value = e.response?.data?.message || "인증 확인에 실패했습니다.";
  }
};
  
  // 닉네임 유효성 검사
  const validateNickname = () => {
    if (!nickname.value) {
      nicknameError.value = "닉네임을 입력해주세요.";
      isNicknameVerified.value = false;
    } else if (nickname.value.length < 2) {
      nicknameError.value = "닉네임은 2자 이상이어야 합니다.";
      isNicknameVerified.value = false;
    } else {
      nicknameError.value = "";
    }
  };
  
  // 닉네임 중복 확인
  const checkNicknameDuplicate = () => {
    // TODO: 실제 중복 확인 로직 구현
    setTimeout(() => {
      isNicknameVerified.value = true;
    }, 500);
  };
  
  // 주소 검색
  const openAddressSearch = () => {
  if (!isDaumLoaded.value) {
    // 스크립트가 아직 로드 중일 때 사용자에게 알리거나
    alert("주소 검색 기능을 불러오는 중입니다. 잠시만 기다려주세요.");
    return;
  }

  new (window as any).daum.Postcode({
    oncomplete: (data: any) => {
      address.value = data.address;
    },
  }).open();
};
  // 비밀번호 표시 상태
  const showPassword = ref(false);
  const showConfirmPassword = ref(false);
  // 유효성 검사 상태
  const emailError = ref("");
  const emailValid = ref(false);
  const passwordError = ref("");
  const passwordValid = ref(false);
  const confirmPasswordError = ref("");
  const confirmPasswordValid = ref(false);
  const nameError = ref("");
  const phoneError = ref("");
  // 약관 동의 상태
  const agreeAll = ref(false);
  const agreements = ref({
    service: false,
    privacy: false,
    marketing: false,
  });
  // 이메일 유효성 검사
  const validateEmail = () => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!email.value) {
      emailError.value = "이메일을 입력해주세요.";
      emailValid.value = false;
    } else if (!emailRegex.test(email.value)) {
      emailError.value = "유효한 이메일 주소를 입력해주세요.";
      emailValid.value = false;
    } else {
      emailError.value = "";
      emailValid.value = true;
    }
  };
  // 비밀번호 유효성 검사
  const validatePassword = () => {
    const passwordRegex =
      /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/;
    if (!password.value) {
      passwordError.value = "비밀번호를 입력해주세요.";
      passwordValid.value = false;
    } else if (password.value.length < 8) {
      passwordError.value = "비밀번호는 8자 이상이어야 합니다.";
      passwordValid.value = false;
    } else if (!passwordRegex.test(password.value)) {
      passwordError.value = "영문, 숫자, 특수문자를 조합해 입력해주세요.";
      passwordValid.value = false;
    } else {
      passwordError.value = "";
      passwordValid.value = true;
    }
    // 비밀번호 확인 필드가 비어있지 않으면 일치 여부 검사
    if (confirmPassword.value) {
      validateConfirmPassword();
    }
  };
  // 비밀번호 확인 유효성 검사
  const validateConfirmPassword = () => {
    if (!confirmPassword.value) {
      confirmPasswordError.value = "비밀번호 확인을 입력해주세요.";
      confirmPasswordValid.value = false;
    } else if (confirmPassword.value !== password.value) {
      confirmPasswordError.value = "비밀번호가 일치하지 않습니다.";
      confirmPasswordValid.value = false;
    } else {
      confirmPasswordError.value = "";
      confirmPasswordValid.value = true;
    }
  };
  // 이름 유효성 검사
  const validateName = () => {
    if (!name.value) {
      nameError.value = "이름을 입력해주세요.";
    } else if (name.value.length < 2) {
      nameError.value = "이름은 2자 이상이어야 합니다.";
    } else {
      nameError.value = "";
    }
  };
  // 휴대폰 번호 유효성 검사
  const validatePhone = () => {
    if (!phone.value) {
      phoneError.value = "";
      return;
    }
    const phoneRegex = /^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$/;
    if (!phoneRegex.test(phone.value)) {
      phoneError.value = "유효한 휴대폰 번호를 입력해주세요.";
    } else {
      phoneError.value = "";
    }
  };
  // 모든 약관 동의/해제
  const toggleAllAgreements = () => {
    agreements.value.service = agreeAll.value;
    agreements.value.privacy = agreeAll.value;
    agreements.value.marketing = agreeAll.value;
  };
  // 개별 약관 체크 시 전체 동의 상태 업데이트
  const checkAgreements = () => {
    agreeAll.value =
      agreements.value.service &&
      agreements.value.privacy &&
      agreements.value.marketing;
  };
  // 폼 유효성 검사
  const isFormValid = computed(() => {
    return (
      emailValid.value &&
      isEmailVerified.value &&
      isNicknameVerified.value &&
      passwordValid.value &&
      confirmPasswordValid.value &&
      name.value.length >= 2 &&
      !nameError.value &&
      !phoneError.value &&
      address.value &&
      addressDetail.value &&
      agreements.value.service &&
      agreements.value.privacy
    );
  });
  // 폼 제출 처리
  const handleSubmit = async () => {
  // 폼이 유효하지 않거나 이미 요청 중이면 바로 리턴
  if (!isFormValid.value || loading.value) return;

  loading.value = true;
  error.value = null;

  try {
    await axios.post(
      "http://localhost:8080/api/users/signup",
      {
        email:            email.value,
        password:         password.value,
        name:             name.value,
        phoneNumber:      phone.value,
        nickname:         nickname.value,
        address:          address.value,
        addressDetail:    addressDetail.value,
        loginType:       "GENERAL"    // 백엔드가 필요로 하는 경우
      }
    );
    alert("회원가입에 성공했습니다.");
    // 가입 성공하면 로그인 페이지로 이동
    router.push({ name: "Login" });
  } catch (e: any) {
    // 백엔드에서 던진 메시지가 있으면 보여주고, 없으면 일반 에러
    error.value =
      e.response?.data?.message ||
      "회원가입 중 오류가 발생했습니다.";
  } finally {
    loading.value = false;
  }
};
// 1) 서버로 중복 확인 요청
const checkEmailAvailability = async () => {
  if (!emailValid.value) return;
  checkingEmail.value = true;
  try {
    const { data: { available } } = await axios.get(
      "http://localhost:8080/api/users/check-email",
      { params: { email: email.value } }
    );
    emailAvailable.value = available;
    if (!available) emailError.value = "이미 등록된 이메일입니다.";
  } catch {
    emailError.value = "이메일 중복 확인에 실패했습니다.";
  } finally {
    checkingEmail.value = false;
  }
};

// 2) 디바운스 걸어서 너무 자주 호출되지 않게
const debouncedCheckEmail = debounce(checkEmailAvailability, 500);

// 3) 입력마다 호출할 핸들러
const onEmailInput = () => {
  validateEmail();
  emailAvailable.value = null;       // 재검사 전 초기화
  debouncedCheckEmail();
};

// 4) 모델이 바뀔 때, 유효하지 않으면 디바운스 취소
watch(email, () => {
  if (!emailValid.value) {
    emailAvailable.value = null;
    debouncedCheckEmail.cancel();
  }
});

  </script>
  
  <!---------------------------------------------------------------------------------------------------->

  <style scoped>
  /* 숫자 입력 필드의 화살표 제거 */
  input[type="number"]::-webkit-inner-spin-button,
  input[type="number"]::-webkit-outer-spin-button {
    -webkit-appearance: none;
    margin: 0;
  }
  input[type="number"] {
    -moz-appearance: textfield;
  }
  /* 체크박스 커스텀 스타일 */
  input[type="checkbox"] {
    accent-color: #9fb3df;
  }
  </style>
  