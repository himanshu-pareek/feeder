import axios from 'axios';
import type { AxiosInstance } from 'axios';
import { authService } from './authService';
import type { UserFeedEntries } from '../types';

export class ApiService {
    private instance: AxiosInstance;

    constructor() {
        this.instance = axios.create({
            baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8081',
        });

        this.instance.interceptors.request.use(async (config) => {
            const token = await authService.getAccessToken();
            if (token) {
                config.headers.Authorization = `Bearer ${token}`;
            }
            return config;
        });
    }

    public async getFeedEntries(): Promise<UserFeedEntries> {
        const response = await this.instance.get<UserFeedEntries>('/feedentries');
        return response.data;
    }

    public async subscribeToFeed(feedUri: string): Promise<void> {
        await this.instance.post('/users/subscribe', null, {
            params: { feedUri },
        });
    }

    public async registerUser(): Promise<void> {
        await this.instance.post('/users/register');
    }

    public async getUser(): Promise<any> {
        const response = await this.instance.get('/users');
        return response.data;
    }
}

export const createApiService = () => new ApiService();
export const apiService = createApiService();
