package com.steiner.vblog.table

import com.steiner.vblog.ArticleConfigure
import com.steiner.vblog.model.Article
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object Articles: IntIdTable("articles") {
    val title = varchar("title", length = ArticleConfigure.TITLE_LENGTH)
    val markdownContent = text("markdown-content")
    val htmlContent = text("html-content")
    val summary = text("summary")
    val categoryId = reference("category-id", Categories).nullable()
    val authorId = reference("author-id", Users)
    val publishDate = datetime("publish-date")
    val editTime = datetime("edit-time")
    val status = enumeration<Article.Companion.Status>("status")
}