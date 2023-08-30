package com.example.bookstoremvvm.models

import java.io.Serializable

data class Book(val id: Int, val title: String, val price: Double)  : Serializable {
    constructor(title: String, price: Double) : this(-1, title, price)

    override fun toString(): String {
        return "$id  $title,  $price"
    }
}