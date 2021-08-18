package com.janus.aprendiendonumeros.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SignUpViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private lateinit var listFragment: List<Fragment>

    fun setListFragment(list: List<Fragment>) {
        listFragment = list
    }

    fun getFragment(index: Int): Fragment {
        return listFragment[index]
    }

    override fun getItemCount(): Int {
        return listFragment.size
    }

    override fun createFragment(position: Int): Fragment {
        return listFragment[position]
    }
}