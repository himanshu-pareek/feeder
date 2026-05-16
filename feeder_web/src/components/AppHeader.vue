<script setup lang="ts">
import { ref } from 'vue';

const emit = defineEmits<{
  (e: 'subscribe', url: string): void;
}>();

const isModalOpen = ref(false);
const url = ref('');

const openModal = () => {
  isModalOpen.value = true;
};

const closeModal = () => {
  isModalOpen.value = false;
  url.value = '';
};

const onSubscribe = () => {
  if (url.value.trim()) {
    emit('subscribe', url.value.trim());
    closeModal();
  }
};
</script>

<template>
  <header class="h-17.5 flex justify-between items-center px-8 bg-gray-50 border-b border-gray-200">
    <h1 class="text-2xl font-bold text-gray-900">Feeder</h1>
    <button 
      @click="openModal"
      data-test="open-modal-button"
      class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors cursor-pointer"
    >
      Subscribe
    </button>

    <!-- Subscribe Modal -->
    <div v-if="isModalOpen" class="fixed inset-0 z-50 flex items-center justify-center bg-black/50">
      <div class="bg-white p-6 rounded-lg shadow-xl w-full max-w-md mx-4">
        <h2 class="text-xl font-semibold mb-4 text-gray-900">Subscribe to a new feed</h2>
        <div class="space-y-4">
          <input 
            v-model="url" 
            type="url" 
            placeholder="Enter RSS Feed URL" 
            data-test="subscribe-input"
            class="w-full p-2 border border-gray-300 rounded focus:ring-2 focus:ring-blue-500 focus:outline-none text-gray-900"
            @keyup.enter="onSubscribe"
            autofocus
          />
          <div class="flex justify-end gap-3">
            <button 
              @click="closeModal"
              data-test="cancel-button"
              class="px-4 py-2 text-gray-600 hover:text-gray-800 cursor-pointer"
            >
              Cancel
            </button>
            <button 
              @click="onSubscribe"
              data-test="subscribe-button"
              class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 cursor-pointer"
            >
              Subscribe
            </button>
          </div>
        </div>
      </div>
    </div>
  </header>
</template>
