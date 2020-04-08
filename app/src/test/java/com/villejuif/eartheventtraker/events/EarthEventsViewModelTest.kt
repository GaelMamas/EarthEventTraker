package com.villejuif.eartheventtraker.events

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.base.CharMatcher.`is`
import com.google.common.truth.Truth.assertThat
import com.villejuif.eartheventtraker.MainCoroutineRule
import com.villejuif.eartheventtraker.data.FakeEonetRepository
import com.villejuif.eartheventtraker.getOrAwaitValue
import com.villejuif.eartheventtraker.network.EonetEvent
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class EarthEventsViewModelTest {

    private lateinit var viewModel: EarthEventsViewModel

    private lateinit var defaultRepository: FakeEonetRepository

    private val eonetEvent1 = EonetEvent(id = "id1", title = "T1")
    private val eonetEvent2 = EonetEvent(id = "id2", title = "T2")
    private val eonetEvent3 = EonetEvent(id = "id3", title = "T3")

    private val newEonetEvent = EonetEvent(id = "id new", title = "Title new")

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel(){
        defaultRepository = FakeEonetRepository()

        viewModel = EarthEventsViewModel(defaultRepository)
    }

    @Test
    fun status_allTheStatuses(){

        defaultRepository.setReturnError(true)

        val status = viewModel.status.getOrAwaitValue()

        assertThat(status).isEqualTo(EarthEventsStatus.ERROR)
    }




}