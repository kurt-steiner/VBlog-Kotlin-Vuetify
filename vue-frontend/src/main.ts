import { createApp } from 'vue'
import App from './App.vue'
import router from "./router"

import "vuetify/dist/vuetify.css"
import "@mdi/font/css/materialdesignicons.css"

import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'

import {aliases, mdi} from "vuetify/iconsets/mdi"

import { createPinia } from 'pinia'
const pinia = createPinia()

const vuetify = createVuetify({
    components,
    directives,

    icons: {
        defaultSet: "mdi",
        aliases,
        sets: {
            mdi
        }
    }
})

createApp(App)
    .use(pinia)
    .use(router)
    .use(vuetify)
    .mount('#app')
