package com.lwg.paysera.storage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_table")
data class Transaction(
    @PrimaryKey(autoGenerate=true) @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "fromAmount") val fromAmount: Double,
    @ColumnInfo(name = "fromCurrency") val fromCurrency: String,
    @ColumnInfo(name = "amountCurrency") val amountCurrency: Double,
    @ColumnInfo(name = "toCurrency") val toCurrency: String,
    @ColumnInfo(name = "fee") val fee: Double
)