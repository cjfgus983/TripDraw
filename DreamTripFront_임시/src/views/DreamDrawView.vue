<template>
  <div class="p-4 flex flex-col gap-4 rounded-2xl shadow">
    <h2 class="text-2xl font-bold">🖌️ 그림을 그리고 GPT로 장소 추측</h2>

    <!-- 캔버스 -->
    <canvas
      ref="canvasRef"
      @mousedown="startPaint"
      @mousemove="paint"
      @mouseup="endPaint"
      @mouseleave="endPaint"
      class="border border-gray-300 rounded-2xl shadow"
    />

    <!-- 툴바 -->
    <div class="flex flex-wrap gap-4 items-center mt-2">
      <label>색상: <input type="color" v-model="color" /></label>
      <label>두께: <input type="range" min="1" max="30" v-model="lineWidth" /></label>

      <button @click="toggleEraser" class="px-4 py-2 rounded border">
        {{ isEraser ? '펜 모드' : '지우개' }}
      </button>
      <button @click="clearCanvas" class="px-4 py-2 rounded bg-red-500 text-white">전체 지우기</button>
      <button @click="handleComplete" class="px-4 py-2 rounded bg-blue-500 text-white">완성 → GPT 분석</button>
    </div>

    <!-- 결과 텍스트 -->
    <p v-if="resultPlace" class="text-lg font-semibold mt-4">🧠 GPT가 떠올린 장소: {{ resultPlace }}</p>

    <!-- 지도 -->
    <div id="map" class="w-full h-[600px] mt-4 border rounded"></div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, nextTick } from 'vue'

const CANVAS_W = 800
const CANVAS_H = 500

const canvasRef = ref(null)
const ctxRef = ref(null)
const painting = ref(false)
const color = ref('#000000')
const lineWidth = ref(4)
const isEraser = ref(false)
const resultPlace = ref(null)

let map = null
let marker = null

// 🎨 캔버스 초기화
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

  // 🗺️ 지도 초기화
  initMap()
})

// 🖍️ 도구 옵션 변경 감지
watch([color, lineWidth, isEraser], () => {
  if (!ctxRef.value) return
  ctxRef.value.lineWidth = lineWidth.value
  ctxRef.value.strokeStyle = isEraser.value ? '#ffffff' : color.value
})

// 🖌️ 그리기
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

// ✅ 그림 → 분석
async function handleComplete() {
  const dataUrl = canvasRef.value.toDataURL('image/png')
  const gptResponse = await callOpenAI(dataUrl)
  const place = extractLocation(gptResponse)
  resultPlace.value = place
  const coords = await getLatLng(place)
  if (coords) updateMap(coords.lat, coords.lng)
}

// 📤 OpenAI API 호출
async function callOpenAI(base64Img) {
  const res = await fetch('https://api.openai.com/v1/chat/completions', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer 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`, // 🔑 여기에 실제 키로 바꿔야 함
    },
    body: JSON.stringify({
      model: 'gpt-4o',
      messages: [
        {
          role: 'user',
          content: [
            { type: 'text', text: '이 그림을 보고 떠오르는 장소명 (도시/지역)을 알려줘' },
            { type: 'image_url', image_url: { url: base64Img } },
          ],
        },
      ],
    }),
  })

  const data = await res.json()
  return data.choices?.[0]?.message?.content?.trim()
}

// 🔍 장소명 추출
function extractLocation(text) {
  if (!text || typeof text !== 'string') return '알 수 없음'
  const match = text.match(/['"]?([가-힣a-zA-Z ,]+)['"]?/)
  return match ? match[1] : text
}

// 📍 위치 → 좌표
async function getLatLng(placeName) {
  const res = await fetch(`https://maps.googleapis.com/maps/api/geocode/json?address=${encodeURIComponent(placeName)}&key=AIzaSyCSHVlmjjqz44yoKKnKbngFZt-ChIPvve4`)
  const data = await res.json()
  return data.results?.[0]?.geometry?.location
}

// 🗺️ 지도 초기화
function initMap() {
  map = new google.maps.Map(document.getElementById('map'), {
    center: { lat: 37.5665, lng: 126.9780 }, // 서울
    zoom: 5,
    mapId: 'YOUR_MAP_ID' // 🔔 고급 마커를 쓸 거면 반드시 설정
  })
}

// 🎯 마커 찍고 지도 이동
function updateMap(lat, lng) {
  map.setCenter({ lat, lng })
  map.setZoom(12)

  if (marker) marker.setMap(null)
  marker = new google.maps.marker.AdvancedMarkerElement({
    position: { lat, lng },
    map,
    title: 'GPT가 예측한 장소',
  })
}
</script>

<style scoped>
canvas {
  cursor: crosshair;
  background-color: white;
}
</style>
