import { createRouter, createWebHistory } from 'vue-router'
import VehiculoList from '../views/VehiculoList.vue'
import Login from '../views/Login.vue'

const routes = [
  {
    path: '/',
    name: 'VehiculoList',
    component: VehiculoList,
    meta: { requiresAuth: true }
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token');
  if (to.meta.requiresAuth && !token) {
    next('/login');
  } else {
    next();
  }
});

export default router
