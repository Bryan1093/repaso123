<template>
    <div class="login-container">
        <div class="login-card">
            <h2>Iniciar Sesión</h2>
            <form @submit.prevent="handleLogin">
                <div class="form-group">
                    <label>Usuario</label>
                    <input v-model="username" type="text" required placeholder="Ingrese su usuario" />
                </div>
                <div class="form-group">
                    <label>Contraseña</label>
                    <input v-model="password" type="password" required placeholder="Ingrese su contraseña" />
                </div>
                <button type="submit" class="btn-login">Ingresar</button>
            </form>
            <p v-if="error" class="error-msg">{{ error }}</p>
        </div>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            username: '',
            password: '',
            error: ''
        };
    },
    methods: {
        async handleLogin() {
            try {
                const response = await axios.post('http://localhost:8082/auth/login', {
                    username: this.username,
                    password: this.password
                });

                const { accessToken, role } = response.data;
                localStorage.setItem('token', accessToken);
                localStorage.setItem('role', role);

                if (role === 'admin') {
                    this.$router.push('/ingresar-producto');
                } else if (role === 'user') {
                    this.$router.push('/realizar-venta');
                } else {
                    this.$router.push('/');
                }

            } catch (err) {
                console.error(err);
                this.error = 'Credenciales incorrectas';
            }
        }
    }
};
</script>

<style scoped>
.login-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
    background: white;
    padding: 2.5rem;
    border-radius: 15px;
    box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
    width: 100%;
    max-width: 400px;
    text-align: center;
}

h2 {
    color: #333;
    margin-bottom: 1.5rem;
}

.form-group {
    margin-bottom: 1.2rem;
    text-align: left;
}

label {
    display: block;
    margin-bottom: 0.5rem;
    color: #666;
    font-weight: 500;
}

input {
    width: 100%;
    padding: 0.8rem;
    border: 1px solid #ddd;
    border-radius: 6px;
    font-size: 1rem;
}

.btn-login {
    width: 100%;
    padding: 0.8rem;
    background: #764ba2;
    color: white;
    border: none;
    border-radius: 6px;
    font-size: 1rem;
    font-weight: bold;
    cursor: pointer;
    transition: background 0.3s;
    margin-top: 1rem;
}

.btn-login:hover {
    background: #5a3a7e;
}

.error-msg {
    color: #e74c3c;
    margin-top: 1rem;
    font-weight: 500;
}
</style>
