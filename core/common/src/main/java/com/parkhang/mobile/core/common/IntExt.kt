package com.parkhang.mobile.core.common

/** Returns the Int if it is not `null`, or 0 otherwise. */
fun Int?.orZero(): Int = this ?: 0

/** Returns the Long if it is not `null`, or 0L otherwise. */
fun Long?.orZero(): Long = this ?: 0L

/** Returns true if even number, false if odd*/
fun Int.isEven(): Boolean = this % 2 == 0
