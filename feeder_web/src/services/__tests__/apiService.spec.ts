import { describe, it, expect, vi, beforeEach } from 'vitest';
import axios from 'axios';
import { authService } from '../authService';

const { mockRequestUse } = vi.hoisted(() => ({
    mockRequestUse: vi.fn()
}));

vi.mock('axios', () => {
    const mockInstance = {
        interceptors: {
            request: { use: mockRequestUse, eject: vi.fn() },
            response: { use: vi.fn(), eject: vi.fn() },
        },
        get: vi.fn(),
        post: vi.fn(),
    };
    return {
        default: {
            create: vi.fn(() => mockInstance),
        },
    };
});

vi.mock('../authService', () => ({
    authService: {
        getAccessToken: vi.fn(),
    },
}));

import { createApiService } from '../apiService';

describe('apiService interceptors', () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    it('should add Authorization header if token is available', async () => {
        const token = 'test-token';
        (authService.getAccessToken as any).mockResolvedValue(token);

        // Create a new instance for the test
        createApiService();

        expect(mockRequestUse).toHaveBeenCalled();
        const interceptor = mockRequestUse.mock.calls[0][0];
        
        const config = { headers: {} as any };
        const result = await interceptor(config);
        
        expect(result.headers.Authorization).toBe(`Bearer ${token}`);
    });

    it('should NOT add Authorization header if token is NOT available', async () => {
        (authService.getAccessToken as any).mockResolvedValue(null);

        createApiService();

        expect(mockRequestUse).toHaveBeenCalled();
        const interceptor = mockRequestUse.mock.calls[0][0];
        
        const config = { headers: {} as any };
        const result = await interceptor(config);
        
        expect(result.headers.Authorization).toBeUndefined();
    });
});
