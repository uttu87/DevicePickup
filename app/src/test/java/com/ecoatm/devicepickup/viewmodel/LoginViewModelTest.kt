package com.ecoatm.devicepickup.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ecoatm.devicepickup.MainCoroutineRule
import com.ecoatm.devicepickup.getOrAwaitValueTest
import com.ecoatm.devicepickup.repository.FakeRepository
import com.ecoatm.devicepickup.repository.api.RetrofitAPI
import com.ecoatm.devicepickup.utils.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matchers
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: LoginViewModel
    private lateinit var repository: FakeRepository

    @Before
    fun setup() {
        //Test Double
        repository = FakeRepository()
        viewModel = LoginViewModel(repository)
    }

    @Test
    fun `login without userId and userPIN return error`() {
        viewModel.goPressed()

        val value = viewModel.loginStatus.getOrAwaitValueTest()

        assertEquals(Status.ERROR, value.status)
    }

    @Test
    fun `login with userPIN return error`() {
        viewModel.userId.value = "11111"
        viewModel.goPressed()

        val value = viewModel.loginStatus.getOrAwaitValueTest()

        assertEquals(Status.ERROR, value.status)
    }

    @Test
    fun `login with userId return error`() {
        viewModel.userPIN.value = "11111"
        viewModel.goPressed()

        val value = viewModel.loginStatus.getOrAwaitValueTest()

        assertEquals(Status.ERROR, value.status)
    }

    @Test
    fun `login with userId and userPIN return success`() {
        viewModel.userId.value = "11111"
        viewModel.userPIN.value = "11111"
        viewModel.goPressed()

        val value = viewModel.loginStatus.getOrAwaitValueTest()

        assertEquals(Status.SUCCESS, value.status)
    }
}