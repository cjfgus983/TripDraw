<template>
    <div class="p-6">
      <h2 class="text-2xl font-bold mb-4">🖼️ 사진으로 장소 추측</h2>
  
      <!-- 드래그 & 붙여넣기 영역 -->
      <div
        class="border-2 border-dashed border-gray-400 p-6 rounded text-center"
        @dragover.prevent
        @drop="handleDrop"
        @paste="handlePaste"
      >
        <p class="text-gray-600 mb-4">
          이미지를 <strong>선택</strong>, <strong>드래그</strong>, 혹은 <strong>붙여넣기</strong> 해보세요!
        </p>
        <input type="file" @change="handleFileSelect" accept="image/*" class="mb-4" />
        <div v-if="preview" class="mt-4">
          <img :src="preview" class="max-w-[400px] rounded shadow mx-auto" />
        </div>
      </div>
      
      <!-- 결과 -->
      <p v-if="resultPlace" class="text-lg font-semibold mt-6 text-center">
        🧠 GPT가 추측한 장소: <strong>{{ resultPlace }}</strong>
      </p>
  
      <!-- 지도 -->
      <div id="map" class="w-full h-[600px] mt-6 border rounded"></div>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted, nextTick } from 'vue'
  
  const preview = ref(null)
  const resultPlace = ref(null)
  let map = null
  let marker = null
  
  // 📌 지도 초기화
  onMounted(() => {
    initMap()
  })
  
  // 🖼️ 파일 선택
  function handleFileSelect(e) {
    const file = e.target.files[0]
    if (file) readAndAnalyze(file)
  }
  
  // 🖱 드래그 앤 드롭
  function handleDrop(e) {
    const file = e.dataTransfer.files[0]
    if (file && file.type.startsWith('image/')) readAndAnalyze(file)
  }
  
  // 📋 붙여넣기
  function handlePaste(e) {
    for (const item of e.clipboardData.items) {
      if (item.type.startsWith('image/')) {
        const file = item.getAsFile()
        readAndAnalyze(file)
        break
      }
    }
  }
  
  // 📥 파일 읽고 분석
  function readAndAnalyze(file) {
    const reader = new FileReader()
    reader.onload = async (e) => {
      const base64 = e.target.result
      preview.value = base64
      await analyzeImage(base64)
    }
    reader.readAsDataURL(file)
  }
  
  // 🎯 GPT로 장소 추측
  async function analyzeImage(base64Img) {
    const gptResponse = await callOpenAI(base64Img)
    const place = extractLocation(gptResponse)
    resultPlace.value = place
    const coords = await getLatLng(place)
    if (coords) updateMap(coords.lat, coords.lng)
  }
  
  // ✉️ OpenAI 호출
  async function callOpenAI(base64Img) {
    const res = await fetch('https://api.openai.com/v1/chat/completions', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer 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`, // 🔑 교체 필요
      },
      body: JSON.stringify({
        model: 'gpt-4o',
        messages: [
          {
            role: 'user',
            content: [
              { type: 'text', text: '이 사진을 보고 떠오르는 장소명을 알려줘 (도시, 지역 등)' },
              { type: 'image_url', image_url: { url: base64Img } },
            ],
          },
        ],
      }),
    })
    const data = await res.json()
    return data.choices?.[0]?.message?.content?.trim()
  }
  
  // 🧠 텍스트에서 장소만 추출
  function extractLocation(text) {
    if (!text || typeof text !== 'string') return '알 수 없음'
    const match = text.match(/['"]?([가-힣a-zA-Z ,]+)['"]?/)
    return match ? match[1] : text
  }
  
  // 📍 장소명 → 좌표
  async function getLatLng(placeName) {
    const res = await fetch(`https://maps.googleapis.com/maps/api/geocode/json?address=${encodeURIComponent(placeName)}&key=AIzaSyCSHVlmjjqz44yoKKnKbngFZt-ChIPvve4`)
    const data = await res.json()
    return data.results?.[0]?.geometry?.location
  }
  
  // 🗺️ 지도 초기화
  function initMap() {
    map = new google.maps.Map(document.getElementById('map'), {
      center: { lat: 37.5665, lng: 126.9780 },
      zoom: 5,
      mapId: 'YOUR_MAP_ID' // 🎯 발급받은 mapId로 교체 필요
    })
  }
  
  // 📌 지도에 마커 찍기
  function updateMap(lat, lng) {
    map.setCenter({ lat, lng })
    map.setZoom(12)
  
    if (marker) marker.setMap(null)
    marker = new google.maps.marker.AdvancedMarkerElement({
      position: { lat, lng },
      map,
      title: 'GPT가 추측한 위치',
    })
  }
  </script>
  
  <style scoped>
  #map {
    min-height: 400px;
  }
  </style>
  