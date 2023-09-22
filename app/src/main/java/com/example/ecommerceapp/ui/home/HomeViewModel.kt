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
class HomeViewModel @Inject constructor(private val productsRepository: ProductsRepository) :
    ViewModel() {
    private var _homeState = MutableLiveData<HomeState>()
    val homeState: LiveData<HomeState>
        get() = _homeState

    fun getCategory() {
        viewModelScope.launch {
            _homeState.value = HomeState.Loading

            when (val result = productsRepository.getCategories()) {
                is Resource.Success -> {
                    _homeState.value = HomeState.Data2(result.data)
                }

                is Resource.Error -> {
                    _homeState.value = HomeState.Error(result.throwable)
                }
            }

        }
    }
    fun getSaleProduct() {
        viewModelScope.launch {
            _homeState.value = HomeState.Loading

            when (val result = productsRepository.getSaleProducts()) {
                is Resource.Success -> {
                    _homeState.value = HomeState.Data(result.data)
                }

                is Resource.Error -> {
                    _homeState.value = HomeState.Error(result.throwable)
                }
            }
        }
    }

}
sealed interface HomeState {
    object Loading : HomeState
    data class Data(val products: List<ProductUI>) : HomeState
    data class Data2(val products: List<String>) : HomeState
    data class Error(val throwable: Throwable) : HomeState
}