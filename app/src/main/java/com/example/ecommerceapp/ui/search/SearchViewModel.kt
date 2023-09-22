package com.example.ecommerceapp.ui.search

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
class SearchViewModel @Inject constructor(private val productsRepository: ProductsRepository):
    ViewModel(){
    private var _searchState = MutableLiveData<SearchState>()
    val searchState: LiveData<SearchState>
        get() = _searchState
    fun getProduct(query: String) {
        viewModelScope.launch {
            _searchState.value = SearchState.Loading

            when (val result=productsRepository.searchProduct(query)) {
                is Resource.Success -> {
                    _searchState.value = SearchState.Data(result.data)

                }

                is Resource.Error -> {
                    _searchState.value = SearchState.Error(result.throwable)
                }
            }
        }


    }
}
sealed interface SearchState {
    object Loading : SearchState
    data class Data(val products: List<ProductUI>) : SearchState
    data class Error(val throwable: Throwable) : SearchState
}