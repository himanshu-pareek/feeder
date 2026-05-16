<script setup lang="ts">
/// <reference types="../../node_modules/.vue-global-types/vue_99_0.d.ts" />
import type { UserFeedEntry } from '../types';

defineProps<{
  entries: UserFeedEntry[];
  selectedUri: string | null;
}>();

const emit = defineEmits<{
  (e: 'select', entry: UserFeedEntry): void;
}>();

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleDateString(undefined, {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  });
};
</script>

<template>
  <div class="flex flex-col overflow-y-auto h-full bg-white">
    <div 
      v-for="entry in entries" 
      :key="entry.feedEntryUri"
      @click="emit('select', entry)"
      class="feed-item p-4 border-b border-gray-200 cursor-pointer transition-colors hover:bg-gray-50"
      :class="{ 
        'selected bg-blue-50 border-l-4 border-l-blue-600': entry.feedEntryUri === selectedUri,
      }"
    >
      <h3 class="m-0 mb-2 text-lg text-gray-900" :class="{ 'font-bold': !entry.read }">
        {{ entry.title }}
      </h3>
      <div class="text-sm text-gray-500 mb-2">
        <span>{{ formatDate(entry.publishedDate) }}</span>
      </div>
      <p class="m-0 text-sm text-gray-600 line-clamp-2">
        {{ entry.description }}
      </p>
    </div>
  </div>
</template>
