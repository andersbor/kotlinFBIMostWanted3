package com.example.fbimostwanted

import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fbimostwanted.databinding.FragmentFirstBinding
import com.example.fbimostwanted.models.CatalogViewModel
import com.example.fbimostwanted.models.ItemAdapter
import com.google.android.material.snackbar.Snackbar


class FirstFragment : Fragment() {
    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private val catalogViewModel: CatalogViewModel by activityViewModels()
    private lateinit var mDetector: GestureDetectorCompat

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        mDetector = GestureDetectorCompat(requireContext(), MyGestureListener())
        val rootView = binding.root
        rootView.setOnTouchListener { view, motionEvent ->
            mDetector.onTouchEvent(motionEvent)
            Log.d("APPLE", "Touch: " + motionEvent.x + " " + motionEvent.y)
            true
        }
        return rootView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        catalogViewModel.itemsLiveData.observe(viewLifecycleOwner) { catalog ->
            Log.d("APPLEPIE", catalog.toString())

            val adapter = ItemAdapter(catalog.items) { position ->
                val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(position)
                findNavController().navigate(action /*R.id.action_FirstFragment_to_SecondFragment*/)
            }
            binding.recyclerView.layoutManager = LinearLayoutManager(activity)
            binding.recyclerView.adapter = adapter
        }

        catalogViewModel.errorMessageLiveData.observe(viewLifecycleOwner) { errorMessage ->
            binding.textViewError.text = errorMessage
        }

        binding.buttonNext.setOnClickListener {
            catalogViewModel.currentPage++ // TODO upper limit on currentPage
            catalogViewModel.reload()
        }

        binding.buttonPrev.setOnClickListener {
            if (catalogViewModel.currentPage == 1) {
                Snackbar.make(it, "First page", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }
            catalogViewModel.currentPage--
            catalogViewModel.reload()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class MyGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onLongPress(e: MotionEvent) {
            Log.d("APPLE", "onLongPress $e")
            super.onLongPress(e)
        }

        override fun onScroll(
            ev1: MotionEvent?, ev2: MotionEvent, distanceX: Float, distanceY: Float
        ): Boolean {
            Log.d("APPLE", "onScroll: $ev1 \n $ev2")
            doIt(ev1, ev2)
            return super.onScroll(ev1, ev2, distanceX, distanceY)
        }

        override fun onFling(
            ev1: MotionEvent?, ev2: MotionEvent, velocityX: Float, velocityY: Float
        ): Boolean {
            Log.d("APPLE", "onFling: $ev1 \n $ev2")
            doIt(ev1, ev2)
            return super.onFling(ev1, ev2, velocityX, velocityY)
        }

        private fun doIt(ev1: MotionEvent?, ev2: MotionEvent) {
            if (ev1 != null) {
                val xDiff = ev1.x - ev2.x
                Log.d("APPLEPIE", "swipe $xDiff")
                if (xDiff > 0) {
                    catalogViewModel.currentPage++ // TODO upper limit on currentPage
                    catalogViewModel.reload()
                }
            }
        }

    }
}