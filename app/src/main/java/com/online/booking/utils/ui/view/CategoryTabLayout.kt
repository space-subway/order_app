package com.online.booking.utils.ui.view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class CategoryTabLayout : TabLayout {
    private val mTitles: MutableList<String> = arrayListOf()
    private var mUnselectedTypeFace: Typeface? = null

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context?, attrs: AttributeSet?) {
    }

    override fun setupWithViewPager(viewPager: ViewPager?, autoRefresh: Boolean) {
        super.setupWithViewPager(viewPager, autoRefresh)
        addViews()
    }

    private fun addViews() {
        for (i in 0 until tabCount) {
            val tab = getTabAt(i)
            if (tab != null) {
                val customFontTextView = createCustomFontTextViewForTab()
                if (i == 0) {
                    if (mUnselectedTypeFace == null) {
                        mUnselectedTypeFace = customFontTextView.typeface
                    }
                    customFontTextView.setTypeface(customFontTextView.typeface, Typeface.BOLD)
                }
                tab.customView = customFontTextView
            }
        }
    }

    private fun createCustomFontTextViewForTab(): AppCompatTextView {
        val customFontTextView = AppCompatTextView(context)
        customFontTextView.gravity = Gravity.CENTER
        //TextViewCompat.setTextAppearance(customFontTextView, R.style.MyTitleStyle)
        return customFontTextView
    }
}
