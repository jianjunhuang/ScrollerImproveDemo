package xyz.juncat.scrollerimprove

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ViewPager2WithRecyclerViewFragment : BaseConfigureFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return (super.onCreateView(inflater, container, savedInstanceState) as LinearLayout).also { container ->

            val tabLayout = TabLayout(requireContext())
            container.addView(
                tabLayout,
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            )

            val viewPager2 = ViewPager2(requireContext()).apply {
                adapter = object : FragmentStateAdapter(this@ViewPager2WithRecyclerViewFragment) {
                    override fun getItemCount(): Int {
                        return 3
                    }

                    override fun createFragment(position: Int): Fragment {
                        return SubFragment()
                    }
                }
            }
            container.addView(
                viewPager2,
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f)
            )

            TabLayoutMediator(
                tabLayout,
                viewPager2
            ) { tab, pos ->
                tab.text = pos.toString()
            }.attach()
        }
    }

}