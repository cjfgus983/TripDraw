import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import './assets/main.css';

// CSS
import 'swiper/css'
import 'swiper/css/pagination'
import 'swiper/css/autoplay'

import axios from 'axios'

const app = createApp(App);

const token = window.localStorage.getItem('accessToken')
if (token) {
  axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
}

app.use(router);
app.mount('#app');
