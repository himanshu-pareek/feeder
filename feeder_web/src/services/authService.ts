import { UserManager, WebStorageStateStore, User } from 'oidc-client-ts';

export class AuthService {
    private userManager: UserManager;

    constructor() {
        const settings = {
            authority: import.meta.env.VITE_AUTH_AUTHORITY || 'http://localhost:8081/realms/feeder-realm',
            client_id: import.meta.env.VITE_AUTH_CLIENT_ID || 'feeder-app-web',
            redirect_uri: window.location.origin + '/oauth/callback',
            response_type: 'code',
            scope: 'openid profile email',
            post_logout_redirect_uri: window.location.origin,
            userStore: new WebStorageStateStore({ store: window.localStorage }),
        };
        this.userManager = new UserManager(settings);
    }

    public async login(): Promise<void> {
        await this.userManager.signinRedirect();
    }

    public async handleCallback(): Promise<User | void> {
        return await this.userManager.signinCallback();
    }

    public async getUser(): Promise<User | null> {
        return await this.userManager.getUser();
    }

    public async getAccessToken(): Promise<string | null> {
        const user = await this.getUser();
        return user ? user.access_token : null;
    }

    public async logout(): Promise<void> {
        await this.userManager.signoutRedirect();
    }
}

export const authService = new AuthService();
