# VBlog

## 项目介绍

其实以前做过这个项目，[仓库在这里](https://github.com/nesteiner/VBlog)
不过近一年学了新的技术栈，开始使用 **Material Design 3** 风格的前端页面和组件，并接触到 **PostgreSQL** ，就打算重新写一遍，也是给春招的简历上添加一个项目

## 项目运行

### 数据库初始化

有关 **PostgreSQL** 的初始化，可以 [参考这篇文章](https://wiki.archlinuxcn.org/wiki/PostgreSQL)

然后，请自行创建一个 `vblog` 数据库，其数据表后端程序会自己创建

```sh
createdb vblog
```

### 后端

在运行后端前需要进行一些配置，在 `backend/src/main/resources/application.yaml` 中有如下配置需要自己修改

```yaml
postgres:
  url: "jdbc:postgresql://localhost/vblog"
  user: steiner
  password: ********
  driver: org.postgresql.Driver

jwt:
  domain: "https://jwt-provider-domain/"
  audience: "jwt-audience"
  realm: "ktor sample app"
  secret: "secret"

custom:
  summary:
    max-length: 200

  windows-storage: "C:\\Users\\stein\\workspace\\VBlog\\storage"
  unix-storage: "/mnt/c/Users/stein/workspace/VBlog/storage"
```

1. postgresql 的 服务器地址
2. 所连接数据库的用户名和密码
3. jwt 相关设置
4. 后端图片存储路径 (如果路径不存在，请自己创建一个，一定要在本机文件系统下)

在 backend目录下，使用

```sh
# windows
./gradlew.bat buildFatJar

# unix-like
./gradlew buildFatJar


java -jar build/libs/backend-all.jar
```

如果你要编辑这个项目，由于这个项目使用 gradle 构建，而我把 gradle 的 zip 文件放到了本地，你需要在 `gradle/wrapper/gradle-wrapper.properties` 中修改 gradle 的下载地址为
`distributionUrl=https\://services.gradle.org/distributions/gradle-8.9-bin.zip`

### 前端

在前端项目中，后端的地址不是定死的，你可以在 `vue-frontend/.env` 中配置 `VITE_BACKEND_URL` 来指定后端 url

```env
VITE_BACKEND_URL="http://localhost:8080"
```

构建这个项目，只需要在 `vue-frontend` 目录下执行 `yarn build` 即可
(但是我好像不会部署这个前端页面)

## 项目演示

图片太大了，没办法 😂

### 1. 主页

[主页](./docs/主页.png)
[主页 打开 drawer](./docs/主页-打开%20drawer.png)
[文章搜索](./docs/文章搜索.png)

### 2. 登录页/注册页

[登录页](./docs/登录页.png)
[注册页](./docs/注册页.png)

### 3. 添加/编辑 文章

[添加文章](./docs/添加文章.png)
[编辑文章](./docs/编辑文章.png)

### 4. 文章预览

[文章浏览](./docs/文章浏览.png)

### 5. 设置 (用户/Category/Tag)

[用户设置](./docs/用户设置页.png)
[分类设置](./docs/分类设置.png)
[标签设置](./docs/标签设置.png)

## 技术栈

### 1. 这次的后端技术栈使用的是

1. Kotlin 编程语言
2. Ktor Web服务框架
3. Exposed ORM框架
4. PostgreSQL 数据库
5. Koin 依赖注入框架
6. kotlinx.serialization 序列化框架
7. jsoup 进行html过滤
8. JWT 进行token认证

[查看更多](./backend/README.md)

### 2. 这次的 Web 前端技术框架是

1. Vue3 + Typescript
2. Vuetify 组件
3. pinia 状态管理
4. vue-router 路由
5. vite 打包工具
6. md-editor-v3 在线markdown编辑器

[查看更多](./vue-frontend/README.md)

## 项目缺陷

首先是 CORS 设置，由于对网页开发的深入程度不够，这个 CORS 设置都是看前端出现错误，如果没错就直接用 这个程度，我也不知道怎么写是安全的
这是后端的 CORS 设置

```kotlin
allowMethod(HttpMethod.Options)
allowMethod(HttpMethod.Put)
allowMethod(HttpMethod.Delete)
allowMethod(HttpMethod.Get)
allowMethod(HttpMethod.Post)
allowMethod(HttpMethod.Patch)
allowHeaders { s ->
    true
}

allowHeader(HttpHeaders.ContentType)
anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
```

## 下一步

### 再做一个后端

1. 使用 SpringBoot3 框架
2. 使用 Mybatis ORM 框架
3. 使用 Spring Security + JWT 进行认证

ps: SpringBoot 中如何配置序列化 ？
吐槽: SpringBoot 大家用的比较多，就是历史包袱有点重，而且要 IDEA Ultimate 才能用更好的支持，虽然 vscode 上也有相关插件，但是对 kotlin 开发 Spring 应用的支持好像不是很好
