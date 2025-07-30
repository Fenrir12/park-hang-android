package com.parkhang.mobile.core.common

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

inline fun <T> Flow<T>.logStateTransitionsPretty(
    tag: String = "StateMachine",
    maxListItems: Int = 5,
    crossinline toLogString: (old: T, new: T) -> String = { old: T, new: T ->
        val trim = { s: Any? ->
            when (s) {
                is List<*> -> if (s.size > maxListItems) s.take(maxListItems) + "...(${s.size} total)" else s
                else -> s
            }
        }

        val oldStr = trim(old)
        val newStr = trim(new)

        """
        ┌── State Transition ─────────────────
        │ From: $oldStr
        │ To:   $newStr
        └────────────────────────────────────
        """.trimIndent()
    },
): Flow<T> {
    var previous: T? = null
    return this.onEach { new ->
        previous?.let { old ->
            Log.d(tag, toLogString(old, new))
        }
        previous = new
    }
}
