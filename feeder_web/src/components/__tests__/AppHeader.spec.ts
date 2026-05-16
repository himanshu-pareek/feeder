import { describe, it, expect } from 'vitest';
import { mount } from '@vue/test-utils';
import AppHeader from '../AppHeader.vue';

describe('AppHeader', () => {
    it('renders the title', () => {
        const wrapper = mount(AppHeader);
        expect(wrapper.text()).toContain('Feeder');
    });

    it('emits subscribe event with URL when subscribe is confirmed in modal', async () => {
        const wrapper = mount(AppHeader);
        
        // Open modal
        await wrapper.find('[data-test="open-modal-button"]').trigger('click');
        
        const input = wrapper.find('[data-test="subscribe-input"]');
        const confirmButton = wrapper.find('[data-test="subscribe-button"]');
        const testUrl = 'https://example.com/rss';

        await input.setValue(testUrl);
        await confirmButton.trigger('click');

        expect(wrapper.emitted('subscribe')).toBeTruthy();
        expect(wrapper.emitted('subscribe')![0]).toEqual([testUrl]);
    });

    it('clears input and closes modal after successful submission', async () => {
        const wrapper = mount(AppHeader);
        
        // Open modal
        await wrapper.find('[data-test="open-modal-button"]').trigger('click');
        
        const input = wrapper.find('[data-test="subscribe-input"]');
        const confirmButton = wrapper.find('[data-test="subscribe-button"]');
        
        await input.setValue('https://example.com/rss');
        await confirmButton.trigger('click');
        
        expect(wrapper.find('[data-test="subscribe-input"]').exists()).toBe(false);
    });
});
