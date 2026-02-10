
import axios from 'axios';

// Auth Client (Port 8082)
const authClient = axios.create({
    baseURL: 'http://localhost:8082/auth',
    headers: {
        'Content-Type': 'application/json'
    }
});

// Resource Client (Port 8081)
const resourceClient = axios.create({
    baseURL: 'http://localhost:8081/matricula/api/v1.0',
    headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    }
});

// Interceptor to add token to requests
resourceClient.interceptors.request.use(config => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

// Interceptor to handle 401/403 responses
resourceClient.interceptors.response.use(
    response => response,
    error => {
        if (error.response && (error.response.status === 401 || error.response.status === 403)) {
            // Clear auth data
            localStorage.removeItem('token');
            localStorage.removeItem('role');
            localStorage.removeItem('user');

            // Redirect to login (Comentado para permitir manejo local en esta app de prueba)
            /*
            if (window.location.pathname !== '/login') {
                alert("Su sesión ha expirado o no tiene permisos. Por favor inicie sesión nuevamente.");
                window.location.href = '/login';
            }
            */
            console.warn("Token expirado o inválido (401/403 detectado)");
        }
        return Promise.reject(error);
    }
);

export default {
    // Auth Methods
    async login(username, password) {
        const response = await authClient.get('/token', {
            params: { user: username, password: password }
        });
        if (response.data && response.data.accessToken) {
            localStorage.setItem('token', response.data.accessToken);
            localStorage.setItem('role', response.data.role);
            localStorage.setItem('user', username);
        }
        return response.data;
    },

    logout() {
        localStorage.removeItem('token');
        localStorage.removeItem('role');
        localStorage.removeItem('user');
    },

    // Estudiante Methods
    createEstudiante(estudiante) {
        return resourceClient.post('/estudiantes', estudiante);
    },

    getAllEstudiantes() {
        return resourceClient.get('/estudiantes');
    }
};
