package org.smalllibs.actor

import org.smalllibs.actor.impl.ActorImpl

interface ActorExecution {

    fun manage(actor: ActorImpl<*>)

    fun notifyActorTurn(actor: ActorImpl<*>)

}