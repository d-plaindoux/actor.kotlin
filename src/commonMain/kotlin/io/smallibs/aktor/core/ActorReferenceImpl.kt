package io.smallibs.aktor.core

import io.smallibs.aktor.ActorReference
import io.smallibs.aktor.Behavior
import io.smallibs.aktor.Envelop
import io.smallibs.aktor.engine.ActorDispatcher

data class ActorReferenceImpl<T>(
    val dispatcher: ActorDispatcher,
    override val address: ActorAddressImpl
) : ActorReference<T> {
    override fun tell(envelop: Envelop<T>) {
        this.dispatcher.deliver(this, envelop)
    }

    internal fun <R> register(behavior: Behavior<R>, name: String): ActorReference<R> =
        dispatcher.register(newChild(name), behavior).context.self

    internal fun <R> unregister(reference: ActorReferenceImpl<R>): Boolean =
        dispatcher.unregister(reference)

    private fun <R> newChild(name: String): ActorReferenceImpl<R> =
        ActorReferenceImpl(dispatcher, this.address.newChild(name))

    override fun toString(): String {
        return "ActorReference($address)"
    }
}
