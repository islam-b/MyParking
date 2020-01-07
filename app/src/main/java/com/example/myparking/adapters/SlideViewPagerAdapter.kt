package com.example.myparking.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import android.view.View
import com.example.myparking.utils.SlideObject

class SlideViewPagerAdapter(val layouts : Array<SlideObject>, private var context: Context) : PagerAdapter() {

    private val inflater =  LayoutInflater.from(context)

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val slideObject = layouts[position]
        val layout = inflater.inflate(slideObject.layoutResId,container,false)
        container.addView(layout)
        return layout
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return SlideObject.values().size
    }
}
