<!-- The exported code uses Tailwind CSS. Install Tailwind CSS in your dev environment to ensure all styles work. -->
<template>
    <div class="flex w-full">
      <!-- Canvas Area -->
      <div class="flex-grow relative" ref="canvasContainer">
        <canvas
          ref="canvas"
          @mousedown="startDrawing"
          @mousemove="draw"
          @mouseup="stopDrawing"
          @mouseleave="stopDrawing"
          class="w-full h-full cursor-crosshair"
        ></canvas>
      </div>
      <!-- Tools Panel -->
      <div class="w-[280px] bg-white shadow-lg p-6 flex flex-col gap-6">
        <!-- Drawing Tools -->
        <div class="space-y-4">
          <h3 class="text-lg font-bold mb-4">도구</h3>
          <div class="grid grid-cols-2 gap-3">
            <button
              @click="setTool('pen')"
              :class="{'bg-blue-100': currentTool === 'pen'}"
              class="flex items-center justify-center p-3 rounded-lg border hover:bg-blue-50 transition-colors !rounded-button whitespace-nowrap cursor-pointer"
            >
              <i class="fas fa-pen mr-2"></i> 펜
            </button>
            <button
              @click="setTool('brush')"
              :class="{'bg-blue-100': currentTool === 'brush'}"
              class="flex items-center justify-center p-3 rounded-lg border hover:bg-blue-50 transition-colors !rounded-button whitespace-nowrap cursor-pointer"
            >
              <i class="fas fa-paint-brush mr-2"></i> 붓
            </button>
            <button
              @click="setTool('rectangle')"
              :class="{'bg-blue-100': currentTool === 'rectangle'}"
              class="flex items-center justify-center p-3 rounded-lg border hover:bg-blue-50 transition-colors !rounded-button whitespace-nowrap cursor-pointer"
            >
              <i class="fas fa-square mr-2"></i> 사각형
            </button>
            <button
              @click="setTool('circle')"
              :class="{'bg-blue-100': currentTool === 'circle'}"
              class="flex items-center justify-center p-3 rounded-lg border hover:bg-blue-50 transition-colors !rounded-button whitespace-nowrap cursor-pointer"
            >
              <i class="fas fa-circle mr-2"></i> 원
            </button>
            <button
              @click="setTool('line')"
              :class="{'bg-blue-100': currentTool === 'line'}"
              class="flex items-center justify-center p-3 rounded-lg border hover:bg-blue-50 transition-colors !rounded-button whitespace-nowrap cursor-pointer"
            >
              <i class="fas fa-minus mr-2"></i> 직선
            </button>
            <button
              @click="setTool('eraser')"
              :class="{'bg-blue-100': currentTool === 'eraser'}"
              class="flex items-center justify-center p-3 rounded-lg border hover:bg-blue-50 transition-colors !rounded-button whitespace-nowrap cursor-pointer"
            >
              <i class="fas fa-eraser mr-2"></i> 지우개
            </button>
            <button
              @click="setTool('fill')"
              :class="{'bg-blue-100': currentTool === 'fill'}"
              class="flex items-center justify-center p-3 rounded-lg border hover:bg-blue-50 transition-colors !rounded-button whitespace-nowrap cursor-pointer"
            >
              <i class="fas fa-fill-drip mr-2"></i> 채우기
            </button>
            <button
              @click="clearCanvas"
              class="flex items-center justify-center p-3 rounded-lg border hover:bg-red-50 text-red-500 transition-colors !rounded-button whitespace-nowrap cursor-pointer"
            >
              <i class="fas fa-trash-alt mr-2"></i> 전체 지우기
            </button>
          </div>
        </div>
        <!-- Size Control -->
        <div class="space-y-4">
          <h3 class="text-lg font-bold mb-4">크기 조절</h3>
          <div class="space-y-2">
            <label class="block text-sm text-gray-600">굵기</label>
            <input
              type="range"
              v-model="lineWidth"
              min="1"
              max="50"
              class="w-full"
            />
            <div class="text-sm text-gray-500 text-right">{{ lineWidth }}px</div>
          </div>
        </div>
        <!-- Color Picker -->
        <div class="space-y-4">
          <h3 class="text-lg font-bold mb-4">색상</h3>
          <input
            type="color"
            v-model="currentColor"
            class="w-full h-10 cursor-pointer"
          />
        </div>
        <!-- History Controls -->
        <div class="space-y-4">
          <h3 class="text-lg font-bold mb-4">실행 취소/다시 실행</h3>
          <div class="flex gap-3">
            <button
              @click="undo"
              :disabled="!canUndo"
              class="flex-1 p-3 rounded-lg border hover:bg-blue-50 transition-colors disabled:opacity-50 disabled:cursor-not-allowed !rounded-button whitespace-nowrap cursor-pointer"
            >
              <i class="fas fa-undo mr-2"></i> 실행 취소
            </button>
            <button
              @click="redo"
              :disabled="!canRedo"
              class="flex-1 p-3 rounded-lg border hover:bg-blue-50 transition-colors disabled:opacity-50 disabled:cursor-not-allowed !rounded-button whitespace-nowrap cursor-pointer"
            >
              <i class="fas fa-redo mr-2"></i> 다시 실행
            </button>
          </div>
        </div>
        <!-- Image Upload -->
        <div class="space-y-4">
          <h3 class="text-lg font-bold mb-4">이미지 추가</h3>
          <button
            @click="openImageUpload"
            class="w-full p-3 rounded-lg border-2 border-dashed border-blue-300 hover:border-blue-400 hover:bg-blue-50 transition-colors !rounded-button whitespace-nowrap cursor-pointer"
          >
            <i class="fas fa-image mr-2"></i> 이미지 업로드
          </button>
        </div>
        <!-- Submit Button -->
        <button
          @click="handleSubmit"
          class="mt-auto w-full p-3 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition-colors !rounded-button whitespace-nowrap cursor-pointer"
        >
          <i class="fas fa-paper-plane mr-2"></i> 제출하기
        </button>
      </div>
      <!-- Image Upload Modal -->
      <div
        v-if="showImageModal"
        class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
      >
        <div class="bg-white rounded-lg p-6 w-[500px]">
          <h3 class="text-xl font-bold mb-4">이미지 업로드</h3>
          <div
            class="border-2 border-dashed border-gray-300 rounded-lg p-8 text-center cursor-pointer hover:border-blue-400 transition-colors"
            @dragover.prevent
            @drop.prevent="handleDrop"
            @click="triggerFileInput"
          >
            <i class="fas fa-cloud-upload-alt text-4xl text-gray-400 mb-4"></i>
            <p class="text-gray-600">
              이미지를 드래그하여 놓거나 클릭하여 선택하세요
            </p>
          </div>
          <input
            type="file"
            ref="fileInput"
            @change="handleFileSelect"
            accept="image/*"
            class="hidden"
          />
          <div class="flex justify-end mt-6 gap-3">
            <button
              @click="closeImageModal"
              class="px-4 py-2 border rounded-lg hover:bg-gray-50 transition-colors !rounded-button whitespace-nowrap cursor-pointer"
            >
              취소
            </button>
          </div>
        </div>
      </div>
    </div>
  </template>
  <script lang="ts" setup>
  import { ref, onMounted, onUnmounted } from "vue";
  
  const canvas = ref<HTMLCanvasElement | null>(null);
  const canvasContainer = ref<HTMLDivElement | null>(null);
  const ctx = ref<CanvasRenderingContext2D | null>(null);
  const isDrawing = ref(false);
  const currentTool = ref("pen");
  const lineWidth = ref(5);
  const currentColor = ref("#000000");
  const showImageModal = ref(false);
  const fileInput = ref<HTMLInputElement | null>(null);
  const history = ref<ImageData[]>([]);
  const historyIndex = ref(-1);
  const canUndo = ref(false);
  const canRedo = ref(false);
  let lastX = 0;
  let lastY = 0;
  let lastMouseEvent: MouseEvent;
  
  onMounted(() => {
    if (canvas.value && canvasContainer.value) {
      ctx.value = canvas.value.getContext("2d");
      resizeCanvas();
      window.addEventListener("resize", resizeCanvas);
      saveState();
    }
  });
  
  onUnmounted(() => {
    window.removeEventListener("resize", resizeCanvas);
  });
  
  const resizeCanvas = () => {
    if (canvas.value && canvasContainer.value) {
      const rect = canvasContainer.value.getBoundingClientRect();
      canvas.value.width = rect.width;
      canvas.value.height = rect.height;
    }
  };
  
  const setTool = (tool: string) => {
    currentTool.value = tool;
  };
  
  const startDrawing = (e: MouseEvent) => {
  const rect = canvas.value?.getBoundingClientRect();
  if (!rect) return;

  const x = Math.floor(e.clientX - rect.left);
  const y = Math.floor(e.clientY - rect.top);

  if (currentTool.value === "fill") {
    floodFill(x, y, currentColor.value);
    return;
  }

  isDrawing.value = true;
  lastX = x;
  lastY = y;
};

  
  const draw = (e: MouseEvent) => {
    lastMouseEvent = e;
    if (currentTool.value === "fill") return;
    if (!ctx.value || !canvas.value) return;
  
    const rect = canvas.value.getBoundingClientRect();
    const x = e.clientX - rect.left;
    const y = e.clientY - rect.top;
  
    if (currentTool.value === "eraser" || currentTool.value === "pen" || currentTool.value === "brush") {
      if (!isDrawing.value) return;
      ctx.value.beginPath();
      ctx.value.lineWidth = currentTool.value === "brush" ? lineWidth.value * 2 : lineWidth.value;
      ctx.value.lineCap = "round";
      ctx.value.strokeStyle = currentTool.value === "eraser" ? "#ffffff" : currentColor.value;
      ctx.value.globalAlpha = currentTool.value === "brush" ? 0.5 : 1.0;
      ctx.value.moveTo(lastX, lastY);
      ctx.value.lineTo(x, y);
      ctx.value.stroke();
      ctx.value.globalAlpha = 1.0;
      lastX = x;
      lastY = y;
    } else if (currentTool.value === "rectangle" || currentTool.value === "circle" || currentTool.value === "line") {
      if (!isDrawing.value) return;
      ctx.value.clearRect(0, 0, canvas.value.width, canvas.value.height);
      if (historyIndex.value >= 0) {
        ctx.value.putImageData(history.value[historyIndex.value], 0, 0);
      }
      ctx.value.beginPath();
      ctx.value.strokeStyle = currentColor.value;
      ctx.value.lineWidth = lineWidth.value;
      if (currentTool.value === "rectangle") {
        ctx.value.strokeRect(lastX, lastY, x - lastX, y - lastY);
      } else if (currentTool.value === "circle") {
        const radius = Math.sqrt(Math.pow(x - lastX, 2) + Math.pow(y - lastY, 2));
        ctx.value.arc(lastX, lastY, radius, 0, Math.PI * 2);
        ctx.value.stroke();
      } else if (currentTool.value === "line") {
        ctx.value.moveTo(lastX, lastY);
        ctx.value.lineTo(x, y);
        ctx.value.stroke();
      }
    }
    if (currentTool.value === "fill") {
        floodFill(Math.floor(e.clientX - rect.left), Math.floor(e.clientY - rect.top), currentColor.value);
        return;
    }
  };
  
  const stopDrawing = () => {
    if (isDrawing.value) {
      isDrawing.value = false;
      if (currentTool.value === "rectangle" || currentTool.value === "circle" || currentTool.value === "line") {
        draw(lastMouseEvent);
      }
      saveState();
    }
  };
  
  const clearCanvas = () => {
    if (ctx.value && canvas.value) {
      ctx.value.fillStyle = "#ffffff";
      ctx.value.fillRect(0, 0, canvas.value.width, canvas.value.height);
      saveState();
    }
  };
  
  const saveState = () => {
    if (ctx.value && canvas.value) {
      historyIndex.value++;
      history.value.splice(historyIndex.value);
      history.value.push(ctx.value.getImageData(0, 0, canvas.value.width, canvas.value.height));
      updateHistoryButtons();
    }
  };
  
  const updateHistoryButtons = () => {
    canUndo.value = historyIndex.value > 0;
    canRedo.value = historyIndex.value < history.value.length - 1;
  };
  
  const undo = () => {
    if (historyIndex.value > 0 && ctx.value) {
      historyIndex.value--;
      ctx.value.putImageData(history.value[historyIndex.value], 0, 0);
      updateHistoryButtons();
    }
  };
  
  const redo = () => {
    if (historyIndex.value < history.value.length - 1 && ctx.value) {
      historyIndex.value++;
      ctx.value.putImageData(history.value[historyIndex.value], 0, 0);
      updateHistoryButtons();
    }
  };
  
  const openImageUpload = () => {
    showImageModal.value = true;
  };
  
  const closeImageModal = () => {
    showImageModal.value = false;
  };
  
  const triggerFileInput = () => {
    fileInput.value?.click();
  };
  
  const handleFileSelect = (event: Event) => {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      loadImage(input.files[0]);
    }
  };
  
  const handleDrop = (event: DragEvent) => {
    const file = event.dataTransfer?.files[0];
    if (file) {
      loadImage(file);
    }
  };
  
  const loadImage = (file: File) => {
    const reader = new FileReader();
    reader.onload = (e) => {
      const img = new Image();
      img.onload = () => {
        if (ctx.value && canvas.value) {
          const aspectRatio = img.width / img.height;
          let newWidth = canvas.value.width;
          let newHeight = newWidth / aspectRatio;
          if (newHeight > canvas.value.height) {
            newHeight = canvas.value.height;
            newWidth = newHeight * aspectRatio;
          }
          const x = (canvas.value.width - newWidth) / 2;
          const y = canvas.value.height - newHeight;
          ctx.value.drawImage(img, x, y, newWidth, newHeight);
          saveState();
        }
      };
      img.src = e.target?.result as string;
    };
    reader.readAsDataURL(file);
    closeImageModal();
  };
  
  const handleSubmit = () => {
    if (canvas.value) {
      const imageData = canvas.value.toDataURL("image/png");
      console.log("Submitting drawing:", imageData);
      // Upload logic here
    }
  };

  const floodFill = (startX: number, startY: number, fillColor: string) => {
  if (!ctx.value || !canvas.value) return;

  const imageData = ctx.value.getImageData(0, 0, canvas.value.width, canvas.value.height);
  const data = imageData.data;

  const width = canvas.value.width;
  const height = canvas.value.height;
  const stack = [[startX, startY]];

  const targetColor = getPixel(data, startX, startY);
  const fillRgba = hexToRgba(fillColor);

  if (colorsMatch(targetColor, fillRgba)) return;

  while (stack.length) {
    const [x, y] = stack.pop()!;
    const currentColor = getPixel(data, x, y);

    if (!colorsMatch(currentColor, targetColor)) continue;

    setPixel(data, x, y, fillRgba);

    if (x > 0) stack.push([x - 1, y]);
    if (x < width - 1) stack.push([x + 1, y]);
    if (y > 0) stack.push([x, y - 1]);
    if (y < height - 1) stack.push([x, y + 1]);
    if (x > 0 && y > 0) stack.push([x - 1, y - 1]);       // 좌상
    if (x < width - 1 && y > 0) stack.push([x + 1, y - 1]); // 우상
    if (x > 0 && y < height - 1) stack.push([x - 1, y + 1]); // 좌하
    if (x < width - 1 && y < height - 1) stack.push([x + 1, y + 1]); // 우하
  }

  ctx.value.putImageData(imageData, 0, 0);
  saveState();
};

const getPixel = (data: Uint8ClampedArray, x: number, y: number) => {
  const index = (y * canvas.value!.width + x) * 4;
  return [data[index], data[index + 1], data[index + 2], data[index + 3]];
};

const setPixel = (data: Uint8ClampedArray, x: number, y: number, [r, g, b, a]: number[]) => {
  const index = (y * canvas.value!.width + x) * 4;
  data[index] = r;
  data[index + 1] = g;
  data[index + 2] = b;
  data[index + 3] = a;
};

const colorsMatch = (color1: number[], color2: number[]) => {
  return color1[0] === color2[0] && color1[1] === color2[1] && color1[2] === color2[2] && color1[3] === color2[3];
};

const hexToRgba = (hex: string) => {
  const bigint = parseInt(hex.slice(1), 16);
  const r = (bigint >> 16) & 255;
  const g = (bigint >> 8) & 255;
  const b = bigint & 255;
  return [r, g, b, 255];
};

  </script>
  