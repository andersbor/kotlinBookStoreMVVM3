package com.example.bookstoremvvm

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bookstoremvvm.databinding.FragmentFirstBinding
import com.example.bookstoremvvm.models.BooksViewModel
import com.example.bookstoremvvm.models.MyAdapter

class FirstFragment : Fragment() {
    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private val booksViewModel: BooksViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // This fragment show the SAME data in a spinner + recyclerView.
        // This is quite unusual and should NOT be copied.

        booksViewModel.booksLiveData.observe(viewLifecycleOwner) { books ->
            //Log.d("APPLE", "observer $books")
            binding.progressbar.visibility = View.GONE
            binding.recyclerView.visibility = if (books == null) View.GONE else View.VISIBLE
            if (books != null) {
                val adapter = MyAdapter(books) { position ->
                    val action =
                        FirstFragmentDirections.actionFirstFragmentToSecondFragment(position)
                    findNavController().navigate(action /*R.id.action_FirstFragment_to_SecondFragment*/)
                }
                // binding.recyclerView.layoutManager = LinearLayoutManager(activity)
                var columns = 2
                val currentOrientation = this.resources.configuration.orientation
                if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                    columns = 4
                } else if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
                    columns = 2
                }
                binding.recyclerView.layoutManager = GridLayoutManager(this.context, columns)

                binding.recyclerView.adapter = adapter
            }
        }

        booksViewModel.errorMessageLiveData.observe(viewLifecycleOwner) { errorMessage ->
            binding.textviewMessage.text = errorMessage
        }

        booksViewModel.reload()

        binding.swiperefresh.setOnRefreshListener {
            booksViewModel.reload()
            binding.swiperefresh.isRefreshing = false // TODO too early
        }

        booksViewModel.reloadingLiveData.observe(viewLifecycleOwner) { isLoading ->
            binding.swiperefresh.isRefreshing = isLoading
        }

        booksViewModel.booksLiveData.observe(viewLifecycleOwner) { books ->
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, books)
            //binding.spinnerBooks.adapter = adapter
            /* binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                 override fun onItemSelected(
                     parent: AdapterView<*>?,
                     view: View?,
                     position: Int,
                     id: Long
                 ) { // reacts instantly: Much to quick.
                     val action =
                         FirstFragmentDirections.actionFirstFragmentToSecondFragment(position)
                     findNavController().navigate(action /*R.id.action_FirstFragment_to_SecondFragment*/)
                 }

                 override fun onNothingSelected(parent: AdapterView<*>?) {
                     TODO("Not yet implemented")
                 }
             }*/
        }

        /*binding.buttonShowDetails.setOnClickListener {
            val position = binding.spinnerBooks.selectedItemPosition
            val action =
                FirstFragmentDirections.actionFirstFragmentToSecondFragment(position)
            findNavController().navigate(action /*R.id.action_FirstFragment_to_SecondFragment*/)
        }*/

        binding.buttonSort.setOnClickListener {
            when (binding.spinnerSorting.selectedItemPosition) {
                0 -> booksViewModel.sortByTitle()
                1 -> booksViewModel.sortByTitleDescending()
                2 -> booksViewModel.sortByPrice()
                3 -> booksViewModel.sortByPriceDescending()
            }
        }

        binding.searchviewFilterTitle.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isEmpty()) {
                    booksViewModel.reload()
                    return false
                }
                binding.searchviewFilterTitle.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                /*if (newText.isNullOrEmpty()) {
                    booksViewModel.reload()
                    return false
                }*/
                booksViewModel.filterByTitle(newText.trim())
                return true
            }

        })

        binding.buttonFilter.setOnClickListener {
            //val title = binding.edittextFilterTitle.text.toString().trim()
            val title = binding.searchviewFilterTitle.query.toString().trim()
            /* if (title.isBlank()) {
                 binding.edittextFilterTitle.error = "No title"
                 return@setOnClickListener
             }*/
            booksViewModel.filterByTitle(title)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}