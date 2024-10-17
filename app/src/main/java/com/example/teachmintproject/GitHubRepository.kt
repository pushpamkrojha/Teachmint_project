package com.example.teachmintproject

import retrofit2.Response

class GitHubRepository(private val apiService: GitHubApiService) {
    suspend fun searchRepositories(query: String, page: Int): List<Repository> {
        val response = apiService.searchRepositories(query, page)
        if (response.isSuccessful) {
            return response.body()?.items ?: emptyList()
        } else {
            throw Exception("Failed to load data")
        }
    }
}
