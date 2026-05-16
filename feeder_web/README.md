# Feeder Web

A modern, responsive RSS Feed Reader built with Vue 3, TypeScript, and Tailwind CSS.

## Purpose
Feeder Web is the frontend component of the Feeder ecosystem. It provides a user-friendly interface to:
- **Authenticate:** Secure login via OAuth2 (Keycloak).
- **Read:** View a personalized list of RSS feed entries and read their detailed content.
- **Subscribe:** Easily add new RSS feeds to your profile via a modal-based subscription flow.
- **Responsive:** Optimized for both desktop (split-view) and mobile (single-column) experiences.

## Technology Stack
- **Framework:** [Vue 3](https://vuejs.org/) (Composition API)
- **Build Tool:** [Vite](https://vitejs.dev/)
- **Styling:** [Tailwind CSS 4](https://tailwindcss.com/) with the Typography plugin.
- **Authentication:** [oidc-client-ts](https://github.com/authts/oidc-client-ts) for OpenID Connect integration.
- **API Client:** [Axios](https://axios-http.com/) with request interceptors for automatic Bearer token injection.
- **Testing:** [Vitest](https://vitest.dev/) and [Vue Test Utils](https://test-utils.vuejs.org/). Developed using a **Test-Driven Development (TDD)** approach.

## Project Structure
The project is organized by feature and responsibility:

```
feeder_web/
├── src/
│   ├── components/       # UI components (Header, FeedList, FeedDetail)
│   │   └── __tests__/    # Component unit tests
│   ├── services/         # Business logic and API wrappers
│   │   ├── authService.ts # Keycloak/OIDC management
│   │   ├── apiService.ts  # Axios client with Auth interceptors
│   │   └── __tests__/    # Service unit tests
│   ├── views/            # Orchestration components (Dashboard)
│   │   └── __tests__/    # View integration tests
│   ├── router/           # Vue Router config with Auth Guards
│   ├── types/            # TypeScript interfaces matching backend models
│   ├── App.vue           # Root component
│   ├── main.ts           # App initialization
│   └── style.css         # Global Tailwind & base styles
├── .env                  # Development environment configuration
├── .env.production       # Production environment configuration
└── vitest.config.ts      # Test environment configuration
```

## Getting Started

### 1. Configuration
Update the `.env` file with your environment's URLs:
```env
VITE_AUTH_AUTHORITY=http://your-keycloak-url/auth/realms/feeder
VITE_AUTH_CLIENT_ID=feeder-web
VITE_API_BASE_URL=http://your-backend-api-url
```

### 2. Installation
```bash
npm install
```

### 3. Development
Starts the development server with Hot Module Replacement (HMR):
```bash
npm run dev
```

### 4. Running Tests
Executes the Vitest test suite (TDD-verified):
```bash
npm run test
```

### 5. Production Build
Generates optimized assets for production in the `dist/` folder:
```bash
npm run build
```

## Features
- **OAuth2 Flow:** Seamless integration with Keycloak including automatic user registration on first login.
- **Interactive UI:** Smooth transitions between list and detail views.
- **Error Handling:** Detailed feedback from the backend is displayed to the user during subscription failures.
- **Clean Code:** Strictly typed with TypeScript and thoroughly tested.
