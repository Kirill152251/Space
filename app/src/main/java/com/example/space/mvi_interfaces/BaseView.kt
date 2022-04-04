package com.example.space.mvi_interfaces

interface BaseView<Presenter> {
    fun getPresenter(): Presenter
}