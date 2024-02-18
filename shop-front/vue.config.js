// const { defineConfig } = require('@vue/cli-service')
// module.exports = defineConfig({
//   transpileDependencies: true
// })

module.exports = {
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:80', // 백엔드 서버 주소
        changeOrigin: true, // cross origin 요청을 위해 origin을 변경
        pathRewrite: {'^/api' : ''} // '/api'를 제거하고 프록시
      }
    }
  },

  pluginOptions: {
    vuetify: {
			// https://github.com/vuetifyjs/vuetify-loader/tree/next/packages/vuetify-loader
		}
  }
};
