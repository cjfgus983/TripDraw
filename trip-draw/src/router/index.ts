import { createRouter, createWebHistory } from 'vue-router';

import { useUserStore } from '@/stores/user';
import OAuthCallback from '@/pages/OAuthCallback.vue'


// 카카오 로그인 후 콜백


import MainPage from '@/pages/MainPage.vue';
import FreeBoard from '@/pages/board/free/FreeBoard.vue'; // ← 새로 추가
import FreeDetail from '../pages/board/free/FreeDetail.vue';
import FreeRegist from '../pages/board/free/FreeRegist.vue';
import NoticeBoard from '@/pages/board/notice/NoticeBoard.vue';
import NoticeDetail from '@/pages/board/notice/NoticeDetail.vue';
import NoticeRegist from '@/pages/board/notice/NoticeRegist.vue';
import ContactBoard from '@/pages/board/contact/ContactBoard.vue';
import ContactDetail from '@/pages/board/contact/ContactDetail.vue';
import ContactRegist from '@/pages/board/contact/ContactRegist.vue';
import DrawPage from '../pages/drawing/DrawPage.vue';
import DrawResult from '../pages/drawing/DrawResult.vue';
import TripPlanPage from '../pages//board/tripplan/TripPlanPage.vue';
import TripPlanDetail from '../pages/board/tripplan/TripPlanDetail.vue';
import TripPlanBoard from '../pages/board/tripplan/TripPlanBoard.vue';
import Login from '../pages/user/Login.vue';
import SignUp from '../pages/user/SignUp.vue';
import Loading from '../pages/drawing/Loading.vue';
import MyPage from '../pages/user/MyPage.vue';
import FindIdPassword from '../pages/user/FindIdPassword.vue';
import EditProfile from '../pages/user/EditProfile.vue';
import DrawingRegist from '@/pages/board/drawing/DrawingRegist.vue';
import DrawingBoard from '@/pages/board/drawing/DrawingBoard.vue';

const routes = [
  { path: '/', name: 'Main', component: MainPage },
  { path: '/freeboard', name: 'FreeBoard', component: FreeBoard }, // ← 추가된 라우트
  { path: '/freedetail/:freeId', name: 'FreeDetail', component: FreeDetail, props: true  },
  { path: '/freeregist', name: 'FreeRegist', component: FreeRegist },
  { path: '/noticeboard', name: 'NoticeBoard', component: NoticeBoard },
  { path: '/noticedetail/:noticeId', name: 'NoticeDetail', component: NoticeDetail,  props: true  },
  { path: '/noticeregist', name: 'NoticeRegist', component: NoticeRegist },
  { path: '/contactboard', name: 'ContactBoard', component: ContactBoard },
  { path: '/contactdetail/:contactId', name: 'ContactDetail', component: ContactDetail, props: true  },
  { path: '/contactregist', name: 'ContactRegist', component: ContactRegist },
  { path: '/drawpage', name: 'DrawPage', component: DrawPage },
  { path: '/drawresult', name: 'DrawResult', component: DrawResult },
  { path: '/tripplanpage/:planCode?', name: 'TripPlanPage', component: TripPlanPage, props: true }, // planCode가 있을 수도 있고 없을 수도 있음
  { path: '/tripplandetail/:id', name: 'TripPlanDetail', component: TripPlanDetail, props: true  },
  { path: '/tripplanboard', name: 'TripPlanBoard', component: TripPlanBoard },
  { path: '/login', name: 'Login', component: Login },
  { path: '/signup', name: 'SignUp', component: SignUp },
  { path: '/loading', name: 'Loading', component: Loading },
  { path: '/mypage', name: 'MyPage', component: MyPage },
  { path: '/findidpassword', name: 'FindIdPassword', component: FindIdPassword },
  { path: '/editprofile', name: 'EditProfile', component: EditProfile, meta: { requiresPasswordConfirm: true }} ,
  { path: '/drawingregist', name: 'DrawingRegist', component: DrawingRegist },
  { path: '/drawingboard', name: 'DrawingBoard', component: DrawingBoard },
  { // 카카오 콜백
    path: '/oauth-callback',
    name: 'OAuthCallback',
    component: OAuthCallback
  },
  
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});


// 메타 인증 없이 넘어가고자 한다면 mypage로 되돌리기
router.beforeEach((to, from, next) => {
  const userStore = useUserStore();
  if (to.meta.requiresPasswordConfirm && !userStore.passwordConfirmed) {
    return next('/mypage');
  }
  next();
});

export default router;
