package com.example.space.mvi_interfaces

import androidx.annotation.CallSuper
import com.example.space.utils.RetainedState

abstract class BasePresenter<VIEW>: PresenterContract<VIEW> {

    protected var view: VIEW? = null
    protected var retainedState: RetainedState? = null

    override fun attach(view: VIEW, retainedState: RetainedState) {
        this.view = view
        this.retainedState = retainedState
        onViewAttached()
    }

    override fun detach() {
        view = null
        retainedState = null
        onViewDetached()
    }

    @CallSuper
    override fun onViewAttached() { /* do nothing */ }

    @CallSuper
    override fun onViewDetached() { /* do nothing */ }
}