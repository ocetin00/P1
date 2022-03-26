package com.oguzhancetin.p1.remote

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.oguzhancetin.p1.util.SelfCleaningLiveData

class MyService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    companion object {
        //hold data for asking product price to customer
        val customerPayStatusRequest = SelfCleaningLiveData<String?>()

        //hold data for paymentType result from customer
        val customerPayStatusResult = SelfCleaningLiveData<String?>()
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d("RECEIVED", "Refreshed token: $message")

        if (!message.data["price"].isNullOrBlank() && !message.data["source"].isNullOrBlank()) {
            val source = message.data["source"]
            val price = message.data["price"]
            val messageForCustomer = "$price,$source"
            customerPayStatusRequest.postValue(messageForCustomer)
        }
        message.data["pay_type"]?.let {
            customerPayStatusResult.postValue(it)
        }
        message.data["from"]?.let { Log.d("messageFrom", it) }

    }

}