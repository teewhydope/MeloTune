package com.teewhydope.melotune.util

import com.teewhydope.melotune.BuildConfig


actual class BuildConfig {
    actual fun isDebug() = BuildConfig.DEBUG
    actual fun isAndroid() = true
}