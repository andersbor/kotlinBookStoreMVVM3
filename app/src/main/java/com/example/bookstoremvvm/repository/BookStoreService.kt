package com.example.bookstoremvvm.repository

import com.example.bookstoremvvm.models.Book
import retrofit2.Call
import retrofit2.http.*

// The methods in this interface are attributed with the specific (collection related) part of the URL
// The base URL is found in the class BooksRepository

interface BookStoreService {
    @GET("books")
    fun getAllBooks(): Call<List<Book>>

    @GET("books/{bookId}")
    fun getBookById(@Path("bookId") bookId: Int): Call<Book>

    @POST("books")
    fun saveBook(@Body book: Book): Call<Book>

    @DELETE("books/{id}")
    fun deleteBook(@Path("id") id: Int): Call<Book>

    @PUT("books/{id}")
    fun updateBook(@Path("id") id: Int, @Body book: Book): Call<Book>
}