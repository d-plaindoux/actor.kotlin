package io.smallibs.aktor

interface  ActorBuilder {

    infix fun <R> actorFor(property: ActorProperty<R>): ActorReference<R> =
        property install this

    infix fun <R> actorFor(protocolReceiver: ProtocolReceiver<R>): ActorReference<R> =
        actorFor(protocolReceiver, Names.generate())

    fun <R> actorFor(protocol: ProtocolReceiver<R>, name: String): ActorReference<R> =
        actorFor(Behavior of protocol, name)

    infix fun <R> actorFor(behavior: Behavior<R>): ActorReference<R> =
        actorFor(behavior, Names.generate())

    fun <R> actorFor(behavior: Behavior<R>, name: String): ActorReference<R>

}
