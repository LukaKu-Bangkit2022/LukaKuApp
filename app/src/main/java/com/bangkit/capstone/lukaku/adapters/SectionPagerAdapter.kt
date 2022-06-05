package com.bangkit.capstone.lukaku.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bangkit.capstone.lukaku.ui.notifications.NotificationFragment.Companion.TAB_TITLES_NOTIFICATION
import com.bangkit.capstone.lukaku.ui.notifications.messages.MessagesFragment
import com.bangkit.capstone.lukaku.ui.notifications.updates.UpdatesFragment

class SectionPagerAdapter(
    fa: FragmentActivity
) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = TAB_TITLES_NOTIFICATION.size

    override fun createFragment(
        position: Int
    ): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = UpdatesFragment()
            1 -> fragment = MessagesFragment()
        }
        return fragment as Fragment
    }
}