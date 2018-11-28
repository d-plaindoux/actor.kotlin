package org.smalllibs.actor

interface ActorReference<T> {

    val address: ActorAddress

    infix fun tell(envelop: Envelop<T>)

    infix fun tell(content: T) = tell(Envelop(content))

}
