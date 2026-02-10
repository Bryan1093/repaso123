<template>
    <div class="login-container">
        <div class="login-card">
            <h2>Iniciar Sesión - Inscripción UCE</h2>
            <form @submit.prevent="handleLogin">
                <div class="form-group">
                    <label for="username">Usuario</label>
                    <input type="text" id="username" v-model="username" required placeholder="Ingrese su usuario" />
                </div>
                <div class="form-group">
                    <label for="password">Contraseña</label>
                    <input type="password" id="password" v-model="password" required
                        placeholder="Ingrese su contraseña" />
                </div>
                <button type="submit" :disabled="loading" class="login-btn">
                    {{ loading ? 'Ingresando...' : 'Ingresar' }}
                </button>
                <p v-if="error" class="error-msg">{{ error }}</p>
            </form>
        </div>
    </div>
</template>

<script>
import ApiClient from '@/clients/ApiClient';

export default {
    data() {
        return {
            username: '',
            password: '',
            loading: false,
            error: null
        };
    },
    methods: {
        async handleLogin() {
            this.loading = true;
            this.error = null;
            try {
                await ApiClient.login(this.username, this.password);
                this.$router.push('/guardar-estudiante'); // Redirect to main view
            } catch (err) {
                console.error(err);
                this.error = 'Credenciales inválidas o error en el servidor.';
            } finally {
                this.loading = false;
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
    background-color: #f4f7f6;
}

.login-card {
    background: white;
    padding: 2rem;
    border-radius: 8px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    width: 100%;
    max-width: 400px;
}

h2 {
    text-align: center;
    margin-bottom: 1.5rem;
    color: #333;
}

.form-group {
    margin-bottom: 1rem;
}

label {
    display: block;
    margin-bottom: 0.5rem;
    color: #666;
}

input {
    width: 100%;
    padding: 0.75rem;
    border: 1px solid #ddd;
    border-radius: 4px;
    font-size: 1rem;
}

input:focus {
    border-color: #42b983;
    outline: none;
}

.login-btn {
    width: 100%;
    padding: 0.75rem;
    background-color: #42b983;
    color: white;
    border: none;
    border-radius: 4px;
    font-size: 1rem;
    cursor: pointer;
    transition: background-color 0.3s;
}

.login-btn:hover {
    background-color: #3aa876;
}

.login-btn:disabled {
    background-color: #a8d5c2;
    cursor: not-allowed;
}

.error-msg {
    color: #e74c3c;
    text-align: center;
    margin-top: 1rem;
}
</style>
