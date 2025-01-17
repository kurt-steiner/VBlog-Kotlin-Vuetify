import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from "vite-plugin-vue-devtools"
import { fileURLToPath, URL} from "node:url"

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(), 
    vueDevTools({
      launchEditor: "code"
    })
  ],

  resolve: {
   /*  alias: { 
      find: '@', 
      replacement: fileURLToPath(new URL('./src', import.meta.url)) 
    } */

      alias: {
        "@": fileURLToPath(new URL('./src', import.meta.url))
      }
  },
  
  server: {
    watch: {
      usePolling: true,
      interval: 1000
    }
  }
})
