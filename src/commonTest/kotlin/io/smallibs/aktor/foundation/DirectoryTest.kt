package io.smallibs.aktor.foundation

import io.smallibs.aktor.Aktor
import io.smallibs.aktor.ProtocolBehavior
import io.smallibs.aktor.core.Core
import io.smallibs.aktor.foundation.Directory.tryFound
import io.smallibs.utils.Await
import io.smallibs.utils.TimeOutException
import kotlinx.atomicfu.atomic
import kotlin.test.Test
import kotlin.test.assertFailsWith

class DirectoryTest {

    object TestActor {
        interface Protocol

        val receiver: ProtocolBehavior<Protocol> = { a, _ -> a.same() }
    }

    @Test
    fun shouldRetrieveARegisteredActor() {
        val site = Aktor.new("site")
        val directory = Directory from site

        directory register (site actorFor TestActor.receiver)

        val atomic = atomic(false)
        directory find (site actorFor tryFound<TestActor.Protocol>({ atomic.getAndSet(true) }))

        Await(5000).until { atomic.value }
    }

    @Test
    fun shouldRetrieveARegisteredActorUsingAlsoItsName() {
        val site = Aktor.new("site")
        val directory = Directory from site

        directory register site.actorFor(TestActor.receiver, "test")

        val atomic = atomic(false)
        directory.find("test", site actorFor tryFound<TestActor.Protocol>({ atomic.getAndSet(true) }))

        Await(5000).until { atomic.value }
    }

    @Test
    fun shouldNotRetrieveARegisteredActorUsingAWrongName() {
        val site = Aktor.new("site")
        val directory = Directory from site

        directory register (site actorFor TestActor.receiver)

        val atomic = atomic(false)
        directory.find("dummy", site actorFor tryFound<TestActor.Protocol>({}, { atomic.getAndSet(true) }))

        Await(5000).until { atomic.value }
    }

    @Test
    fun shouldUnregisterWhenKillingRegisteredActor() {
        val site = Aktor.new("site")
        val directory = Directory from site

        val test = site actorFor TestActor.receiver

        directory register test

        val atomic = atomic(false)
        directory find (site actorFor tryFound<TestActor.Protocol>({ atomic.getAndSet(true) }))

        Await(5000).until { atomic.value }

        test tell Core.Kill

        atomic.getAndSet(false)
        directory find (site actorFor tryFound<TestActor.Protocol>({}, { atomic.getAndSet(true) }))

        Await(5000).until { atomic.value }
    }

    @Test
    fun shouldNotRetrieveAnUnregisteredActor() {
        val site = Aktor.new("site")

        val directory = Directory from site

        val atomic = atomic(false)
        directory find (site actorFor tryFound<Directory.Protocol>({ atomic.getAndSet(true) }))

        assertFailsWith<TimeOutException> { Await(5000).until { atomic.value } }
    }

    @Test
    fun shouldNotRetrieveAnRegisteredAndThenUnregisteredActor() {
        val site = Aktor.new("site")
        val directory = Directory from site

        val test = site actorFor TestActor.receiver

        directory register test
        val atomic = atomic(false)
        directory find (site actorFor tryFound<TestActor.Protocol>({ atomic.getAndSet(true) }))

        Await(5000).until { atomic.value }

        directory unregister test

        atomic.getAndSet(false)
        directory find (site actorFor tryFound<TestActor.Protocol>({}, { atomic.getAndSet(true) }))

        Await(5000).until { atomic.value }
    }
}