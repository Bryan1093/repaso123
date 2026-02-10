<template>
  <div class="lista-container">
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Nombre</th>
          <th>Apellido</th>
          <th>Cédula</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="est in estudiantes" :key="est.id">
          <td>{{ est.id }}</td>
          <td>{{ est.nombre }}</td>
          <td>{{ est.apellido }}</td>
          <td>{{ est.cedula }}</td>
        </tr>
        <tr v-if="estudiantes.length === 0">
          <td colspan="4">...</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
export default {
  props: {
    estudiantes: {
      type: Array,
      default: () => []
    }
  }
};
</script>

<style scoped>
.lista-container {
  border: 1px dashed #34495e;
  padding: 20px;
  max-width: 600px;
  margin: 20px auto;
}

table {
  width: 100%;
  border-collapse: collapse;
  color: #3498db;
}

th, td {
  border: 1px solid #34495e;
  padding: 8px;
  text-align: left;
}

th {
  font-weight: bold;
  text-align: center;
}
</style>
