package com.steiner.vblog.util

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun <T> dbQuery(database: Database, block: suspend () -> T): T {
    return if (TransactionManager.currentOrNull() != null) {
        block()
    } else {
        newSuspendedTransaction(db = database) {
            block()
        }
    }
}