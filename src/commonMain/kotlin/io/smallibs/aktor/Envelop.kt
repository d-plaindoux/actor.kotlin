package io.smallibs.aktor

import io.smallibs.aktor.system.SystemMessage

sealed class Envelop<T>
data class ProtocolEnvelop<T>(val content: T) : Envelop<T>()
data class SystemEnvelop<T>(val content: SystemMessage) : Envelop<T>()
