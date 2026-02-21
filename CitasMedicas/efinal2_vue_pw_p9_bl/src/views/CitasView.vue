<template>
  <div class="appointments-page animate-fade-in">
    <header class="view-header mb-5">
      <div class="header-content">
        <span class="badge-premium">Planificación</span>
        <h1>Control de <span class="gradient-text">Agenda</span></h1>
        <p>Gestione las citas médicas y horarios de los especialistas.</p>
      </div>
    </header>

    <div class="row g-5">
      <div class="col-xl-4 col-lg-5">
        <FormularioCita @cita-agendada="actualizarLista" />
      </div>
      <div class="col-xl-8 col-lg-7">
        <ListaCitas ref="listaCitas" />
      </div>
    </div>
  </div>
</template>

<script>
import FormularioCita from '@/components/FormularioCita.vue';
import ListaCitas from '@/components/ListaCitas.vue';

export default {
  components: {
    FormularioCita,
    ListaCitas
  },
  methods: {
    actualizarLista() {
      this.$refs.listaCitas.fetchCitas();
    }
  }
};
</script>

<style scoped>
.appointments-page {
  padding: 3rem 2rem;
  max-width: 1700px;
  margin: 0 auto;
}

.view-header h1 {
  font-size: 3rem;
  font-weight: 800;
  color: var(--text-main);
}
</style>
