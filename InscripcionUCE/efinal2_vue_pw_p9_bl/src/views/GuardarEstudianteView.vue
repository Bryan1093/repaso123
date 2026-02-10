<template>
  <div class="guardar-view">
    <div class="row">
      <div class="col-component content-col">
        <FormularioEstudiante @estudiante-guardado="agregarEstudiante" />
      </div>
    </div>

    <div class="row">
      <div class="col-component content-col">
        <ListaEstudiantes :estudiantes="estudiantes" />
      </div>
    </div>
  </div>
</template>

<script>
import FormularioEstudiante from '@/components/FormularioEstudiante.vue';
import ListaEstudiantes from '@/components/ListaEstudiantes.vue';
import ApiClient from '@/clients/ApiClient';

export default {
  components: {
    FormularioEstudiante,
    ListaEstudiantes
  },
  data() {
    return {
      estudiantes: []
    }
  },
  mounted() {
    const token = localStorage.getItem('token');
    if (token) {
      this.cargarEstudiantes();
    }
  },
  methods: {
    async cargarEstudiantes() {
      try {
        const response = await ApiClient.getAllEstudiantes();
        this.estudiantes = response.data;
      } catch (error) {
        console.error("Error al cargar estudiantes", error);
      }
    },
    agregarEstudiante(nuevoEstudiante) {
      this.estudiantes.push(nuevoEstudiante);
    }
  }
}
</script>

<style scoped>
.guardar-view {
  padding: 20px;
}

.row {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 30px;
}

.label-col {
  width: 150px;
  text-align: right;
  padding-right: 20px;
  color: #3498db;
  font-style: italic;
  font-size: 1.2rem;
}

.content-col {
  flex-grow: 1;
  display: flex;
  justify-content: center;
}
</style>
