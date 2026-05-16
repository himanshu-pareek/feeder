<script setup lang="ts">
import axios from 'axios';
import { ref, onMounted } from 'vue';
import AppHeader from '../components/AppHeader.vue';
import FeedList from '../components/FeedList.vue';
import FeedDetail from '../components/FeedDetail.vue';
import { apiService } from '../services/apiService';
import type { UserFeedEntry } from '../types';

const entries = ref<UserFeedEntry[]>([]);
const selectedEntry = ref<UserFeedEntry | null>(null);
const loading = ref(false);

const fetchEntries = async () => {
  loading.value = true;
  try {
    const data = await apiService.getFeedEntries();
    entries.value = data.items;
  } catch (error) {
    console.error('Failed to fetch entries:', error);
  } finally {
    loading.value = false;
  }
};

const handleSelect = (entry: UserFeedEntry) => {
  selectedEntry.value = entry;
};

const handleSubscribe = async (url: string) => {
  try {
    await apiService.subscribeToFeed(url);
    await fetchEntries();
  } catch (error) {
    console.error('Failed to subscribe:', error);
    let errorMessage = 'Failed to subscribe to feed. Please check the URL.';
    
    if (axios.isAxiosError(error) && error.response?.data?.detail) {
      errorMessage = error.response.data.detail;
    }
    
    alert(errorMessage);
  }
};

onMounted(() => {
  fetchEntries();
});
</script>

<template>
  <div class="flex flex-col h-screen overflow-hidden">
    <AppHeader @subscribe="handleSubscribe" />
    
    <main class="flex flex-1 overflow-hidden relative">
      <!-- Sidebar / List -->
      <aside 
        class="w-full md:w-[400px] border-r border-gray-200 bg-white z-10 overflow-hidden flex flex-col"
        :class="{ 'hidden md:flex': selectedEntry }"
      >
        <div v-if="loading" class="p-8 text-center text-gray-500">
          Loading feeds...
        </div>
        <FeedList 
          v-else
          :entries="entries" 
          :selected-uri="selectedEntry?.feedEntryUri || null"
          @select="handleSelect" 
        />
      </aside>

      <!-- Content Viewer -->
      <section 
        class="flex-1 bg-white overflow-hidden"
        :class="{ 
          'hidden md:block': !selectedEntry,
          'block absolute inset-0 md:relative z-20': selectedEntry 
        }"
      >
        <div v-if="selectedEntry" class="md:hidden p-4 border-b border-gray-200 flex items-center bg-gray-50">
          <button 
            @click="selectedEntry = null"
            class="text-blue-600 font-medium flex items-center gap-1 cursor-pointer"
          >
            <span>&larr;</span> Back to list
          </button>
        </div>
        <FeedDetail :entry="selectedEntry" />
      </section>
    </main>
  </div>
</template>

<style scoped>
/* No changes needed here, keeping for completeness if needed, but write_file expects full content */
</style>
