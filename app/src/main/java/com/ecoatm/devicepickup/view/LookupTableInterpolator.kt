package com.ecoatm.devicepickup.view

import android.view.animation.Interpolator

abstract class LookupTableInterpolator(private val mValues: FloatArray) : Interpolator {
    private val mStepSize: Float

    /**
     * Constructor.
     * @param values values.
     */
    init {
        mStepSize = 1f / (mValues.size - 1)
    }

    /**
     * Get interpolation.
     * @param input input
     * @return
     */
    override fun getInterpolation(input: Float): Float {
        if (input >= 1.0f) {
            return 1.0f
        }
        if (input <= 0f) {
            return 0f
        }

        // Calculate index - We use min with length - 2 to avoid IndexOutOfBoundsException when
        // we lerp (linearly interpolate) in the return statement
        val position = Math.min((input * (mValues.size - 1)).toInt(), mValues.size - 2)

        // Calculate values to account for small offsets as the lookup table has discrete values
        val quantized = position * mStepSize
        val diff = input - quantized
        val weight = diff / mStepSize

        // Linearly interpolate between the table values
        return mValues[position] + weight * (mValues[position + 1] - mValues[position])
    }
}