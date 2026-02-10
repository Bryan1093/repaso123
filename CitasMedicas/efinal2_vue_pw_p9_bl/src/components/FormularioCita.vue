<template>
  <div class="card p-3">
    <h3>Agendar Cita</h3>
    <div v-if="message" :class="['alert', isError ? 'alert-danger' : 'alert-success']">
      {{ message }}
    </div>
    <form @submit.prevent="agendar">
      <div class="form-group">
        <label>Cédula Doctor:</label>
        <input v-model="cita.doctorCedula" type="text" class="form-control" required>
      </div>

      <div class="form-group">
        <label>Cédula Paciente:</label>
        <input v-model="cita.pacienteCedula" type="text" class="form-control" required>
      </div>

      <div class="form-group">
        <label>Fecha Cita:</label>
        <input v-model="cita.fechaCita" type="datetime-local" class="form-control" required>
      </div>

      <button type="submit" class="btn btn-primary mt-3">Agendar</button>
    </form>
  </div>
</template>

<script>
import ApiClient from '@/clients/ApiClient';

export default {
  data() {
    return {
      cita: {
        doctorCedula: '',
        pacienteCedula: '',
        fechaCita: ''
      },
      message: '',
      isError: false
    };
  },
  methods: {
    async agendar() {
      try {
        await ApiClient.createCita(this.cita);
        this.showNotification('Cita agendada exitosamente', false);
        this.cita = { doctorCedula: '', pacienteCedula: '', fechaCita: '' };
        this.$emit('cita-agendada');
      } catch (error) {
        let errorDetails = error.message;
        if (error.response && error.response.data) {
          errorDetails = typeof error.response.data === 'object'
            ? JSON.stringify(error.response.data)
            : error.response.data;
        }
        const errorMsg = 'Error al agendar cita: ' + errorDetails;
        this.showNotification(errorMsg, true);
      }
    },
    showNotification(msg, isError) {
      this.message = msg;
      this.isError = isError;
      setTimeout(() => {
        this.message = '';
        this.isError = false;
      }, 5000);
    }
  }
};
</script>
