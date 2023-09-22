package com.example.ecommerceapp.ui.detail

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
class DetailViewModel @Inject constructor(private val productsRepository: ProductsRepository) :
    ViewModel() {
    private var _detailState = MutableLiveData<DetailState>()
    val detailState: LiveData<DetailState>
        get() = _detailState
    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean>
        get() = _isFavorite
    val user = Firebase.auth.currentUser
    var userId = user?.uid
    private var productId: Int = 0


    fun checkFavoriteState(userId: String, productId: Int) {
        this.userId = userId
        this.productId = productId
        viewModelScope.launch {
            _isFavorite.value = productsRepository.isProductFavorite(userId, productId)
        }
    }


    fun toggleFavoriteProduct(product: ProductUI) {
        viewModelScope.launch {
            if (_isFavorite.value == true) {
                productsRepository.deleteFavorites(product.id)
            } else {
                userId?.let { productsRepository.addToFavorites(userId!!, product) }
            }
            _isFavorite.value = !_isFavorite.value!!
        }
    }


    fun getDetailProducts(id: Int) {
        viewModelScope.launch {
            _detailState.value = DetailState.Loading

            when (val result = productsRepository.getProductDetail(id)) {
                is Resource.Success -> {
                    _detailState.value = DetailState.Data(result.data)
                }

                is Resource.Error -> {
                    _detailState.value = DetailState.Error(result.throwable)
                }
            }
        }

    }

    suspend fun addProductToBasket(userId: String, productId: Int) {


        viewModelScope.launch {
            _detailState.value = DetailState.Loading

            when (val result = productsRepository.addToCart(userId, productId)) {
                is Resource.Success -> {
                    _detailState.value = DetailState.Data2(result.data)
                }

                is Resource.Error -> {
                    _detailState.value = DetailState.Error(result.throwable)
                }
            }
        }

        }



    sealed interface DetailState {
        object Loading : DetailState
        data class Data(val products: ProductUI) : DetailState
        data class Error(val throwable: Throwable) : DetailState
        data class Data2(val response: String) : DetailState

    }
}
