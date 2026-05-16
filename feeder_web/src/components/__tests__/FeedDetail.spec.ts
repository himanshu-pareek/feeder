import { describe, it, expect } from 'vitest';
import { mount } from '@vue/test-utils';
import FeedDetail from '../FeedDetail.vue';
import { UserFeedEntry } from '../../types';

const mockEntry: UserFeedEntry = {
    title: 'Full Entry Title',
    publishedDate: '2023-01-01T10:00:00',
    description: 'Detailed description here...',
    link: 'https://example.com/full',
    feedEntryUri: 'uri:full',
    contents: [
        { type: 'html', value: '<p>Actual content body</p>' }
    ],
    read: true,
} as UserFeedEntry;

describe('FeedDetail', () => {
    it('renders placeholder when no entry is selected', () => {
        const wrapper = mount(FeedDetail, {
            props: { entry: null }
        });
        expect(wrapper.text()).toContain('Select an entry to read');
    });

    it('renders the entry details', () => {
        const wrapper = mount(FeedDetail, {
            props: { entry: mockEntry }
        });
        expect(wrapper.find('h2').text()).toBe('Full Entry Title');
        expect(wrapper.find('.content').html()).toContain('<p>Actual content body</p>');
        expect(wrapper.find('a').attributes('href')).toBe('https://example.com/full');
    });

    it('falls back to description if contents are empty', () => {
        const entryWithoutContent = { ...mockEntry, contents: [] };
        const wrapper = mount(FeedDetail, {
            props: { entry: entryWithoutContent }
        });
        expect(wrapper.find('.content').text()).toBe('Detailed description here...');
    });
});
