package com.example.bookstoremvvm.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.bookstoremvvm.models.Book
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BooksRepository {
    private val baseUrl = "https://anbo-restbookquerystring.azurewebsites.net/api/"
    // the specific (collection) part of the URL is on the individual methods in the interface BookstoreService

    //"http://anbo-restserviceproviderbooks.azurewebsites.net/Service1.svc/"
    private val bookStoreService: BookStoreService
    val booksLiveData: MutableLiveData<List<Book>> = MutableLiveData<List<Book>>()
    val errorMessageLiveData: MutableLiveData<String> = MutableLiveData()
    val updateMessageLiveData: MutableLiveData<String> = MutableLiveData()
    val reloadingLiveData: MutableLiveData<Boolean> = MutableLiveData()

    init {
        //val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val build: Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create()) // GSON
            //.addConverterFactory(KotlinJsonAdapterFactory)
            //.addConverterFactory(MoshiConverterFactory.create(moshi)) // Moshi, added to Gradle dependencies
            .build()
        bookStoreService = build.create(BookStoreService::class.java)
        getBooks()
    }

    fun getBooks() {
        reloadingLiveData.value = true
        bookStoreService.getAllBooks().enqueue(object : Callback<List<Book>> {
            override fun onResponse(call: Call<List<Book>>, response: Response<List<Book>>) {
                reloadingLiveData.value = false
                if (response.isSuccessful) {
                    //Log.d("APPLE", response.body().toString())
                    val b: List<Book>? = response.body()
                    booksLiveData.postValue(b!!)
                    errorMessageLiveData.postValue("")
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("APPLE", message)
                }
            }

            override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                //booksLiveData.postValue(null)
                reloadingLiveData.value = false
                errorMessageLiveData.postValue(t.message)
                Log.d("APPLE", t.message!!)
            }
        })
    }

    fun add(book: Book) {
        bookStoreService.saveBook(book).enqueue(object : Callback<Book> {
            override fun onResponse(call: Call<Book>, response: Response<Book>) {
                if (response.isSuccessful) {
                    Log.d("APPLE", "Added: " + response.body())
                    updateMessageLiveData.postValue("Added: " + response.body())
                    getBooks()
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("APPLE", message)
                }
            }

            override fun onFailure(call: Call<Book>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
                Log.d("APPLE", t.message!!)
            }
        })
    }

    fun delete(id: Int) {
        bookStoreService.deleteBook(id).enqueue(object : Callback<Book> {
            override fun onResponse(call: Call<Book>, response: Response<Book>) {
                if (response.isSuccessful) {
                    Log.d("APPLE", "Updated: " + response.body())
                    updateMessageLiveData.postValue("Deleted: " + response.body())
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("APPLE", message)
                }
            }

            override fun onFailure(call: Call<Book>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
                Log.d("APPLE", t.message!!)
            }
        })
    }

    fun update(book: Book) {
        bookStoreService.updateBook(book.id, book).enqueue(object : Callback<Book> {
            override fun onResponse(call: Call<Book>, response: Response<Book>) {
                if (response.isSuccessful) {
                    Log.d("APPLE", "Updated: " + response.body())
                    updateMessageLiveData.postValue("Updated: " + response.body())
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("APPLE", message)
                }
            }

            override fun onFailure(call: Call<Book>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
                Log.d("APPLE", t.message!!)
            }
        })
    }

    fun sortByTitle() {
        booksLiveData.value = booksLiveData.value?.sortedBy { it.title }
    }

    fun sortByTitleDescending() {
        booksLiveData.value = booksLiveData.value?.sortedByDescending { it.title }
    }

    fun sortByPrice() {
        booksLiveData.value = booksLiveData.value?.sortedBy { it.price }
    }

    fun sortByPriceDescending() {
        booksLiveData.value = booksLiveData.value?.sortedByDescending { it.price }
    }

    fun filterByTitle(title: String) {
        if (title.isBlank()) {
            getBooks()
        }else {
            booksLiveData.value = booksLiveData.value?.filter { book -> book.title.contains(title) }
            // TODO: bug fix: booksLiveData.value keeps getting smaller for each filter
        }
    }
}