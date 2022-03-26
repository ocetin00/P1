package com.oguzhancetin.p1.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.gson.Gson
import com.oguzhancetin.p1.remote.FirebaseServiceApi
import com.oguzhancetin.p1.remote.model.PushNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding()
        return binding.root
    }

    abstract fun getViewBinding(): VB

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun sendNotification(notification: PushNotification, firebaseApi: FirebaseServiceApi) =
        CoroutineScope(Dispatchers.IO).launch {

            try {
                val response = firebaseApi.postNotification(notification)
                if (response.isSuccessful) {
                    Log.d("RESPONSE", "Response: ${Gson().toJson(response)}")
                } else {
                    Log.e("RESPONSE", response.errorBody().toString())
                }
            } catch (e: Exception) {
                Log.e("RESPONSE", e.toString())
            }
        }


}