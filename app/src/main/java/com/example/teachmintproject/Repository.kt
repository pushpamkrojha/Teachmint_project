package com.example.teachmintproject

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Repository(
    val id: Int,
    val name: String,
    val description: String?,
    val html_url: String,
    val contributors_url: String,
    val owner: RepoOwner
) : Parcelable

@Parcelize
data class Owner(
    val login: String,
    val avatar_url: String
) : Parcelable
