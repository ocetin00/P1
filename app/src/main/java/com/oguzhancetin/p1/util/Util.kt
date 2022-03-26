package com.oguzhancetin.p1.util

import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.view.View

fun View.turnVisible() {
    this.visibility = View.VISIBLE
}

fun View.turnInVisible() {
    this.visibility = View.INVISIBLE
}

fun CameraManager.getBackFacingCameraId(): Int? {
    for (cameraId in this.cameraIdList) {
        val characteristics: CameraCharacteristics = this.getCameraCharacteristics(cameraId)
        val cOrientation = characteristics.get(CameraCharacteristics.LENS_FACING)!!
        if (cOrientation == CameraCharacteristics.LENS_FACING_BACK) return cameraId.toInt()
    }
    return null
}