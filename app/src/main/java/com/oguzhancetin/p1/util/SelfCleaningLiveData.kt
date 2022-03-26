package com.oguzhancetin.p1.util

import androidx.lifecycle.MutableLiveData

//Live data will clear when observer inActive(fragment destroyed)
class SelfCleaningLiveData<T> : MutableLiveData<T>() {
    override fun onInactive() {
        super.onInactive()
        value = null
    }

}