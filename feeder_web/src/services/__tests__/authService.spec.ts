import { describe, it, expect, vi, beforeEach } from 'vitest';
import { AuthService } from '../authService';
import { UserManager } from 'oidc-client-ts';

vi.mock('oidc-client-ts', () => {
    return {
        UserManager: vi.fn().mockImplementation(function () {
            return {
                signinRedirect: vi.fn(),
                signinCallback: vi.fn(),
                getUser: vi.fn(),
                clearStaleState: vi.fn(),
            };
        }),
        WebStorageStateStore: vi.fn(),
    };
});

describe('AuthService', () => {
    let authService: AuthService;
    let mockUserManager: any;

    beforeEach(() => {
        vi.clearAllMocks();
        authService = new AuthService();
        mockUserManager = (UserManager as any).mock.results[0].value;
    });

    it('should call signinRedirect when login is called', async () => {
        await authService.login();
        expect(mockUserManager.signinRedirect).toHaveBeenCalled();
    });

    it('should call signinCallback when handleCallback is called', async () => {
        await authService.handleCallback();
        expect(mockUserManager.signinCallback).toHaveBeenCalled();
    });

    it('should call getUser when getUser is called', async () => {
        const mockUser = { access_token: 'test-token' };
        mockUserManager.getUser.mockResolvedValue(mockUser);
        
        const user = await authService.getUser();
        expect(mockUserManager.getUser).toHaveBeenCalled();
        expect(user).toEqual(mockUser);
    });
});
