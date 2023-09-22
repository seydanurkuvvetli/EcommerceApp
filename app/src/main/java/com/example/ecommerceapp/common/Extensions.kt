package com.example.ecommerceapp.common

import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

fun ImageView.loadImage(url: String?) {
    Glide.with(this.context).load(url).into(this)
}
fun View.gone(){
    visibility = View.GONE
}

fun View.visible(){
    visibility = View.VISIBLE
}
fun EditText.isNullorEmpty(errorString: String): Boolean {
    val textInputLayout = this.parent.parent as TextInputLayout
    return if (text.toString().trim().isNotEmpty()) {
        textInputLayout.isErrorEnabled = false
        true
    } else {
        textInputLayout.error = errorString
        false
    }
}

fun EditText.isValidEmail(errorString: String): Boolean {
    val textInputLayout = this.parent.parent as TextInputLayout
    return if (Patterns.EMAIL_ADDRESS.matcher(text.toString()).matches()) {
        textInputLayout.isErrorEnabled = false
        true
    } else {
        textInputLayout.error = errorString
        false
    }
}
