package com.teewhydope.melotune

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}