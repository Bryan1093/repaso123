<template>
  <div id="app">
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4" v-if="!isLoginRoute">
      <div class="container-fluid">
        <a class="navbar-brand" href="#">Agendamiento Médico</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
          aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
          <ul class="navbar-nav me-auto" v-if="isLoggedIn">
            <li class="nav-item">
              <router-link class="nav-link" to="/doctor">Crear Nuevo Doctor</router-link>
            </li>
            <li class="nav-item">
              <router-link class="nav-link" to="/paciente">Crear Nuevo Paciente</router-link>
            </li>
            <li class="nav-item">
              <router-link class="nav-link" to="/citas">Agendar Citas</router-link>
            </li>
          </ul>
          <ul class="navbar-nav ms-auto" v-if="isLoggedIn">
            <li class="nav-item">
              <span class="nav-link text-white">Hola, {{ username }}</span>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#" @click.prevent="logout">Cerrar Sesión</a>
            </li>
          </ul>
        </div>
      </div>
    </nav>
    <router-view />
  </div>
</template>

<script>
import ApiClient from '@/clients/ApiClient';

export default {
  data() {
    return {
      isLoggedIn: false,
      username: ''
    };
  },
  computed: {
    isLoginRoute() {
      return this.$route.path === '/login';
    }
  },
  watch: {
    $route() {
      this.checkAuth();
    }
  },
  mounted() {
    this.checkAuth();
  },
  methods: {
    checkAuth() {
      const token = localStorage.getItem('token');
      this.isLoggedIn = !!token;
      this.username = localStorage.getItem('user') || 'Usuario';
    },
    logout() {
      ApiClient.logout();
      this.isLoggedIn = false;
      this.$router.push('/login');
    }
  }
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  color: #2c3e50;
}
</style>
