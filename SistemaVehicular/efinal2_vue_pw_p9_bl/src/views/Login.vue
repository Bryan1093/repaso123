<template>
  <div class="login-overlay">
    <div class="login-card">
      <div class="login-header">
        <h2>Bienvenido</h2>
        <p>Sistema de Gestión Vehicular</p>
      </div>
      <form @submit.prevent="login">
        <div class="input-group">
          <label for="username">Usuario</label>
          <input type="text" id="username" v-model="username" placeholder="Ingrese su usuario" required>
        </div>
        <div class="input-group">
          <label for="password">Contraseña</label>
          <input type="password" id="password" v-model="password" placeholder="••••••••" required>
        </div>
        <button type="submit" class="btn-login">
          <span v-if="isLoading">Ingresando...</span>
          <span v-else>Iniciar Sesión</span>
        </button>
        <div v-if="error" class="error-banner">
          {{ error }}
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      username: '',
      password: '',
      error: '',
      isLoading: false
    };
  },
  methods: {
    async login() {
      if (this.isLoading) return;
      this.isLoading = true;
      this.error = '';

      try {
        // Calling the Auth API
        const response = await axios.get('http://localhost:8082/auth/token', {
          params: {
            user: this.username,
            password: this.password
          }
        });

        const token = response.data.accessToken;
        localStorage.setItem('token', token);
        localStorage.setItem('role', response.data.role); // Optional: store role if needed
        this.$router.push('/');
      } catch (err) {
        if (err.response && err.response.status === 401) {
          this.error = 'Credenciales incorrectas. Inténtelo de nuevo.';
        } else {
          this.error = 'Error de conexión con el servidor.';
        }
        console.error(err);
      } finally {
        this.isLoading = false;
      }
    }
  }
};
</script>

<style scoped>
.login-overlay {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.login-card {
  background: white;
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
  width: 100%;
  max-width: 400px;
  animation: fadeIn 0.5s ease-out;
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.login-header h2 {
  color: #333;
  margin: 0 0 10px;
  font-size: 28px;
}

.login-header p {
  color: #666;
  margin: 0;
  font-size: 14px;
}

.input-group {
  margin-bottom: 20px;
}

.input-group label {
  display: block;
  margin-bottom: 8px;
  color: #555;
  font-weight: 500;
  font-size: 14px;
}

.input-group input {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 16px;
  transition: border-color 0.3s;
  box-sizing: border-box;
}

.input-group input:focus {
  border-color: #2a5298;
  outline: none;
}

.btn-login {
  width: 100%;
  padding: 14px;
  background: #2a5298;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  transition: background 0.3s, transform 0.1s;
}

.btn-login:hover {
  background: #1e3c72;
}

.btn-login:active {
  transform: scale(0.98);
}

.error-banner {
  margin-top: 20px;
  padding: 10px;
  background-color: #ffebee;
  color: #c62828;
  border-radius: 4px;
  text-align: center;
  font-size: 14px;
  border: 1px solid #ffcdd2;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
