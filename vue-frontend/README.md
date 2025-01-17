# Vue 3 + TypeScript + Vite

## 一点吐槽

其实在上大学之前就学了 Vue，那个时候还是 Vue2 + Option API，  
然后上安卓开发课的时候用的是 Flutter 而不是原生安卓(学了好几年才发现他说的跨平台跨的是界面啊)，  
考虑到 Flutter 和 原生安卓的界面样式都是 **Material Design 3**(希望谷歌不要弃坑，不然我就白学了)，自己做样式怕审美不行，于是就选择了同样使用 **Material Design 3** 的 Vuetify 框架

## 全局错误处理

由于前端主要是用来交互的，数据的有效性都交给后端来验证，这样也是为了避免有人直接操作后端 API 接口  
就算提交了空数据，前端也有验证器用来验证数据的有效性  
如果在与后端交互的过程中发生了错误，会有一个 snackbar 显示错误信息  
项目中已经为 axios 实例在 response 阶段设置了拦截器

```typescript
const errorHandler = (error: any): any => {
    const snackBarStore = useSnackbarStore()
    snackBarStore.showSnackbar({
        status: "error",
        message: (error.response.data.message as string) || "Unknown error"
    })

    if (error.response.status === 401) {
        const store = useVBlogStore()
        store.logout()
        router.replace({ name: "Login" })
    }

    return Promise.reject(error)
}
```

其中 `snackbar store` 用来存储 错误信息 和 控制 snackbar 的显示  
在 `App.vue` 中，

```html
<VSnackbar v-model="snackbar.actived" 
                   color="error"
                   width="50%"
                   :height="60"
                   position="fixed"
                   location="top"
                   :text="snackbar.message"/>
```

## 处理刷新页面时的数据丢失问题

刷新的时候，pinia中的 store 会重新构造一遍，这个时候原来存储的数据会丢失，为了防止刷新产生数据缺失，在一些页面 views 中的 `onMounted` 回调中有如下代码

```typescript
onMounted(async () => {
    await store.reloadMissingData()
})
```

如果 `store` 中的数据为空列表(因为构造的时候就是空列表)， 他会把 `store` 中的数据重新获取一遍

## 存储 jwt token

如上所述，页面刷新一下数据就没了，这里需要刷新后还能避开登录，可以在 `localStorage` 中存储 jwt token
