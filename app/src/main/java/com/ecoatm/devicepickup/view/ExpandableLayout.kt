package com.ecoatm.devicepickup.view

import android.animation.Animator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.animation.Interpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.ecoatm.devicepickup.R

class ExpandableLayout @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) :
    FrameLayout(
        context!!, attrs
    ) {
    /**
     * Expandable state.
     */
    interface State {
        companion object {
            const val COLLAPSED = 0
            const val COLLAPSING = 1
            const val EXPANDING = 2
            const val EXPANDED = 3
        }
    }
    /**
     * Get duration.
     * @return duration
     */
    /**
     * Set animate duration.
     * @param duration duration.
     */
    var duration = DEFAULT_DURATION
    private var parallax = 0f
    private var expansion = 0f
    private var orientation = 0

    /**
     * Get expansion state
     *
     * @return one of [State]
     */
    var state = 0
        private set
    private val interpolator: Interpolator = FastOutSlowInInterpolator
    private var animator: ValueAnimator? = null

    /**
     * Get listener.
     * @return OnExpansionUpdateListener
     */
    var listener: OnExpansionUpdateListener? = null
        private set
    /**
     * Constructor.
     * @param context context application.
     * @param attrs attrs.
     */
    /**
     * Constructor.
     * @param context context application.
     */
    init {
        if (attrs != null) {
            val a = getContext().obtainStyledAttributes(attrs, R.styleable.ExpandableLayout)
            duration = a.getInt(R.styleable.ExpandableLayout_el_duration, DEFAULT_DURATION)
            expansion = if (a.getBoolean(
                    R.styleable.ExpandableLayout_el_expanded,
                    false
                )
            ) 1F else 0.toFloat()
            orientation = a.getInt(R.styleable.ExpandableLayout_android_orientation, VERTICAL)
            parallax = a.getFloat(R.styleable.ExpandableLayout_el_parallax, 1f)
            a.recycle()
            state = if (expansion == 0f) State.COLLAPSED else State.EXPANDED
            setParallax(parallax)
        }
    }

    /**
     * Save instance state.
     * @return parcelable bundle.
     */
    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val bundle = Bundle()
        expansion = if (isExpanded) 1F else 0.toFloat()
        bundle.putFloat(KEY_EXPANSION, expansion)
        bundle.putParcelable(KEY_SUPER_STATE, superState)
        return bundle
    }

    /**
     * Restore instance state.
     * @param parcelable parcelable bundle.
     */
    override fun onRestoreInstanceState(parcelable: Parcelable) {
        val bundle = parcelable as Bundle
        expansion = bundle.getFloat(KEY_EXPANSION)
        state = if (expansion == 1f) State.EXPANDED else State.COLLAPSED
        val superState = bundle.getParcelable<Parcelable>(KEY_SUPER_STATE)
        super.onRestoreInstanceState(superState)
    }

    /**
     * Measure method.
     * @param widthMeasureSpec width measure spec.
     * @param heightMeasureSpec height measure spec.
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measuredWidth
        val height = measuredHeight
        val size = if (orientation == LinearLayout.HORIZONTAL) width else height
        visibility =
            if (expansion == 0f && size == 0) GONE else VISIBLE
        val expansionDelta = size - Math.round(size * expansion)
        if (parallax > 0) {
            val parallaxDelta = expansionDelta * parallax
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                if (orientation == HORIZONTAL) {
                    var direction = -1
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && layoutDirection == LAYOUT_DIRECTION_RTL) {
                        direction = 1
                    }
                    child.translationX = direction * parallaxDelta
                } else {
                    child.translationY = -parallaxDelta
                }
            }
        }
        if (orientation == HORIZONTAL) {
            setMeasuredDimension(width - expansionDelta, height)
        } else {
            setMeasuredDimension(width, height - expansionDelta)
        }
    }

    /**
     * Configuration changed method.
     * @param newConfig new configuration.
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        if (animator != null) {
            animator!!.cancel()
        }
        super.onConfigurationChanged(newConfig)
    }

    /**
     * Get state.
     * @return true/false.
     */
    val isExpanded: Boolean
        get() = state == State.EXPANDING || state == State.EXPANDED
    /**
     * Set animation.
     * @param animate animate.
     */
    /**
     * Set toggle to true.
     */
    @JvmOverloads
    fun toggle(animate: Boolean = true) {
        if (isExpanded) {
            collapse(animate)
        } else {
            expand(animate)
        }
    }
    /**
     * Set expand animation.
     * @param animate animate.
     */
    /**
     * Set expand animation.
     */
    @JvmOverloads
    fun expand(animate: Boolean = true) {
        setExpanded(true, animate)
    }
    /**
     * Set collapse animation.
     * @param animate animation.
     */
    /**
     * Set collapse animation to true.
     */
    @JvmOverloads
    fun collapse(animate: Boolean = true) {
        setExpanded(false, animate)
    }

    /**
     * Set expanded.
     * @param expand expand.
     * @param animate animation.
     */
    fun setExpanded(expand: Boolean, animate: Boolean) {
        if (expand == isExpanded) {
            return
        }
        val targetExpansion = if (expand) 1 else 0
        if (animate) {
            animateSize(targetExpansion)
        } else {
            setExpansion(targetExpansion.toFloat())
        }
    }

    /**
     * Set expansion.
     * @param expansion expansion.
     */
    fun setExpansion(expansion: Float) {
        if (this.expansion == expansion) {
            return
        }

        // Infer state from previous value
        val delta = expansion - this.expansion
        if (expansion == 0f) {
            state = State.COLLAPSED
        } else if (expansion == 1f) {
            state = State.EXPANDED
        } else if (delta < 0) {
            state = State.COLLAPSING
        } else if (delta > 0) {
            state = State.EXPANDING
        }
        visibility =
            if (state == State.COLLAPSED) GONE else VISIBLE
        this.expansion = expansion
        requestLayout()
        if (listener != null) {
            listener!!.onExpansionUpdate(expansion, state)
        }
    }

    /**
     * Set parallax.
     * @param parallax parallax
     */
    fun setParallax(parallax: Float) {
        // Make sure parallax is between 0 and 1
        var parallax = parallax
        parallax = Math.min(1f, Math.max(0f, parallax))
        this.parallax = parallax
    }

    /**
     * Get parallax.
     * @return parallax.
     */
    fun getParallax(): Float {
        return parallax
    }

    /**
     * Get orientation.
     * @return orientation.
     */
    fun getOrientation(): Int {
        return orientation
    }

    /**
     * Set orientation.
     * @param orientation orientation.
     */
    fun setOrientation(orientation: Int) {
        require(!(orientation < 0 || orientation > 1)) { "Orientation must be either 0 (horizontal) or 1 (vertical)" }
        this.orientation = orientation
    }

    /**
     * Set on expansion update listener.
     * @param listener listener.
     */
    fun setOnExpansionUpdateListener(listener: OnExpansionUpdateListener?) {
        this.listener = listener
    }

    /**
     * Animation size.
     * @param targetExpansion target expansion.
     */
    private fun animateSize(targetExpansion: Int) {
        if (animator != null) {
            animator!!.cancel()
            animator = null
        }
        animator = ValueAnimator.ofFloat(expansion, targetExpansion.toFloat())
        animator?.apply {
            interpolator = this@ExpandableLayout.interpolator
            duration = this@ExpandableLayout.duration.toLong()
            addUpdateListener { valueAnimator: ValueAnimator ->
                setExpansion(
                    valueAnimator.animatedValue as Float
                )
            }
            addListener(ExpansionListener(targetExpansion))
            start()
        }
    }

    /**
     * Expansion Update Listener.
     */
    interface OnExpansionUpdateListener {
        /**
         * Callback for expansion updates
         *
         * @param expansionFraction Value between 0 (collapsed) and 1 (expanded) representing the the expansion progress
         * @param state             One of [State] repesenting the current expansion state
         */
        fun onExpansionUpdate(expansionFraction: Float, state: Int)
    }

    /**
     * Animate Expansion Listener.
     */
    inner class ExpansionListener
    /**
     * Constructor.
     * @param targetExpansion target expansion.
     */(private val targetExpansion: Int) : Animator.AnimatorListener {
        private var canceled = false

        /**
         * On Animation Start method.
         * @param animation animation.
         */
        override fun onAnimationStart(animation: Animator) {
            state = if (targetExpansion == 0) State.COLLAPSING else State.EXPANDING
        }

        /**
         * On Animation End method.
         * @param animation animation.
         */
        override fun onAnimationEnd(animation: Animator) {
            if (!canceled) {
                state = if (targetExpansion == 0) State.COLLAPSED else State.EXPANDED
                setExpansion(targetExpansion.toFloat())
            }
        }

        /**
         * On Animation cancel method.
         * @param animation animation
         */
        override fun onAnimationCancel(animation: Animator) {
            canceled = true
        }

        /**
         * On Animation repeat method.
         * @param animation animation.
         */
        override fun onAnimationRepeat(animation: Animator) {}
    }

    companion object {
        const val KEY_SUPER_STATE = "super_state"
        const val KEY_EXPANSION = "expansion"
        const val HORIZONTAL = 0
        const val VERTICAL = 1
        private const val DEFAULT_DURATION = 300
    }
}