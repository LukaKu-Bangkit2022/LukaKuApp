package com.bangkit.capstone.lukaku.helper

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class ActivityLifeObserver(private val update: () -> Unit) : DefaultLifecycleObserver {
    override fun onCreate(lifecycleOwner: LifecycleOwner) {
        super.onCreate(lifecycleOwner)
        lifecycleOwner.lifecycle.removeObserver(this)
        update()
    }
}

