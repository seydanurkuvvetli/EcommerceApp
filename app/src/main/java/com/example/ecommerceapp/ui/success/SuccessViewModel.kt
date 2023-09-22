package com.example.ecommerceapp.ui.success

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.common.Resource
import com.example.ecommerceapp.data.model.ProductUI
import com.example.ecommerceapp.data.repository.ProductsRepository
import com.example.ecommerceapp.ui.home.CartState
import com.example.ecommerceapp.ui.search.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SuccessViewModel @Inject constructor(private val productsRepository: ProductsRepository):
    ViewModel(){
    private var _successState = MutableLiveData<SuccessState>()
    val successState: LiveData<SuccessState>
        get() = _successState
}
sealed interface SuccessState {
    object Loading : SuccessState
    data class Data(val products: List<ProductUI>) : SuccessState
    data class Data2(val products: String) : SuccessState
    data class Error(val throwable: Throwable) : SuccessState
}