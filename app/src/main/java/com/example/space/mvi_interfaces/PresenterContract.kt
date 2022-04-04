package com.example.space.mvi_interfaces

import com.example.space.utils.RetainedState

interface PresenterContract<VIEW> {
    fun attach(view: VIEW, retainedState: RetainedState)
    fun detach()
    fun onViewAttached()
    fun onViewDetached()
}
