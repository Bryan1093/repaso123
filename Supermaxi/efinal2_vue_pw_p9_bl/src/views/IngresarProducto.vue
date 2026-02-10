<template>
  <div class="page-container">
    <div class="glass-card">
      <h2 class="title">Ingresar Producto</h2>
      <form @submit.prevent="guardar" class="form-grid">
        <div class="form-group">
          <label>Código Barras</label>
          <input v-model="producto.codigoBarras" type="text" required placeholder="Ej. 123456" />
        </div>
        <div class="form-group">
          <label>Nombre</label>
          <input v-model="producto.nombre" type="text" required placeholder="Ej. Pan" />
        </div>
        <div class="form-group">
          <label>Categoría</label>
          <input v-model="producto.categoria" type="text" required placeholder="Ej. Panadería" />
        </div>
        <div class="form-group">
          <label>Stock</label>
          <input v-model.number="producto.stock" type="number" min="1" required />
        </div>
        <div class="form-group">
          <label>Precio Unitario</label>
          <input v-model.number="producto.precioUnitario" type="number" step="0.01" required />
        </div>
        <div class="form-actions">
          <button type="submit" class="btn-primary">Guardar</button>
        </div>
      </form>
      <div v-if="message" :class="['notification', messageType]">
        {{ message }}
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      producto: {
        codigoBarras: '',
        nombre: '',
        categoria: '',
        stock: 0,
        precioUnitario: 0.0
      },
      message: '',
      messageType: ''
    };
  },
  methods: {
    async guardar() {
      try {
        await axios.post('http://localhost:8081/supermaxi/api/v1.0/productos', this.producto);
        this.message = 'Producto ingresado correctamente';
        this.messageType = 'success';
        this.resetForm();
      } catch (error) {
        console.error(error);
        this.message = 'Error al ingresar el producto';
        this.messageType = 'error';
      }
      setTimeout(() => this.message = '', 3000);
    },
    resetForm() {
      this.producto = { codigoBarras: '', nombre: '', categoria: '', stock: 0, precioUnitario: 0 };
    }
  }
};
</script>

<style scoped>
.page-container {
  display: flex;
  justify-content: center;
  align-items: flex-start;
  padding-top: 50px;
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

.glass-card {
  background: rgba(255, 255, 255, 0.95);
  padding: 2.5rem;
  border-radius: 20px;
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.15);
  backdrop-filter: blur(4px);
  border: 1px solid rgba(255, 255, 255, 0.18);
  width: 100%;
  max-width: 500px;
  transition: transform 0.3s ease;
}

.glass-card:hover {
  transform: translateY(-5px);
}

.title {
  color: #2c3e50;
  margin-bottom: 2rem;
  text-align: center;
  font-weight: 700;
  font-size: 1.8rem;
}

.form-grid {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

label {
  font-weight: 600;
  color: #34495e;
  font-size: 0.9rem;
}

input {
  padding: 0.8rem;
  border: 2px solid #e0e6ed;
  border-radius: 10px;
  font-size: 1rem;
  transition: all 0.3s ease;
  background: #f8fafc;
}

input:focus {
  outline: none;
  border-color: #3498db;
  background: white;
  box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.1);
}

.form-actions {
  margin-top: 1rem;
  display: flex;
  justify-content: center;
}

.btn-primary {
  background: linear-gradient(135deg, #3498db, #2980b9);
  color: white;
  border: none;
  padding: 0.8rem 2.5rem;
  border-radius: 10px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 1rem;
  box-shadow: 0 4px 6px rgba(52, 152, 219, 0.2);
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 12px rgba(52, 152, 219, 0.3);
}

.notification {
  margin-top: 1.5rem;
  padding: 1rem;
  border-radius: 10px;
  text-align: center;
  font-weight: 500;
  animation: fadeIn 0.3s ease;
}

.success {
  background-color: #d4edda;
  color: #155724;
  border: 1px solid #c3e6cb;
}

.error {
  background-color: #f8d7da;
  color: #721c24;
  border: 1px solid #f5c6cb;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
