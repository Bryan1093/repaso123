import axios from 'axios';

const authClient = axios.create({
    baseURL: 'http://localhost:8082/auth',
    headers: {
        'Content-Type': 'application/json'
    }
});

const resourceClient = axios.create({
    baseURL: 'http://localhost:8081/agenda/api/v1.0',
    headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    }
});

resourceClient.interceptors.request.use(config => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

resourceClient.interceptors.response.use(
    response => response,
    error => {
        if (error.response && (error.response.status === 401 || error.response.status === 403)) {
            localStorage.removeItem('token');
            localStorage.removeItem('role');
            localStorage.removeItem('user');

            if (window.location.pathname !== '/login') {
                alert("Su sesión ha expirado o no tiene permisos. Por favor inicie sesión nuevamente.");
                window.location.href = '/login';
            }
        }
        return Promise.reject(error);
    }
);

export default {
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

    createDoctor(doctor) {
        return resourceClient.post('/doctores', doctor);
    },

    createPaciente(paciente) {
        return resourceClient.post('/pacientes', paciente);
    },

    createCita(citaRequest) {
        return resourceClient.post('/citas', citaRequest);
    },
    getAllCitas() {
        return resourceClient.get('/citas');
    },

    getAllPacientes() {
        return resourceClient.get('/pacientes');
    },

    getPacienteByUrl(url) {
        // Al ser una URL completa (HATEOAS), usamos axios directamente con el token
        return axios.get(url, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`,
                'Accept': 'application/json'
            }
        });
    }
};
