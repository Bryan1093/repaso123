<template>
  <div class="container">
    <h2>Agendar Citas</h2>
    <div class="row">
      <div class="col-md-12 mb-4">
        <FormularioCita @cita-agendada="actualizarLista" />
      </div>
      <div class="col-md-12">
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
