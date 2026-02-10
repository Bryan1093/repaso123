<template>
  <div class="page-container">

    <div class="glass-card header-card">
      <div class="input-row">
        <div class="form-group">
          <label>Número Venta</label>
          <input v-model="venta.numeroVenta" type="text" placeholder="001" />
        </div>
        <div class="form-group">
          <label>Cédula</label>
          <input v-model="venta.cedula" type="text" placeholder="17..." />
        </div>
        <button @click="guardarVenta" class="btn-primary" :disabled="productos.length === 0">GUARDAR</button>
      </div>
    </div>


    <div class="glass-card action-card">
      <div class="input-row">
        <div class="form-group">
          <label>Código Barras</label>
          <input v-model="item.codigoBarras" type="text" placeholder="Código" />
        </div>
        <div class="form-group">
          <label>Cantidad</label>
          <input v-model.number="item.cantidad" type="number" min="1" />
        </div>
        <button @click="agregarProducto" class="btn-secondary">Agregar</button>
      </div>
    </div>


    <div class="glass-card table-card">
      <table class="styled-table">
        <thead>
          <tr>
            <th>C. Barras</th>
            <th>Nombre</th>
            <th>Cantidad</th>
            <th>Precio</th>
            <th>Subtotal</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(p, index) in productos" :key="index">
            <td>{{ p.codigoBarras }}</td>
            <td>{{ p.nombre }}</td>
            <td>{{ p.cantidad }}</td>
            <td>{{ p.precioUnitario }}</td>
            <td>{{ p.subtotal.toFixed(2) }}</td>
          </tr>
        </tbody>
      </table>
      <div class="total-section">
        <h3>TOTAL: {{ totalVenta.toFixed(2) }}</h3>
      </div>
    </div>

    <div v-if="message" :class="['notification', messageType]">
      {{ message }}
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      venta: {
        numeroVenta: '',
        cedula: ''
      },
      item: {
        codigoBarras: '',
        cantidad: 1
      },
      productos: [],
      message: '',
      messageType: ''
    };
  },
  computed: {
    totalVenta() {
      return this.productos.reduce((sum, p) => sum + p.subtotal, 0);
    }
  },
  methods: {
    async agregarProducto() {
      if (!this.item.codigoBarras || this.item.cantidad <= 0) {
        this.showNotification('Ingrese código y cantidad válida', 'error');
        return;
      }

      try {
        const response = await axios.get(`http://localhost:8081/supermaxi/api/v1.0/productos/${this.item.codigoBarras}`);
        const prod = response.data;

        if (prod.stock < this.item.cantidad) {
          this.showNotification('Stock no disponible', 'error');
          return;
        }

        const subtotal = prod.precioUnitario * this.item.cantidad;

        this.productos.push({
          codigoBarras: prod.codigoBarras,
          nombre: prod.nombre,
          cantidad: this.item.cantidad,
          precioUnitario: prod.precioUnitario,
          subtotal: subtotal
        });

        this.item.codigoBarras = '';
        this.item.cantidad = 1;
        this.showNotification('Producto agregado', 'success');

      } catch (error) {
        if (error.response && error.response.status === 404) {
          this.showNotification('Producto no encontrado', 'error');
        } else {
          this.showNotification('Error al buscar producto', 'error');
        }
      }
    },
    async guardarVenta() {
      if (!this.venta.numeroVenta || !this.venta.cedula) {
        this.showNotification('Complete los datos de la venta', 'error');
        return;
      }

      const payload = {
        numeroVenta: this.venta.numeroVenta,
        cedula: this.venta.cedula,
        productos: this.productos.map(p => ({
          codigoBarras: p.codigoBarras,
          cantidad: p.cantidad
        }))
      };

      try {
        await axios.post('http://localhost:8081/supermaxi/api/v1.0/ventas', payload);
        this.showNotification('Venta guardada exitosamente', 'success');
        this.resetVenta();
      } catch (error) {
        this.showNotification('Error al guardar la venta', 'error');
      }
    },
    resetVenta() {
      this.venta = { numeroVenta: '', cedula: '' };
      this.productos = [];
      this.item = { codigoBarras: '', cantidad: 1 };
    },
    showNotification(msg, type) {
      this.message = msg;
      this.messageType = type;
      setTimeout(() => this.message = '', 3000);
    }
  }
};
</script>

<style scoped>
.page-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 2rem;
  gap: 1.5rem;
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

.glass-card {
  background: rgba(255, 255, 255, 0.95);
  padding: 1.5rem;
  border-radius: 15px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 800px;
}

.input-row {
  display: flex;
  gap: 1.5rem;
  align-items: flex-end;
  flex-wrap: wrap;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  flex: 1;
}

label {
  font-weight: 600;
  color: #34495e;
}

input {
  padding: 0.8rem;
  border: 1px solid #ced6e0;
  border-radius: 8px;
  font-size: 1rem;
}

.btn-primary,
.btn-secondary {
  padding: 0.8rem 2rem;
  border: none;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  color: white;
  transition: all 0.2s;
}

.btn-primary {
  background: #3498db;
}

.btn-primary:disabled {
  background: #95a5a6;
  cursor: not-allowed;
}

.btn-secondary {
  background: #2ecc71;
}

.styled-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
  font-size: 0.95rem;
}

.styled-table th,
.styled-table td {
  padding: 0.8rem;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

.styled-table th {
  background-color: #f1f2f6;
  color: #2c3e50;
  font-weight: 700;
}

.total-section {
  display: flex;
  justify-content: flex-end;
  margin-top: 1rem;
  color: #2c3e50;
  font-size: 1.2rem;
  font-weight: bold;
}

.notification {
  position: fixed;
  bottom: 2rem;
  right: 2rem;
  padding: 1rem 2rem;
  border-radius: 8px;
  color: white;
  font-weight: 500;
  animation: slideIn 0.3s ease;
  z-index: 1000;
}

.success {
  background: #2ecc71;
}

.error {
  background: #e74c3c;
}

@keyframes slideIn {
  from {
    transform: translateX(100%);
    opacity: 0;
  }

  to {
    transform: translateX(0);
    opacity: 1;
  }
}
</style>
