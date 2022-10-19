package com.liaudev.storydicoding.vo


/**
 * Created by Budiliauw87 on 2022-10-18.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */
sealed class Resource <out T>(val data: T? = null, val message: String? = null) {
    class Success<out T>(data: T) : Resource<T>(data)
    class Error<out T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<out T>(data: T? = null) : Resource<T>(data)
}