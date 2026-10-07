package com.example.brewkery.presentation.common

import java.io.IOException

fun Throwable.toUserMessage(): String =
    if (this is IOException) "No internet connection. Check your network and try again."
    else "Something went wrong. Please try again."