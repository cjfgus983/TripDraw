import { createRouter, createWebHistory } from 'vue-router'
import SignupForm from '../components/SignupForm.vue'
import HomeView from '../views/HomeView.vue'
import DreamDrawView from '../views/DreamDrawView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/signup',
      name: 'signup',
      component: SignupForm, // ✅ 회원가입 페이지 추가
    },
    {
      path: '/dream-draw',
      name: 'dreamDraw',
      component: DreamDrawView
    },
  ],
})

export default router
