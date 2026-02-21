<template>
  <div id="app" class="app-container">
    <!-- Loader Global -->
    <transition name="fade">
      <div v-if="globalLoading" class="global-loader">
        <div class="loader-orb"></div>
        <h4 class="mt-4 fw-bold gradient-text">Sincronizando MedisysPro...</h4>
      </div>
    </transition>
    <!-- Navbar Premium -->
    <nav class="navbar navbar-expand-lg navbar-glass sticky-top" v-if="!isLoginRoute">
      <div class="container">
        <router-link class="navbar-brand d-flex align-items-center" to="/">
          <div class="brand-hex me-2">M</div>
          <span class="brand-text">Medisys<span class="text-primary">Pro</span></span>
        </router-link>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarContent">
          <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarContent">
          <ul class="navbar-nav mx-auto" v-if="isLoggedIn">
            <li class="nav-item">
              <router-link class="nav-link-modern" to="/home">Inicio</router-link>
            </li>
            <li class="nav-item">
              <router-link class="nav-link-modern" to="/doctor">Doctores</router-link>
            </li>
            <li class="nav-item">
              <router-link class="nav-link-modern" to="/paciente">Pacientes</router-link>
            </li>
            <li class="nav-item">
              <router-link class="nav-link-modern" to="/citas">Agenda</router-link>
            </li>
          </ul>

          <div class="d-flex align-items-center nav-actions" v-if="isLoggedIn">
            <!-- Theme Toggle -->
            <button @click="toggleTheme" class="btn-theme me-3"
              :title="theme === 'light' ? 'Tema Oscuro' : 'Tema Claro'">
              <i class="bi" :class="theme === 'light' ? 'bi-moon-stars-fill' : 'bi-sun-fill'"></i>
            </button>

            <div class="user-pill d-none d-md-flex align-items-center">
              <div class="user-avatar">{{ username.charAt(0).toUpperCase() }}</div>
              <span class="ms-2 me-3 fw-semibold">{{ username }}</span>
            </div>
            <button @click="logout" class="btn-logout">
              <i class="bi bi-box-arrow-right"></i>
            </button>
          </div>
        </div>
      </div>
    </nav>

    <!-- Content -->
    <main class="main-content">
      <router-view v-slot="{ Component }">
        <transition name="fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </main>

    <!-- Dynamic Toast Notification (Inspired by Sileo) -->
    <transition name="toast-spring">
      <div v-if="toast.show" class="premium-toast" :class="toast.type">
        <div class="toast-blur"></div>
        <div class="toast-content">
          <div class="toast-icon">
            <i class="bi" :class="toast.type === 'success' ? 'bi-check2-circle' : 'bi-exclamation-circle'"></i>
          </div>
          <div class="toast-text">
            <span class="toast-title">{{ toast.type === 'success' ? 'Completado' : 'Aviso' }}</span>
            <p class="toast-msg">{{ toast.message }}</p>
          </div>
          <button @click="toast.show = false" class="toast-close">✕</button>
        </div>
      </div>
    </transition>

    <!-- Floating Music Widget -->
    <div class="music-widget" :class="{ 'exp': musicExp }">
      <div class="music-controls">
        <button @click="musicExp = !musicExp" class="btn-music" title="Toggle Music">
          <span v-if="!musicExp">🎵</span>
          <span v-else>✕</span>
        </button>
      </div>
      <div class="music-player" v-if="musicExp">
        <iframe width="300" height="150"
          src="https://www.youtube.com/embed/jfKfPfyJRdk?autoplay=1&loop=1&playlist=jfKfPfyJRdk" frameborder="0"
          allow="autoplay; encrypted-media" allowfullscreen>
        </iframe>
      </div>
    </div>
  </div>
</template>

<script>
import ApiClient from '@/clients/ApiClient';
import { toastState } from '@/utils/notify';

export default {
  data() {
    return {
      isLoggedIn: false,
      username: '',
      musicExp: false,
      theme: 'light',
      globalLoading: true,
      toast: toastState
    };
  },
  computed: {
    isLoginRoute() {
      return this.$route.path === '/login';
    }
  },
  watch: {
    $route() {
      this.checkAuth();
      this.triggerLoader();
    }
  },
  mounted() {
    this.checkAuth();
    this.initTheme();
    setTimeout(() => { this.globalLoading = false; }, 1500);
  },
  methods: {
    triggerLoader() {
      this.globalLoading = true;
      setTimeout(() => { this.globalLoading = false; }, 800);
    },
    initTheme() {
      const savedTheme = localStorage.getItem('theme') || 'light';
      this.theme = savedTheme;
      document.documentElement.setAttribute('data-theme', savedTheme);
    },
    toggleTheme() {
      this.theme = this.theme === 'light' ? 'dark' : 'light';
      document.documentElement.setAttribute('data-theme', this.theme);
      localStorage.setItem('theme', this.theme);
    },
    checkAuth() {
      const token = localStorage.getItem('token');
      this.isLoggedIn = !!token;
      this.username = localStorage.getItem('user') || 'Usuario';
    },
    logout() {
      ApiClient.logout();
      this.isLoggedIn = false;
      this.$router.push('/login');
    }
  }
};
</script>

<style>
.app-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

/* Theme Toggle Button */
.btn-theme {
  background: var(--card-surface);
  color: var(--primary);
  border: 1px solid var(--glass-border);
  width: 40px;
  height: 40px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.2rem;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: var(--shadow-soft);
}

.btn-theme:hover {
  transform: scale(1.1) rotate(10deg);
  background: var(--primary);
  color: white;
}

/* Updated Navbar for Dark Mode */
.navbar-glass {
  background: var(--glass-bg);
  backdrop-filter: blur(15px);
  -webkit-backdrop-filter: blur(15px);
  border-bottom: 1px solid var(--glass-border);
  padding: 0.75rem 0;
  box-shadow: 0 4px 30px rgba(0, 0, 0, 0.03);
  z-index: 1000;
  transition: background 0.3s ease;
}

[data-theme='dark'] .brand-text {
  color: #f1f5f9;
}

[data-theme='dark'] .nav-link-modern {
  color: #94a3b8;
}

[data-theme='dark'] .user-pill {
  background: #0f172a;
  border-color: rgba(255, 255, 255, 0.1);
}

.brand-hex {
  width: 32px;
  height: 32px;
  background: var(--primary);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  border-radius: 8px;
  transform: rotate(-10deg);
  box-shadow: 0 4px 10px rgba(99, 102, 241, 0.3);
}

.brand-text {
  font-weight: 700;
  font-size: 1.4rem;
  letter-spacing: -0.5px;
  color: var(--text-main);
}

.nav-link-modern {
  color: #64748b;
  font-weight: 600;
  margin: 0 1rem;
  text-decoration: none;
  transition: all 0.3s ease;
  padding: 0.5rem 0;
  position: relative;
}

.nav-link-modern::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 0;
  height: 2px;
  background: var(--primary);
  transition: width 0.3s ease;
}

.nav-link-modern:hover {
  color: var(--primary);
}

.nav-link-modern:hover::after {
  width: 100%;
}

.router-link-active.nav-link-modern {
  color: var(--primary);
}

.router-link-active.nav-link-modern::after {
  width: 100%;
}

/* User Pill */
.user-pill {
  background: #f1f5f9;
  padding: 0.4rem 0.5rem;
  padding-right: 1rem;
  border-radius: 999px;
  border: 1px solid #e2e8f0;
}

.user-avatar {
  width: 28px;
  height: 28px;
  background: var(--secondary);
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.8rem;
  font-weight: 700;
}

.btn-logout {
  background: rgba(239, 68, 68, 0.15);
  color: #ef4444;
  border: none;
  width: 38px;
  height: 38px;
  border-radius: 50%;
  margin-left: 0.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s ease;
}

.btn-logout:hover {
  background: #fecaca;
  transform: scale(1.05);
}

/* Main Content Wrapper */
.main-content {
  flex: 1;
}

/* Global Loader */
.global-loader {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: var(--body-bg);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}

.loader-orb {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  border: 4px solid var(--primary);
  border-top-color: transparent;
  animation: spin 1s linear infinite;
  box-shadow: 0 0 20px rgba(99, 102, 241, 0.2);
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* Music Widget */
.music-widget {
  position: fixed;
  bottom: 20px;
  left: 20px;
  z-index: 2000;
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
}

.btn-music {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  background: white;
  border: none;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  font-size: 1.2rem;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: transform 0.3s;
}

.btn-music:hover {
  transform: rotate(15deg) scale(1.1);
}

.music-player {
  margin-top: 10px;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
  animation: slideIn 0.3s ease-out;
}
</style>
