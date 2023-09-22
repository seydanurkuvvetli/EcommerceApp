package com.example.ecommerceapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.common.Resource
import com.example.ecommerceapp.data.model.ProductUI
import com.example.ecommerceapp.data.repository.ProductsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val productsRepository: ProductsRepository) :
    ViewModel() {
    private var totalAmount: Double = 0.0
    private var _cartState = MutableLiveData<CartState>()
    val cartState: LiveData<CartState>
        get() = _cartState


    fun getCartProducts(userId:String) {
        viewModelScope.launch {
            _cartState.value = CartState.Loading

            when (val result=productsRepository.getCartProduct(userId)) {
                is Resource.Success -> {
                    _cartState.value = CartState.Data(result.data)
                }

                is Resource.Error -> {
                    _cartState.value = CartState.Error(result.throwable)
                }
            }
        }


    }
    suspend  fun removeFromCart(id:Int,userId: String) {

        viewModelScope.launch {
            _cartState.value = CartState.Loading

            when (val result=productsRepository.deleteCartProduct(id)) {
                is Resource.Success -> {
                    _cartState.value = CartState.Data2(result.data)
                    getCartProducts(userId)
                }

                is Resource.Error -> {
                    _cartState.value = CartState.Error(result.throwable)
                }
            }
        }


    }


    fun increase(price: Double) {
        productsRepository.addToTotalAmount(price)
        val newTotal = productsRepository.getTotalAmount()
        _cartState.value = CartState.totalCount(newTotal)
    }

    fun decrease(price: Double) {
        productsRepository.removeFromTotalAmount(price)
        val newTotal = productsRepository.getTotalAmount()
        _cartState.value = CartState.totalCount(newTotal)
    }




     fun updateCartState() {
        _cartState.value = CartState.totalCount(totalAmount)
    }

  suspend   fun clearCart(userId: String){

        viewModelScope.launch {
            _cartState.value = CartState.Loading

            when (val result=  productsRepository.clearCart(userId)) {
                is Resource.Success -> {
                    _cartState.value = CartState.Data2(result.data)
                    getCartProducts(userId)

                }

                is Resource.Error -> {
                    _cartState.value = CartState.Error(result.throwable)
                }
            }
        }
    }




}

sealed interface CartState {
    object Loading : CartState
    data class Data(val products: List<ProductUI>) : CartState
    data class Data2(val products: String) : CartState
    data class Error(val throwable: Throwable) : CartState
    data class totalCount(val total:Double):CartState
}