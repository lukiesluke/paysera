package com.lwg.paysera.repository

import androidx.lifecycle.LiveData
import com.lwg.paysera.storage.Balance
import com.lwg.paysera.storage.BalanceDao

class BalanceRepository(private val balanceDao: BalanceDao) {

    val getBalance : LiveData<Balance> = balanceDao.getBalance()
    val getBalanceId : LiveData<Long> = balanceDao.getId()

    suspend fun insert(balance: Balance) {
        balanceDao.insert(balance)
    }

    suspend fun update(balance: Balance) {
        balanceDao.update(balance)
    }

    suspend fun clearBalance() {
        balanceDao.clearBalance()
    }
}