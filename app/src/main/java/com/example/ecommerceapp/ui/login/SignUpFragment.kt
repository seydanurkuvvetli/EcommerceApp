package com.example.ecommerceapp.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.repository.SignInViewState
import com.example.ecommerceapp.databinding.FragmentSignUpBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private val signUpViewModel: SignUpViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            signupButton.setOnClickListener {
                val email = signupEmail.text.toString()
                val password = signupPassword.text.toString()
                val password2 = signupConfirm.text.toString()
                if (password == password2) {
                    if (email.isNotEmpty() && password.isNotEmpty() && password.length >= 6) {
                        signUpViewModel.signUp(email, password)
                    } else {
                        Snackbar.make(
                            requireView(),
                            "Email ve şifre boş geçilemez",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Snackbar.make(requireView(), "Şifreler aynı değil", Snackbar.LENGTH_SHORT)
                        .show()
                }

            }
        }

        signUpViewModel.signInViewState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SignInViewState.Loading -> {
                }

                is SignInViewState.Success -> {
                    findNavController().navigate(R.id.action_signUpFragment_to_homeFragment)
                }

                is SignInViewState.Error -> {
                    Snackbar.make(requireView(), state.errorMessage, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

}