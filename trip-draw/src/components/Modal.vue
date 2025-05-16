<!-- src/components/Modal.vue -->
<template>
    <!-- Teleport to body to overlay everything -->
    <teleport to="body">
      <div v-if="modelValue" class="fixed inset-0 flex items-center justify-center z-50">
        <!-- Backdrop -->
        <div
          class="absolute inset-0 bg-black bg-opacity-50"
          @click="close"
        ></div>
        <!-- Modal Content -->
        <div class="relative bg-white rounded-2xl shadow-xl p-6 max-w-lg w-full z-10">
          <!-- Close Button -->
          <button
            @click="close"
            class="absolute top-4 right-4 text-gray-500 hover:text-gray-700 focus:outline-none"
            aria-label="Close modal"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
          <!-- Slot for header -->
          <header class="mb-4">
            <slot name="header" />
          </header>
          <!-- Slot for body -->
          <main class="mb-4">
            <slot name="body" />
          </main>
          <!-- Slot for footer -->
          <footer class="text-right">
            <slot name="footer" />
          </footer>
        </div>
      </div>
    </teleport>
  </template>
  
  <script setup lang="ts">
  import { defineProps, defineEmits } from 'vue'
  
  const props = defineProps({
    modelValue: { type: Boolean, required: true }
  })
  const emit = defineEmits<{
    (e: 'update:modelValue', value: boolean): void
  }>()
  
  function close() {
    emit('update:modelValue', false)
  }
  </script>
  
  <style scoped>
  /* Optional: 스크롤 락 방지 */
  body.modal-open {
    overflow: hidden;
  }
  </style>
  