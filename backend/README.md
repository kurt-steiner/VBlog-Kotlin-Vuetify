# VBlog

## 后端项目介绍

虽然使用的不是 SpringBoot，但是他的分层思想还是体现在代码中的，具体可以参考项目结构

## API 暴露接口

base-url: http://localhost
port: 8080

需要经过 jwt 验证的url在请求时加上 "Authentication": "Bearer {{ jwttoken }}" 请求头

### 1. 用户认证和注册[无需经过 jwt 验证]

| url       | method | params | body            | return                                         | 说明                         |
|-----------|--------|--------|-----------------|------------------------------------------------|----------------------------|
| /login    | POST   |        | [LoginRequest](./src/main/kotlin/com/steiner/vblog/request/LoginRequest.kt)    | {message: string, data: string (token string)} | 请求登录，并返回一个 jwt token，两天后过期 |
| /register | POST   |        | [RegisterRequest](./src/main/kotlin/com/steiner/vblog/request/RegisterRequest.kt) | {message: string, data: Unit}                  | 注册用户，不返回数据 data            |

### 2. 用户操作[需要经过 jwt 验证]

| url        | method  | body           | return                        | 说明                     |
|------------|---------|----------------|----------------|-------------------------------|
| /user      | PUT     | [PutUserRequest](./src/main/kotlin/com/steiner/vblog/request/PutUserRequest.kt) | {message: string, data: Unit} | 更改用户数据，不返回数据 data      |
| /user/{id: int} | DELETE |                | {message: string, data: Unit} | 删除指定 id 的用户，不返回数据 data |

### 3. Tag 操作[需要经过 jwt 验证]

| url       | method  | params | body           | return                       | 说明              |
|-----------|---------|--------|----------------|------------------------------|-----------------|
| /tag      | POST    |        | [PostTagRequest](./src/main/kotlin/com/steiner/vblog/request/PostTagRequest.kt)] | {message: string, data: Tag} | post 一个 tag     |
| /tag      | PUT     |        | [PutTagRequest](./src/main/kotlin/com/steiner/vblog/request/PutTagRequest.kt)  | {message: string, data: Tag} | put 一个 tag      |
| /tag/{id: int} | DELETE  |        |                | {message: string, data: Unit} | 删除一个指定 id 的 tag |
| /tag   | GET     |        |                | {message: string, data: Tag[]} | 根据 jwt token 获取用户所有 tag       |

### 4. Category 操作[需要经过 jwt 验证]

| url       | method | params | body | return | 说明 |
|-----------|--------|--------|------|--------|----|
| /category | POST    |        | [PostCategoryRequest](./src/main/kotlin/com/steiner/vblog/request/PostCategoryRequest.kt)     |   {message: string, data: Category}     | 为用户创建一个分类   |
| /category/{id: int}          | DELETE       |        |      | {message: string, data: Unit}       | 删除匹配 id 的分类   |
| /category          |  PUT      |        |  [PutCategoryRequest](./src/main/kotlin/com/steiner/vblog/request/PutCategoryRequest.kt)    |  {message: string, data: Category}      | 更新一个 Category   |
| /category          |  GET      |        |      | {message: string, data: Category[]}       | 根据 jwt token 获取用户所有分类   |

### 5. Article 操作[需要经过 jwt 验证]

| url       | method | params | body | return | 说明 |
|-----------|--------|--------|------|--------|----|
| /article | POST| | [PostArticleRequest](./src/main/kotlin/com/steiner/vblog/request/PostArticleRequest.kt) | {message: string, data: Article} | 创建一个文章 |
| /article/{id: int} | DELETE | | | {message: string, data: Unit} | 删除匹配 id 的文章 |
| /article | PUT | | [PutArticleRequest](./src/main/kotlin/com/steiner/vblog/request/PutArticleRequest.kt) | {message: string, data: Article} | 更新一个文章 |
| /article | GET |{page?: int = 10 size?: int = 20, title ?: string, status ?: 0..=3, tag-id?: int, category-id?: int}  | | {message: string, data: Article[]} | 根据 jwt token 获取当前用户符合查询条件的文章 |
| /article/{id: int} | GET | | | {message: string, data: Article} | 获取匹配 id 的文章 |

### 6. ImageItem 操作

| url       | method | params | body | return | 说明 |
|-----------|--------|--------|------|--------|----|
| /image/download/{id: int} | GET | | | File | 下载匹配 id 的图片 |
| /image/upload | POST | | FormData("file"=ImageFile) | {message: string, data: ImageItem} | 上传图片 |

## PROBLEM

routing  中有关 pipeline 的 [intercept](https://ktor.kotlincn.net/advanced/pipeline.html)

## Model 数据模型定义

### 1. User 用户类型 (passwordHash 已屏蔽)

```kotlin
@Serializable
class User(
    val id: Int,
    val name: String,
    val nickname: String?,
    val email: String?,
    val registerTime: LocalDateTime,
    val avatar: ImageItem?
)
```

### 2. Article 文章相关

```kotlin
@Serializable
class Article(
    val id: Int,
    val title: String,
    val markdownContent: String,
    val htmlContent: String,
    val summary: String,
    val category: Category?,
    val author: User,
    val publishDate: LocalDateTime,
    val editTime: LocalDateTime,
    val status: ArticleStatus,
    val tags: List<Tag>
)

@Serializable(with = ArticleStatusSerializer::class)
enum class ArticleStatus(val code: Int) {
    Draft(0),
    Published(1),
    Dustbin(2)
}
```

### 3. Tag 标签类型

```kotlin
@Serializable
class Tag(
    val id: Int,
    val name: String,
    val userId: Int
)
```

### 4. Category 分类类型

```kotlin
@Serializable
class Category(
    val id: Int,
    val name: String,
    val createTime: LocalDateTime,
    val userId: Int
)
```

## Validation 请求体验证

### 1. 登录请求体验证

1. username 不能为空
2. passwordHash 不能为空

### 2. 注册请求体验证

1. name 不能为空字符串，最大长度64
2. passwordHash 不能为空字符串
3. 如果 nickname 不为空，不能是空字符串，并且最大长度是64
4. 如果 email 不为空，不能是空字符串，并且最大长度是64

### 3. 添加文章请求体验证

1. title 不能为空字符串，最大长度 256
2. markdownContent 不能为空字符串
3. htmlContent 不能为空字符串

### 4. 添加标签请求体验证

1. name 不能为空字符串，最大长度64

### 5. 添加分类请求体验证

1. name 不能为空字符串，最大长度64

### 6. 更改文章请求体验证

如果相关字段为空(null)，表示不更新该字段，否则更新该字段

1. 如果 title 不为空，不能是空字符串，并且最大长度是256
2. 如果 markdownContent 不为空，不能是空字符串

### 7. 更改标签请求体验证

1. name 不能为空字符串，最大长度64

### 8. 更改分类请求体验证

1. name 不能为空字符串，最大长度64

### 9. 更改用户请求体验证

如果相关字段为空(null)，表示不更新该字段，否则更新该字段

1. 如果 name 不为空，不能是空字符串，并且最大长度是64
2. 如果 nickname 不为空，不能是空字符串，并且最大长度是64
3. 如果 email 不为空，不能是空字符串，并且最大长度是64，符合 email 格式
4. 如果 passwordHash 不为空，不能是空字符串，最大长度 64
