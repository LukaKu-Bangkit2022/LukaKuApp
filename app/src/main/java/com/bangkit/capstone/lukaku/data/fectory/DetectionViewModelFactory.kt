package com.bangkit.capstone.lukaku.data.fectory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.capstone.lukaku.data.repository.result.ResultRepository
import com.bangkit.capstone.lukaku.di.Injection
import com.bangkit.capstone.lukaku.ui.profile.ProfileViewModel
import com.bangkit.capstone.lukaku.ui.result.ResultViewModel

class DetectionViewModelFactory(
    private val resultRepository: ResultRepository?,
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResultViewModel::class.java)) {
            return ResultViewModel(resultRepository!!) as T
        }else if(modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(resultRepository!!) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass.name")
    }

    companion object {
        @Volatile
        private var instance: DetectionViewModelFactory? = null
        fun getInstance(context: Context): DetectionViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: DetectionViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}