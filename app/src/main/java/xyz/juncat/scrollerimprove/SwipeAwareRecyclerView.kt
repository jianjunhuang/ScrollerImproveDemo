package xyz.juncat.scrollerimprove

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SwipeAwareRecyclerView : RecyclerView {

    var debug = false

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, style: Int) : super(
        context,
        attributeSet,
        style
    )

    private var disallowIntercept = false

    private var startX = 0
    private var startY = 0

    var isDispatch: Boolean = true
    var isSwipeFast: Boolean = false
    private var triggerSwipeFast = false
    private var nestedTargetView: View? = null

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        triggerSwipeFast = false
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
                    log(
                        "dispatchTouchEvent: disX: $disX, dexY: $disY, disallowIntercept: $disallowIntercept"
                    )
                    if (disX > disY) {
                        //check Horizontal scroll RecyclerView in RecyclerView
                        if (disallowIntercept) {
                            val canScroll = nestedTargetView?.canScrollHorizontally(
                                startX - endX
                            )
                            log("can child scroll -> $canScroll")
                            if (canScroll == true) {
                                parent.requestDisallowInterceptTouchEvent(disallowIntercept)
                            } else {
                                parent.requestDisallowInterceptTouchEvent(false)
                            }
                        } else {
                            val canScroll = canScrollHorizontally(
                                startX - endX
                            )
                            log("dispatchTouchEvent: can scroll: $canScroll")
                            //conflict with [NestedScrollableHost]
                            parent.requestDisallowInterceptTouchEvent(canScroll)
                            //key code for swipe by ViewPager2 quick
                            if (isSwipeFast) {
                                triggerSwipeFast = true
                            }
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


    override fun onTouchEvent(e: MotionEvent?): Boolean {
        log("${triggerSwipeFast}, onTouchEvent: $e ")
        if (triggerSwipeFast) {
            //stop fling to trigger ViewPager2 swipe
            (layoutManager as? Scrollable)?.isScrollEnable = false
        }
        return super.onTouchEvent(e).apply {
            (layoutManager as? Scrollable)?.isScrollEnable = true
        }
    }

    override fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        this.disallowIntercept = disallowIntercept
        super.requestDisallowInterceptTouchEvent(disallowIntercept)

    }

    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        log("onStartNestedScroll: $nestedScrollAxes")
        nestedTargetView = target
        return super.onStartNestedScroll(child, target, nestedScrollAxes)
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int
    ) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
        log("onNestedScroll: $dxConsumed, $dxUnconsumed")
        disallowIntercept = false
        target.parent.requestDisallowInterceptTouchEvent(false)
    }

    override fun onStopNestedScroll(child: View) {
        super.onStopNestedScroll(child)
        nestedTargetView = null
        log("onStopNestedScroll")
    }

    private fun log(info: String) {
        if (debug)
            Log.i(TAG, info)
    }

    class SwipeAwareLinearLayoutManager : LinearLayoutManager, Scrollable {
        override var isScrollEnable = true

        constructor(context: Context) : super(context)

        override fun canScrollVertically(): Boolean {
            return super.canScrollVertically() && isScrollEnable
        }

        override fun canScrollHorizontally(): Boolean {
            return super.canScrollHorizontally() && isScrollEnable
        }

    }

    class SwipeAwareGridLayoutManager(context: Context, spanCount: Int) :
        GridLayoutManager(context, spanCount), Scrollable {
        override var isScrollEnable = true

        override fun canScrollVertically(): Boolean {
            return super.canScrollVertically() && isScrollEnable
        }

        override fun canScrollHorizontally(): Boolean {
            return super.canScrollHorizontally() && isScrollEnable
        }

    }

    interface Scrollable {
        var isScrollEnable: Boolean

    }

    companion object {
        private const val TAG = "CombineWithViewPager2RecyclerView"
    }

}