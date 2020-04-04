package com.villejuif.eartheventtraker.data

import com.google.common.truth.Truth.assertThat
import com.villejuif.eartheventtraker.network.EonetEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DefaultEonetEventRepositoryTest {

    private val eonetEvent1 = EonetEvent(id = "id1", title = "T1")
    private val eonetEvent2 = EonetEvent(id = "id2", title = "T2")
    private val eonetEvent3 = EonetEvent(id = "id3", title = "T3")

    private val newEonetEvent = EonetEvent(id = "id new", title = "Title new")

    private val remoteEvents = listOf(eonetEvent1, eonetEvent2).sortedBy { it.id }
    private val localEvents = listOf(eonetEvent3).sortedBy { it.id }
    private val newEvents = listOf(newEonetEvent).sortedBy { it.id }

    private lateinit var remoteSource:FakeDataSource
    private lateinit var localSource:FakeDataSource


    private lateinit var defaultRepository: DefaultEonetEventRepository


    @Before
    fun createRepository(){
        remoteSource = FakeDataSource(remoteEvents.toMutableList())
        localSource = FakeDataSource(localEvents.toMutableList())

        //defaultRepository = DefaultEonetEventRepository(remoteSource, localSource)
    }

    @Test
    fun getEvents_emptyRepositoryAndUninitializedCache() = runBlockingTest {
        val emptySource = FakeDataSource()
        val eventsRepository = DefaultEonetEventRepository(
            emptySource, emptySource)

        assertThat(eventsRepository.getEonetEvents() is Result.Success).isTrue()
    }

    @Test
    fun getEvents_repositoryCachesAfterFirstApiCall() = runBlockingTest {
        val initial = defaultRepository.getEonetEvents()

        remoteSource.events = newEvents.toMutableList()

        val second = defaultRepository.getEonetEvents()

        assertThat(second).isEqualTo(initial)
    }

    @Test
    fun getEvents_requestAllEventsFromRemoteDataSource() = runBlockingTest {
        val events = defaultRepository.getEonetEvents() as Result.Success

        assertThat(events.data).isEqualTo(localEvents)
    }

    @Test
    fun getEvents_WithDirtyCache_EventsAreRetrievedFromRemote() = runBlockingTest {
        val events = defaultRepository.getEonetEvents()

        remoteSource.events = newEvents.toMutableList()

        val cachedEvents = defaultRepository.getEonetEvents()
        assertThat(cachedEvents).isEqualTo(events)

        val refreshEvents = defaultRepository.getEonetEvents(true) as Result.Success

        assertThat(refreshEvents.data).isEqualTo(newEvents)
    }


}