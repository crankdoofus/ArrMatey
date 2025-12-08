package com.dnfapps.arrmatey

import android.os.Build
import com.dnfapps.arrmatey.shared.BuildConfig

//class AndroidPlatform : Platform {
//    override val name: String = "Android ${Build.VERSION.SDK_INT}"
//}
//
//actual fun getPlatform(): Platform = AndroidPlatform()

actual fun isDebug() = BuildConfig.DEBUG