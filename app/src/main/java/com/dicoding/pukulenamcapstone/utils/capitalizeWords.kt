package com.dicoding.pukulenamcapstone.utils

import java.util.Locale

fun String.capitalizeWords(): String = split(" ").joinToString(" ") { it.capitalize(Locale.ROOT) }