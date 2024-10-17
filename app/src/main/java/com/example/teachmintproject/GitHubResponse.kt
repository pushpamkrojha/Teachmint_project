package com.example.teachmintproject

import com.google.gson.annotations.SerializedName

data class GitHubResponse(
    @SerializedName("items") val items: List<Repository>
)
