package com.example.space

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.example.space.databinding.ActivityMainBinding
import com.example.space.mvi_interfaces.MainActivityView
import com.example.space.notification.RestartServiceBroadcastReceiver
import com.example.space.notification.NotificationChargingService
import com.example.space.presenters.MainActivityPresenter
import com.example.space.ui.main_screen.MainScreenFragment
import com.example.space.ui.map_screen.MapScreenFragment
import com.example.space.utils.*
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.androidx.AppNavigator
import dagger.hilt.android.AndroidEntryPoint
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider


@AndroidEntryPoint
class MainActivity : MvpAppCompatActivity(R.layout.activity_main), MainActivityView {

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    private val navigator = AppNavigator(this, R.id.fragment_container)

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var presenterProvider: Provider<MainActivityPresenter>
    private val presenter by moxyPresenter { presenterProvider.get() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createNotificationChannel()
        setupBottomMenu()

        val service = NotificationChargingService()
        val serviceIntent = Intent(this, service::class.java)

        if (!service.isServiceRunning) {
            startService(serviceIntent)
        }
        if (intent.hasExtra(PUSH_NOTIFICATION_ACTION_KEY) && intent.getStringExtra(
                PUSH_NOTIFICATION_ACTION_KEY
            ) == PUSH_NOTIFICATION_TO_MAP
        ) {
            presenter.navigateToMapScreen()
            binding.apply {
                itemMainScreen.setImageResource(R.drawable.main_bottom_menu_uncheck)
                itemMapsScreen.setImageResource(R.drawable.maps_bottom_menu_check)
            }
            intent.putExtra(PUSH_NOTIFICATION_ACTION_KEY, PUSH_NOTIFICATION_NOT_TO_MAP)
        } else {
            if (savedInstanceState == null) {
                presenter.openSplashScreen()
            }
        }
    }

    private fun setupBottomMenu() {
        binding.itemMainScreen.setOnClickListener {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
            if (currentFragment !is MainScreenFragment) {
                binding.apply {
                    itemMainScreen.setImageResource(R.drawable.main_bottom_menu_check)
                    itemMapsScreen.setImageResource(R.drawable.maps_bottom_menu_uncheck)
                }
                presenter.navigateToMainScreen()
            }
        }
        binding.itemMapsScreen.setOnClickListener {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
            if (currentFragment !is MapScreenFragment) {
                binding.apply {
                    itemMainScreen.setImageResource(R.drawable.main_bottom_menu_uncheck)
                    itemMapsScreen.setImageResource(R.drawable.maps_bottom_menu_check)
                }
                presenter.navigateToMapScreen()
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment is MainScreenFragment) {
            return
        } else {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onDestroy() {
        val serviceIntent = Intent(this, NotificationChargingService::class.java)
        stopService(serviceIntent)
        val broadcastIntent = Intent().apply {
            action = INTENT_ACTION
            setClass(this@MainActivity, RestartServiceBroadcastReceiver::class.java)
        }
        sendBroadcast(broadcastIntent)
        super.onDestroy()
    }
}