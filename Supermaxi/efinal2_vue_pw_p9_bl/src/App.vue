<template>
  <nav class="navbar" v-if="!isLoginPage">
    <div class="nav-content">
      <div class="brand">Supermaxi</div>
      <div class="nav-links">
        <router-link v-if="isAdmin" to="/ingresar-producto" class="nav-link">Ingresar Producto</router-link>
        <router-link v-if="isUser || isAdmin" to="/realizar-venta" class="nav-link">Realizar Venta</router-link>
        <a @click="logout" class="nav-link logout-btn" style="cursor: pointer;">Salir</a>
      </div>
    </div>
  </nav>
  <router-view />
</template>

<script>
export default {
  data() {
    return {
      userRole: ''
    }
  },
  computed: {
    isLoginPage() {
      return this.$route.name === 'Login';
    },
    isAdmin() {
      return this.userRole === 'admin';
    },
    isUser() {
      return this.userRole === 'user';
    }
  },
  watch: {
    $route() {
      this.userRole = localStorage.getItem('role');
    }
  },
  mounted() {
    this.userRole = localStorage.getItem('role');
  },
  methods: {
    logout() {
      localStorage.removeItem('token');
      localStorage.removeItem('role');
      this.userRole = '';
      this.$router.push('/login');
    }
  }
}
</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap');

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: 'Inter', sans-serif;
}

body {
  background: #f0f2f5;
}

.navbar {
  background: white;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  padding: 1rem 2rem;
  position: sticky;
  top: 0;
  z-index: 100;
}

.nav-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.brand {
  font-size: 1.5rem;
  font-weight: 800;
  color: #df2020;

  letter-spacing: -0.5px;
}

.nav-links {
  display: flex;
  gap: 2rem;
}

.nav-link {
  text-decoration: none;
  color: #4a5568;
  font-weight: 500;
  padding: 0.5rem 1rem;
  border-radius: 6px;
  transition: all 0.2s ease;
}

.nav-link:hover {
  background: #f7fafc;
  color: #df2020;
}

.nav-link.router-link-active {
  background: #fff5f5;
  color: #df2020;
  font-weight: 600;
}
</style>
