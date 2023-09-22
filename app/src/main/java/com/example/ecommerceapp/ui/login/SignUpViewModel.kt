package com.example.ecommerceapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.data.repository.SignInViewState
import com.example.ecommerceapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val authRepository: UserRepository) : ViewModel() {

    private val _signInViewState = MutableLiveData<SignInViewState>()
    val signInViewState: LiveData<SignInViewState> = _signInViewState

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _signInViewState.value = SignInViewState.Loading
            val result = authRepository.signUp(email, password)
            _signInViewState.value = result
        }
    }
}