package xyz.juncat.scrollerimprove

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
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
            val recyclerView = CombineWithViewPager2RecyclerView(requireContext()).also {
                it.debug = true
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

    class SubFragmentViewModel : ViewModel() {

        val swipeFast = MutableLiveData<Boolean>()
        val dispatch = MutableLiveData<Boolean>()

    }
}
