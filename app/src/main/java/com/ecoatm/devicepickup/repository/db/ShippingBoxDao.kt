package com.ecoatm.devicepickup.repository.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ecoatm.devicepickup.model.ShippingBoxModel
import com.ecoatm.devicepickup.utils.Constants

@Dao
interface ShippingBoxDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShippingBox(box: ShippingBoxModel)

    @Delete
    suspend fun deleteShippingBox(box: ShippingBoxModel)

    @Query("SELECT * FROM " + Constants.SHIPPING_BOX_TABLE_NAME
            + " WHERE user_id=:userId ORDER BY timestamp DESC")
    fun observeShippingBoxes(userId: String) : LiveData<List<ShippingBoxModel>>

}