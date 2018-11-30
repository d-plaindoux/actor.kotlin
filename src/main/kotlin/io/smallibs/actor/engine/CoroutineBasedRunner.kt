package io.smallibs.actor.engine

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

internal class CoroutineBasedRunner : ActorRunner {

    override fun execute(run: () -> Unit) {
        GlobalScope.launch { run() }
    }


}