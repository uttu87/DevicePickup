package com.ecoatm.devicepickup.repository.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.ecoatm.devicepickup.getOrAwaitValue
import com.ecoatm.devicepickup.model.ShippingBoxModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@SmallTest
@ExperimentalCoroutinesApi
@HiltAndroidTest
class ShippingBoxDaoTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("testDatabase")
    lateinit var database: DevicePickupDatabase
    private lateinit var shippingBoxDao: ShippingBoxDao

    @Before
    fun setup() {
        hiltRule.inject()
        shippingBoxDao = database.shippingBoxDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertShippingBoxTesting() = runTest {
        val userId = "Test"
        val exampleBox = ShippingBoxModel(shippingBoxId = 1,
        trackingNumber = "12345",
        shippingProvider = "UPS",
        numberOfDevices = 5,
        userId = userId)

        shippingBoxDao.insertShippingBox(exampleBox)

        val list = shippingBoxDao.observeShippingBoxes(userId).getOrAwaitValue()

        assertTrue(list.contains(exampleBox))
    }

    @Test
    fun deleteShippingBoxTesting() = runTest {
        val userId = "Test"
        val exampleBox = ShippingBoxModel(shippingBoxId = 1,
            trackingNumber = "12345",
            shippingProvider = "UPS",
            numberOfDevices = 5,
            userId = userId)

        shippingBoxDao.insertShippingBox(exampleBox)

        var list = shippingBoxDao.observeShippingBoxes(userId).getOrAwaitValue()

        assertTrue(list.contains(exampleBox))

        shippingBoxDao.deleteShippingBox(exampleBox)
        list = shippingBoxDao.observeShippingBoxes(userId).getOrAwaitValue()

        assertFalse(list.contains(exampleBox))
    }

    @Test
    fun observeShippingBoxesTesting() = runTest {
        val userId = "userId"
        val exampleBox1 = ShippingBoxModel(shippingBoxId = 1,
            trackingNumber = "12345",
            shippingProvider = "UPS",
            numberOfDevices = 5,
            userId = userId)
        val exampleBox2 = ShippingBoxModel(shippingBoxId = 1,
            trackingNumber = "1234522",
            shippingProvider = "UPS",
            numberOfDevices = 1,
            userId = userId)

        shippingBoxDao.insertShippingBox(exampleBox1)
        shippingBoxDao.insertShippingBox(exampleBox2)

        var list = shippingBoxDao.observeShippingBoxes(userId).getOrAwaitValue()
        assertEquals(2, list.size)

        val differentUserId = "userId2"
        list = shippingBoxDao.observeShippingBoxes(differentUserId).getOrAwaitValue()
        assertEquals(0, list.size)
    }

}