package com.villejuif.eartheventtraker.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.villejuif.eartheventtraker.data.Result
import com.villejuif.eartheventtraker.data.succeeded
import com.villejuif.eartheventtraker.network.EonetEvent
import com.villejuif.eartheventtraker.network.EonetGeometry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class EonetLocalSourceTest {

    private lateinit var localSource: EonetEventsLocalSource
    private lateinit var database: EonetEventDatabase

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            EonetEventDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        localSource = EonetEventsLocalSource(database.eonetEventDao(), Dispatchers.IO)
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun saveEonetEvent_AndRetrieveIt() = runBlocking {
        val geometries = listOf(EonetGeometry("Today", coordinates = listOf(1.1f,2.2f)))

        val eonetEvent = EonetEvent("id test", "Test Database", categories = emptyList(), geometries = geometries)

        localSource.saveEonetEvent(eonetEvent)

        val retrieved = localSource.getEonetEvent(eonetEvent.id)

        assertThat(retrieved.succeeded, `is`(true))
        assertThat((retrieved as Result.Success).data.geometries, `is`(geometries))

    }
}