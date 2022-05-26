package com.bangkit.capstone.lukaku.ui.main

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AnticipateInterpolator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.bangkit.capstone.lukaku.R
import com.bangkit.capstone.lukaku.databinding.ActivityMainBinding
import com.bangkit.capstone.lukaku.utils.Constants.KEY_EVENT_ACTION
import com.bangkit.capstone.lukaku.utils.Constants.KEY_EVENT_EXTRA
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreenWithAnim(savedInstanceState)
        supportActionBar?.hide()

        lifecycleScope.launch {
            viewModel.getThemeSetting().collect { isDarkModeActive ->
                val theme = if (isDarkModeActive == true) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else AppCompatDelegate.MODE_NIGHT_NO

                AppCompatDelegate.setDefaultNightMode(theme)
            }
        }
        setupPreDrawListener()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.fragmentContainerView)
        setupActionBarWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.button_nav_menu, menu)
        binding.bottomBar.setupWithNavController(menu, navController)
        return false
    }

    override fun onSupportNavigateUp(): Boolean {
        navController.navigateUp()
        return true
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                val intent = Intent(KEY_EVENT_ACTION).apply { putExtra(KEY_EVENT_EXTRA, keyCode) }
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
                true
            }
            else -> super.onKeyDown(keyCode, event)
        }
    }

    private fun setupPreDrawListener() {
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    // Check if the initial data is ready.
                    return if (viewModel.isReady()) {
                        // The content is ready; start drawing.
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        // The content is not ready; suspend.
                        false
                    }
                }
            }
        )
    }

    private fun installSplashScreenWithAnim(savedInstanceState: Bundle?) {
        installSplashScreen().also {
            if (savedInstanceState == null) {
                it.setOnExitAnimationListener { splashScreenView ->
                    // Create custom animation.
                    splashScreenView.iconView.animate().rotation(180F).duration = 500L
                    ObjectAnimator.ofFloat(
                        splashScreenView.iconView,
                        View.TRANSLATION_Y,
                        0f,
                        -splashScreenView.iconView.height.toFloat()
                    ).apply {
                        interpolator = AnticipateInterpolator()
                        duration = 500L
                        doOnEnd { splashScreenView.remove() }
                        start()
                    }
                }
            }
        }
    }
}