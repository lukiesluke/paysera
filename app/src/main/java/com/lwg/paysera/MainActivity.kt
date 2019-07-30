package com.lwg.paysera

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.lwg.paysera.model.Amount
import com.lwg.paysera.model.SellCurrency
import com.lwg.paysera.mvp.PayPresenter
import com.lwg.paysera.mvp.PayView
import com.lwg.paysera.storage.Balance
import com.lwg.paysera.storage.Transaction
import com.lwg.paysera.viewModel.TransactionViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.balance_dialog.view.*
import java.math.BigDecimal
import java.text.DecimalFormat

class MainActivity : AppCompatActivity(), PayView {

    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var sellCurrency: SellCurrency
    private lateinit var amount: Amount
    private lateinit var presenter: PayPresenter
    private lateinit var adapter: TransactionAdapter
    private val currencyList = listOf("EUR", "USD", "JPY")
    private val decimalFormat = DecimalFormat("#,###.00")
    private var userBalance: Double = 0.00
    private var userCurrency: String = ""
    private var userId: Long = 0
    private var toCurrency: String = ""
    private var totalTransactionHistory: Int = 0
    private var feeCharge = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        transactionViewModel = ViewModelProviders.of(this).get(TransactionViewModel::class.java)

        adapter = TransactionAdapter(this)
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(this)

        presenter = PayPresenter(this, this)
        presenter.ready()
        presenter.showBalanceInformation(userBalance, userCurrency)
    }

    override fun init() {

        transactionViewModel.getTransaction.observe(this, Observer { words ->
            words?.let { adapter.setWords(it) }
        })

        transactionViewModel.countTransaction.observe(this, Observer {
            totalTransactionHistory = it
            totalTransactionTv.text = "${getString(R.string.total_transaction)} ${totalTransactionHistory}"
        })

        transactionViewModel.totalTransactionCharge.observe(this, Observer {
            presenter.totalTransactionCharge(it, userCurrency)
        })

        transactionViewModel.getBalance.observe(this, Observer {
            presenter.getBalance(it)
        })

        transactionViewModel.getBalanceId.observe(this, Observer {
            presenter.getBalanceId(it)
        })

        convertValueEdTxt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (convertValueEdTxt.text.toString().equals(".")) {
                    convertValueEdTxt.setText("0.")
                    convertValueEdTxt.setSelection(convertValueEdTxt.text.toString().length)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        convertValueEdTxt.setSelection(convertValueEdTxt.text.toString().length)

        convertBtn.setOnClickListener {

            convertBtn.isEnabled = false
            sellCurrency = SellCurrency.Builder()
                .fromAmount(convertToDouble(convertValueEdTxt.text))
                .fromCurrency(userCurrency)
                .toCurrency(toCurrency)
                .build()

            presenter.onConvertValue(sellCurrency, userBalance)

            hideKeyboard()
            convertValueEdTxt.clearFocus()
        }

        settingBtn.setOnClickListener {
            showDialogBalanceSetup("Save and Reset")
        }

        setSpinner(spinner)
    }

    private fun convertToDouble(textEditable: Editable): Double {
        var value = 0.00
        if (!textEditable.toString().isEmpty()) {
            value = textEditable.toString().toDouble()
        }
        return value
    }

    private fun setSpinner(sp: Spinner) {

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencyList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp.adapter = adapter

        when (sp.id) {
            R.id.spinner -> {
                sp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        toCurrency = parent?.getItemAtPosition(position).toString()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }
                }
            }
            R.id.dialogSpinner -> {
                sp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        userCurrency = parent?.getItemAtPosition(position).toString()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }
                }
            }
        }
    }

    override fun onConvertCurrencyResponse(amount: Amount) {
        this.amount = amount
        convertBtn.isEnabled = true

        var percent = 0.0
        feeCharge = 0.00
        if (totalTransactionHistory >= 5) {
            percent = 0.07
            val decimalSubtotal = BigDecimal(percent.toString())
            feeCharge = convertToDouble(convertValueEdTxt.text).toBigDecimal().multiply(decimalSubtotal).toDouble()
        }

        val message = getString(R.string.messageConversionInfo).format(
            decimalFormat.format(convertToDouble(convertValueEdTxt.text)),
            userCurrency,
            amount.amount,
            amount.currency,
            feeCharge,
            toCurrency
        )

        onAlertDialogBox(getString(R.string.thank_you), message)

        userBalance =
            userBalance.toBigDecimal().minus(convertToDouble(convertValueEdTxt.text).toBigDecimal()).toString().toDouble()
        userBalance = userBalance.toBigDecimal().minus(feeCharge.toBigDecimal()).toDouble()

        val transaction = Transaction(
            0,
            convertToDouble(convertValueEdTxt.text),
            userCurrency,
            amount.amount.toDouble(),
            amount.currency,
            feeCharge
        )

        transactionViewModel.insert(transaction)
        transactionViewModel.updateBalance(Balance(userId, userBalance, userCurrency))

        presenter.showBalanceInformation(userBalance, userCurrency)
    }

    override fun onErrorService(titile: String, message: String) {
        convertBtn.isEnabled = true
        onAlertDialogBox(titile, message)
    }

    override fun userId(id: Long) {
        userId = id
    }

    private fun onAlertDialogBox(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)

        builder.setMessage(message)
        builder.setCancelable(false)

        builder.setPositiveButton(getString(R.string.okay)) { dialog, which ->
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun totalTransactionCharge(transactionCharge: String) {
        totalTransactionChargeTv.text = transactionCharge
    }

    override fun showDialogBalanceSetup(buttonTitle: String) {

        val dialogView = LayoutInflater.from(this).inflate(R.layout.balance_dialog, null)
        val alertDialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle(getString(R.string.balance_setup))

        val alertDialog = alertDialogBuilder.setCancelable(false).show()
        val spinnerDialog = dialogView.dialogSpinner
        setSpinner(spinnerDialog)
        dialogView.dialogLoginBtn.text = buttonTitle
        dialogView.dialogLoginBtn.setOnClickListener {

            val dialogAmount = convertToDouble(dialogView.dialogValueEt.text)
            if (dialogAmount < 1) {
                onErrorService(getString(R.string.alert), getString(R.string.error_please_enter_amount))
            } else {

                transactionViewModel.clearBalance()
                transactionViewModel.clearTransaction()

                val bal = Balance(0, dialogAmount, userCurrency)
                transactionViewModel.insert(bal)
                alertDialog.dismiss()
            }
        }
    }

    override fun showUserBalanceInformation(balanceCredit: Double, currency: String) {
        this.userBalance = balanceCredit
        this.userCurrency = currency
        availableBalanceTv.text =
            getString(R.string.available_balance).format(decimalFormat.format(this.userBalance), this.userCurrency)
        convertValueEdTxt.text.clear()
    }

    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            view.clearFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
