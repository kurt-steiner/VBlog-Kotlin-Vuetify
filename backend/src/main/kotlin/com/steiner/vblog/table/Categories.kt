package com.steiner.vblog.table

import com.steiner.vblog.CategoryConfigure
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime


object Categories: IntIdTable("categories") {
    val name = varchar("name", CategoryConfigure.NAME_LENGTH).uniqueIndex()
    val createTime = datetime("create-time").defaultExpression(CurrentDateTime)
    val userId = reference("user-id", Users)
}