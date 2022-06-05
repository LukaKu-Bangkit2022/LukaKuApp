package com.bangkit.capstone.lukaku.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bangkit.capstone.lukaku.ui.profile.ProfileFragment.Companion.TAB_TITLES_LOCAL_PROFILE
import com.bangkit.capstone.lukaku.ui.profile.bookmarks.BookmarksFragment
import com.bangkit.capstone.lukaku.ui.profile.history.HistoryFragment

class LocalProfilePagerAdapter(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = TAB_TITLES_LOCAL_PROFILE.size

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = HistoryFragment()
            1 -> fragment = BookmarksFragment()
        }
        return fragment as Fragment
    }
}