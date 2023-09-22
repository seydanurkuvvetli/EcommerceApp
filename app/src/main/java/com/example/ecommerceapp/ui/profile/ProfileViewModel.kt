package com.example.ecommerceapp.ui.profile

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
class ProfileViewModel@Inject constructor(private val authRepository: UserRepository) : ViewModel() {

    private val _signOutResult = MutableLiveData<SignOutResult>()
    val signOutResult: LiveData<SignOutResult> = _signOutResult

    fun signOut() {
        viewModelScope.launch {
            _signOutResult.value = SignOutResult.Loading

            try {
                authRepository.signOut()
                _signOutResult.value = SignOutResult.Success
            } catch (e: Exception) {
                _signOutResult.value = SignOutResult.Error(e)
            }
        }
    }
}
sealed class SignOutResult {
    object Loading : SignOutResult()
    object Success : SignOutResult()
    data class Error(val throwable: Throwable) : SignOutResult()
}
