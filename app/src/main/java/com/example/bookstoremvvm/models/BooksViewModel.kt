package com.example.bookstoremvvm.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.bookstoremvvm.repository.BooksRepository

class BooksViewModel : ViewModel() {
    private val repository = BooksRepository()
    val booksLiveData: LiveData<List<Book>> = repository.booksLiveData
    val errorMessageLiveData: LiveData<String> = repository.errorMessageLiveData
    val updateMessageLiveData: LiveData<String> = repository.updateMessageLiveData
    val reloadingLiveData: LiveData<Boolean> = repository.reloadingLiveData

    init {
        reload()
    }

    fun reload() {
        repository.getBooks()
    }

    operator fun get(index: Int): Book? {
        return booksLiveData.value?.get(index)
    }

    fun add(book: Book) {
         repository.add(book)
    }

    fun delete(id: Int) {
        repository.delete(id)
    }

    fun update(book: Book) {
        repository.update(book)
    }

    fun sortByTitle() {
        repository.sortByTitle()
    }

    fun sortByTitleDescending() {
        repository.sortByTitleDescending()
    }

    fun sortByPrice() {
        repository.sortByPrice()
    }

    fun sortByPriceDescending() {
        repository.sortByPriceDescending()
    }

    fun filterByTitle(title: String) {
        repository.filterByTitle(title)
    }
}