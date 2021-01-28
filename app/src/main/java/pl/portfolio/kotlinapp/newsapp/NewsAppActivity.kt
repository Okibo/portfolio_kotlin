package pl.portfolio.kotlinapp.newsapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.news_app_layout.*
import pl.portfolio.kotlinapp.R
import pl.portfolio.kotlinapp.newsapp.db.ArticleDatabase
import pl.portfolio.kotlinapp.newsapp.repository.NewsRepository
import pl.portfolio.kotlinapp.newsapp.ui.NewsViewModel
import pl.portfolio.kotlinapp.newsapp.ui.NewsViewModelProviderFactory

class NewsAppActivity: AppCompatActivity() {

    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.news_app_layout)
        title = "News App"

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(application, newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)

        bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())


    }
}