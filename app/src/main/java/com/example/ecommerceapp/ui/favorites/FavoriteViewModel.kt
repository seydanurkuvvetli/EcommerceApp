package com.example.ecommerceapp.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.common.Resource
import com.example.ecommerceapp.data.model.ProductUI
import com.example.ecommerceapp.data.repository.ProductsRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val productsRepository: ProductsRepository):
    ViewModel() {
    private var _favState = MutableLiveData<FavState>()
    val user = Firebase.auth.currentUser
    val userId = user?.uid
    val favState: LiveData<FavState>
        get() = _favState

    fun getCartProducts(userId:String) {
        viewModelScope.launch {
            _favState.value = FavState.Loading

            when (val result=productsRepository.getProductFav(userId)) {
                is Resource.Success -> {
                    _favState.value =FavState.Data(result.data)
                }

                is Resource.Error -> {
                    _favState.value = FavState.Error(result.throwable)
                }
            }
        }


    }
    fun deleteProductFromCart(product: ProductUI){
        viewModelScope.launch {
            productsRepository.deleteFavorites(product.id)
            if (userId != null) {
                getCartProducts(userId)
            }

        }
    }

    }

sealed interface FavState {
    object Loading : FavState
    data class Data(val products: List<ProductUI>) : FavState
    data class Error(val throwable: Throwable) : FavState
}