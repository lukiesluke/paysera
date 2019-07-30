package com.lwg.paysera.repository

import androidx.lifecycle.LiveData
import com.lwg.paysera.storage.Balance
import com.lwg.paysera.storage.Transaction
import com.lwg.paysera.storage.TransactionDao

class TransactionRepository(private val transactionDao: TransactionDao) {

    val getTransaction: LiveData<List<Transaction>> = transactionDao.getAllTransaction()
    val countTransaction : LiveData<Int> = transactionDao.countTransaction()
    val totalTransactionCharge : LiveData<Double> = transactionDao.totalTransactionCharge()

    suspend fun insert(transaction: Transaction) {
        transactionDao.insert(transaction)
    }

    suspend fun clearTransaction() {
        transactionDao.clearTransaction()
    }
}