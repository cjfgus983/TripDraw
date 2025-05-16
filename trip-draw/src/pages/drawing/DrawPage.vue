<template>
  <div class="flex w-full h-full">
    <!-- Canvas + Tools 영역 -->
    <div class="flex-grow relative flex">
      <!-- 캔버스 컨테이너 (정사각형) -->
      <div ref="canvasContainer" class="flex-grow aspect-square relative">
        <canvas
          ref="canvas"
          @mousedown="startDrawing"
          @mousemove="draw"
          @mouseup="stopDrawing"
          @mouseleave="stopDrawing"
          class="w-full h-full cursor-crosshair"
        ></canvas>
      </div>
      <!-- 툴 패널 -->
      <div class="w-[280px] bg-white shadow-lg p-6 flex flex-col gap-6">
        <!-- 도구 선택 -->
        <section>
          <h3 class="text-lg font-bold mb-2">도구</h3>
          <div class="grid grid-cols-2 gap-2">
            <button @click="setTool('pen')" :class="btnClass('pen')">
              <i class="fas fa-pen mr-1"/>펜
            </button>
            <button @click="setTool('brush')" :class="btnClass('brush')">
              <i class="fas fa-paint-brush mr-1"/>붓
            </button>
            <button @click="setTool('rectangle')" :class="btnClass('rectangle')">
              <i class="fas fa-square mr-1"/>사각형
            </button>
            <button @click="setTool('circle')" :class="btnClass('circle')">
              <i class="fas fa-circle mr-1"/>원
            </button>
            <button @click="setTool('line')" :class="btnClass('line')">
              <i class="fas fa-minus mr-1"/>직선
            </button>
            <button @click="setTool('eraser')" :class="btnClass('eraser')">
              <i class="fas fa-eraser mr-1"/>지우개
            </button>
            <button @click="setTool('fill')" :class="btnClass('fill')">
              <i class="fas fa-fill-drip mr-1"/>채우기
            </button>
            <button @click="clearCanvas" class="tool-btn text-red-500 hover:bg-red-50">
              <i class="fas fa-trash-alt mr-1"/>전체 지우기
            </button>
          </div>
        </section>
        <!-- 굵기 조절 -->
        <section>
          <h3 class="text-lg font-bold mb-2">굵기</h3>
          <input type="range" v-model="lineWidth" min="1" max="50" class="w-full"/>
          <div class="text-right text-sm">{{ lineWidth }}px</div>
        </section>
        <!-- 색상 선택 -->
        <section>
          <h3 class="text-lg font-bold mb-2">색상</h3>
          <input type="color" v-model="currentColor" class="w-full h-10"/>
        </section>
        <!-- 실행취소/다시실행 -->
        <section>
          <h3 class="text-lg font-bold mb-2">실행취소/다시실행</h3>
          <div class="flex gap-2">
            <button @click="undo" :disabled="!canUndo" class="flex-1 tool-btn disabled:opacity-50">
              <i class="fas fa-undo mr-1"/>실행 취소
            </button>
            <button @click="redo" :disabled="!canRedo" class="flex-1 tool-btn disabled:opacity-50">
              <i class="fas fa-redo mr-1"/>다시 실행
            </button>
          </div>
        </section>
        <!-- 이미지 업로드 & 제출 -->
        <section class="mt-auto">
          <button @click="openCropper" class="w-full tool-btn border-dashed border-2">
            <i class="fas fa-image mr-1"/>이미지 업로드
          </button>
          <button @click="handleSubmit" class="w-full mt-4 bg-blue-500 text-white tool-btn">
            <i class="fas fa-paper-plane mr-1"/>제출하기
          </button>
        </section>
      </div>
    </div>

    <!-- 크롭 모달 -->
    <Modal v-model="showCropper">
      <template #header>
        <h2 class="text-xl font-bold">이미지 크롭</h2>
      </template>
      <template #body>
        <vue-cropper
          ref="cropper"
          :src="uploadedImage"
          :aspect-ratio="1"
          :auto-crop-area="1"
          class="h-96 w-full"
        />
      </template>
      <template #footer>
        <button @click="confirmCrop" class="px-4 py-2 bg-green-500 text-white rounded mr-2">확인</button>
        <button @click="showCropper = false" class="px-4 py-2 border rounded">취소</button>
      </template>
    </Modal>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAiStore } from '../../stores/ai'
import VueCropper from 'vue-cropperjs'
import 'cropperjs/dist/cropper.css'
import Modal from '@/components/Modal.vue'

// refs
const canvas = ref<HTMLCanvasElement|null>(null)
const canvasContainer = ref<HTMLDivElement|null>(null)
const ctx = ref<CanvasRenderingContext2D|null>(null)

// 그리기 상태
const isDrawing = ref(false)
const currentTool = ref<'pen'|'brush'|'rectangle'|'circle'|'line'|'eraser'|'fill'>('pen')
const lineWidth = ref(5)
const currentColor = ref('#000000')

// 히스토리
const history = ref<ImageData[]>([])
const historyIndex = ref(-1)
const canUndo = ref(false)
const canRedo = ref(false)
const lastX = ref(0)
const lastY = ref(0)
let lastMouseEvent = <MouseEvent>{}

// 라우터 & 스토어
const router = useRouter()
const aiStore = useAiStore()

// 크롭퍼 상태
const showCropper = ref(false)
const uploadedImage = ref('')
const cropper = ref<any>(null)

// mounted
onMounted(() => {
  // willReadFrequently 옵션 추가
  ctx.value = canvas.value!.getContext('2d', { willReadFrequently: true })
  resizeCanvas()
  window.addEventListener('resize', resizeCanvas)
  saveState()
})
onUnmounted(() => {
  window.removeEventListener('resize', resizeCanvas)
})

// 캔버스 초기화
function resizeCanvas() {
  const rect = canvasContainer.value!.getBoundingClientRect()
  const size = rect.width
  canvas.value!.width = size
  canvas.value!.height = size
  ctx.value!.fillStyle = '#ffffff'
  ctx.value!.fillRect(0, 0, size, size)
  saveState()
}

// 툴 버튼 클래스
function btnClass(tool: string) {
  return ['tool-btn', currentTool.value===tool?'bg-blue-100':''].join(' ')
}
function setTool(tool: typeof currentTool.value) {
  currentTool.value = tool
}

// 그리기
function startDrawing(e: MouseEvent) {
  const rect = canvas.value!.getBoundingClientRect()
  const x = e.clientX - rect.left
  const y = e.clientY - rect.top
  if (currentTool.value==='fill') {
    floodFill(Math.floor(x), Math.floor(y), currentColor.value)
    return
  }
  isDrawing.value = true
  lastX.value = x
  lastY.value = y
}
function draw(e: MouseEvent) {
  if (!ctx.value) return
  const rect = canvas.value!.getBoundingClientRect()
  const x = e.clientX - rect.left
  const y = e.clientY - rect.top

  if (currentTool.value==='rectangle' || currentTool.value==='circle' || currentTool.value==='line') {
    if (!isDrawing.value) return
    // clear + restore
    ctx.value.clearRect(0,0,canvas.value!.width,canvas.value!.height)
    if (historyIndex.value >= 0) {
      ctx.value.putImageData(history.value[historyIndex.value],0,0)
    }
    ctx.value.beginPath()
    ctx.value.strokeStyle = currentColor.value
    ctx.value.lineWidth = lineWidth.value
    if (currentTool.value==='rectangle') {
      ctx.value.strokeRect(lastX.value, lastY.value, x-lastX.value, y-lastY.value)
    } else if (currentTool.value==='circle') {
      const r = Math.hypot(x-lastX.value, y-lastY.value)
      ctx.value.arc(lastX.value, lastY.value, r, 0, Math.PI*2)
      ctx.value.stroke()
    } else if (currentTool.value==='line') {
      ctx.value.moveTo(lastX.value, lastY.value)
      ctx.value.lineTo(x, y)
      ctx.value.stroke()
    }
  } else {
    if (!isDrawing.value) return
    ctx.value.beginPath()
    ctx.value.lineCap = 'round'
    ctx.value.lineWidth = currentTool.value==='brush'?lineWidth.value*2:lineWidth.value
    ctx.value.strokeStyle = currentTool.value==='eraser'? '#ffffff': currentColor.value
    ctx.value.globalAlpha = currentTool.value==='brush'?0.5:1
    ctx.value.moveTo(lastX.value, lastY.value)
    ctx.value.lineTo(x, y)
    ctx.value.stroke()
    ctx.value.globalAlpha = 1
    lastX.value = x
    lastY.value = y
  }
}
function stopDrawing() {
  if (!isDrawing.value) return
  isDrawing.value = false
  // shape finalize
  draw(lastMouseEvent)
  saveState()
}
function clearCanvas() {
  ctx.value!.fillStyle = '#ffffff'
  ctx.value!.fillRect(0,0,canvas.value!.width,canvas.value!.height)
  saveState()
}

// 히스토리
function saveState() {
  historyIndex.value++
  history.value.splice(historyIndex.value)
  history.value.push(ctx.value!.getImageData(0,0,canvas.value!.width,canvas.value!.height))
  updateHistory()
}
function updateHistory() {
  canUndo.value = historyIndex.value>0
  canRedo.value = historyIndex.value<history.value.length-1
}
function undo() {
  if (!canUndo.value) return
  historyIndex.value--
  ctx.value!.putImageData(history.value[historyIndex.value],0,0)
  updateHistory()
}
function redo() {
  if (!canRedo.value) return
  historyIndex.value++
  ctx.value!.putImageData(history.value[historyIndex.value],0,0)
  updateHistory()
}

// fill
function floodFill(sx:number, sy:number, hex:string) {
  const img = ctx.value!.getImageData(0,0,canvas.value!.width,canvas.value!.height)
  const data=img.data, w=canvas.value!.width, h=canvas.value!.height
  const [fr,fg,fb]=hexToRgba(hex)
  const target=getPixel(data,sx,sy)
  const stack=[[sx,sy]]
  const eq=(c:number[])=>c.every((v,i)=>v===target[i])
  while(stack.length){
    const [x,y]=stack.pop()!
    const idx=(y*w+x)*4
    if(!eq([data[idx],data[idx+1],data[idx+2],data[idx+3]])) continue
    data[idx]=fr; data[idx+1]=fg; data[idx+2]=fb; data[idx+3]=255
    if(x>0) stack.push([x-1,y]); if(x<w-1) stack.push([x+1,y])
    if(y>0) stack.push([x,y-1]); if(y<h-1) stack.push([x,y+1])
  }
  ctx.value!.putImageData(img,0,0)
  saveState()
}
function getPixel(d:Uint8ClampedArray,x:number,y:number){
  const idx=(y*canvas.value!.width+x)*4
  return [d[idx],d[idx+1],d[idx+2],d[idx+3]]
}
function hexToRgba(hex:string){
  const n=parseInt(hex.slice(1),16)
  return [(n>>16)&255,(n>>8)&255,n&255]
}

// 업로드 → 크롭
function openCropper() {
  const inp=document.createElement('input')
  inp.type='file'; inp.accept='image/*'
  inp.onchange = e => {
    const file=(e.target as HTMLInputElement).files![0]
    const reader=new FileReader()
    reader.onload = ()=> {
      uploadedImage.value = reader.result as string
      // 이미지를 읽은 뒤에 모달 열기
      showCropper.value = true
    }
    reader.readAsDataURL(file)
  }
  inp.click()
}
function confirmCrop() {
  const canvasCrop = cropper.value.getCroppedCanvas({
    width: canvas.value!.width,
    height: canvas.value!.height
  })
  if (!canvasCrop) return
  const src = canvasCrop.toDataURL('image/png')
  const img = new Image()
  img.onload = () => {
    ctx.value!.clearRect(0,0,canvas.value!.width,canvas.value!.height)
    ctx.value!.drawImage(img,0,0)
    saveState()
    showCropper.value = false
  }
  img.src = src
}

// 제출
function handleSubmit() {
  // Base64 형태로 저장
  const dataUrl = canvas.value!.toDataURL('image/png')
  aiStore.setOriginalDataUrl(dataUrl)

  // Blob 형태로 저장 후 이동
  canvas.value!.toBlob(blob => {
    if (blob) {
      aiStore.setOriginalBlob(blob)
      router.push('/loading')
    }
  }, 'image/png')
}
</script>

<style scoped>
.tool-btn {
  @apply flex items-center justify-center p-2 rounded-lg border hover:bg-blue-50 transition;
}
</style>
