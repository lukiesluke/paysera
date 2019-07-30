package com.lwg.paysera.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.lwg.paysera.repository.BalanceRepository
import com.lwg.paysera.repository.TransactionRepository
import com.lwg.paysera.storage.AppDatabase
import com.lwg.paysera.storage.Balance
import com.lwg.paysera.storage.Transaction
import kotlinx.coroutines.launch

class TransactionViewModel(application: Application) : AndroidViewModel(application) {

    private val repositoryTransaction: TransactionRepository
    private val repositoryBalance: BalanceRepository
    val getTransaction: LiveData<List<Transaction>>
    val countTransaction: LiveData<Int>
    val totalTransactionCharge: LiveData<Double>
    val getBalance: LiveData<Balance>
    val getBalanceId: LiveData<Long>


    init {
        val transactionDao = AppDatabase.getDatabase(application, viewModelScope).transactionDao()
        val balanceDao = AppDatabase.getDatabase(application, viewModelScope).balanceDao()

        repositoryTransaction = TransactionRepository(transactionDao)
        getTransaction = repositoryTransaction.getTransaction
        countTransaction = repositoryTransaction.countTransaction
        totalTransactionCharge = repositoryTransaction.totalTransactionCharge

        repositoryBalance = BalanceRepository(balanceDao)
        getBalance = repositoryBalance.getBalance
        getBalanceId = repositoryBalance.getBalanceId
    }

    fun insert(transaction: Transaction) = viewModelScope.launch {
        repositoryTransaction.insert(transaction)
    }

    fun clearTransaction() = viewModelScope.launch {
        repositoryTransaction.clearTransaction()
    }

    fun insert(balance: Balance) = viewModelScope.launch {
        repositoryBalance.insert(balance)
    }

    fun updateBalance(balance: Balance) = viewModelScope.launch {
        repositoryBalance.update(balance)
    }

    fun clearBalance() = viewModelScope.launch {
        repositoryBalance.clearBalance()
    }

}