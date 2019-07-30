package com.lwg.paysera.storage

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface BalanceDao {

    @Query("SELECT * from balance_credit_table")
    fun getBalance(): LiveData<Balance>

    @Query("SELECT id FROM balance_credit_table")
    fun getId(): LiveData<Long>

    @Insert
    suspend fun insert(balance: Balance)

    @Update
    suspend fun update(balance: Balance)

    @Query("DELETE FROM balance_credit_table")
    suspend fun clearBalance()

}