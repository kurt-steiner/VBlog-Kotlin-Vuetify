package com.steiner.vblog.table

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object ArticleTag: Table("article-tag") {
    val articleId = reference("article-id", Articles, onDelete = ReferenceOption.CASCADE)
    val tagId = reference("tag-id", Tags, onDelete = ReferenceOption.CASCADE)
}