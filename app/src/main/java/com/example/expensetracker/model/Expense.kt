package com.example.expensetracker.model

data class Expense(
    var id: String = "",
    val amount: Double = 0.0,
    val category: String = "",
    val date: Long = System.currentTimeMillis(),
    val description: String = ""
)