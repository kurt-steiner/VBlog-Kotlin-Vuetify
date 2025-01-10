package com.steiner.vblog.table

import com.steiner.vblog.UserConfigure
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object Users: IntIdTable("users") {
    val name = varchar("name", UserConfigure.NAME_LENGTH).uniqueIndex()
    val nickname = varchar("nickname", UserConfigure.NICKNAME_LENGTH).nullable()
    val passwordHash = char("password-hash", UserConfigure.PASSWORD_LENGTH)
    val email = varchar("email", UserConfigure.EMAIL_LENGTH).nullable()
    val registerTime = datetime("registerTime").defaultExpression(CurrentDateTime)
    val avatarId = reference("avatar-id", ImageItems).nullable()
}