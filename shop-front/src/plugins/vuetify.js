// vuetify.js

// Styles
import '@mdi/font/css/materialdesignicons.css'
import 'vuetify/styles'

// Vuetify
import { createVuetify } from 'vuetify'

export default createVuetify({
  theme: {
    defaultTheme: 'myTheme', // 기본 테마 설정
    themes: {
      myTheme: {
        colors: {
          primary: '#f5f1f0', // 기본 색상을 빨간색으로 변경
          // 여기에 필요한 다른 색상들을 설정할 수 있습니다.
        }
      }
    }
  }
})
