package com.example.teachmintproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: GitHubRepository) : ViewModel() {

    private val _searchResults = MutableLiveData<List<Repository>>()
    val searchResults: LiveData<List<Repository>> get() = _searchResults

    private var currentPage = 1
    private var currentQuery: String? = null

    fun search(query: String) {
        currentQuery = query
        currentPage = 10
        fetchRepositories()
    }

    fun loadMore() {
        currentQuery?.let {
            currentPage++
            fetchRepositories()
        }
    }

    private fun fetchRepositories() {
        viewModelScope.launch {
            try {
                val results = repository.searchRepositories(currentQuery ?: "", currentPage)
                // Append new results to existing ones
                val updatedResults = _searchResults.value.orEmpty() + results
                _searchResults.postValue(updatedResults)
            } catch (e: Exception) {
                // Handle error (could notify UI)
            }
        }
    }
}
