package com.notes.ui._base

import android.util.Log
import androidx.fragment.app.Fragment

inline fun <reified L : Any> Fragment.findImplementationOrThrow(): L {
    return findImplementation(L::class.java)
        ?: throw IllegalStateException("Implementation of ${L::class.java.name} was not found")
}

inline fun <reified L : Any> Fragment.findImplementation(): L? {
    return findImplementation(L::class.java)
}

fun <L : Any> Fragment.findImplementation(klass: Class<L>): L? {
    val activity = this.activity
    val parentFragment = this.parentFragment

    return when {
        klass.isInstance(parentFragment) -> klass.cast(parentFragment)
        klass.isInstance(activity) && parentFragment == null -> klass.cast(activity)
        else -> parentFragment?.findImplementation(klass)
    }
}

// для логов
fun Fragment.log(msg: String){
    Log.i("self-log", msg)
}
