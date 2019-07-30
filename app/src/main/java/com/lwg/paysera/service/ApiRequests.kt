package com.lwg.paysera.service

import com.lwg.paysera.model.Amount
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiRequests {

    @GET("currency/commercial/exchange/{fromAmount}-{fromCurrency}/{toCurrency}/latest")
    fun onCurrencyConverter(@Path("fromAmount") fromAmount: Double?,
                            @Path("fromCurrency") fromCurrency: String?,
                            @Path("toCurrency") toCurrency: String?) : Call<Amount>
}