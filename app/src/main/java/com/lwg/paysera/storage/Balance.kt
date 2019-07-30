package com.lwg.paysera.storage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "balance_credit_table")
data class Balance(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "balanceCredit") val balanceCredit: Double,
    @ColumnInfo(name = "currency") val currency: String
)