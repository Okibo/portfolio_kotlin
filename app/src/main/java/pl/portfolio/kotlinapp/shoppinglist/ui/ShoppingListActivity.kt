package pl.portfolio.kotlinapp.shoppinglist.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.shoppinglist_layout.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import pl.portfolio.kotlinapp.R
import pl.portfolio.kotlinapp.shoppinglist.data.db.ShoppingDatabase
import pl.portfolio.kotlinapp.shoppinglist.data.db.entity.ShoppingItem
import pl.portfolio.kotlinapp.shoppinglist.data.other.ShoppingItemAdapter
import pl.portfolio.kotlinapp.shoppinglist.data.repositories.ShoppingRepository
import pl.portfolio.kotlinapp.shoppinglist.ui.shoppinglist.AddDialogListener
import pl.portfolio.kotlinapp.shoppinglist.ui.shoppinglist.AddShoppingItemDialog
import pl.portfolio.kotlinapp.shoppinglist.ui.shoppinglist.ShoppingViewModel
import pl.portfolio.kotlinapp.shoppinglist.ui.shoppinglist.ShoppingViewModelFactory

class ShoppingListActivity: AppCompatActivity(), KodeinAware {

    override val kodein by kodein()
    private val factory: ShoppingViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shoppinglist_layout)
        title = "Shopping List"

        val viewModel = ViewModelProvider(this, factory).get(ShoppingViewModel::class.java)

        val adapter = ShoppingItemAdapter(listOf(), viewModel)
        rvShoppingItems.layoutManager = LinearLayoutManager(this)
        rvShoppingItems.adapter = adapter

        viewModel.getAllShoppingItems().observe(this, Observer {
            adapter.items = it
            adapter.notifyDataSetChanged()
        })

        fab.setOnClickListener{
            AddShoppingItemDialog(this, object: AddDialogListener{
                override fun onAddButtonClicked(item: ShoppingItem) {
                    viewModel.upsert(item)
                }

            }).show()
        }
    }
}