package com.example.ecommerceapp.ui.payment

import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.common.Resource
import com.example.ecommerceapp.data.model.ProductUI
import com.example.ecommerceapp.data.repository.ProductsRepository
import com.example.ecommerceapp.data.repository.SignInViewState
import com.example.ecommerceapp.data.repository.UserRepository
import com.example.ecommerceapp.ui.home.CartState
import com.example.ecommerceapp.ui.home.CartViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class PaymentViewModel @Inject constructor(private val productsRepository: ProductsRepository):
    ViewModel() {

    private var totalAmount: Double = 0.0
    private var _paymentState = MutableLiveData<PaymentState>()
    val paymentState: LiveData<PaymentState>
        get() = _paymentState
    suspend   fun clearCart(userId: String){

        viewModelScope.launch {
            _paymentState.value = PaymentState.Loading

            when (val result=  productsRepository.clearCart(userId)) {
                is Resource.Success -> {
                    _paymentState.value = PaymentState.Data2(result.data)
                    getCartProducts(userId)

                }

                is Resource.Error -> {
                    _paymentState.value = PaymentState.Error(result.throwable)
                }
            }
        }
    }

    fun getCartProducts(userId:String) {
        viewModelScope.launch {
            _paymentState.value = PaymentState.Loading

            when (val result=productsRepository.getCartProduct(userId)) {
                is Resource.Success -> {
                    _paymentState.value = PaymentState.Data(result.data)
                }

                is Resource.Error -> {
                    _paymentState.value = PaymentState.Error(result.throwable)
                }
            }
        }


    }


}
sealed interface PaymentState {
    object Loading : PaymentState
    data class Data(val products: List<ProductUI>) : PaymentState
    data class Data2(val products: String) : PaymentState
    data class Error(val throwable: Throwable) : PaymentState
    data class totalCount(val total:Double):PaymentState
}