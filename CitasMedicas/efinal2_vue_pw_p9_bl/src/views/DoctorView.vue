<template>
  <div class="doctor-page animate-fade-in">
    <div class="row justify-content-center">
      <div class="col-lg-8">
        <header class="view-header text-center d-block mb-5">
          <span class="badge-premium mb-3">Staff Médico</span>
          <h1>Registro de <span class="gradient-text">Especialistas</span></h1>
          <p>Alta de nuevos profesionales al cuerpo médico del consultorio.</p>
        </header>

        <div class="glass-container premium-card p-5">
          <div class="accent-bar mx-auto"></div>

          <form @submit.prevent="submitForm" class="modern-form mt-4">
            <div class="row g-4">
              <div class="col-md-12">
                <div class="p-input-group">
                  <label class="p-label">Número de Cédula</label>
                  <input v-model="doctor.cedula" type="text" class="p-input" required maxlength="10"
                    placeholder="Ej: 1712345678">
                </div>
              </div>

              <div class="col-md-6">
                <div class="p-input-group">
                  <label class="p-label">Nombres Completos</label>
                  <input v-model="doctor.nombre" type="text" class="p-input" required placeholder="Ej: Dr. Julian">
                </div>
              </div>

              <div class="col-md-6">
                <div class="p-input-group">
                  <label class="p-label">Apellidos Completos</label>
                  <input v-model="doctor.apellido" type="text" class="p-input" required placeholder="Ej: Viteri">
                </div>
              </div>

              <div class="col-md-12">
                <div class="p-input-group">
                  <label class="p-label">Género / Identidad</label>
                  <select v-model="doctor.genero" class="p-input" required>
                    <option value="" disabled selected>Seleccione el género...</option>
                    <option value="Hombre">👨 Especialista Varón</option>
                    <option value="Mujer">👩 Especialista Mujer</option>
                  </select>
                </div>
              </div>
            </div>

            <div class="text-center mt-5">
              <button type="submit" class="btn-save-premium">
                <i class="bi bi-person-plus-fill me-2"></i>
                Habilitar Especialista
              </button>
            </div>
          </form>

        </div>
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
      doctor: {
        cedula: '',
        nombre: '',
        apellido: '',
        genero: ''
      }
    };
  },
  methods: {
    async submitForm() {
      try {
        await ApiClient.createDoctor(this.doctor);
        notify('Especialista registrado con éxito en la plataforma.', 'success');
        this.doctor = { cedula: '', nombre: '', apellido: '', genero: '' };
      } catch (error) {
        notify('Error: ' + (error.response?.data || error.message), 'error');
      }
    }
  }
};
</script>

<style scoped>
.doctor-page {
  padding: 4rem 2rem;
  background: transparent;
}

.gradient-text {
  background: linear-gradient(135deg, var(--primary), var(--secondary));
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
}

.badge-premium {
  display: inline-block;
  background: var(--input-bg);
  color: var(--primary);
  padding: 0.5rem 1.2rem;
  border-radius: 99px;
  font-weight: 700;
  font-size: 0.8rem;
  text-transform: uppercase;
  letter-spacing: 2px;
  border: 1px solid var(--glass-border);
}

.view-header h1 {
  font-size: 3.5rem;
  font-weight: 800;
  letter-spacing: -2px;
  color: var(--text-main);
}

.view-header p {
  font-size: 1.2rem;
  color: var(--text-muted);
}

/* Fix for select arrow */
select.p-input {
  appearance: none;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='16' height='16' fill='%2364748b' class='bi bi-chevron-down' viewBox='0 0 16 16'%3E%3Cpath fill-rule='evenodd' d='M1.646 4.646a.5.5 0 0 1 .708 0L8 10.293l5.646-5.647a.5.5 0 0 1 .708.708l-6 6a.5.5 0 0 1-.708 0l-6-6a.5.5 0 0 1 0-.708z'/%3E%3C/svg%3E");
  background-repeat: no-repeat;
  background-position: right 1.2rem center;
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
