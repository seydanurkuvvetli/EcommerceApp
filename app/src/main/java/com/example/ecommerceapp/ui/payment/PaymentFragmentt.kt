package com.example.ecommerceapp.ui.payment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecommerceapp.R
import com.example.ecommerceapp.common.CreditCartTextFormatter
import com.example.ecommerceapp.common.viewBinding
import com.example.ecommerceapp.databinding.FragmentPaymentFragmenttBinding
import com.example.ecommerceapp.ui.favorites.FavoriteViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch


class PaymentFragmentt : Fragment(R.layout.fragment_payment_fragmentt) {
    private val viewmodel by viewModels<PaymentViewModel>()
    private val binding by viewBinding(FragmentPaymentFragmenttBinding::bind)
    private val monthList = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
    private val yearList = listOf(2023, 2024, 2025, 2026, 2027, 2028, 2029, 2030, 2031, 2032)

    private var monthValue = 0
    private var yearValue = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val monthAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown_menu, monthList)
        val yearAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown_menu, yearList)

        with(binding) {

            etCreditCardNumber.addTextChangedListener(CreditCartTextFormatter())

            actExpireOnMonth.setAdapter(monthAdapter)
            actExpireOnYear.setAdapter(yearAdapter)

            actExpireOnMonth.setOnItemClickListener { _, _, position, _ ->
                monthValue = monthList[position]
            }

            actExpireOnYear.setOnItemClickListener { _, _, position, _ ->
                yearValue = yearList[position]
            }


            imgBack.setOnClickListener {
                findNavController().navigateUp()
            }

            btnPayNow.setOnClickListener {
                if (checkInfos(
                        etCardholderName,
                        etCreditCardNumber,
                        actExpireOnMonth,
                        actExpireOnYear,
                        etCvcCode,
                        etAddress
                    )

                ) {


                    
                    findNavController().navigate(R.id.action_paymentFragmentt_to_successFragment)

                    

                } else {


                    val alertDialogBuilder = AlertDialog.Builder(requireContext())
                    alertDialogBuilder.setTitle("Uyarı")
                    alertDialogBuilder.setMessage("Lütfen tüm alanları doldurun.")
                    alertDialogBuilder.setPositiveButton("Tamam") { dialog, _ ->
                        dialog.dismiss()
                    }
                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.show()
                }
            }
        }
    }

    private fun checkInfos(
        cardHolderName: TextInputEditText,
        creditCardNumber: TextInputEditText,
        month: AutoCompleteTextView,
        year: AutoCompleteTextView,
        cvcCode: TextInputEditText,
        address: TextInputEditText,
    ): Boolean {
        val checkInfos = when {
            cardHolderName.text.isNullOrBlank() -> false
            creditCardNumber.text.isNullOrBlank() -> false
            month.text.isNullOrBlank() -> false
            year.text.isNullOrBlank() -> false
            cvcCode.text.isNullOrBlank() -> false
            address.text.isNullOrBlank() -> false
            else -> true
        }

        return checkInfos
    }
}

