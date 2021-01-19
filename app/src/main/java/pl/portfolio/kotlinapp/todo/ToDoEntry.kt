package pl.portfolio.kotlinapp.todo

data class ToDoEntry(val id: Long, var isChecked: Boolean, var title: String, var note:String)