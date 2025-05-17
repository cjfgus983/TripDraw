<template>
    <div class="min-h-screen flex items-center justify-center bg-gradient-to-br from-[#9FB3DF] via-[#9EC6F3] to-[#BDDDE4] py-10">
      <div class="max-w-md w-full bg-white/90 backdrop-blur-sm p-10 rounded-lg shadow-lg border border-[#FFF1D5]/20">
        <h1 class="text-2xl font-bold text-[#9FB3DF] mb-8 text-center">회원정보 수정</h1>
        <form @submit.prevent="handleSubmit" class="space-y-6">
          <!-- 이메일 (읽기전용) -->
          <div>
            <label class="block text-sm font-medium mb-1">이메일</label>
            <input
              type="email"
              v-model="profile.email"
              readonly
              class="w-full px-4 py-2 border border-gray-300 rounded-lg bg-gray-100 cursor-not-allowed"
            />
          </div>
          <!-- 이름 (읽기전용) -->
          <div>
            <label class="block text-sm font-medium mb-1">이름</label>
            <input
              type="text"
              v-model="profile.name"
              readonly
              class="w-full px-4 py-2 border border-gray-300 rounded-lg bg-gray-100 cursor-not-allowed"
            />
          </div>
          <!-- 휴대폰 (읽기전용) -->
          <div>
            <label class="block text-sm font-medium mb-1">휴대폰 번호</label>
            <input
              type="tel"
              v-model="profile.phoneNumber"
              readonly
              class="w-full px-4 py-2 border border-gray-300 rounded-lg bg-gray-100 cursor-not-allowed"
            />
          </div>
          <!-- 닉네임 (수정 가능) -->
          <div>
            <label class="block text-sm font-medium mb-1">닉네임</label>
            <input
              type="text"
              v-model="profile.nickname"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-[#9EC6F3]"
            />
          </div>
          <!-- 주소 + 상세주소 -->
          <div>
            <label class="block text-sm font-medium mb-1">주소</label>
            <div class="flex gap-2 mb-2">
              <input
                type="text"
                v-model="profile.address"
                readonly
                class="flex-1 px-4 py-2 border border-gray-300 rounded-lg bg-gray-100 cursor-pointer"
              />
              <button
                type="button"
                @click="openAddressSearch"
                class="px-4 py-2 bg-[#9FB3DF] text-white rounded-lg hover:opacity-90"
              >
                주소 검색
              </button>
            </div>
            <input
              type="text"
              v-model="profile.addressDetail"
              placeholder="상세 주소를 입력하세요"
              class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-[#9EC6F3]"
            />
          </div>
          <!-- 비밀번호 변경 토글 -->
          <div>
            <button
              type="button"
              @click="editingPassword = !editingPassword"
              class="text-[#9FB3DF] font-medium hover:underline"
            >
              {{ editingPassword ? '비밀번호 변경 취소' : '비밀번호 변경하기' }}
            </button>
          </div>
          <!-- 새 비밀번호 입력칸 (editingPassword=true 일 때만) -->
          <div v-if="editingPassword" class="space-y-4">
            <div>
              <label class="block text-sm font-medium mb-1">새 비밀번호</label>
              <input
                type="password"
                v-model="newPassword"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-[#9EC6F3]"
                placeholder="8자 이상, 영문/숫자/특수문자 조합"
              />
            </div>
            <div>
              <label class="block text-sm font-medium mb-1">비밀번호 확인</label>
              <input
                type="password"
                v-model="confirmPassword"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-[#9EC6F3]"
                placeholder="다시 입력해주세요"
              />
            </div>
            <p v-if="passwordMismatch" class="text-xs text-red-500">
              새 비밀번호가 일치하지 않습니다.
            </p>
          </div>
          <!-- 수정하기 버튼 -->
          <button
            type="submit"
            :disabled="submitDisabled"
            class="w-full py-3 text-white font-medium rounded-lg
              bg-gradient-to-r from-[#9FB3DF] to-[#9EC6F3]
              hover:opacity-90 disabled:opacity-50"
          >
            수정하기
          </button>
        </form>
      </div>
    </div>
  </template>
  
  <script lang="ts" setup>
  import { ref, reactive, computed, onMounted } from 'vue'
  import axios from 'axios'
  import { useRouter } from 'vue-router'
  
  const router = useRouter()
  
  interface Profile {
    email: string
    name: string
    phoneNumber: string
    nickname: string
    address: string
    addressDetail: string
  }
  
  const profile = reactive<Profile>({
    email: '',
    name: '',
    phoneNumber: '',
    nickname: '',
    address: '',
    addressDetail: ''
  })
  
  const editingPassword = ref(false)
  const newPassword = ref('')
  const confirmPassword = ref('')
  
  // 비밀번호 일치 여부
  const passwordMismatch = computed(() =>
    editingPassword.value &&
    newPassword.value !== confirmPassword.value
  )
  
  // 제출 버튼 활성화 조건
  const submitDisabled = computed(() => {
    if (!profile.nickname.trim() || !profile.address.trim()) return true
    if (editingPassword.value) {
      return (
        newPassword.value.length < 8 ||
        passwordMismatch.value
      )
    }
    return false
  })
  
  // 초기 데이터 로드
  onMounted(async () => {
    try {
      const { data } = await axios.get<Profile>('/api/users/me')
      Object.assign(profile, data)
    } catch {
      alert('프로필을 불러오는 중 오류가 발생했습니다.')
      router.push('/')
    }
  })
  
  // Daum 주소 검색 열기
  const openAddressSearch = () => {
    const script = document.createElement('script')
    script.src = '//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js'
    script.onload = () => {
      new (window as any).daum.Postcode({
        oncomplete: (data: any) => {
          profile.address = data.address
        }
      }).open()
    }
    document.head.appendChild(script)
  }
  
  // 폼 제출
  const handleSubmit = async () => {
    if (submitDisabled.value) return
  
    const payload: any = {
      email: profile.email,
      phoneNumber: profile.phoneNumber,
      nickname: profile.nickname,
      address: profile.address,
      addressDetail: profile.addressDetail,
    }
    if (editingPassword.value && newPassword.value) {
      payload.newPassword = newPassword.value
    }
  
    try {
      await axios.put('/api/users/me', payload)
      alert('회원정보가 성공적으로 업데이트되었습니다.')
      router.push('/mypage')  // 성공하면 마이페이지 이동
    } catch (e: any) {
      const msg = e.response?.data?.message || '업데이트 중 오류가 발생했습니다.'
      alert(msg)
    }
  }
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
  