package com.example.expensetracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.model.Expense
import com.example.expensetracker.repository.FirebaseRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExpenseViewModel : ViewModel() {
    private val repo = FirebaseRepo()
    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses: StateFlow<List<Expense>> = _expenses

    fun fetchExpenses() {
        viewModelScope.launch {
            _expenses.value = repo.getExpenses()
        }
    }

    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            repo.addExpense(expense)
            fetchExpenses()
        }
    }

    fun deleteExpense(id: String) {
        viewModelScope.launch {
            repo.deleteExpense(id)
            fetchExpenses()
        }
    }
}