import { createRouter, createWebHistory } from 'vue-router';
import Dashboard from '../views/Dashboard.vue';
import { authService } from '../services/authService';
import { apiService } from '../services/apiService';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'dashboard',
      component: Dashboard,
      meta: { requiresAuth: true }
    },
    {
      path: '/oauth/callback',
      name: 'oauth-callback',
      component: {}, // Empty component for callback
      beforeEnter: async (_to, _from, next) => {
        try {
          await authService.handleCallback();
          try {
            await apiService.registerUser();
          } catch (regError) {
            console.warn('User registration failed (might already be registered):', regError);
          }
          next({ name: 'dashboard' });
        } catch (error) {
          console.error('Callback error:', error);
          next({ name: 'dashboard' }); // Or show error
        }
      }
    }
  ]
});

router.beforeEach(async (to, _from, next) => {
  if (to.meta.requiresAuth) {
    const user = await authService.getUser();
    if (user && !user.expired) {
      next();
    } else {
      await authService.login();
    }
  } else {
    next();
  }
});

export default router;
