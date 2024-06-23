// const { defineConfig } = require('@vue/cli-service')
// module.exports = defineConfig({
//   transpileDependencies: true
// })

// Node.js 모듈 시스템에서 모듈을 내보내는 객체. 이 코드는 Vue CLI의 설정 파일
module.exports = {
  // 개발 서버 설정. 패키징 이후 적용되지 않음
  devServer: {
    port: 8080,
    // 프록시 설정을 정의
    proxy: {
      // 기본 경로 '/'로 시작하는 모든 요청을 백엔드 서버로 프록시합니다.
      '/': {
        target: 'http://localhost:80', // 백엔드 서버 주소
        // pathRewrite: {'^/api' : ''} // '/api'를 제거하고 프록시
        changeOrigin: true, // cross origin 요청을 위해 origin을 변경
        // `bypass`는 특정 조건에 따라 프록시를 우회하도록 함
        bypass: (req) => {
          // 요청 URL이 '/vue'로 시작하면 프록시하지 않고 그대로 반환
          if (req.url.startsWith('/vue')) {
            return req.url; // 이 경로는 프록시하지 않음
          }
        },

        /**
         * Vue Webpack의 Hot Module Replacement (HMR) 기능과 관련으로 보임
         *  오류
         *    'WebSocket connection to '~' failed: Invalid frame header'
         *  원인
         *    1. client(vue)랑 서버(Spring)랑 웹 소켓 버전이 안맞기 때문
         *    2. client는 웹 소켓이 설정 되어있는데, 서버는 설정이 안되어있어서 받을 수 없는 경우
         */
        ws: false,
        
      },
    }
  },

  // `pluginOptions`는 Vue CLI 플러그인의 추가 옵션을 설정
  pluginOptions: {
    // `vuetify`는 Vuetify 관련 설정을 정의
    vuetify: {
      // Vuetify 로더의 설정에 대한 링크를 제공
      // https://github.com/vuetifyjs/vuetify-loader/tree/next/packages/vuetify-loader
    }
  }
};

