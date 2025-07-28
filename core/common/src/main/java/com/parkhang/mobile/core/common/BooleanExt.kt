package com.parkhang.mobile.core.common

/** Returns the Boolean if it is not `null`, or false otherwise. */
fun Boolean?.orFalse(): Boolean = this ?: false

/** Returns the Boolean if it is not `null`, or true otherwise. */
fun Boolean?.orTrue(): Boolean = this ?: true
