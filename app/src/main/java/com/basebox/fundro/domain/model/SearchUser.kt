package com.basebox.fundro.domain.model

data class SearchUser(
    val id: String,
    val username: String,
    val fullName: String,
    var isSelected: Boolean = false
)