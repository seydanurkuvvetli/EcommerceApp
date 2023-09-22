package com.example.ecommerceapp.data.repository

import com.example.ecommerceapp.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) {
    fun getFirebaseUserUid(): String = auth.currentUser?.uid.orEmpty()

    suspend fun signIn(email: String, password: String): SignInViewState {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            SignInViewState.Success("Giriş başarılı")

        } catch (e: Exception) {
            SignInViewState.Error(e.message ?: "Giriş sırasında bir hata oluştu")
        }
    }

    suspend fun getCurrentUser(): User {
        val user =
            firebaseFirestore.collection("users").document(getFirebaseUserUid())
                .get().await()

        return User(
            user["id"] as String,
            user["email"] as String,
            user["password"] as String,
        )
    }

    suspend fun signUp(email: String, password: String): SignInViewState {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            SignInViewState.Success("Kayıt başarılı")
        } catch (e: Exception) {
            SignInViewState.Error(e.message ?: "Kayıt sırasında bir hata oluştu")
        }
    }

    fun signOut() = auth.signOut()

}

sealed class SignInViewState {
    object Loading : SignInViewState()
    data class Success(val message: String) : SignInViewState()
    data class Error(val errorMessage: String) : SignInViewState()
}
