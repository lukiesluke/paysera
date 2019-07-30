package com.lwg.paysera.storage


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TransactionDao {

    @Query("SELECT * from transaction_table order by id DESC")
    fun getAllTransaction(): LiveData<List<Transaction>>

    @Query("SELECT COUNT(id) FROM transaction_table")
    fun countTransaction(): LiveData<Int>

    @Query("SELECT SUM(fee) FROM transaction_table")
    fun totalTransactionCharge(): LiveData<Double>

    @Insert
    suspend fun insert(transaction: Transaction)

    @Query("DELETE FROM transaction_table")
    suspend fun clearTransaction()
}