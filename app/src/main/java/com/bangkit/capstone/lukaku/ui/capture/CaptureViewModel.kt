package com.bangkit.capstone.lukaku.ui.capture

import androidx.camera.core.Camera
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CaptureViewModel : ViewModel() {
    val mutableCamera: MutableLiveData<Camera> = MutableLiveData<Camera>()

    val mutableLensFacing: MutableLiveData<Int> = MutableLiveData<Int>()

    val mutableCameraProvider: MutableLiveData<ProcessCameraProvider> =
        MutableLiveData<ProcessCameraProvider>()

    val mutableIsFlash: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    fun getCamera(): LiveData<Camera> = mutableCamera

    fun getLensFacing(): LiveData<Int> = mutableLensFacing

    fun getCameraProvider(): LiveData<ProcessCameraProvider> = mutableCameraProvider

    fun isFlash(): LiveData<Boolean> = mutableIsFlash
}