package com.example.teachmintproject

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RepoOwner(
    val login: String,
    val avatar_url: String
) : Parcelable
