package com.lwg.paysera.mvp

import android.content.Context
import com.lwg.paysera.R
import com.lwg.paysera.callbacks.RepositoryCallbacks
import com.lwg.paysera.interactor.PayInteractor
import com.lwg.paysera.interactor.PayInteractorView
import com.lwg.paysera.model.Amount
import com.lwg.paysera.model.SellCurrency
import com.lwg.paysera.storage.Balance
import java.text.DecimalFormat


class PayPresenter(
    private val context: Context,
    private val view: PayView
) : RepositoryCallbacks.IResponseCallback<Amount> {

    private val decimalFormat = DecimalFormat("#,###.00")
    var payInteractor: PayInteractorView = PayInteractor()
    var sellCurrency: SellCurrency? = null

    fun onCurrencyConvert(sellCurrency: SellCurrency?) {
        this.sellCurrency = sellCurrency
        payInteractor.onConvertCurrency(sellCurrency, this)
    }

    fun ready() {
        view.init()
    }

    override fun onSuccess(response: Amount?) {
        view.onConvertCurrencyResponse(response!!)
    }

    override fun onError(errorMessage: String?) {
        view.onErrorService(context.getString(R.string.alert), context.getString(R.string.error_please_check_internet))
    }

    fun onConvertValue(sellCurrency: SellCurrency, currencyBalance: Double) {

        if (currencyBalance >= sellCurrency.fromAmount!! && sellCurrency.fromAmount > 0) {
            onCurrencyConvert(sellCurrency)
        } else {
            if (sellCurrency.fromAmount < 1) {
                view.onErrorService(context.getString(R.string.alert),context.getString(R.string.error_please_enter_amount))
            } else {
                view.onErrorService(context.getString(R.string.alert),context.getString(R.string.error_unable_to_convert))
            }
        }
    }

    fun getBalanceId(it: Long?) {
        if (it != null) {
            view.userId(it)
        }
    }

    fun showBalanceInformation(balance: Double, currency: String) {
        view.showUserBalanceInformation(balance, currency)
    }

    fun getBalance(balance: Balance?) {
        if (balance != null) {
            view.showUserBalanceInformation(balance.balanceCredit, balance.currency)
        } else {
            view.showDialogBalanceSetup("Save")
        }
    }

    fun totalTransactionCharge(it: Double?, userCurrency: String) {
        if (it == null) {
            view.totalTransactionCharge(context.getString(R.string.total_transaction_charge).format(userCurrency))
        } else {
            view.totalTransactionCharge("Total transaction charge: ${decimalFormat.format(it)} ${userCurrency}")
        }
    }
}