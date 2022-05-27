package com.furkan.sockettest

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings

object Utils {
    @SuppressLint("HardwareIds")
    fun getAndroidId(context: Context): String {
        var androidId = "-1"
        androidId = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        return androidId
    }

    fun getBuildModel(): String {
        return Build.MODEL
    }
}