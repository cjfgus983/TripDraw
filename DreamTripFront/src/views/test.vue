<template>
  <div class="p-6">
    <h2 class="text-2xl font-bold mb-4">🗺️ 구글맵 테스트</h2>
    <div id="map" class="w-full h-[600px] border rounded"></div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'

const initMap = () => {
  if (!window.google || !window.google.maps) {
    console.error("Google Maps JS API가 아직 로드되지 않았습니다.")
    return
  }

  const map = new google.maps.Map(document.getElementById("map"), {
  center: { lat: 37.5665, lng: 126.9780 },
  zoom: 12,
  mapId: 'YOUR_MAP_ID'  // 🔥 여기에 실제 Map ID를 넣어야 함!
})

  const marker = new google.maps.marker.AdvancedMarkerElement({
  position: { lat: 37.5665, lng: 126.9780 },
  map,
  title: "서울"
});

}

onMounted(() => {
  // 로딩이 느린 경우 재시도
  const tryLoadMap = () => {
    if (window.google && window.google.maps) {
      initMap()
    } else {
      console.log("Google Maps API 대기 중...")
      setTimeout(tryLoadMap, 300)
    }
  }
  tryLoadMap()
})
</script>

<style scoped>
#map {
  min-height: 400px;
}
</style>
