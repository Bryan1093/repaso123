
import { createRouter, createWebHistory } from 'vue-router'
import GenerarTokenView from '../views/GenerarTokenView.vue'
import GuardarEstudianteView from '../views/GuardarEstudianteView.vue'
import LoginView from '../views/LoginView.vue'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'login',
    component: LoginView
  },
  {
    path: '/generar-token',
    name: 'generar-token',
    component: GenerarTokenView,
    meta: { requiresAuth: true, roles: ['admin'] }
  },
  {
    path: '/guardar-estudiante',
    name: 'guardar-estudiante',
    component: GuardarEstudianteView,
    meta: { requiresAuth: true, roles: ['admin', 'user'] }
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token');
  const role = localStorage.getItem('role');

  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!token) {
      next({ name: 'login' });
    } else {
      // Check for role permissions
      if (to.meta.roles && !to.meta.roles.includes(role)) {
        // Role not authorized, redirect to default or login
        alert("No tiene permisos para acceder a esta ruta");
        if (role === 'user') {
          next({ name: 'guardar-estudiante' });
        } else {
          next({ name: 'login' });
        }
      } else {
        next();
      }
    }
  } else {
    next();
  }
});

export default router
