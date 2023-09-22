package com.example.ecommerceapp.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.repository.SignInViewState
import com.example.ecommerceapp.databinding.FragmentSignInBinding
import com.example.ecommerceapp.databinding.FragmentSignUpBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment() {
    // Diğer değişkenler ve onCreateView işlevi burada bulunur.
    private lateinit var binding: FragmentSignInBinding
    private val signInViewModel: SignInViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            loginButton.setOnClickListener {
                val email = email.text.toString()
                val password = password.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty() && password.length >= 6) {
                    signInViewModel.signIn(email, password)
                } else {
                    Snackbar.make(
                        requireView(),
                        "Email ve şifre boş geçilemez",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.signupText.setOnClickListener {
            findNavController().navigate(R.id.action_signInragment_to_signUpFragment)
        }

        signInViewModel.signInViewState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SignInViewState.Loading -> {
                    // Yükleme durumunu işleyin
                }

                is SignInViewState.Success -> {
                    // Başarı durumunu işleyin
                    findNavController().navigate(R.id.action_signInFragment_to_homeFragment)

                }

                is SignInViewState.Error -> {
                    // Hata durumunu işleyin
                    Snackbar.make(requireView(), state.errorMessage, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
}
