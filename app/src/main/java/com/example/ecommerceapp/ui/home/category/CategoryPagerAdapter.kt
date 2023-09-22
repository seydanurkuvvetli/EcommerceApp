package com.example.ecommerceapp.ui.home.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ecommerceapp.common.Constans.Endpoint.CATEGORY
import com.example.ecommerceapp.common.Constans.Endpoint.GET_CATEGORİES

class CategoryPagerAdapter(fragment:Fragment, private val categories: List<String>):FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int=categories.size

    override fun createFragment(position: Int): Fragment {
        val fragment=CategoryFragment()
        fragment.arguments= Bundle().apply {
            putString(CATEGORY,categories[position])
        }
return fragment
    }

}