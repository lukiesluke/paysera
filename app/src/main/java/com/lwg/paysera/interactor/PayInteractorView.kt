package com.lwg.paysera.interactor

import com.lwg.paysera.callbacks.RepositoryCallbacks
import com.lwg.paysera.model.Amount
import com.lwg.paysera.model.SellCurrency

interface PayInteractorView {

    fun onConvertCurrency(sell: SellCurrency?, callback: RepositoryCallbacks.IResponseCallback<Amount>)
}