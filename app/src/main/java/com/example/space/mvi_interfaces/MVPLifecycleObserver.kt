package com.example.space.mvi_interfaces

import androidx.lifecycle.*
import com.example.space.utils.RetainedState

class MVPLifecycleObserver<VIEW : BaseView<PRESENTER>, PRESENTER : BasePresenter<VIEW>>(
    private val view: VIEW,
    private val presenter: PRESENTER,
    private val retainedState: RetainedState
) : DefaultLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        presenter.attach(view, retainedState)
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        presenter.detach()
    }
}