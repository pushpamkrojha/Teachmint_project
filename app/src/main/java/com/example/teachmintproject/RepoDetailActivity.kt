package com.example.teachmintproject

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class RepoDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo_detail)

        val repoName: TextView = findViewById(R.id.repo_name)
        val repoDescription: TextView = findViewById(R.id.repo_description)
        val projectLink: TextView = findViewById(R.id.project_link)
        val contributors: TextView = findViewById(R.id.contributors)
        val ownerAvatar: ImageView = findViewById(R.id.owner_avatar)

        // Retrieve the repository data from intent
        val repository: Repository? = intent.getParcelableExtra("repository")

        repository?.let {
            repoName.text = it.name
            repoDescription.text = it.description ?: "No description available"
            projectLink.text = it.html_url
            contributors.text = it.contributors_url // You may want to fetch and display contributor names

            // Load the owner's avatar
            Glide.with(this).load(it.owner.avatar_url).into(ownerAvatar)

            // Open project link in WebView when clicked
            projectLink.setOnClickListener {
                repository?.let { repo ->
                    openWebView(repo.html_url)
                }
            }
        }
    }

    private fun openWebView(url: String) {
        val webView: WebView = WebView(this)
        webView.webViewClient = WebViewClient()
        webView.loadUrl(url)
        setContentView(webView)
    }
}
