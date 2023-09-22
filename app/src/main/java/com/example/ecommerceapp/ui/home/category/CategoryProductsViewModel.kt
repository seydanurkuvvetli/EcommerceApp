package com.example.ecommerceapp.ui.home.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceapp.common.Resource
import com.example.ecommerceapp.data.model.ProductUI
import com.example.ecommerceapp.data.repository.ProductsRepository
import com.example.ecommerceapp.ui.home.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryProductsViewModel @Inject constructor(private val productsRepository: ProductsRepository) :
    ViewModel() {
    private var _categoryState = MutableLiveData<CategoryState>()
    val categoryState: LiveData<CategoryState>
        get() = _categoryState

    fun getProduct() {
        viewModelScope.launch {
            _categoryState.value = CategoryState.Loading

            when (val result = productsRepository.getProducts()) {
                is Resource.Success -> {
                    _categoryState.value = CategoryState.Data(result.data)
                }

                is Resource.Error -> {
                    _categoryState.value = CategoryState.Error(result.throwable)
                }
            }
        }

    }

    fun getProductsByCategory(category: String) {
        viewModelScope.launch {
            _categoryState.value = CategoryState.Loading
            when (val result = productsRepository.getProductsByCategory(category)) {
                is Resource.Success -> {
                    _categoryState.value = CategoryState.Data(result.data)
                }

                is Resource.Error -> {
                    _categoryState.value = CategoryState.Error(result.throwable)
                }
            }
        }
    }


    sealed interface CategoryState {
        object Loading : CategoryState
        data class Data(val products: List<ProductUI>) : CategoryState
        data class Error(val throwable: Throwable) : CategoryState
    }
}