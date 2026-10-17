package com.example.couriertracker.util

import android.content.Context
import android.os.Build

fun Context.getAppVersion(): String {
    val info = packageManager.getPackageInfo(packageName, 0)

    val versionCode = if (Build.VERSION.SDK_INT >= 28) {
        info.longVersionCode
    } else {
        @Suppress("DEPRECATION")
        info.versionCode.toLong()
    }

    return "${info.versionName} ($versionCode)"
}