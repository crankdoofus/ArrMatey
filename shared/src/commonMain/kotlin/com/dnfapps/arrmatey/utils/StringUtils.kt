package com.dnfapps.arrmatey.utils

import androidx.compose.runtime.Composable
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc
import org.koin.compose.getKoin
import org.koin.compose.koinInject

interface StringResolver {
    fun resolve(stringDesc: StringDesc): String
}

expect fun createResolver(): StringResolver

class MokoStrings {
    private val resolver: StringResolver = createResolver()

    fun getString(resource: StringResource): String {
        val desc = StringDesc.Resource(resource)
        return resolver.resolve(desc)
    }
}

@Composable
fun mokoString(resource: StringResource): String {
    val moko: MokoStrings = koinInject()
    return moko.getString(resource)
}