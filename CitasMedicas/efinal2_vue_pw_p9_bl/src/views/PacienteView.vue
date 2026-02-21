<template>
  <div class="pacientes-page animate-fade-in">
    <header class="view-header">
      <div class="header-content">
        <span class="badge-premium">Administración</span>
        <h1>Registro de <span class="gradient-text">Pacientes</span></h1>
        <p>Sistema integral para la gestión y consulta de expedientes médicos.</p>
      </div>
      <div class="header-stats d-none d-md-flex">
        <div class="stat-pill">
          <span class="stat-value">{{ pacientes.length }}</span>
          <span class="stat-label">Total</span>
        </div>
      </div>
    </header>

    <div class="content-grid">
      <!-- Primer Componente: Lista de Pacientes -->
      <section class="glass-container list-section">
        <div class="section-header">
          <div class="d-flex align-items-center">
            <div class="icon-circle primary me-3">
              <i class="bi bi-people-fill"></i>
            </div>
            <div>
              <h3 class="mb-0">Lista de Pacientes</h3>
              <small class="text-muted">Directorio principal</small>
            </div>
          </div>
        </div>

        <div class="table-frame">
          <table class="premium-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Nombre Completo</th>
                <th>Nacimiento</th>
                <th class="text-end">Acciones</th>
              </tr>
            </thead>
            <tbody>
              <transition-group name="list-stagger">
                <tr v-for="(p, idx) in pacientes" :key="p.data.id" :style="{ '--idx': idx }">
                  <td class="col-id"><span class="id-tag">{{ p.data.id }}</span></td>
                  <td class="col-name">
                    <div class="name-wrapper">
                      <span class="main-name">{{ p.data.nombre }} {{ p.data.apellido }}</span>
                      <span class="sub-detail">CI: {{ p.data.cedula }}</span>
                    </div>
                  </td>
                  <td>{{ formatearFecha(p.data.fechaNacimiento) }}</td>
                  <td class="text-end">
                    <button @click="verDetalle(p)" class="btn-action-view">
                      <span>Ver Ficha</span>
                      <i class="bi bi-chevron-right ms-2"></i>
                    </button>
                  </td>
                </tr>
              </transition-group>
              <tr v-if="pacientes.length === 0">
                <td colspan="4" class="empty-placeholder">
                  <div class="empty-icon">📂</div>
                  <p>No se encontraron registros en el sistema.</p>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </section>

      <!-- Segundo Componente: Formulario Detalle -->
      <section class="glass-container detail-section">
        <div class="section-header d-flex justify-content-between align-items-center">
          <div class="d-flex align-items-center">
            <div class="icon-circle secondary me-3">
              <i class="bi bi-person-badge"></i>
            </div>
            <h3 class="mb-0">Expediente</h3>
          </div>
          <button @click="modoCrear" class="btn-premium-action" :class="{ 'active': isCrear }">
            <i class="bi bi-plus-lg me-1"></i> Nuevo
          </button>
        </div>

        <div class="form-wrapper">
          <div class="row g-4">
            <div class="col-12">
              <div class="floating-group">
                <input type="text" v-model="detalle.cedula" :readonly="!isCrear" class="f-input" placeholder=" " />
                <label>Número de Cédula</label>
              </div>
            </div>
            <div class="col-md-6">
              <div class="floating-group">
                <input type="text" v-model="detalle.nombre" :readonly="!isCrear" class="f-input" placeholder=" " />
                <label>Nombres</label>
              </div>
            </div>
            <div class="col-md-6">
              <div class="floating-group">
                <input type="text" v-model="detalle.apellido" :readonly="!isCrear" class="f-input" placeholder=" " />
                <label>Apellidos</label>
              </div>
            </div>
            <div class="col-12">
              <div class="floating-group">
                <input type="text" v-model="detalle.direccion" :readonly="!isCrear" class="f-input" placeholder=" " />
                <label>Dirección Domiciliaria</label>
              </div>
            </div>
            <div class="col-12">
              <div class="floating-group">
                <input type="date" v-model="detalle.fechaNacimiento" :readonly="!isCrear" class="f-input"
                  placeholder=" " />
                <label>Fecha de Nacimiento</label>
              </div>
            </div>
          </div>

          <div v-if="isCrear" class="mt-5 action-area">
            <button @click="guardarPaciente" class="btn-save-premium">
              <span class="btn-content">
                <i class="bi bi-check-all me-2"></i>
                Registrar Paciente
              </span>
            </button>
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

<script>
import ApiClient from '@/clients/ApiClient';
import notify from '@/utils/notify';

export default {
  name: 'PacienteView',
  data() {
    return {
      pacientes: [],
      detalle: {
        id: null,
        cedula: '',
        nombre: '',
        apellido: '',
        direccion: '',
        fechaNacimiento: ''
      },
      isCrear: false
    };
  },
  mounted() {
    this.cargarPacientes();
  },
  methods: {
    async cargarPacientes() {
      try {
        const response = await ApiClient.getAllPacientes();
        this.pacientes = response.data;
      } catch (error) {
        notify('Error al sincronizar lista de pacientes.', 'error');
      }
    },
    modoCrear() {
      this.isCrear = true;
      this.detalle = {
        id: null,
        cedula: '',
        nombre: '',
        apellido: '',
        direccion: '',
        fechaNacimiento: ''
      };
    },
    async guardarPaciente() {
      try {
        await ApiClient.createPaciente(this.detalle);
        notify('Paciente registrado con éxito.', 'success');
        this.isCrear = false;
        await this.cargarPacientes();
      } catch (error) {
        notify('No se pudo completar el registro del paciente.', 'error');
      }
    },
    async verDetalle(pacienteObj) {
      this.isCrear = false;
      const selfLink = pacienteObj.links.find(l => l.rel === 'self');
      if (selfLink) {
        try {
          const response = await ApiClient.getPacienteByUrl(selfLink.href);
          const data = response.data.data || response.data;
          this.detalle = { ...data };
        } catch (error) {
          notify('Error al recuperar detalles del expediente.', 'error');
        }
      }
    },
    formatearFecha(fecha) {
      if (!fecha) return '-';
      return fecha;
    }
  }
};
</script>

<style scoped>
.pacientes-page {
  padding: 3rem 2rem;
  max-width: 1600px;
  margin: 0 auto;
}

/* Header Enhancements */
.view-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 3rem;
}

.header-content h1 {
  font-size: 3rem;
  font-weight: 800;
  color: var(--text-main);
  margin: 0.5rem 0;
  letter-spacing: -1px;
}

.gradient-text {
  background: linear-gradient(135deg, var(--primary), var(--secondary));
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
}

.badge-premium {
  background: var(--input-bg);
  color: var(--primary);
  padding: 0.4rem 1rem;
  border-radius: 99px;
  font-size: 0.8rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 1px;
  border: 1px solid var(--glass-border);
}

.stat-pill {
  background: var(--card-surface);
  padding: 1rem 1.5rem;
  border-radius: 16px;
  box-shadow: var(--shadow-soft);
  display: flex;
  flex-direction: column;
  align-items: center;
  min-width: 120px;
  border: 1px solid var(--glass-border);
}

.stat-value {
  font-size: 2rem;
  font-weight: 800;
  color: var(--primary);
}

.stat-label {
  font-size: 0.8rem;
  color: var(--text-muted);
  font-weight: 600;
}

/* Grid Layout */
.content-grid {
  display: grid;
  grid-template-columns: 1.1fr 0.9fr;
  gap: 2.5rem;
}

.glass-container {
  background: var(--glass-bg);
  backdrop-filter: blur(10px);
  border: 1px solid var(--glass-border);
  border-radius: 24px;
  padding: 2rem;
  box-shadow: var(--shadow-soft);
  transition: transform 0.3s ease, background 0.3s ease;
}

.glass-container:hover {
  transform: translateY(-5px);
}

.section-header {
  margin-bottom: 2rem;
  padding-bottom: 1.5rem;
  border-bottom: 1px solid var(--glass-border);
}

.icon-circle {
  width: 48px;
  height: 48px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.2rem;
}

.icon-circle.primary {
  background: var(--input-bg);
  color: var(--primary);
  border: 1px solid var(--glass-border);
}

.icon-circle.secondary {
  background: var(--input-bg);
  color: var(--secondary);
  border: 1px solid var(--glass-border);
}

/* Table Styling */
.table-frame {
  max-height: 600px;
  overflow-y: auto;
}

.premium-table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0 10px;
}

.premium-table th {
  padding: 1rem;
  color: var(--text-muted);
  font-weight: 600;
  font-size: 0.9rem;
  text-transform: uppercase;
}

.premium-table tbody tr {
  background: var(--card-surface);
  transition: all 0.3s ease;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.02);
  color: var(--text-main);
}

.premium-table tbody tr:hover {
  transform: scale(1.01) translateX(5px);
  box-shadow: var(--shadow-soft);
  z-index: 10;
  border-left: 4px solid var(--primary);
}

.premium-table td {
  padding: 1.2rem 1rem;
  border: none;
}

.premium-table td:first-child {
  border-radius: 12px 0 0 12px;
}

.premium-table td:last-child {
  border-radius: 0 12px 12px 0;
}

.id-tag {
  background: var(--input-bg);
  padding: 0.3rem 0.8rem;
  border-radius: 8px;
  font-weight: 700;
  color: var(--text-muted);
  border: 1px solid var(--glass-border);
}

.name-wrapper {
  display: flex;
  flex-direction: column;
}

.main-name {
  font-weight: 700;
  color: var(--text-main);
}

.sub-detail {
  font-size: 0.8rem;
  color: var(--text-muted);
}

.btn-action-view {
  background: var(--input-bg);
  border: 1px solid var(--glass-border);
  padding: 0.6rem 1.2rem;
  border-radius: 10px;
  color: var(--primary);
  font-weight: 700;
  display: inline-flex;
  align-items: center;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-action-view:hover {
  background: var(--primary);
  color: white;
  border-color: var(--primary);
}

/* Form Styling */
.floating-group {
  position: relative;
  margin-bottom: 0;
}

.f-input {
  width: 100%;
  padding: 1.2rem 1rem 0.6rem;
  border: 2px solid var(--glass-border);
  border-radius: 14px;
  background: var(--input-bg);
  font-size: 1rem;
  font-weight: 600;
  transition: border-color 0.3s;
  outline: none;
  color: var(--text-main);
}

.f-input:focus {
  border-color: var(--primary);
  background: var(--card-surface);
}

.floating-group label {
  position: absolute;
  top: 50%;
  left: 1rem;
  transform: translateY(-50%);
  color: #94a3b8;
  transition: all 0.3s;
  pointer-events: none;
}

.f-input:focus~label,
.f-input:not(:placeholder-shown)~label {
  top: 0.8rem;
  font-size: 0.75rem;
  color: var(--primary);
  font-weight: 700;
}

.btn-premium-action {
  background: var(--input-bg);
  border: 1px solid var(--glass-border);
  padding: 0.6rem 1.5rem;
  border-radius: 12px;
  color: var(--text-muted);
  font-weight: 700;
  transition: all 0.3s;
}

.btn-premium-action:hover,
.btn-premium-action.active {
  background: var(--secondary);
  color: white;
  border-color: var(--secondary);
  box-shadow: 0 4px 12px rgba(20, 184, 166, 0.3);
}

.btn-save-premium {
  width: 100%;
  background: var(--primary);
  color: white;
  border: none;
  padding: 1.2rem;
  border-radius: 16px;
  font-weight: 700;
  font-size: 1.1rem;
  cursor: pointer;
  transition: all 0.3s;
  position: relative;
  overflow: hidden;
}

.btn-save-premium:hover {
  background: var(--primary-hover);
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(99, 102, 241, 0.4);
}

.status-box {
  background: #ecfdf5;
  color: #065f46;
  padding: 1rem;
  border-radius: 12px;
  display: flex;
  align-items: center;
  gap: 0.75rem;
  font-weight: 600;
}

.status-box.error {
  background: #fef2f2;
  color: #991b1b;
}

/* Animations */
.list-stagger-enter-active {
  animation: fadeInUp 0.5s ease-out forwards;
  animation-delay: calc(var(--idx) * 0.05s);
}

.empty-placeholder {
  text-align: center;
  padding: 5rem 0;
}

.empty-icon {
  font-size: 3rem;
  margin-bottom: 1rem;
}

@media (max-width: 1200px) {
  .content-grid {
    grid-template-columns: 1fr;
  }
}
</style>
