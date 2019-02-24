package io.smallibs.aktor.core

import io.smallibs.aktor.ActorReference
import io.smallibs.aktor.CoreReceiver
import io.smallibs.aktor.ProtocolReceiver
import io.smallibs.aktor.utils.exhaustive
import io.smallibs.aktor.utils.reject

object Core {

    interface Protocol
    object Start : Protocol
    object Stop : Protocol
    data class Stopped(val reference: ActorReference<*>) : Protocol

    object Behaviors {

        val core: CoreReceiver<*> =
            { actor, message ->
                when (message.content) {
                    is Core.Stop -> {
                        actor.context.children().forEach { it tell Core.Stop }
                        actor.finish()
                        actor.context.parent()?.let { it tell Core.Stopped(actor.context.self) }
                    }
                    is Core.Stopped -> {
                        actor.context.parent()?.let { it tell message.content }
                    }
                    else -> reject
                }.exhaustive
            }

        val consume: ProtocolReceiver<*> = { _, _ -> Unit }
    }

}
