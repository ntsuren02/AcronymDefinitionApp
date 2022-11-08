package com.example.acronymdefinitionapp.utils

sealed class RequestState {
    object LOADING : RequestState()
    class SUCCESS<T>(val definitions: T) : RequestState()
    class ERROR(val exception: Throwable) : RequestState()
}
