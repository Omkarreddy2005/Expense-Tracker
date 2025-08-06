package com.example.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensetracker.model.Expense
import com.example.expensetracker.viewmodel.ExpenseViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: ExpenseViewModel = viewModel()
            val expenses by viewModel.expenses.collectAsState()
            var showDialog by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                viewModel.fetchExpenses()
            }

            Scaffold(
                topBar = { TopAppBar(title = { Text("Expense Tracker") }) },
                floatingActionButton = {
                    FloatingActionButton(onClick = { showDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Expense")
                    }
                }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    ExpensePieChart(expenses)
                    Spacer(Modifier.height(16.dp))
                    LazyColumn {
                        items(expenses.size) { index ->
                            ExpenseItem(expense = expenses[index], onDelete = {
                                viewModel.deleteExpense(expenses[index].id)
                            })
                        }
                    }
                }

                if (showDialog) {
                    AddExpenseDialog(
                        onAdd = { expense ->
                            viewModel.addExpense(expense)
                            showDialog = false
                        },
                        onDismiss = { showDialog = false }
                    )
                }
            }
        }
    }
}

@Composable
fun ExpensePieChart(expenses: List<Expense>) {
    val grouped = expenses.groupBy { it.category }
    val entries = grouped.map { PieEntry(it.value.sumOf { e -> e.amount }.toFloat(), it.key) }
    if (entries.isEmpty()) return

    AndroidView(
        factory = { context ->
            PieChart(context).apply {
                data = PieData(PieDataSet(entries, "Expenses"))
                description.isEnabled = false
            }
        },
        modifier = Modifier.height(200.dp).fillMaxWidth()
    )
}

@Composable
fun ExpenseItem(expense: Expense, onDelete: () -> Unit) {
    Card(Modifier.fillMaxWidth().padding(4.dp)) {
        Row(Modifier.padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(expense.category, style = MaterialTheme.typography.h6)
                Text("₹${expense.amount} - ${expense.description}")
            }
            IconButton(onClick = { onDelete() }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}