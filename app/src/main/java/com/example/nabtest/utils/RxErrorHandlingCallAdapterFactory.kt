package com.example.nabtest.utils

import com.example.nabtest.utils.RetrofitException.Companion.asRetrofitException
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.Function
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import java.lang.reflect.Type

internal class RxErrorHandlingCallAdapterFactory private constructor() : CallAdapter.Factory() {
    private val original = RxJava3CallAdapterFactory.create()

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        return RxCallAdapterWrapper(original.get(returnType, annotations, retrofit) ?: return null)
    }

    private class RxCallAdapterWrapper<R>(private val wrapped: CallAdapter<R, *>) :
        CallAdapter<R, Any> {

        override fun responseType(): Type {
            return wrapped.responseType()
        }

        override fun adapt(call: Call<R>): Any {
            return when (val result = wrapped.adapt(call)) {
                is Single<*> -> result.onErrorResumeNext(Function { throwable ->
                    Single.error(
                        asRetrofitException(throwable)
                    )
                })
                is Maybe<*> -> result.onErrorResumeNext(Function { throwable ->
                    Maybe.error(
                        asRetrofitException(throwable)
                    )
                })
                is Observable<*> -> result.onErrorResumeNext(Function { throwable ->
                    Observable.error(
                        asRetrofitException(throwable)
                    )
                })
                is Completable -> result.onErrorResumeNext(Function { throwable ->
                    Completable.error(
                        asRetrofitException(throwable)
                    )
                })
                else -> result
            }

        }
    }

    companion object {
        fun create(): CallAdapter.Factory {
            return RxErrorHandlingCallAdapterFactory()
        }
    }
}