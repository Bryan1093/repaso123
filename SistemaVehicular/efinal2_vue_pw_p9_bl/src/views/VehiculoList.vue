<template>
  <div class="container">
    <div class="header-actions">
      <h1>Inventario de Vehículos</h1>
      <button class="btn-logout" @click="logout">Cerrar Sesión</button>
    </div>
    <div v-if="error" class="error-msg">
      Error: {{ error }}
    </div>
    <table class="vehicle-table">
      <thead>
        <tr>
          <th>Id</th>
          <th>Placa</th>
          <th>Marca</th>
          <th>Modelo</th>
          <th>Color</th>
          <th>Acción</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="v in vehiculos" :key="v.id">
          <td>{{ v.id }}</td>
          <td>{{ v.placa }}</td>
          <td>{{ v.marca }}</td>
          <td>{{ v.modelo }}</td>
          <td>{{ v.color }}</td>
          <td>
            <button class="btn-visualizar" @click="visualizar(v)">Visualizar</button>
          </td>
        </tr>
      </tbody>
    </table>

    <div v-if="selectedVehiculo" class="detail-section">
      <h3>Detalles del Vehículo</h3>
      <div class="form-group">
        <label>Id</label>
        <input type="text" :value="selectedVehiculo.id" disabled>
      </div>
      <div class="form-group">
        <label>Placa</label>
        <input type="text" :value="selectedVehiculo.placa" disabled>
      </div>
      <div class="form-group">
        <label>Marca</label>
        <input type="text" :value="selectedVehiculo.marca" disabled>
      </div>
      <div class="form-group">
        <label>Modelo</label>
        <input type="text" :value="selectedVehiculo.modelo" disabled>
      </div>
      <div class="form-group">
        <label>Color</label>
        <input type="text" :value="selectedVehiculo.color" disabled>
      </div>
    </div>

    <div class="create-section">
      <h3>Agregar Nuevo Vehículo</h3>
      <form @submit.prevent="crearVehiculo">
        <div class="form-group">
          <label>Placa</label>
          <input type="text" v-model="newVehiculo.placa" required>
        </div>
        <div class="form-group">
          <label>Marca</label>
          <input type="text" v-model="newVehiculo.marca" required>
        </div>
        <div class="form-group">
          <label>Modelo</label>
          <input type="text" v-model="newVehiculo.modelo" required>
        </div>
        <div class="form-group">
          <label>Color</label>
          <input type="text" v-model="newVehiculo.color" required>
        </div>
        <button type="submit">Guardar</button>
      </form>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'VehiculoList',
  data() {
    return {
      vehiculos: [],
      selectedVehiculo: null,
      error: null,
      newVehiculo: {
        placa: '',
        marca: '',
        modelo: '',
        color: ''
      }
    }
  },
  async mounted() {
    await this.fetchVehiculos();
  },
  methods: {
    async fetchVehiculos() {
      this.error = null;
      try {
        const token = localStorage.getItem('token');
        const config = {
          headers: { Authorization: `Bearer ${token}` }
        };
        const response = await axios.get('http://localhost:8081/vehiculos/api/v1.0/vehiculos', config);
        this.vehiculos = response.data;
      } catch (error) {
        console.error("Error fetching vehicles", error);
        if (error.response && error.response.status === 401) {
          this.error = "Sesión expirada o no autorizada. Por favor inicie sesión nuevamente.";
          this.$router.push('/login');
        } else {
          this.error = "No se pudieron cargar los vehículos. Verifique que el backend esté ejecutándose en el puerto 8081.";
        }
      }
    },
    async visualizar(vehiculo) {
      const selfLink = vehiculo.links.find(l => l.rel === 'self');
      if (selfLink) {
        try {
          const token = localStorage.getItem('token');
          const config = {
            headers: { Authorization: `Bearer ${token}` }
          };
          const response = await axios.get(selfLink.href, config);
          this.selectedVehiculo = response.data;
        } catch (error) {
          console.error("Error fetching vehicle details", error);
          if (error.response && error.response.status === 401) {
            this.$router.push('/login');
          }
        }
      } else {
        console.warn("No hypermedia link found for this vehicle");
      }
    },
    async crearVehiculo() {
      try {
        const token = localStorage.getItem('token');
        const config = {
          headers: { Authorization: `Bearer ${token}` }
        };
        await axios.post('http://localhost:8081/vehiculos/api/v1.0/vehiculos', this.newVehiculo, config);
        this.newVehiculo = { placa: '', marca: '', modelo: '', color: '' };
        await this.fetchVehiculos(); // Refresh list
      } catch (error) {
        console.error("Error creating vehicle", error);
        if (error.response && error.response.status === 401) {
          this.$router.push('/login');
        } else {
          this.error = "Error al crear vehículo. (Solo Admin puede crear)";
          setTimeout(() => this.error = null, 5000);
        }
      }
    },
    logout() {
      localStorage.removeItem('token');
      localStorage.removeItem('role');
      this.$router.push('/login');
    }
  }
}
</script>

<style>
.container {
  padding: 20px;
  font-family: Arial, sans-serif;
  max-width: 800px;
  margin: 0 auto;
}

.header-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.btn-logout {
  background-color: #d9534f;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
}

.btn-logout:hover {
  background-color: #c9302c;
}

.vehicle-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
}

.vehicle-table th,
.vehicle-table td {
  border: 1px solid #000;
  padding: 8px;
  text-align: left;
}

.vehicle-table th {
  background-color: #f2f2f2;
  font-weight: bold;
}

.btn-visualizar {
  background-color: white;
  border: 1px solid black;
  cursor: pointer;
  padding: 5px 15px;
  font-size: 14px;
}

.btn-visualizar:hover {
  background-color: #f0f0f0;
}

.detail-section {
  margin-top: 30px;
  width: 300px;
  margin-left: auto;

  margin-right: auto;
}

.create-section {
  margin-top: 30px;
  width: 300px;
  margin-left: auto;
  margin-right: auto;
  border-top: 1px solid #ccc;
  padding-top: 20px;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: normal;
}

.form-group input {
  width: 100%;
  padding: 8px;
  border: 1px solid #000;
  box-sizing: border-box;
}

.error-msg {
  background-color: #ffdddd;
  color: #d8000c;
  border: 1px solid #d8000c;
  padding: 10px;
  margin-bottom: 20px;
  border-radius: 4px;
}
</style>
