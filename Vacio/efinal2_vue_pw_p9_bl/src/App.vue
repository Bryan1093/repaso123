<template>
  <nav>
    <router-link to="/">Home</router-link> |
    <router-link to="/about">About</router-link> |
    <router-link to="/consultar-todos">Consultar Todos</router-link> |
    <router-link to="/consultar-id">Consultar por ID</router-link> |
    <router-link to="/guardar">Guardar</router-link> |
    <router-link to="/actualizar">Actualizar</router-link> |
    <router-link to="/actualizar-parcial">Actualizar Parcial</router-link> |
    <router-link to="/eliminar">Eliminar</router-link> |
    <router-link to="/login">Login</router-link> |
    <button @click="logout"
      style="cursor: pointer; background: none; border: none; color: #2c3e50; font-weight: bold; font-family: Avenir, Helvetica, Arial, sans-serif;">Logout</button>
  </nav>
  <p v-if="logoutMessage" class="logout-toast">{{ logoutMessage }}</p>
  <router-view />
</template>

<script>
export default {
  data() {
    return {
      logoutMessage: null
    }
  },
  methods: {
    logout() {
      localStorage.removeItem("token");
      localStorage.removeItem("role");
      this.logoutMessage = "Has cerrado sesión correctamente.";

      setTimeout(() => {
        this.logoutMessage = null;
        this.$router.push("/login");
      }, 2000);
    }
  }
}
</script>

<style>
.logout-toast {
  background-color: #34495e;
  color: white;
  padding: 10px 20px;
  position: fixed;
  top: 20px;
  right: 20px;
  border-radius: 4px;
  z-index: 1000;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav a.router-link-exact-active {
  color: #42b983;
}
</style>
