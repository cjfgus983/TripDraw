import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import path from 'path';

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },
  server: {
    // 개발용 프록시 설정
    proxy: {
      // 정적 파일 (이미지 등)
      '/uploads': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        // 필요하다면 경로 재작성도 지정할 수 있습니다.
        // rewrite: (p) => p.replace(/^\/uploads/, '/uploads')
      },
      // API 호출 전부도 한 번에 백엔드로 포워딩
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        // rewrite: (p) => p.replace(/^\/api/, '/api')
      },
    }
  }
});
