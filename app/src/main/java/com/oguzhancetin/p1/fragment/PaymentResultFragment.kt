package com.oguzhancetin.p1.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.oguzhancetin.p1.databinding.FragmentPaymentResultBinding

class PaymentResultFragment : BaseFragment<FragmentPaymentResultBinding>() {
    private val args by navArgs<PaymentResultFragmentArgs>()

    override fun getViewBinding(): FragmentPaymentResultBinding {
        return FragmentPaymentResultBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.paymentType?.let { paymentType ->
            binding.textviewPaymentResult.text = paymentType
        }

        binding.buttonNewReceipt.setOnClickListener {
            findNavController().navigate(
                PaymentResultFragmentDirections.actionPaymentResultFragmentToMainFragment()
            )
        }


    }


}