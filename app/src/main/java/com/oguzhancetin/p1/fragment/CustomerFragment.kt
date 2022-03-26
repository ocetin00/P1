package com.oguzhancetin.p1.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.oguzhancetin.p1.R
import com.oguzhancetin.p1.databinding.FragmentCustomerBinding
import com.oguzhancetin.p1.remote.MyService.Companion.customerPayStatusRequest
import com.oguzhancetin.p1.remote.FirebaseServiceApi
import com.oguzhancetin.p1.remote.model.NotificationData
import com.oguzhancetin.p1.remote.model.PushNotification
import com.oguzhancetin.p1.util.turnInVisible
import com.oguzhancetin.p1.util.turnVisible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class CustomerFragment : BaseFragment<FragmentCustomerBinding>() {
    @Inject
    lateinit var firebaseApi: FirebaseServiceApi

    private var sourceToken = ""
    private var destinationToken = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            buttonCredit.setOnClickListener {
                notifyCashier(
                    "Müşteri ödeme yapmıştır\n" +
                            "Ödeme Bilgisi: Kredi kartı ile ödendi"
                )
            }
            buttonCash.setOnClickListener {
                notifyCashier(
                    "Müşteri ödeme yapmıştır\n" +
                            "Ödeme Bilgisi: Nakit ödendi"
                )
            }
            buttonCancel.setOnClickListener { notifyCashier("Müşteri siparişi iptal etmiştir") }
        }


        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            sourceToken = token
            Log.d("FCM", token)
            try {
                val barcodeEncoder = BarcodeEncoder()
                val bitmap = barcodeEncoder.encodeBitmap(token, BarcodeFormat.QR_CODE, 400, 400)
                binding.imageviewQrcode.setImageBitmap(bitmap)
            } catch (e: Exception) {
            }
        })
        observeLiveData()

    }

    private fun notifyCashier(pay_type: String) {
        val to = destinationToken
        val notificationData = NotificationData(
            price = null,
            pay_type = pay_type,
            source = sourceToken
        )
        val pushNotification = PushNotification(notificationData, to)
        sendNotification(pushNotification, firebaseApi)
        Toast.makeText(this.requireActivity(), getString(R.string.thanks), Toast.LENGTH_SHORT)
            .show()

        findNavController().navigate(CustomerFragmentDirections.actionCustomerFragmentToMainFragment2())

    }

    private fun observeLiveData() {
        customerPayStatusRequest.observe(this.viewLifecycleOwner) {
            it?.let {
                Log.d("gelen", it)
                val list1 = it.split(",")
                val price = list1[0]
                destinationToken = list1[1]

                binding.apply {
                    val paymentText = "ödeme tutarı: $price"
                    imageviewQrcode.turnInVisible()
                    textviewPaymentStatus.text = paymentText
                    buttonCancel.turnVisible()
                    buttonCash.turnVisible()
                    buttonCredit.turnVisible()
                    textviewPaymentStatus.turnVisible()
                }
            }
        }
    }


    override fun getViewBinding(): FragmentCustomerBinding {
        return FragmentCustomerBinding.inflate(layoutInflater)
    }


}
