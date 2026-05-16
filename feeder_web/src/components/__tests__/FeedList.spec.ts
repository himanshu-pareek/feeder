import { describe, it, expect } from 'vitest';
import { mount } from '@vue/test-utils';
import FeedList from '../FeedList.vue';
import { UserFeedEntry } from '../../types';

const mockEntries: UserFeedEntry[] = [
    {
        title: 'Entry 1',
        publishedDate: '2023-01-01T10:00:00',
        description: 'Description 1',
        link: 'https://example.com/1',
        feedEntryUri: 'uri:1',
        read: false,
    } as UserFeedEntry,
    {
        title: 'Entry 2',
        publishedDate: '2023-01-02T10:00:00',
        description: 'Description 2',
        link: 'https://example.com/2',
        feedEntryUri: 'uri:2',
        read: true,
    } as UserFeedEntry,
];

describe('FeedList', () => {
    it('renders the list of entries', () => {
        const wrapper = mount(FeedList, {
            props: {
                entries: mockEntries,
                selectedUri: null
            }
        });
        const items = wrapper.findAll('.feed-item');
        expect(items.length).toBe(2);
        expect(items[0].text()).toContain('Entry 1');
        expect(items[1].text()).toContain('Entry 2');
    });

    it('emits select event when an item is clicked', async () => {
        const wrapper = mount(FeedList, {
            props: {
                entries: mockEntries,
                selectedUri: null
            }
        });
        await wrapper.findAll('.feed-item')[0].trigger('click');
        expect(wrapper.emitted('select')).toBeTruthy();
        expect(wrapper.emitted('select')![0]).toEqual([mockEntries[0]]);
    });

    it('highlights the selected item', () => {
        const wrapper = mount(FeedList, {
            props: {
                entries: mockEntries,
                selectedUri: 'uri:1'
            }
        });
        const items = wrapper.findAll('.feed-item');
        expect(items[0].classes()).toContain('selected');
        expect(items[1].classes()).not.toContain('selected');
    });
});
