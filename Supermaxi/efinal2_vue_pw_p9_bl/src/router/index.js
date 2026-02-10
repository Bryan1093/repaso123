import { createRouter, createWebHistory } from 'vue-router'
import IngresarProducto from '../views/IngresarProducto.vue'
import RealizarVenta from '../views/RealizarVenta.vue'
import Login from '../views/Login.vue'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/ingresar-producto',
    name: 'IngresarProducto',
    component: IngresarProducto,
    meta: { requiresAuth: true, roles: ['admin'] }
  },
  {
    path: '/realizar-venta',
    name: 'RealizarVenta',
    component: RealizarVenta,
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

  if (to.meta.requiresAuth) {
    if (!token) {
      next('/login');
    } else if (to.meta.roles && !to.meta.roles.includes(role)) {
      if (role === 'admin') next('/ingresar-producto');
      else if (role === 'user') next('/realizar-venta');
      else next('/login');
    } else {
      next();
    }
  } else {
    next();
  }
});

export default router
