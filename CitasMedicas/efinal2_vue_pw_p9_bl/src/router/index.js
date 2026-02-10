import { createRouter, createWebHistory } from 'vue-router';
import DoctorView from '../views/DoctorView.vue';
import PacienteView from '../views/PacienteView.vue';
import CitasView from '../views/CitasView.vue';
import LoginView from '../views/LoginView.vue';

const routes = [
    {
        path: '/',
        redirect: '/login'
    },
    {
        path: '/login',
        name: 'Login',
        component: LoginView
    },
    {
        path: '/doctor',
        name: 'Doctor',
        component: DoctorView,
        meta: { requiresAuth: true }
    },
    {
        path: '/paciente',
        name: 'Paciente',
        component: PacienteView,
        meta: { requiresAuth: true }
    },
    {
        path: '/citas',
        name: 'Citas',
        component: CitasView,
        meta: { requiresAuth: true }
    }
];

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
});

router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token');
    if (to.matched.some(record => record.meta.requiresAuth)) {
        if (!token) {
            next({ name: 'Login' });
        } else {
            next();
        }
    } else {
        next();
    }
});

export default router;
