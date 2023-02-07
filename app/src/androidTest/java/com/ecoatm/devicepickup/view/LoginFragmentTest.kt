package com.ecoatm.devicepickup.view

import androidx.fragment.app.FragmentFactory
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.MediumTest
import com.ecoatm.devicepickup.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import com.ecoatm.devicepickup.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito


@MediumTest
@HiltAndroidTest
class LoginFragmentTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun testNavigationFromLoginToDevicePickup() = runTest{
        val navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer <LoginFragment> (factory = FragmentFactory()){
            Navigation.setViewNavController(requireView(), navController)
        }

        Espresso.onView(ViewMatchers.withId(R.id.userIdEditText)).perform(ViewActions.replaceText("userId"))
        Espresso.onView(ViewMatchers.withId(R.id.pinEditText)).perform(ViewActions.replaceText("userPIN"))

        Espresso.onView(ViewMatchers.withId(R.id.btnGo)).perform(ViewActions.click())

        runBlocking {
            delay(5000)
        }

        Mockito.verify(navController).navigate(
            LoginFragmentDirections.actionLoginFragmentToDevicePickupFragment()
        )
    }
}