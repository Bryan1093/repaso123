<template>
  <div class="login-wrapper bg-animate">
    <div class="glass-login animate-fade-in">
      <div class="login-header">
        <div class="brand-logo">M</div>
        <h1>Bienvenido a <span class="brand-accent">MedisysPro</span></h1>
        <p>Inicie sesión en su portal administrativo</p>
      </div>

      <form @submit.prevent="handleLogin" class="modern-form">
        <div class="floating-field">
          <input type="text" v-model="username" required placeholder=" " id="user" />
          <label for="user">Usuario Especialista</label>
          <div class="field-bar"></div>
        </div>

        <div class="floating-field mt-4">
          <input type="password" v-model="password" required placeholder=" " id="pass" />
          <label for="pass">Contraseña</label>
          <div class="field-bar"></div>
        </div>

        <button type="submit" :disabled="loading" class="btn-login-premium mt-5">
          <span v-if="loading" class="spinner-border spinner-border-sm me-2"></span>
          {{ loading ? 'Autenticando...' : 'Acceder al Sistema' }}
        </button>
      </form>

      <div class="login-footer">
        <small>© 2026 MedisysPro - Seguridad de Nivel Médico</small>
      </div>
    </div>
  </div>
</template>

<script>
import ApiClient from '@/clients/ApiClient';
import notify from '@/utils/notify';

export default {
  data() {
    return {
      username: '',
      password: '',
      loading: false
    };
  },
  methods: {
    async handleLogin() {
      this.loading = true;
      try {
        await ApiClient.login(this.username, this.password);
        notify('Bienvenido de nuevo, Dr. ' + (localStorage.getItem('user') || ''), 'success');
        this.$router.push('/home');
      } catch (err) {
        notify('Acceso denegado. Verifique su usuario y contraseña.', 'error');
      } finally {
        this.loading = false;
      }
    }
  }
};
</script>

<style scoped>
.login-wrapper {
  height: 100vh;
  width: 100vw;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 1rem;
}

.glass-login {
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(25px) saturate(200%);
  -webkit-backdrop-filter: blur(25px) saturate(200%);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 35px;
  padding: 3.5rem;
  width: 100%;
  max-width: 500px;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
  text-align: center;
}

.brand-logo {
  width: 60px;
  height: 60px;
  background: var(--primary);
  color: white;
  margin: 0 auto 1.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 2rem;
  font-weight: 800;
  border-radius: 18px;
  transform: rotate(-10deg);
  box-shadow: 0 10px 20px rgba(99, 102, 241, 0.4);
}

.login-header h1 {
  color: white;
  font-size: 2rem;
  font-weight: 700;
  margin-bottom: 0.5rem;
}

.brand-accent {
  color: #c7d2fe;
}

.login-header p {
  color: rgba(255, 255, 255, 0.7);
  margin-bottom: 3rem;
}

/* Floating Fields */
.floating-field {
  position: relative;
  text-align: left;
}

.floating-field input {
  width: 100%;
  background: transparent;
  border: none;
  border-bottom: 2px solid rgba(255, 255, 255, 0.3);
  padding: 1rem 0;
  color: white;
  font-size: 1.1rem;
  outline: none;
  transition: all 0.3s;
}

.field-bar {
  position: absolute;
  bottom: 0;
  left: 50%;
  width: 0;
  height: 2px;
  background: var(--secondary);
  transition: all 0.4s cubic-bezier(0.23, 1, 0.32, 1);
}

.floating-field input:focus~.field-bar {
  width: 100%;
  left: 0;
}

.floating-field label {
  position: absolute;
  top: 1rem;
  left: 0;
  color: rgba(255, 255, 255, 0.5);
  pointer-events: none;
  transition: all 0.3s ease;
}

.floating-field input:focus~label,
.floating-field input:not(:placeholder-shown)~label {
  top: -1.2rem;
  font-size: 0.85rem;
  color: var(--secondary);
  font-weight: 600;
}

.btn-login-premium {
  width: 100%;
  padding: 1.2rem;
  background: var(--primary);
  color: white;
  border: none;
  border-radius: 15px;
  font-size: 1.1rem;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.3s;
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
}

.btn-login-premium:hover:not(:disabled) {
  background: var(--primary-hover);
  transform: translateY(-3px);
  box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.2);
}

.btn-login-premium:disabled {
  opacity: 0.7;
  cursor: wait;
}

.error-alert {
  background: rgba(244, 63, 94, 0.2);
  border: 1px solid rgba(244, 63, 94, 0.3);
  color: #fecaca;
  padding: 1rem;
  border-radius: 12px;
  font-size: 0.95rem;
  font-weight: 600;
}

.login-footer {
  margin-top: 3rem;
  color: rgba(255, 255, 255, 0.4);
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
