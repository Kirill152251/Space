package com.example.space

import android.os.Bundle
import com.example.space.databinding.ActivityMainBinding
import com.example.space.mvi_interfaces.MainActivityView
import com.example.space.presenters.MainActivityPresenter
import com.example.space.ui.main_screen.MainScreenFragment
import com.example.space.ui.splash_screen.SplashScreenFragment

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
        if (savedInstanceState == null) {
            presenter.openSplashScreen()
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
}