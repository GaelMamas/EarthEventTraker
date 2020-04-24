package com.villejuif.eartheventtraker.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.villejuif.eartheventtraker.network.EonetEvent
import com.villejuif.eartheventtraker.network.EonetGeometry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class EonetEventDaoTest {
    private lateinit var database: EonetEventDatabase

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(getApplicationContext(),
            EonetEventDatabase::class.java).build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertEonetEventAndGetItById() = runBlockingTest {
        val geometries = listOf(EonetGeometry("Today", coordinates = listOf(1.1f,2.2f)))

        val eonetEvent = EonetEvent("id test", "Test Database", categories = emptyList(), geometries = geometries)

        database.eonetEventDao().insertEvent(eonetEvent)

        val loaded = database.eonetEventDao().getEventById(eonetEvent.id)

        assertThat(loaded as EonetEvent, notNullValue())
        assertThat(loaded.geometries, `is`(geometries))

    }

}