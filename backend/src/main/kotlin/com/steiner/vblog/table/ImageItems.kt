package com.steiner.vblog.table

import com.steiner.vblog.ImageItemConfigure
import org.jetbrains.exposed.dao.id.IntIdTable

object ImageItems: IntIdTable("image-items") {
    val name = varchar("name", ImageItemConfigure.NAME_LENGTH)
    val path = varchar("path", ImageItemConfigure.PATH_LENGTH).uniqueIndex()
}