package com.example.ecommerceapp.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.ecommerceapp.R
import com.example.ecommerceapp.databinding.FragmentSplashBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(layoutInflater)
        auth = Firebase.auth

        Handler(Looper.getMainLooper()).postDelayed({
            auth.currentUser?.let {
                findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
            } ?: findNavController().navigate(R.id.action_splashFragment_to_signInragment)


        },3000)
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }


}