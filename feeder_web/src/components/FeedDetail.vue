<script setup lang="ts">
import { computed } from 'vue';
import type { UserFeedEntry } from '../types';

const props = defineProps<{
  entry: UserFeedEntry | null;
}>();

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleDateString(undefined, {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  });
};

const displayContent = computed(() => {
  if (!props.entry) return '';
  if (props.entry.contents && props.entry.contents.length > 0) {
    return props.entry.contents.map(c => c.value).join('<br/>');
  }
  return props.entry.description;
});
</script>

<template>
  <div class="p-8 overflow-y-auto h-full bg-white">
    <div v-if="entry" class="max-w-3xl mx-auto">
      <header class="mb-8 border-b-2 border-gray-100 pb-4">
        <h2 class="m-0 mb-4 text-3xl font-bold text-gray-900 leading-tight">
          {{ entry.title }}
        </h2>
        <div class="flex justify-between items-center text-gray-500 text-base">
          <span>{{ formatDate(entry.publishedDate) }}</span>
          <a :href="entry.link" target="_blank" rel="noopener" class="text-blue-600 hover:underline">
            View Original
          </a>
        </div>
      </header>
      <div 
        class="content prose prose-blue max-w-none text-gray-800 text-lg leading-relaxed space-y-4"
        v-html="displayContent"
      ></div>
    </div>
    <div v-else class="flex justify-center items-center h-full text-gray-400 text-xl">
      <p>Select an entry to read</p>
    </div>
  </div>
</template>

<style scoped>
/* To handle potential nested HTML from RSS */
:deep(p) { margin-bottom: 1.5rem; }
:deep(img) { max-width: 100%; height: auto; border-radius: 0.5rem; }
:deep(ul) { list-style-type: disc; margin-left: 1.5rem; }
:deep(ol) { list-style-type: decimal; margin-left: 1.5rem; }
</style>
