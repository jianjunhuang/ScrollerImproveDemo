package xyz.juncat.scrollerimprove

import android.content.Context
import android.graphics.Color
import android.media.MediaCodecList
import android.media.MediaFormat
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        val viewPager2 = findViewById<ViewPager2>(R.id.view_pager)
        viewPager2.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return 3
            }

            override fun createFragment(position: Int): Fragment {
                return SubFragment()
            }

        }

        TabLayoutMediator(
            tabLayout,
            viewPager2,
            TabLayoutMediator.TabConfigurationStrategy { tab, pos ->
                tab.setText(pos.toString())
            }).attach()
    }


    class SubFragment : Fragment() {

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return FrameLayout(requireContext()).apply {
                addView(CustomRecyclerView(requireContext()).also {
                    it.adapter = object : RecyclerView.Adapter<ViewHolder>() {
                        override fun onCreateViewHolder(
                            parent: ViewGroup,
                            viewType: Int
                        ): ViewHolder {
                            return ViewHolder(TextView(context).apply {
                                layoutParams =
                                    ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 112)
                                setTextColor(Color.BLACK)
                                gravity = Gravity.CENTER
                            })
                        }

                        override fun getItemCount(): Int {
                            return 1000
                        }

                        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                            (holder.itemView as? TextView)?.text = position.toString()
                        }

                    }
                    it.layoutManager = LinearLayoutManager(requireContext())
                }, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    class CustomRecyclerView(context: Context) : RecyclerView(context) {

        private var disallowIntercept = false

        private var startX = 0
        private var startY = 0
        var isDispatch: Boolean = true
        override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
            if (isDispatch) {
                when (ev.action) {
                    MotionEvent.ACTION_DOWN -> {
                        startX = ev.x.toInt()
                        startY = ev.y.toInt()
                        parent.requestDisallowInterceptTouchEvent(true)
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val endX = ev.x.toInt()
                        val endY = ev.y.toInt()
                        val disX = kotlin.math.abs(endX - startX)
                        val disY = kotlin.math.abs(endY - startY)
                        Log.i(
                            TAG,
                            "dispatchTouchEvent: disX: $disX, dexY: $disY, disallowIntercept: $disallowIntercept"
                        )
                        if (disX > disY) {
                            //check Horizontal scroll RecyclerView in RecyclerView
                            if (disallowIntercept) {
                                parent.requestDisallowInterceptTouchEvent(disallowIntercept)
                            } else {
                                val canScroll = canScrollHorizontally(
                                    startX - endX
                                )
                                Log.i(TAG, "dispatchTouchEvent: can scroll: $canScroll")
                                parent.requestDisallowInterceptTouchEvent(canScroll)
                                //key code for swipe by ViewPager2 quick
                                return false
                            }
                        } else {
                            parent.requestDisallowInterceptTouchEvent(true)
                        }
                    }

                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> parent.requestDisallowInterceptTouchEvent(
                        false
                    )
                }
            }

            return super.dispatchTouchEvent(ev)
        }

        override fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
            this.disallowIntercept = disallowIntercept
            super.requestDisallowInterceptTouchEvent(disallowIntercept)

        }

    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
