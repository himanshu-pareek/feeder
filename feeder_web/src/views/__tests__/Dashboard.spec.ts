import { describe, it, expect, vi, beforeEach } from 'vitest';
import { mount, flushPromises } from '@vue/test-utils';
import Dashboard from '../Dashboard.vue';
import { apiService } from '../../services/apiService';

vi.mock('../../services/apiService', () => ({
    apiService: {
        getFeedEntries: vi.fn(),
        subscribeToFeed: vi.fn(),
    },
}));

const mockEntries = {
    items: [
        { title: 'Entry 1', feedEntryUri: 'uri:1' },
        { title: 'Entry 2', feedEntryUri: 'uri:2' },
    ]
};

describe('Dashboard', () => {
    beforeEach(() => {
        vi.clearAllMocks();
        (apiService.getFeedEntries as any).mockResolvedValue(mockEntries);
    });

    it('fetches entries on mount', async () => {
        mount(Dashboard);
        expect(apiService.getFeedEntries).toHaveBeenCalled();
    });

    it('passes entries to FeedList', async () => {
        const wrapper = mount(Dashboard);
        await flushPromises();
        const feedList = wrapper.findComponent({ name: 'FeedList' });
        expect(feedList.props('entries')).toEqual(mockEntries.items);
    });

    it('updates selected entry when FeedList emits select', async () => {
        const wrapper = mount(Dashboard);
        await flushPromises();
        const feedList = wrapper.findComponent({ name: 'FeedList' });
        
        await feedList.vm.$emit('select', mockEntries.items[0]);
        
        const feedDetail = wrapper.findComponent({ name: 'FeedDetail' });
        expect(feedDetail.props('entry')).toEqual(mockEntries.items[0]);
    });

    it('calls subscribeToFeed and refreshes list when AppHeader emits subscribe', async () => {
        const wrapper = mount(Dashboard);
        await flushPromises();
        const appHeader = wrapper.findComponent({ name: 'AppHeader' });
        
        const testUrl = 'https://example.com/rss';
        (apiService.subscribeToFeed as any).mockResolvedValue(undefined);
        
        await appHeader.vm.$emit('subscribe', testUrl);
        
        expect(apiService.subscribeToFeed).toHaveBeenCalledWith(testUrl);
        // Should have been called twice: once on mount, once after subscribe
        expect(apiService.getFeedEntries).toHaveBeenCalledTimes(2);
    });

    it('shows detailed error message from backend when subscription fails', async () => {
        const wrapper = mount(Dashboard);
        await flushPromises();
        const appHeader = wrapper.findComponent({ name: 'AppHeader' });
        
        const testUrl = 'https://example.com/rss';
        const errorDetail = 'User not found: 123';
        const mockError = {
            isAxiosError: true,
            response: {
                data: {
                    detail: errorDetail
                }
            }
        };
        
        // Mock axios.isAxiosError
        const axios = await import('axios');
        vi.spyOn(axios.default, 'isAxiosError').mockReturnValue(true);
        
        (apiService.subscribeToFeed as any).mockRejectedValue(mockError);
        const alertSpy = vi.spyOn(window, 'alert').mockImplementation(() => {});
        
        await appHeader.vm.$emit('subscribe', testUrl);
        
        expect(alertSpy).toHaveBeenCalledWith(errorDetail);
    });
});
