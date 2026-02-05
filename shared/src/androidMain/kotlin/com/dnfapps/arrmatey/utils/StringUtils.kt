package com.dnfapps.arrmatey.utils

import android.content.Context
import androidx.compose.runtime.Composable
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.StringDesc
import org.koin.compose.koinInject
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class AndroidResolver: KoinComponent, StringResolver {
    private val context: Context = get()

    override fun resolve(stringDesc: StringDesc): String {
        return stringDesc.toString(context)
    }
}

actual fun createResolver(): StringResolver {
    return AndroidResolver()
}