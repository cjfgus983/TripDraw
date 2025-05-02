import { createRouter, createWebHistory } from 'vue-router';
import MainPage from '@/pages/MainPage.vue';
import FreeBoard from '@/pages/FreeBoard.vue'; // ← 새로 추가
import DrawPage from '../pages/drawPage.vue';
import DrawResult from '../pages/drawResult.vue';
import TripPlanPage from '../pages/tripPlanPage.vue';
import TripPlanDetail from '../pages/tripPlanDetail.vue';
import TripPlanBoard from '../pages/tripPlanBoard.vue';
import FreeDetail from '../pages/FreeDetail.vue';

const routes = [
  { path: '/', name: 'Main', component: MainPage },
  { path: '/freeboard', name: 'FreeBoard', component: FreeBoard }, // ← 추가된 라우트
  { path: '/drawpage', name: 'DrawPage', component: DrawPage },
  { path: '/drawresult', name: 'DrawResult', component: DrawResult },
  { path: '/tripplanpage', name: 'TripPlanPage', component: TripPlanPage },
  { path: '/tripplandetail', name: 'TripPlanDetail', component: TripPlanDetail },
  { path: '/tripplanboard', name: 'TripPlanBoard', component: TripPlanBoard },
  { path: '/freedetail', name: 'FreeDetail', component: FreeDetail },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
