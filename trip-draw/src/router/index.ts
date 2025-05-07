import { createRouter, createWebHistory } from 'vue-router';
import MainPage from '../pages/MainPage.vue';
import FreeBoard from '../pages/board/free/FreeBoard.vue'; // ← 새로 추가
import DrawPage from '../pages/drawing/DrawPage.vue';
import DrawResult from '../pages/drawing/DrawResult.vue';
import TripPlanPage from '../pages//board/tripplan/TripPlanPage.vue';
import TripPlanDetail from '../pages/board/tripplan/TripPlanDetail.vue';
import TripPlanBoard from '../pages/board/tripplan/TripPlanBoard.vue';
import FreeDetail from '../pages/board/free/FreeDetail.vue';
import Login from '../pages/user/Login.vue';
import SignUp from '../pages/user/SignUp.vue';
import Loading from '../pages/drawing/Loading.vue';
import MyPage from '../pages/user/MyPage.vue';

const routes = [
  { path: '/', name: 'Main', component: MainPage },
  { path: '/freeboard', name: 'FreeBoard', component: FreeBoard }, // ← 추가된 라우트
  { path: '/drawpage', name: 'DrawPage', component: DrawPage },
  { path: '/drawresult', name: 'DrawResult', component: DrawResult },
  { path: '/tripplanpage', name: 'TripPlanPage', component: TripPlanPage },
  { path: '/tripplandetail', name: 'TripPlanDetail', component: TripPlanDetail },
  { path: '/tripplanboard', name: 'TripPlanBoard', component: TripPlanBoard },
  { path: '/freedetail', name: 'FreeDetail', component: FreeDetail },
  { path: '/login', name: 'Login', component: Login },
  { path: '/signup', name: 'SignUp', component: SignUp },
  { path: '/loading', name: 'Loading', component: Loading },
  { path: '/mypage', name: 'MyPage', component: MyPage },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
