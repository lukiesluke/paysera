package com.lwg.paysera.mvp

import com.lwg.paysera.model.Amount

interface PayView {

    fun init()
    fun onConvertCurrencyResponse(amount: Amount)
    fun onErrorService(title: String, message: String)
    fun userId(id: Long)
    fun showUserBalanceInformation(balanceCredit: Double, currency: String)
    fun showDialogBalanceSetup(buttonTitle: String)
    fun totalTransactionCharge(transactionCharge: String)
}