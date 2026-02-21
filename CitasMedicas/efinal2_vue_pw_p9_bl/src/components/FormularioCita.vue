<template>
  <div class="booking-glass animate-fade-in">
    <div class="booking-header">
      <div class="icon-orb">
        <i class="bi bi-calendar-check"></i>
      </div>
      <div class="ms-3">
        <h3 class="mb-0">Agendar Cita</h3>
        <small class="text-muted">Nueva entrada en agenda</small>
      </div>
    </div>

    <form @submit.prevent="agendar" class="mt-4">
      <div class="p-input-group">
        <label class="p-label">ID Especialista (Cédula)</label>
        <div class="input-wrapper">
          <i class="bi bi-person-badge input-icon"></i>
          <input v-model="cita.doctorCedula" type="text" class="p-input ps-5" required placeholder="Ej: 1712345678">
        </div>
      </div>

      <div class="p-input-group">
        <label class="p-label">ID Paciente (Cédula)</label>
        <div class="input-wrapper">
          <i class="bi bi-person-fill input-icon"></i>
          <input v-model="cita.pacienteCedula" type="text" class="p-input ps-5" required placeholder="Ej: 0912345678">
        </div>
      </div>

      <div class="p-input-group">
        <label class="p-label">Fecha y Hora de Cita</label>
        <div class="input-wrapper">
          <i class="bi bi-clock input-icon"></i>
          <input v-model="cita.fechaCita" type="datetime-local" class="p-input ps-5" required>
        </div>
      </div>

      <button type="submit" class="btn-save-premium w-100 mt-2">
        <span>Confirmar Agendamiento</span>
      </button>
    </form>
  </div>
</template>

<script>
import ApiClient from '@/clients/ApiClient';
import notify from '@/utils/notify';

export default {
  data() {
    return {
      cita: {
        doctorCedula: '',
        pacienteCedula: '',
        fechaCita: ''
      }
    };
  },
  methods: {
    async agendar() {
      try {
        await ApiClient.createCita(this.cita);
        notify('Cita médica agendada correctamente.', 'success');
        this.cita = { doctorCedula: '', pacienteCedula: '', fechaCita: '' };
        this.$emit('cita-agendada');
      } catch (error) {
        notify('Error al procesar el agendamiento.', 'error');
      }
    }
  }
};
</script>

<style scoped>
.booking-glass {
  background: var(--card-surface);
  border-radius: 28px;
  padding: 2.5rem;
  box-shadow: var(--shadow-soft);
  border: 1px solid var(--glass-border);
  transition: background 0.3s ease;
}

.booking-header {
  display: flex;
  align-items: center;
  margin-bottom: 2rem;
}

.icon-orb {
  width: 50px;
  height: 50px;
  background: linear-gradient(135deg, var(--primary), var(--secondary));
  border-radius: 15px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 1.5rem;
  box-shadow: 0 8px 15px rgba(99, 102, 241, 0.3);
}

.input-wrapper {
  position: relative;
}

.input-icon {
  position: absolute;
  left: 1.2rem;
  top: 50%;
  transform: translateY(-50%);
  color: #94a3b8;
  font-size: 1.1rem;
}

.ps-5 {
  padding-left: 3.5rem !important;
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
