<template>
  <div class="container">
    <h2>Crear Nuevo Paciente</h2>

    <div v-if="message" :class="['alert', isError ? 'alert-danger' : 'alert-success']">
      {{ message }}
    </div>

    <form @submit.prevent="submitForm">
      <div class="form-group">
        <label>Cédula:</label>
        <input v-model="paciente.cedula" type="text" class="form-control" required maxlength="10">
      </div>

      <div class="form-group">
        <label>Nombre:</label>
        <input v-model="paciente.nombre" type="text" class="form-control" required>
      </div>

      <div class="form-group">
        <label>Apellido:</label>
        <input v-model="paciente.apellido" type="text" class="form-control" required>
      </div>

      <div class="form-group">
        <label>Fecha Nacimiento:</label>
        <input v-model="paciente.fechaNacimiento" type="date" class="form-control" required>
      </div>

      <button type="submit" class="btn btn-primary mt-3">Guardar</button>
    </form>
  </div>
</template>

<script>
import ApiClient from '@/clients/ApiClient';

export default {
  data() {
    return {
      paciente: {
        cedula: '',
        nombre: '',
        apellido: '',
        fechaNacimiento: ''
      },
      message: '',
      isError: false
    };
  },
  methods: {
    async submitForm() {
      try {
        await ApiClient.createPaciente(this.paciente);
        this.showNotification('Paciente guardado exitosamente', false);

        this.paciente = {
          cedula: '',
          nombre: '',
          apellido: '',
          fechaNacimiento: ''
        };

      } catch (error) {
        let errorDetails = error.message;
        if (error.response && error.response.data) {
          errorDetails = typeof error.response.data === 'object'
            ? JSON.stringify(error.response.data)
            : error.response.data;
        }
        const errorMsg = 'Error al guardar el paciente: ' + errorDetails;
        this.showNotification(errorMsg, true);
      }
    },
    showNotification(msg, isError) {
      this.message = msg;
      this.isError = isError;
      setTimeout(() => {
        this.message = '';
        this.isError = false;
      }, 10000);
    }
  }
};
</script>

<style scoped>
.container {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}

.form-group {
  margin-bottom: 15px;
}
</style>
