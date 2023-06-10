package xyz.juncat.scrollerimprove

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SubFragment : Fragment() {

    private val viewModel: SubFragmentViewModel by lazy {
        ViewModelProvider(requireActivity())[SubFragmentViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FrameLayout(requireContext()).apply {
            val recyclerView = SwipeAwareRecyclerView(requireContext()).also {
                it.debug = true
                it.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

                    private val TYPE_SUBLIST = 0
                    private val TYPE_NORMAL = 1
                    override fun getItemViewType(position: Int): Int {
                        return if (position == 0) {
                            TYPE_SUBLIST
                        } else {
                            TYPE_NORMAL
                        }
                    }

                    override fun onCreateViewHolder(
                        parent: ViewGroup,
                        viewType: Int
                    ): RecyclerView.ViewHolder {
                        if (viewType == TYPE_SUBLIST) {
                            return ItemRvHolder.create(parent)
                        }
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

                    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                        (holder.itemView as? TextView)?.text = position.toString()
                    }

                }
                it.layoutManager =
                    SwipeAwareRecyclerView.SwipeAwareLinearLayoutManager(requireContext())
            }
            addView(
                recyclerView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            viewModel.swipeFast.observe(viewLifecycleOwner) {
                recyclerView.isSwipeFast = it
            }
            viewModel.swipeFast.observe(viewLifecycleOwner) {
                recyclerView.isDispatch = it
            }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    class ItemRvHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        companion object {
            fun create(parent: ViewGroup): ItemRvHolder {
                return ItemRvHolder(LinearLayout(parent.context).also { lv ->
                    fun div(): View =
                        View(lv.context).apply {
                            layoutParams =
                                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1)
                            setBackgroundColor(Color.BLACK)
                        }
                    lv.orientation = LinearLayout.VERTICAL
                    lv.addView(div())
                    lv.addView(RecyclerView(parent.context).apply {
                        layoutParams =
                            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300)
                        layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                        adapter = object : RecyclerView.Adapter<ViewHolder>() {
                            override fun onCreateViewHolder(
                                parent: ViewGroup,
                                viewType: Int
                            ): ViewHolder {
                                return ViewHolder(TextView(context).apply {
                                    layoutParams =
                                        ViewGroup.LayoutParams(300, 300)
                                    setTextColor(Color.BLACK)
                                    gravity = Gravity.CENTER
                                })
                            }

                            override fun getItemCount(): Int {
                                return 10
                            }

                            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                                (holder.itemView as? TextView)?.text = position.toString()
                            }

                        }
                    })

                    lv.addView(div())
                    lv.layoutParams =
                        ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 302)
                })
            }
        }
    }

    class SubFragmentViewModel : ViewModel() {

        val swipeFast = MutableLiveData<Boolean>()
        val dispatch = MutableLiveData<Boolean>()

    }
}
