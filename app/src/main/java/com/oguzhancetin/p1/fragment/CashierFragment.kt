package com.oguzhancetin.p1.fragment

import android.content.Context.CAMERA_SERVICE
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import com.oguzhancetin.p1.databinding.FragmentCashierBinding
import com.oguzhancetin.p1.remote.MyService.Companion.customerPayStatusResult
import com.oguzhancetin.p1.remote.FirebaseServiceApi
import com.oguzhancetin.p1.remote.model.NotificationData
import com.oguzhancetin.p1.remote.model.PushNotification
import com.oguzhancetin.p1.util.getBackFacingCameraId
import com.oguzhancetin.p1.util.turnVisible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class CashierFragment : BaseFragment<FragmentCashierBinding>() {
    @Inject
    lateinit var firebaseApi: FirebaseServiceApi

    private var sourceToken = ""
    private var destinationToken = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //get current device token
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            sourceToken = task.result

        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //set selected price
        binding.button100.setOnClickListener { sendPriceToCustomer("100 TL") }
        binding.button50.setOnClickListener { sendPriceToCustomer("50 TL") }
        binding.button200.setOnClickListener { sendPriceToCustomer("200 TL") }
        binding.buttonSend.setOnClickListener { sendPriceToCustomer(binding.editTextTextPersonName.text.toString() + " TL") }
        binding.buttonOther.setOnClickListener {
            binding.editTextTextPersonName.turnVisible()
            binding.buttonSend.turnVisible()
        }
        scanBarcodeFrontCamera()

        customerPayStatusResult.observe(this.viewLifecycleOwner) {
            it?.let {
                findNavController().navigate(
                    CashierFragmentDirections.actionCashierFragmentToPaymentResultFragment(
                        it
                    )
                )
            }

        }
    }

    private fun sendPriceToCustomer(price: String) {
        val to = destinationToken
        val notificationData = NotificationData(
            price = price,
            pay_type = null,
            source = sourceToken
        )
        val pushNotification = PushNotification(notificationData, to)
        sendNotification(pushNotification, firebaseApi)
    }

    private fun scanBarcodeFrontCamera() {
        val cameraManager = this.requireContext().getSystemService(CAMERA_SERVICE) as CameraManager
        val cameraId = cameraManager.getBackFacingCameraId()
        cameraId?.let {
            val options = ScanOptions()
            options.setPrompt("")
            options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
            options.setCameraId(it)
            barcodeLauncher.launch(options)
        }
    }

    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            Toast.makeText(this.requireActivity(), "Cancelled", Toast.LENGTH_LONG).show()
        } else {

            destinationToken = result.contents
            binding.button100.turnVisible()
            binding.button200.turnVisible()
            binding.button50.turnVisible()
            binding.buttonOther.turnVisible()
        }
    }


    override fun getViewBinding(): FragmentCashierBinding {
        return FragmentCashierBinding.inflate(layoutInflater)
    }

}