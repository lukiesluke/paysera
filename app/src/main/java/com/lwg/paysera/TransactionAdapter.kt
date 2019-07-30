package com.lwg.paysera

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lwg.paysera.storage.Transaction
import java.text.DecimalFormat

class TransactionAdapter internal constructor(context: Context) :
    RecyclerView.Adapter<TransactionAdapter.TransactionHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var transaction = emptyList<Transaction>()


    inner class TransactionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val amountItemView: TextView = itemView.findViewById(R.id.fromAmount)
        val fromCurrency: TextView = itemView.findViewById(R.id.fromCurrency)
        val amountCurrency: TextView = itemView.findViewById(R.id.amountCurrency)
        val toCurrency: TextView = itemView.findViewById(R.id.toCurrency)
        val fee: TextView = itemView.findViewById(R.id.fee)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return TransactionHolder(itemView)
    }

    override fun getItemCount(): Int = transaction.size


    override fun onBindViewHolder(holder: TransactionHolder, position: Int) {
        val decimalFormat = DecimalFormat("#,###.00")
        val current = transaction[position]

        holder.amountItemView.text = decimalFormat.format(current.fromAmount).toString()
        holder.fromCurrency.text = current.fromCurrency
        holder.toCurrency.text = current.toCurrency
        holder.amountCurrency.text = decimalFormat.format(current.amountCurrency).toString()
        holder.fee.text = decimalFormat.format(current.fee).toString()
    }

    internal fun setWords(transaction: List<Transaction>) {
        this.transaction = transaction
        notifyDataSetChanged()
    }
}