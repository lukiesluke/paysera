package com.lwg.paysera.model

class SellCurrency(val fromAmount: Double?, val fromCurrency: String?, val toCurrency: String?) {

    data class Builder(
        var fromAmount: Double? = null,
        var fromCurrency: String? = null,
        var toCurrency: String? = null
    ) {
        fun fromAmount(fromAmount: Double) = apply { this.fromAmount = fromAmount }
        fun fromCurrency(fromCurrency: String) = apply { this.fromCurrency = fromCurrency }
        fun toCurrency(toCurrency: String) = apply { this.toCurrency = toCurrency }
        fun build() = SellCurrency(fromAmount, fromCurrency, toCurrency)
    }
}