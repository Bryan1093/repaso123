<template>
  <div class="formulario-container">
    <h3 class="title">ESTUDIANTE</h3>

    <div class="form-group">
      <label for="token">Token</label>
      <textarea id="token" v-model="token" class="form-input token-area"></textarea>
    </div>

    <div class="form-group">
      <label for="nombre">Nombre</label>
      <input type="text" id="nombre" v-model="estudiante.nombre" class="form-input" />
    </div>

    <div class="form-group">
      <label for="apellido">Apellido</label>
      <input type="text" id="apellido" v-model="estudiante.apellido" class="form-input" />
    </div>

    <div class="form-group">
      <label for="cedula">Cédula</label>
      <input type="text" id="cedula" v-model="estudiante.cedula" class="form-input" />
    </div>

    <div v-if="mensaje" :class="['mensaje', tipoMensaje]">
      {{ mensaje }}
    </div>

    <div class="btn-container">
      <button @click="guardarEstudiante" class="btn-guardar">Guardar</button>
    </div>
  </div>
</template>

<script>
import ApiClient from '@/clients/ApiClient';

export default {
  data() {
    return {
      token: '',
      estudiante: {
        nombre: '',
        apellido: '',
        cedula: ''
      },
      mensaje: null,
      tipoMensaje: ''
    };
  },
  mounted() {
    // Iniciar con campo vacío
    this.token = '';
  },
  watch: {
    token(newVal) {
      if (newVal) {
        localStorage.setItem('token', newVal);
      }
    }
  },
  methods: {
    async guardarEstudiante() {
      this.mensaje = null;

      // Validación de campos vacíos
      if (!this.estudiante.nombre || !this.estudiante.apellido || !this.estudiante.cedula) {
        this.mensaje = "Por favor complete todos los campos (Nombre, Apellido, Cédula)";
        this.tipoMensaje = "error";
        return;
      }

      // Validación de token
      if (!this.token) {
        this.mensaje = "Por favor ingrese un token válido";
        this.tipoMensaje = "error";
        return;
      }

      try {
        // Asegurar que el token en localStorage es el actual (el que está en el textarea)
        if (this.token) {
          localStorage.setItem('token', this.token);

          // Validación manual de expiración para feedback inmediato
          try {
            const parts = this.token.split('.');
            if (parts.length === 3) {
              const payload = JSON.parse(atob(parts[1]));
              const now = Math.floor(Date.now() / 1000);
              if (payload.exp && payload.exp < now) {
                this.mensaje = "El token ha expirado. Por favor genere uno nuevo.";
                this.tipoMensaje = "error";
                return;
              }
            }
          } catch (e) {
            console.error("Error al validar expiración local", e);
          }
        }

        const response = await ApiClient.createEstudiante(this.estudiante);

        // Emitir evento para avisar al padre
        this.$emit('estudiante-guardado', response.data);

        this.mensaje = "Estudiante guardado exitosamente";
        this.tipoMensaje = "success";
        this.limpiarFormulario();
      } catch (error) {
        console.error("Error al guardar", error);
        let errorDetails = error.message;
        if (error.response && error.response.data) {
          // Si el error es 401/403, probablemente sea expiración
          if (error.response.status === 401 || error.response.status === 403) {
            this.mensaje = "ERROR: El token ha expirado o no es válido. Genere uno nuevo.";
            this.tipoMensaje = "error";
            return; // Salimos aquí para no sobrescribir el mensaje abajo
          } else {
            errorDetails = typeof error.response.data === 'object'
              ? JSON.stringify(error.response.data)
              : error.response.data;
          }
        }
        this.mensaje = "Error al guardar: " + errorDetails;
        this.tipoMensaje = "error";

        // Si hay error de token, podríamos limpiar localStorage para evitar estados inconsistentes,
        // pero como es manual, dejamos que el usuario lo corrija.
      }
    },
    limpiarFormulario() {
      // No limpiamos el token para facilitar multiples guardados
      this.estudiante = { nombre: '', apellido: '', cedula: '' };
    }
  }
};
</script>

<style scoped>
.formulario-container {
  border: 1px dashed #34495e;
  /* Dashed border as in image */
  padding: 20px;
  border-radius: 8px;
  max-width: 400px;
  margin: 0 auto;
}

.title {
  color: #3498db;
  text-align: center;
}

.form-group {
  margin-bottom: 10px;
  text-align: left;
}

.label-title {
  color: #3498db;
  font-weight: bold;
  text-align: center;
  margin-bottom: 15px;
  display: block;
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
  height: 60px;
  border-radius: 10px;
  /* More rounded like image */
}

.btn-container {
  text-align: center;
  margin-top: 15px;
}


.btn-guardar {
  background-color: #00a8ff;
  color: white;
  padding: 10px 30px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.mensaje {
  margin-top: 10px;
  padding: 10px;
  border-radius: 4px;
  text-align: center;
}

.success {
  background-color: #d4edda;
  color: #155724;
  border: 1px solid #c3e6cb;
}

.error {
  background-color: #f8d7da;
  color: #721c24;
  border: 1px solid #f5c6cb;
}
</style>
