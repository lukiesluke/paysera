package com.lwg.paysera.interactor

import android.util.Log
import com.lwg.paysera.callbacks.RepositoryCallbacks
import com.lwg.paysera.model.Amount
import com.lwg.paysera.model.SellCurrency
import com.lwg.paysera.service.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PayInteractor : PayInteractorView {

    override fun onConvertCurrency(sell: SellCurrency?, callback: RepositoryCallbacks.IResponseCallback<Amount>) {

        val call: Call<Amount> = ApiClient.getClient.onCurrencyConverter(sell?.fromAmount, sell?.fromCurrency, sell?.toCurrency)
        Log.d("lwg", "onConvertCurrencyResponse sell?.fromAmount: " + sell?.fromAmount)

        call.enqueue(object : Callback<Amount> {

            override fun onResponse(call: Call<Amount>, response: Response<Amount>) {
                if (response.isSuccessful) {
                    Log.d("lwg", "onResponse " + response.body()?.amount!!.toDouble())
                    Log.d("lwg", "onResponse " + response.body()?.currency)
                    val amount: Amount = response.body()!!
                    callback.onSuccess(amount)
                } else {
                    callback.onError(response.message())
                    Log.d("lwg", "error really" + response.message())
                }
            }

            override fun onFailure(call: Call<Amount>, t: Throwable) {
                Log.d("lwg", "errorT " + t.message)
                callback.onError(t.message)
            }
        })
    }
}