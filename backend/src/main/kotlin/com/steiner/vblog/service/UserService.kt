package com.steiner.vblog.service

import com.steiner.vblog.exception.RegisterException
import com.steiner.vblog.model.User
import com.steiner.vblog.request.PutUserRequest
import com.steiner.vblog.request.RegisterRequest
import com.steiner.vblog.table.Users
import com.steiner.vblog.util.dbQuery
import io.ktor.server.plugins.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UserService(val database: Database): KoinComponent {
    init {
        transaction(database) {
            SchemaUtils.create(Users)
        }
    }

    val imageItemService: ImageItemService by inject<ImageItemService>()
    val articleService: ArticleService by inject<ArticleService>()

    suspend fun register(request: RegisterRequest): User = dbQuery(database) {
        val exist = with (Users) {
            selectAll().where(name eq request.name)
                .firstOrNull() != null
        }

        if (exist) {
            throw RegisterException("username duplicated")
        }

        val id = with (Users) {
            insert {
                it[name] = request.name

                if (request.email != null) {
                    it[email] = request.email
                }

                it[passwordHash] = request.passwordHash

                if (request.nickname != null) {
                    it[nickname] = request.nickname
                }

                if (request.avatarId != null) {
                    it[avatarId] = request.avatarId
                }
            } get id
        }


        findOne(id.value)!!
    }

    suspend fun deleteOne(id: Int) = dbQuery(database) {
        articleService.deleteAllOfAuthor(id)

        with (Users) {
            deleteWhere {
                this.id eq id
            }
        }
    }

    suspend fun updateOne(request: PutUserRequest): User = dbQuery(database) {
        val exist = findOne(request.id) != null

        if (!exist) {
            throw BadRequestException("user not exist with id: ${request.id}")
        }

        with (Users) {
            update({ id eq request.id}) {
                if (request.passwordHash != null) {
                    it[passwordHash] = request.passwordHash
                }

                if (request.nickname != null) {
                    it[nickname] = request.nickname
                }

                if (request.email != null) {
                    it[email] = request.email
                }

                if (request.avatarId != null) {
                    it[avatarId] = request.avatarId
                }

                if (request.name != null) {
                    it[name] = request.name
                }
            }
        }

        findOne(request.id)!!
    }

    suspend fun findOne(id: Int): User? = dbQuery(database) {
        with (Users) {
            selectAll().where(this.id eq id)
                .firstOrNull()?.let {
                    val avatarId = it[avatarId]?.value

                    User(
                        id = it[this.id].value,
                        name = it[name],
                        email = it[email],
                        avatar = if (avatarId != null) {
                            imageItemService.findOne(avatarId)
                        } else {
                            null
                        },

                        nickname = it[nickname],
                        registerTime = it[registerTime]
                    )
                }
        }
    }

    suspend fun findOne(name: String): User? = dbQuery(database) {
        with (Users) {
            selectAll().where(this.name eq name)
                .firstOrNull()?.let {
                    val avatarId = it[avatarId]?.value

                    User(
                        id = it[this.id].value,
                        name = it[this.name],
                        email = it[email],
                        avatar = if (avatarId != null) {
                            imageItemService.findOne(avatarId)
                        } else {
                            null
                        },

                        nickname = it[nickname],
                        registerTime = it[registerTime]
                    )
                }
        }
    }

    suspend fun findAll(): List<User> = dbQuery(database) {
        with (Users) {
            selectAll().map {
                val avatarId = it[avatarId]?.value
                val avatar = if (avatarId != null) {
                    imageItemService.findOne(avatarId)
                } else {
                    null
                }

                User(
                    id = it[id].value,
                    name = it[name],
                    nickname = it[nickname],
                    email = it[email],
                    avatar = avatar,
                    registerTime = it[registerTime]
                )
            }
        }
    }

    suspend fun clear() = dbQuery(database) {
        with (Users) {
            selectAll().map {
                it[id].value
            }.forEach {
                articleService.deleteAllOfAuthor(it)
            }

            deleteAll()
        }
    }
}