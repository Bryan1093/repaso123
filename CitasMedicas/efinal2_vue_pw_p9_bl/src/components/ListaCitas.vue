<template>
  <div class="citas-glass animate-fade-in">
    <div class="section-header d-flex justify-content-between align-items-center mb-4">
      <div>
        <h3 class="mb-0">Citas en Agenda</h3>
        <p class="text-muted small mb-0">Listado cronológico de pacientes citados</p>
      </div>
      <div class="header-tools">
        <button @click="fetchCitas" class="btn-refresh" title="Actualizar">
          <i class="bi bi-arrow-clockwise"></i>
        </button>
      </div>
    </div>

    <div class="table-responsive">
      <table class="premium-table">
        <thead>
          <tr>
            <th>Paciente</th>
            <th>Cédula</th>
            <th>Fecha y Hora</th>
            <th>Especialista</th>
            <th class="text-center">Estado</th>
          </tr>
        </thead>
        <tbody>
          <transition-group name="list-stagger">
            <tr v-for="(cita, idx) in citas" :key="cita.id" :style="{ '--idx': idx }">
              <td>
                <div class="d-flex align-items-center">
                  <div class="mini-avatar me-3">{{ cita.paciente.nombre.charAt(0) }}</div>
                  <div class="name-info">
                    <span class="d-block fw-bold text-main">{{ cita.paciente.nombre }} {{ cita.paciente.apellido
                    }}</span>
                  </div>
                </div>
              </td>
              <td><span class="id-tag">{{ cita.paciente.cedula }}</span></td>
              <td class="text-nowrap">
                <i class="bi bi-calendar3 me-2 text-primary"></i>
                {{ formatearFecha(cita.fechaCita) }}
              </td>
              <td>
                <div class="doctor-badge">
                  <i class="bi bi-person-heart me-1"></i>
                  Dr. {{ cita.doctor.nombre }}
                </div>
              </td>
              <td class="text-center">
                <span class="status-pill verified">Confirmada</span>
              </td>
            </tr>
          </transition-group>
          <tr v-if="citas.length === 0">
            <td colspan="5" class="empty-placeholder">
              <div class="empty-icon">📅</div>
              <p>No hay citas programadas para hoy.</p>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import ApiClient from '@/clients/ApiClient';

export default {
  data() {
    return {
      citas: []
    };
  },
  mounted() {
    this.fetchCitas();
  },
  methods: {
    async fetchCitas() {
      try {
        const response = await ApiClient.getAllCitas();
        this.citas = response.data;
      } catch (error) {
        console.error('Error fetching citas:', error);
      }
    },
    formatearFecha(fecha) {
      if (!fecha) return '';
      const date = new Date(fecha);
      return date.toLocaleDateString() + ' ' + date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    }
  }
};
</script>

<style scoped>
.citas-glass {
  background: var(--glass-bg);
  backdrop-filter: blur(10px);
  border-radius: 28px;
  padding: 2.5rem;
  box-shadow: var(--shadow-soft);
  border: 1px solid var(--glass-border);
}

.mini-avatar {
  width: 36px;
  height: 36px;
  background: var(--input-bg);
  color: var(--primary);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 800;
  font-size: 0.9rem;
  border: 1px solid var(--glass-border);
}

.id-tag {
  background: var(--input-bg);
  padding: 0.2rem 0.6rem;
  border-radius: 6px;
  font-size: 0.85rem;
  color: var(--text-muted);
  font-weight: 600;
  border: 1px solid var(--glass-border);
}

.doctor-badge {
  display: inline-flex;
  align-items: center;
  background: var(--input-bg);
  color: var(--primary);
  padding: 0.3rem 0.8rem;
  border-radius: 8px;
  font-size: 0.85rem;
  font-weight: 600;
  border: 1px solid var(--glass-border);
}

.status-pill {
  padding: 0.3rem 0.8rem;
  border-radius: 99px;
  font-size: 0.75rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.status-pill.verified {
  background: #dcfce7;
  color: #166534;
}

.btn-refresh {
  background: var(--card-surface);
  border: 1px solid var(--glass-border);
  width: 40px;
  height: 40px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--text-muted);
  transition: all 0.2s;
  cursor: pointer;
}

.btn-refresh:hover {
  background: var(--primary);
  color: white;
  border-color: var(--primary);
  transform: rotate(180deg);
}

.empty-placeholder {
  text-align: center;
  padding: 4rem 0;
  color: #94a3b8;
}

.empty-icon {
  font-size: 2.5rem;
  margin-bottom: 1rem;
}

.list-stagger-enter-active {
  animation: fadeInUp 0.5s ease-out forwards;
  animation-delay: calc(var(--idx) * 0.05s);
}
</style>
