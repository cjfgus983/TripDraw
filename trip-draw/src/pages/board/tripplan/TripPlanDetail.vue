<!-- The exported code uses Tailwind CSS. Install Tailwind CSS in your dev environment to ensure all styles work. -->
<template>
    <div class="min-h-screen bg-gray-50">
      <div class="container mx-auto px-6 py-8 max-w-7xl">
        <div class="flex flex-col md:flex-row gap-6">
          <!-- 지도 섹션 (60%) -->
          <div class="w-full md:w-3/5">
            <div class="bg-white rounded-lg shadow-md p-4 mb-4">
              <!-- 카테고리 필터 -->
              <div class="flex space-x-3 mb-4">
                <button
                  v-for="(category, index) in categories"
                  :key="index"
                  @click="selectCategory(category.id)"
                  :class="[
  'px-4 py-2 rounded-button whitespace-nowrap cursor-pointer transition-all',
  selectedCategory === category.id
  ? `bg-${category.color}-500 text-white`
  : 'bg-gray-100 hover:bg-gray-200'
  ]"
                >
                  <i :class="`fas ${category.icon} mr-2`"></i>
                  {{ category.name }}
                </button>
              </div>
              <!-- 지도 -->
              <div
                class="h-[668px] relative rounded-lg overflow-hidden"
                ref="mapContainer"
              >
                <div id="map" class="w-full h-full"></div>
              </div>
            </div>
          </div>
          <!-- 리스트 섹션 (40%) -->
          <div class="w-full md:w-2/5">
            <div class="bg-white rounded-lg shadow-md flex flex-col h-[668px]">
              <!-- 리스트 헤더 -->
              <div class="p-4 border-b border-gray-200">
                <h2 class="text-xl font-semibold text-gray-800">
                  방문 장소 목록
                </h2>
                <p class="text-sm text-gray-500">
                  선택한 카테고리: {{ getCategoryName(selectedCategory) }}
                </p>
              </div>
              <!-- 리스트 내용 -->
              <div class="flex-1 overflow-y-auto p-4">
                <div
                  v-if="filteredPlaces.length === 0"
                  class="flex flex-col items-center justify-center h-full text-gray-500"
                >
                  <i class="fas fa-map-marker-alt text-4xl mb-3"></i>
                  <p>선택한 카테고리에 장소가 없습니다</p>
                </div>
                <div v-else>
                  <div
                    v-for="(place, index) in filteredPlaces"
                    :key="index"
                    class="mb-4 p-4 border border-gray-200 rounded-lg hover:shadow-md transition-shadow cursor-pointer"
                    :class="{
  'border-blue-500 bg-blue-50': selectedPlace === place.id,
  'opacity-50': draggedItemIndex === index,
  'border-t-4 border-blue-500': dragOverItemIndex === index
  }"
                    draggable="true"
                    @dragstart="handleDragStart(index)"
                    @dragover="handleDragOver($event, index)"
                    @drop="handleDrop"
                    @dragend="handleDragEnd"
                    @click="selectPlace(place.id)"
                  >
                    <div class="flex justify-between items-start">
                      <div>
                        <h3 class="font-medium text-gray-800">
                          {{ place.name }}
                        </h3>
                        <p class="text-sm text-gray-600 mt-1">
                          {{ place.address }}
                        </p>
                        <div class="flex items-center mt-2">
                          <span
                            class="inline-block w-3 h-3 rounded-full mr-2"
                            :class="`bg-${getCategoryColor(place.categoryId)}-500`"
                          ></span>
                          <span class="text-sm text-gray-600"
                            >{{ getCategoryName(place.categoryId) }}</span
                          >
                        </div>
                      </div>
                      <div class="text-right">
                        <span
                          class="text-sm font-medium bg-gray-100 px-2 py-1 rounded"
                          >{{ place.visitTime }}</span
                        >
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <!-- 수정하기 버튼 -->
              <div class="p-4 border-t border-gray-200">
                <button
                  @click="applyChanges"
                  :disabled="!isModified"
                  :class="[
  'ml-auto block px-6 py-3 rounded-button whitespace-nowrap cursor-pointer transition-colors',
  isModified ? 'bg-[#9FB3DF] text-white hover:bg-[#8FA3CF]' : 'bg-gray-200 text-gray-500 cursor-not-allowed'
  ]"
                >
                  수정하기
                </button>
              </div>
            </div>
          </div>
        </div>
        <!-- 추가 카테고리 및 입력 섹션 -->
        <div class="mt-8 bg-white rounded-lg shadow-md p-6">
          <div class="mb-6">
            <h3 class="text-lg font-semibold mb-4">카테고리 선택</h3>
            <div class="flex flex-wrap gap-3">
              <button
                v-for="tag in additionalTags"
                :key="tag.id"
                @click="toggleTag(tag.id)"
                :class="[
  'px-4 py-2 rounded-button whitespace-nowrap cursor-pointer transition-all',
  selectedTags.includes(tag.id)
  ? 'bg-[#9FB3DF] text-white'
  : 'bg-gray-100 hover:bg-gray-200'
  ]"
              >
                {{ tag.name }}
              </button>
            </div>
          </div>
          <div class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2"
                >제목</label
              >
              <input
                type="text"
                v-model="title"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-[#9FB3DF] focus:border-[#9FB3DF]"
                placeholder="제목을 입력하세요"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2"
                >내용</label
              >
              <textarea
                v-model="content"
                rows="20"
                class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-[#9FB3DF] focus:border-[#9FB3DF]"
                placeholder="내용을 입력하세요"
              ></textarea>
            </div>
            <div class="flex justify-end">
              <button
                class="px-6 py-3 bg-[#9FB3DF] text-white rounded-button hover:bg-[#8FA3CF] transition-colors cursor-pointer whitespace-nowrap"
                @click="submitForm"
              >
                등록하기
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script lang="ts" setup>
  import { ref, onMounted, computed, watch } from "vue";
  const title = ref("");
  const content = ref("");
  const selectedTags = ref<number[]>([]);
  const isModified = ref(false);
  const additionalTags = [
    { id: 1, name: "힐링" },
    { id: 2, name: "액티비티" },
    { id: 3, name: "데이트" },
    { id: 4, name: "먹부림" },
    { id: 5, name: "자연" },
    { id: 6, name: "도심" },
  ];
  const toggleTag = (tagId: number) => {
    const index = selectedTags.value.indexOf(tagId);
    if (index === -1) {
      selectedTags.value.push(tagId);
    } else {
      selectedTags.value.splice(index, 1);
    }
  };
  const submitForm = () => {
    // 실제 구현 시 API 호출
    console.log({
      title: title.value,
      content: content.value,
      selectedTags: selectedTags.value,
    });
  };
  const draggedItemIndex = ref<number | null>(null);
  const dragOverItemIndex = ref<number | null>(null);
  const handleDragStart = (index: number) => {
    draggedItemIndex.value = index;
  };
  const handleDragOver = (event: DragEvent, index: number) => {
    event.preventDefault();
    dragOverItemIndex.value = index;
  };
  const handleDrop = (event: DragEvent) => {
    event.preventDefault();
    if (draggedItemIndex.value !== null && dragOverItemIndex.value !== null) {
      const items = [...filteredPlaces.value];
      const draggedItem = items[draggedItemIndex.value];
      items.splice(draggedItemIndex.value, 1);
      items.splice(dragOverItemIndex.value, 0, draggedItem);
      places.value = items;
      isModified.value = true;
    }
    draggedItemIndex.value = null;
    dragOverItemIndex.value = null;
  };
  const handleDragEnd = () => {
    draggedItemIndex.value = null;
    dragOverItemIndex.value = null;
  };
  const categories = [
    { id: 1, name: "관광지", color: "blue", icon: "fa-landmark" },
    { id: 2, name: "음식점", color: "red", icon: "fa-utensils" },
    { id: 3, name: "카페", color: "green", icon: "fa-coffee" },
  ];
  const places = ref([
    {
      id: 1,
      name: "경복궁",
      address: "서울 종로구 사직로 161",
      categoryId: 1,
      visitTime: "10:00 - 12:00",
      lat: 37.579617,
      lng: 126.977041,
    },
    {
      id: 2,
      name: "광화문 광장",
      address: "서울 종로구 세종로 172",
      categoryId: 1,
      visitTime: "12:30 - 13:30",
      lat: 37.572976,
      lng: 126.976882,
    },
    {
      id: 3,
      name: "삼청동 수제비",
      address: "서울 종로구 삼청로 100",
      categoryId: 2,
      visitTime: "13:00 - 14:00",
      lat: 37.582839,
      lng: 126.981549,
    },
    {
      id: 4,
      name: "스타벅스 광화문점",
      address: "서울 종로구 세종대로 167",
      categoryId: 3,
      visitTime: "14:30 - 15:30",
      lat: 37.573214,
      lng: 126.976879,
    },
    {
      id: 5,
      name: "인사동 전통 찻집",
      address: "서울 종로구 인사동길 12",
      categoryId: 3,
      visitTime: "16:00 - 17:00",
      lat: 37.574187,
      lng: 126.985565,
    },
    {
      id: 6,
      name: "북촌 한옥마을",
      address: "서울 종로구 계동길 37",
      categoryId: 1,
      visitTime: "15:00 - 17:00",
      lat: 37.582285,
      lng: 126.983922,
    },
    {
      id: 7,
      name: "통인시장",
      address: "서울 종로구 자하문로 15길",
      categoryId: 2,
      visitTime: "11:30 - 12:30",
      lat: 37.579415,
      lng: 126.969254,
    },
  ]);
  const selectedCategory = ref(0);
  const selectedPlace = ref<number | null>(null);
  const map = ref<any>(null);
  const markers = ref<any[]>([]);
  const mapContainer = ref<HTMLElement | null>(null);
  const filteredPlaces = computed(() => {
    if (selectedCategory.value === 0) {
      return places.value;
    }
    return places.value.filter(
      (place) => place.categoryId === selectedCategory.value,
    );
  });
  const selectCategory = (categoryId: number) => {
    selectedCategory.value =
      categoryId === selectedCategory.value ? 0 : categoryId;
    updateMarkers();
  };
  const selectPlace = (placeId: number) => {
    selectedPlace.value = placeId;
    const place = places.value.find((p) => p.id === placeId);
    if (place && map.value) {
      map.value.setCenter(new window.kakao.maps.LatLng(place.lat, place.lng));
      const marker = markers.value.find((m) => m.id === placeId);
      if (marker) {
        // 마커 정보창 표시 로직
      }
    }
  };
  const getCategoryName = (categoryId: number): string => {
    if (categoryId === 0) return "전체";
    const category = categories.find((c) => c.id === categoryId);
    return category ? category.name : "";
  };
  const getCategoryColor = (categoryId: number): string => {
    const category = categories.find((c) => c.id === categoryId);
    return category ? category.color : "gray";
  };
  const updateMarkers = () => {
    markers.value.forEach((marker) => {
      marker.setMap(null);
    });
    markers.value = [];
    filteredPlaces.value.forEach((place) => {
      const category = categories.find((c) => c.id === place.categoryId);
      const markerPosition = new window.kakao.maps.LatLng(place.lat, place.lng);
      const marker = new window.kakao.maps.Marker({
        position: markerPosition,
        map: map.value,
      });
      marker.id = place.id;
      window.kakao.maps.event.addListener(marker, "click", () => {
        selectPlace(place.id);
      });
      markers.value.push(marker);
    });
  };
  const applyChanges = () => {
    if (isModified.value) {
      alert("수정사항이 저장되었습니다.");
      isModified.value = false;
    }
  };
  const initMap = () => {
    if (window.kakao && window.kakao.maps && mapContainer.value) {
      const options = {
        center: new window.kakao.maps.LatLng(37.576, 126.9769),
        level: 5,
      };
      map.value = new window.kakao.maps.Map(mapContainer.value, options);
      updateMarkers();
    } else {
      console.error("카카오맵 API가 로드되지 않았습니다.");
    }
  };
  watch(selectedCategory, () => {
    updateMarkers();
  });
  onMounted(() => {
    setTimeout(() => {
      initMap();
    }, 500);
  });
  </script>
  
  <style scoped>
  .map-container {
    position: relative;
    width: 100%;
    height: 100%;
  }
  [draggable="true"] {
    cursor: move;
    user-select: none;
  }
  [draggable="true"]:hover {
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  }
  ::-webkit-scrollbar {
    width: 8px;
  }
  ::-webkit-scrollbar-track {
    background: #f1f1f1;
    border-radius: 10px;
  }
  ::-webkit-scrollbar-thumb {
    background: #c1c1c1;
    border-radius: 10px;
  }
  ::-webkit-scrollbar-thumb:hover {
    background: #a1a1a1;
  }
  </style>
  