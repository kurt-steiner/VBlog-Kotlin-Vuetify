package com.steiner.vblog.service

import com.steiner.vblog.model.Category
import com.steiner.vblog.request.PostCategoryRequest
import com.steiner.vblog.request.PutCategoryRequest
import com.steiner.vblog.table.Articles
import com.steiner.vblog.table.Categories
import com.steiner.vblog.util.dbQuery
import io.ktor.server.plugins.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class CategoryService(val database: Database) {
    companion object {
        const val CATEGORY_DEFAULT_NAME = "default"
    }

    init {
        transaction(database) {
            SchemaUtils.create(Categories)
        }
    }

    suspend fun insertOne(request: PostCategoryRequest): Category = dbQuery(database) {
        val category = with (Categories) {
            selectAll().where((name eq request.name) and (userId eq request.userId))
                .firstOrNull()?.let {
                    Category(
                        id = it[id].value,
                        name = it[name],
                        createTime = it[createTime],
                        userId = it[userId].value
                    )
                }
        }

        return@dbQuery if (category != null) {
            category
        } else {
            val id = with (Categories) {
                insert {
                    it[name] = request.name
                    it[userId] = request.userId
                } get id
            }

            findOne(id.value)!!
        }
    }

    suspend fun deleteOne(id: Int) = dbQuery(database) {
        with (Articles) {
            update({ categoryId eq id}) {
                it[categoryId] = null
            }
        }

        with (Categories) {
            deleteWhere {
                this.id eq id
            }
        }
    }

    suspend fun updateOne(request: PutCategoryRequest): Category = dbQuery(database) {
        val exist = findOne(request.id) != null

        if (!exist) {
            throw BadRequestException("no such category with id ${request.id}")
        }

        with (Categories) {
            update({id eq request.id}) {
                it[name] = request.name
            }
        }

        findOne(request.id)!!
    }

    suspend fun findOne(id: Int): Category? = dbQuery(database) {
        with (Categories) {
            selectAll().where(this.id eq id)
                .firstOrNull()?.let {
                    Category(
                        id = it[this.id].value,
                        name = it[name],
                        createTime = it[createTime],
                        userId = it[userId].value
                    )
                }
        }
    }

    suspend fun findAll(): List<Category> = dbQuery(database) {
        with (Categories) {
            selectAll().map {
                findOne(it[id].value)!!
            }
        }
    }
}