<template>
  <div class="card p-3 mt-4">
    <h3>Citas Agendadas</h3>
    <table class="table table-striped">
      <thead>
        <tr>
          <th>Nombre Paciente</th>
          <th>Apellido Paciente</th>
          <th>Cédula Paciente</th>
          <th>Fecha Cita</th>
          <th>Nombre Doctor</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="cita in citas" :key="cita.id">
          <td>{{ cita.paciente.nombre }}</td>
          <td>{{ cita.paciente.apellido }}</td>
          <td>{{ cita.paciente.cedula }}</td>
          <td>{{ formatearFecha(cita.fechaCita) }}</td>
          <td>{{ cita.doctor.nombre }}</td>
        </tr>
      </tbody>
    </table>
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
        return new Date(fecha).toLocaleString();
    }
  }
};
</script>
