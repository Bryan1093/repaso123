<template>
  <div class="generar-token-container">
    <h2 class="title">TOKEN</h2>

    <div class="form-group">
      <label for="semilla">Semilla</label>
      <input type="text" id="semilla" v-model="semilla" class="form-input" />
    </div>

    <div class="form-group">
      <label for="tiempo">Tiempo de vigencia</label>
      <input type="number" id="tiempo" v-model="tiempoVigencia" class="form-input" />
    </div>

    <div class="form-group">
      <label for="token">Token</label>
      <textarea id="token" v-model="token" class="form-input token-area" readonly></textarea>
      <p v-if="token && token.startsWith('Error')" class="error-msg">
        {{ token }}
      </p>
    </div>

    <button @click="generarToken" class="btn-generar">Generar</button>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      semilla: '',
      tiempoVigencia: '',
      token: ''
    };
  },
  mounted() {
    // Limpiar visualmente los campos al entrar
    this.semilla = '';
    this.tiempoVigencia = '';
    this.token = '';
  },
  methods: {
    async generarToken() {
      if (!this.semilla || !this.tiempoVigencia) {
        alert('Por favor completa todos los campos');
        return;
      }

      const tiempoEnSegundos = parseInt(this.tiempoVigencia);

      if (isNaN(tiempoEnSegundos) || tiempoEnSegundos <= 0) {
        alert('El tiempo de vigencia debe ser un número mayor que 0');
        return;
      }

      try {
        const url = `http://localhost:8082/auth/token?semilla=${encodeURIComponent(this.semilla)}&tiempo=${tiempoEnSegundos}`;

        const response = await axios.get('http://localhost:8082/auth/token', {
          params: {
            semilla: this.semilla,
            tiempo: tiempoEnSegundos
          }
        });

        this.token = response.data.accessToken;

        // Guardar el token en localStorage automáticamente
        localStorage.setItem('token', response.data.accessToken);
        localStorage.setItem('role', response.data.role);
        localStorage.setItem('user', this.semilla);

      } catch (error) {
        console.error("Error al generar token", error);
        this.token = "Error: " + (error.response?.data?.error || error.message);
      }
    }
  }
};
</script>

<style scoped>
.generar-token-container {
  max-width: 400px;
  margin: 0 auto;
  text-align: left;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
}

.title {
  text-align: center;
  color: #3498db;
  font-weight: bold;
}

.form-group {
  margin-bottom: 15px;
}

label {
  display: block;
  color: #3498db;
  margin-bottom: 5px;
}

.form-input {
  width: 100%;
  padding: 8px;
  border: 1px solid #34495e;
  border-radius: 5px;
  box-sizing: border-box;
}

.token-area {
  height: 80px;
}

.btn-generar {
  width: 100%;
  padding: 10px;
  background-color: #00a8ff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
}

.btn-generar:hover {
  background-color: #0097e6;
}

.error-msg {
  color: #e74c3c;
  font-weight: bold;
  margin-top: 5px;
}
</style>
