<template>
  <div class="p-4 flex flex-col gap-4 rounded-2xl shadow">
    <h2 class="text-2xl font-bold">🖌️ GPT 그림 분석</h2>

    <!-- Canvas Area -->
    <canvas
      ref="canvasRef"
      @mousedown="startPaint"
      @mousemove="paint"
      @mouseup="endPaint"
      @mouseleave="endPaint"
      class="border border-gray-300 rounded-2xl shadow"
    />

    <!-- Tool Bar -->
    <div class="flex flex-wrap gap-4 items-center">
      <div class="flex items-center gap-2">
        <label>색상:</label>
        <input type="color" v-model="color" class="w-10 h-10 border-none bg-transparent" />
      </div>

      <div class="flex items-center gap-2">
        <label>선 두께:</label>
        <input type="range" min="1" max="30" v-model="lineWidth" />
      </div>

      <button @click="toggleEraser" :class="['px-4 py-2 rounded border', isEraser ? 'bg-gray-200' : 'bg-white']">
        {{ isEraser ? '펜 모드' : '지우개' }}
      </button>

      <button @click="clearCanvas" class="px-4 py-2 rounded bg-red-500 text-white">전체 지우기</button>
      <button @click="handleComplete" class="px-4 py-2 rounded bg-blue-500 text-white">완성 → 분석</button>
    </div>

    <!-- 결과 영역 -->
    <div v-if="resultCountry" class="mt-6">
      <p class="text-xl font-semibold">
        🎉 이 그림은 <span class="text-primary">{{ resultCountry }}</span> 을 떠올리게 합니다!
      </p>
      <div class="mt-4">
        <iframe
          v-if="mapUrl"
          :src="mapUrl"
          width="1000"
          height="800"
          style="border:0"
          allowfullscreen=""
          loading="lazy"
          referrerpolicy="no-referrer-when-downgrade"
        ></iframe>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'

const CANVAS_W = 1000
const CANVAS_H = 800

const canvasRef = ref(null)
const ctxRef = ref(null)
const painting = ref(false)
const color = ref('#000000')
const lineWidth = ref(4)
const isEraser = ref(false)
const resultCountry = ref(null)
const mapUrl = ref(null)

// 🖌️ 캔버스 초기화
onMounted(() => {
  const canvas = canvasRef.value
  canvas.width = CANVAS_W
  canvas.height = CANVAS_H
  canvas.style.width = `${CANVAS_W}px`
  canvas.style.height = `${CANVAS_H}px`

  const ctx = canvas.getContext('2d')
  ctx.lineCap = 'round'
  ctx.lineJoin = 'round'
  ctx.lineWidth = lineWidth.value
  ctx.strokeStyle = color.value
  ctxRef.value = ctx
})

// 🎨 옵션 변경 시 적용
watch([color, lineWidth, isEraser], () => {
  if (!ctxRef.value) return
  ctxRef.value.lineWidth = lineWidth.value
  ctxRef.value.strokeStyle = isEraser.value ? '#ffffff' : color.value
})

// 🖍️ 그리기 기능
function startPaint(e) {
  const { offsetX, offsetY } = e
  ctxRef.value.beginPath()
  ctxRef.value.moveTo(offsetX, offsetY)
  painting.value = true
}

function paint(e) {
  if (!painting.value) return
  const { offsetX, offsetY } = e
  ctxRef.value.lineTo(offsetX, offsetY)
  ctxRef.value.stroke()
}

function endPaint() {
  if (!painting.value) return
  painting.value = false
  ctxRef.value.closePath()
}

function clearCanvas() {
  ctxRef.value.clearRect(0, 0, CANVAS_W, CANVAS_H)
}

function toggleEraser() {
  isEraser.value = !isEraser.value
}

// 🌎 GPT 호출 → 나라 추측 → 지도 URL 설정
async function handleComplete() {
  const dataUrl = canvasRef.value.toDataURL('image/png')
  const raw = await callOpenAI(dataUrl)
  const country = extractLocation(raw)
  resultCountry.value = country

  mapUrl.value = `https://www.google.com/maps/embed/v1/place?key=AIzaSyCSHVlmjjqz44yoKKnKbngFZt-ChIPvve4&q=${encodeURIComponent(
    country
  )}&zoom=5`
}

// ✨ OpenAI API 호출 함수
async function callOpenAI(base64Img, retry = 0) {
  const res = await fetch('https://api.openai.com/v1/chat/completions', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer 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`, // 🔑 꼭 변경!
    },
    body: JSON.stringify({
      model: 'gpt-4o',
      messages: [
        {
          role: 'user',
          content: [
            { type: 'text', text: '이 그림을 보고 떠오르는 나라, 혹은 위치, 혹은 장소를 알려줘' },
            { type: 'image_url', image_url: { url: base64Img } },
          ],
        },
      ],
    }),
  })

  if (res.status === 429 && retry < 5) {
    const wait = (res.headers.get('Retry-After') ?? 2) * 1000 * (retry + 1)
    await new Promise((r) => setTimeout(r, wait))
    return callOpenAI(base64Img, retry + 1)
  }

  if (!res.ok) {
    const text = await res.text()
    throw new Error(`OpenAI 오류: ${res.status} - ${text}`)
  }

  const data = await res.json()
  return data.choices?.[0]?.message?.content?.trim()
}

// 🧠 GPT 응답에서 위치만 뽑는 정규식
function extractLocation(text) {
  const match = text.match(/['\"]?([가-힣a-zA-Z ,]+)['\"]?/)
  return match ? match[1] : text
}
</script>

<style scoped>
canvas {
  cursor: crosshair;
  background-color: white;
}
</style>
