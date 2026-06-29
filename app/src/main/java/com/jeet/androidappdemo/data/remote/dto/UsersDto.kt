package com.jeet.androidappdemo.data.remote.dto

import com.jeet.androidappdemo.domain.model.User

data class UsersDto(
    val address: String,
    val company: String,
    val country: String,
    val email: String,
    val id: Int,
    val name: String,
    val phone: String,
    val photo: String,
    val state: String,
    val username: String,
    val zip: String
)

fun UsersDto.toUser(): User {
    return User(
        id = id,
        username = username,
        name = name,
        phone = phone,
        photo = photo,
        email = email,
        address = address,
        state = state,
        company = company,
        country = country,
        zip = zip
    )
}