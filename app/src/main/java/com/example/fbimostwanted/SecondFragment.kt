package com.example.fbimostwanted

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.fbimostwanted.databinding.FragmentSecondBinding
import com.example.fbimostwanted.models.CatalogViewModel

class SecondFragment : Fragment() {
    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    private val catalogViewModel: CatalogViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = requireArguments()
        val secondFragmentArgs: SecondFragmentArgs = SecondFragmentArgs.fromBundle(bundle)
        val position = secondFragmentArgs.position
        val item = catalogViewModel[position]
        if (item == null) {
            binding.textviewDescription.text = "No such item!"
            return
        }
        binding.textviewTitle.text = item.title
        if (item.details.isNullOrBlank()) {
            binding.textviewDescription.text = item.description
        } else {
            // https://www.baeldung.com/kotlin/string-replace-substring
            // https://www.tutorialspoint.com/how-to-remove-the-html-tags-from-a-given-string-in-java
            val regex: Regex = "<.*?>".toRegex()
            val details = item.details.replace(regex, "")
            //item.details.subSequence(3, item.details.length - 4) // remove <p> from details
            binding.textviewDescription.text = details
        }

        if (item.images != null) {
            // https://guides.codepath.com/android/Displaying-Images-with-the-Glide-Library
            Glide.with(requireActivity())
                .load(item.images[0].original)
                .into(binding.imageView)
        }

        binding.buttonSecond.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}