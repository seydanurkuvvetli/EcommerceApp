package com.example.ecommerceapp.ui.success

import android.app.AlertDialog
import android.os.Bundle
import android.os.UserHandle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ecommerceapp.R
import com.example.ecommerceapp.common.gone
import com.example.ecommerceapp.common.visible
import com.example.ecommerceapp.data.repository.UserRepository
import com.example.ecommerceapp.databinding.FragmentSuccess2Binding
import com.example.ecommerceapp.ui.search.SearchState
import com.example.ecommerceapp.ui.search.SearchViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SuccessFragment : Fragment() {

    private lateinit var binding: FragmentSuccess2Binding
    private val viewModel: SuccessViewModel by viewModels()
    val user = Firebase.auth.currentUser
    val userId = user?.uid

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSuccess2Binding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {

            lifecycleScope.launch {
                btnContinueShopping.setOnClickListener {

                    findNavController().navigate(R.id.homeFragment)

                }
            }
        }

    }

}

