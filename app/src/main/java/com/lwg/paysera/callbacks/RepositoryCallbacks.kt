package com.lwg.paysera.callbacks

class RepositoryCallbacks {

    interface IResponseCallback<T> {
        fun onSuccess(response: T?)
        fun onError(errorMessage: String?)
    }
}