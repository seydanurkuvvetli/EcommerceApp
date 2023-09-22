package com.example.ecommerceapp.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.repository.UserRepository
import com.example.ecommerceapp.databinding.FragmentProfileBinding
import com.example.ecommerceapp.databinding.FragmentSignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
    class ProfileFragment : Fragment() {

        private lateinit var binding: FragmentProfileBinding
        private val profileViewModel: ProfileViewModel by viewModels()

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            binding = FragmentProfileBinding.inflate(inflater, container, false)
            return binding.root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSignOut.setOnClickListener {
            profileViewModel.signOut()
        }
        profileViewModel.signOutResult.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is SignOutResult.Loading -> {

                }
                is SignOutResult.Success -> {
                    findNavController().navigate(R.id.action_profileFragment_to_signInFragment)
                } is SignOutResult.Error -> {

            }
            }
        })
    }
    }