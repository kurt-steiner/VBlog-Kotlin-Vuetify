package com.steiner.vblog.table

import com.steiner.vblog.TagConfigure
import org.jetbrains.exposed.dao.id.IntIdTable

object Tags: IntIdTable("tags") {
    val name = varchar("name", TagConfigure.NAME_LENGTH)
    val userId = reference("user-id", Users)
}