package com.villejuif.eartheventtraker.events

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.villejuif.eartheventtraker.MainCoroutineRule
import com.villejuif.eartheventtraker.data.FakeEonetRepository
import com.villejuif.eartheventtraker.getOrAwaitValue
import com.villejuif.eartheventtraker.network.EonetEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class EarthEventsViewModelTest {

    private lateinit var viewModel: EarthEventsViewModel

    private lateinit var defaultRepository: FakeEonetRepository

    private val eonetEvent1 = EonetEvent(id = "id1", title = "T1")

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
    fun status_allTheStatuses() {

        assertThat(highlightApiStatus(true)).isEqualTo(EarthEventsStatus.ERROR)
        assertThat(highlightApiStatus(false)).isEqualTo(EarthEventsStatus.ERROR)

        defaultRepository.addEvents(eonetEvent1)

        assertThat(highlightApiStatus(false)).isEqualTo(EarthEventsStatus.DONE)

        assertThat(viewModel.items.getOrAwaitValue()[0]).isEqualTo(eonetEvent1)
    }

    private fun highlightApiStatus(error : Boolean) : EarthEventsStatus{
        defaultRepository.setReturnError(error)

        viewModel.items.getOrAwaitValue()

        return viewModel.status.getOrAwaitValue()
    }




}