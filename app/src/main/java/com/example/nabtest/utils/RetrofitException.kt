package com.example.nabtest.utils

import com.example.nabtest.api.responses.ErrorResponse
import com.google.gson.Gson
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class RetrofitException private constructor(
    message: String?,
    /**
     * The request URL which produced the error.
     */
    val url: String?,
    /**
     * Response object containing status code, headers, body, etc.
     */
    val response: Response<*>?,
    /**
     * The event kind which triggered this error.
     */
    val type: Type,
    exception: Throwable
) : RuntimeException(message, exception) {

    override fun toString(): String {
        return super.toString() + " : " + type + " : " + url + " : " + response?.errorBody()
            ?.string()
    }

    /**
     * Identifies the event kind which triggered a [RetrofitException].
     */
    enum class Type {
        /**
         * An [IOException] occurred while communicating to the server.
         */
        NETWORK,

        /**
         * A non-200 HTTP status code was received from the server.
         */
        HTTP,

        /**
         * An internal error occurred while attempting to execute a request. It is best practice to
         * re-throw this exception so your application crashes.
         */
        UNEXPECTED
    }

    companion object {
        fun httpError(
            url: String,
            response: Response<*>?,
            httpException: HttpException
        ): RetrofitException {
            val errorResponse = Gson().fromJson(response?.errorBody()?.string(), ErrorResponse::class.java)
            return RetrofitException(errorResponse.message, url, response, Type.HTTP, httpException)
        }

        fun networkError(exception: IOException): RetrofitException {
            return RetrofitException(exception.message, null, null, Type.NETWORK, exception)
        }

        fun unexpectedError(exception: Throwable): RetrofitException {
            return RetrofitException(exception.message, null, null, Type.UNEXPECTED, exception)
        }

        fun asRetrofitException(throwable: Throwable): RetrofitException {
            if (throwable is RetrofitException) {
                return throwable
            }
            // We had non-200 http error
            if (throwable is HttpException) {
                val response = throwable.response()
                return httpError(response?.raw()?.request?.url.toString(), response, throwable)
            }
            // A network error happened
            return if (throwable is IOException) {
                networkError(throwable)
            } else unexpectedError(throwable)
            // We don't know what happened. We need to simply convert to an unknown error
        }
    }
}