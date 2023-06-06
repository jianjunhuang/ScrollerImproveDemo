package xyz.juncat.scrollerimprove

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

open class BaseConfigureFragment : Fragment() {

    protected val viewModel: SubFragment.SubFragmentViewModel by lazy {
        ViewModelProvider(requireActivity())[SubFragment.SubFragmentViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return LinearLayout(requireContext()).also { container ->
            container.setBackgroundColor(Color.WHITE)
            container.orientation = LinearLayout.VERTICAL
            container.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            val improveCheckBox = CheckBox(requireContext())
            improveCheckBox.text = "Swipe Fast?"
            improveCheckBox.isChecked = true
            container.addView(
                improveCheckBox,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            improveCheckBox.setOnCheckedChangeListener { _, isChecked ->
                viewModel.swipeFast.postValue(isChecked)
            }

            val dispatchCheckBox = CheckBox(requireContext())
            dispatchCheckBox.text = "RecyclerView dispatch?"
            dispatchCheckBox.isChecked = true
            container.addView(
                dispatchCheckBox,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dispatchCheckBox.setOnCheckedChangeListener { _, isChecked ->
                viewModel.dispatch.postValue(isChecked)
            }
        }
    }
}