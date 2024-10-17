package com.example.teachmintproject

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.multidex.BuildConfig
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var adapter: RepositoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Mobile Ads SDK on a background thread
        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            MobileAds.initialize(this@MainActivity) {
                Log.d("MobileAds", "SDK initialized successfully")
            }
        }

        // Initialize Retrofit
        val apiService = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GitHubApiService::class.java)

        // Initialize Repository and ViewModel
        val repository = GitHubRepository(apiService)
        searchViewModel = ViewModelProvider(this, ViewModelFactory(repository)).get(SearchViewModel::class.java)

        // Setup RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        // Pass the onItemClick lambda to the adapter
        adapter = RepositoriesAdapter { repository ->
            // Handle item click here, navigate to RepoDetailActivity
            val intent = Intent(this, RepoDetailActivity::class.java).apply {
                putExtra("repository", repository)
            }
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Observe the search results
        searchViewModel.searchResults.observe(this) { results ->
            adapter.submitList(results)
        }

        // Setup SearchView Listener
        val searchView: SearchView = findViewById(R.id.search)
        setupSearchView(searchView)

        // Add Share Button Listener
        val shareButton: Button = findViewById(R.id.share_button)
        shareButton.setOnClickListener {
            shareAppLink() // Call the method to share the app link
        }
    }

    @SuppressLint("RestrictedApi")
    private fun setupSearchView(searchView: SearchView) {
        searchView.setQueryHint("Search") // Set hint text
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    Log.d("Search", "Query submitted: $it")
                    searchViewModel.search(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        // Set hint text color to black
        val searchTextView = searchView.findViewById<androidx.appcompat.widget.SearchView.SearchAutoComplete>(androidx.appcompat.R.id.search_src_text)
        searchTextView.setHintTextColor(Color.BLACK)
    }

    private fun shareAppLink() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        val playStoreLink = "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this app: $playStoreLink")
        startActivity(Intent.createChooser(shareIntent, "Share App via"))
    }
}
