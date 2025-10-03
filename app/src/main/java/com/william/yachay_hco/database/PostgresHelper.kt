package com.william.yachay_hco.database

import java.sql.Connection
import java.sql.DriverManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostgresHelper @Inject constructor() {

    fun getConnection(): Connection? {
        return try {
            Class.forName("org.postgresql.Driver")
            DriverManager.getConnection(
                "jdbc:postgresql://127.0.0.1:5432/yachay_hco",
                "postgres",
                "12345"
            )
        } catch (e: Exception) {
            null
        }
    }
}