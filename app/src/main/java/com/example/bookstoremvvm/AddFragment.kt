package com.example.bookstoremvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.bookstoremvvm.databinding.FragmentAddBinding
import com.example.bookstoremvvm.models.Book
import com.example.bookstoremvvm.models.BooksViewModel

class AddFragment : Fragment() {
    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private val booksViewModel: BooksViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonAdd.setOnClickListener {
            val title = binding.editTextTitle.text.toString().trim()
            if (title.isEmpty()) {
                binding.editTextTitle.error = "Title required"
                return@setOnClickListener
            }
            val priceStr = binding.editTextPrice.text.toString().trim()
            if (priceStr.isEmpty()) {
                binding.editTextPrice.error = "Price required"
                return@setOnClickListener
            }
            val price = priceStr.toDouble()
            val book = Book(title, price)
            booksViewModel.add(book)
            findNavController().popBackStack()
        }

        binding.buttonCancel.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}