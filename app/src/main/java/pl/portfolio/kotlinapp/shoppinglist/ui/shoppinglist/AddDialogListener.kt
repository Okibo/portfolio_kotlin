package pl.portfolio.kotlinapp.shoppinglist.ui.shoppinglist

import pl.portfolio.kotlinapp.shoppinglist.data.db.entity.ShoppingItem

interface AddDialogListener {
    fun onAddButtonClicked(item: ShoppingItem)
}