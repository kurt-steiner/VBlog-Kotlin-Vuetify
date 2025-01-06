# VBlog

## API

base-url: http://localhost

port: 8080

需要经过 jwt 验证的url在请求时加上 "Authentication": "Bearer {{ jwttoken }}" 请求头

[请求体在这个目录下](file://./src/main/kotlin/request)
### 1. 用户认证和注册[无需经过 jwt 验证]

| url       | method | params | body            | return                                         | 说明                         |
|-----------|--------|--------|-----------------|------------------------------------------------|----------------------------|
| /login    | POST   |        | LoginRequest    | {message: string, data: string (token string)} | 请求登录，并返回一个 jwt token，两天后过期 |
| /register | POST   |        | RegisterRequest | {message: string, data: Unit}                  | 注册用户，不返回数据 data            |

### 2. 用户操作[需要经过 jwt 验证]

| url        | method  | body           | return                        | 说明                     |
|------------|---------|----------------|----------------|-------------------------------|
| /user      | PUT     | PutUserRequest | {message: string, data: Unit} | 更改用户数据，不返回数据 data      |
| /user/{id} | DELETE |                | {message: string, data: Unit} | 删除指定 id 的用户，不返回数据 data |

### 3. Tag 操作[需要经过 jwt 验证]

| url       | method  | params | body           | return                       | 说明              |
|-----------|---------|--------|----------------|------------------------------|-----------------|
| /tag      | POST    |        | PostTagRequest | {message: string, data: Tag} | post 一个 tag     |
| /tag      | PUT     |        | PutTagRequest  | {message: string, data: Tag} | put 一个 tag      |
| /tag/{id} | DELETE  |        |                | {message: string, data: Unit | 删除一个指定 id 的 tag |

### 4. Category 操作[需要经过 jwt 验证]

| url       | method | params | body | return | 说明 |
|-----------|--------|--------|------|--------|----|
| /category | GET    |        |      |        |    |
|           |        |        |      |        |    |
|           |        |        |      |        |    |
|           |        |        |      |        |    |

### 5. Article 操作[需要经过 jwt 验证]

### 6. ImageItem 操作
## Notes

routing  中有关 pipeline 的 intercept 
https://ktor.kotlincn.net/advanced/pipeline.html