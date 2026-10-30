package com.example.simpleloginapp.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.simpleloginapp.R
import com.example.simpleloginapp.databinding.FragmentProfileBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Fragmento de perfil do usuário
 * 
 * Funcionalidades:
 * - Exibir informações do usuário
 * - Logout com confirmação
 * - Animações
 * - Persistência de dados
 */
class ProfileFragment : Fragment() {
    
    // View Binding
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    
    // SharedPreferences
    private lateinit var sharedPreferences: SharedPreferences
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Inicializar SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences(
            "user_prefs",
            Context.MODE_PRIVATE
        )
        
        // Carregar dados do usuário
        loadUserData()
        
        // Configurar animações
        setupAnimations()
        
        // Configurar botão de logout
        setupLogoutButton()
    }
    
    /**
     * Carrega dados do usuário do SharedPreferences
     */
    private fun loadUserData() {
        val email = sharedPreferences.getString("userEmail", "user@example.com") ?: "user@example.com"
        val userName = sharedPreferences.getString("userName", "User") ?: "User"
        val loginTime = sharedPreferences.getLong("loginTimestamp", System.currentTimeMillis())
        
        binding.tvUserEmail.text = email
        binding.tvUserName.text = userName.uppercase(Locale.getDefault())
        
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(Date(loginTime))
        binding.tvMemberSince.text = formattedDate.uppercase(Locale.getDefault())
        
        val accountType = if (email.contains("@example.com")) {
            "PREMIUM TIER"
        } else {
            getString(R.string.free_account)
        }
        binding.tvAccountType.text = accountType
    }
    
    /**
     * Configura animações da tela
     */
    private fun setupAnimations() {
        val fadeIn = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        binding.ivAvatar.startAnimation(fadeIn)
        
        val slideUp = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_right)
        binding.cardProfile.startAnimation(slideUp)
        
        val pulse = AnimationUtils.loadAnimation(requireContext(), R.anim.pulse)
        binding.ivAvatar.startAnimation(pulse)
    }
    
    /**
     * Configura botão de logout
     */
    private fun setupLogoutButton() {
        binding.btnLogout.setOnClickListener {
            showLogoutConfirmation()
        }
    }
    
    /**
     * Mostra diálogo de confirmação de logout
     */
    private fun showLogoutConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirm Logout")
            .setMessage(getString(R.string.logout_confirmation))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                performLogout()
            }
            .setNegativeButton(getString(R.string.no), null)
            .setCancelable(true)
            .create()
            .show()
    }
    
    /**
     * Realiza logout do usuário
     */
    private fun performLogout() {
        // Limpar dados do usuário
        sharedPreferences.edit().clear().apply()
        
        // Desabilitar botão durante animação
        binding.btnLogout.isEnabled = false
        binding.btnLogout.text = "LOGGING OUT..."
        
        // Animação de fade out
        val fadeOut = AnimationUtils.loadAnimation(requireContext(), android.R.anim.fade_out)
        binding.cardProfile.startAnimation(fadeOut)
        
        // Navegar de volta para login após delay com verificação de segurança
        Handler(Looper.getMainLooper()).postDelayed({
            if (isAdded && findNavController().currentDestination?.id == R.id.profileFragment) {
                navigateToLogin()
            }
        }, 800)
    }
    
    /**
     * Navega de volta para a tela de login
     */
    private fun navigateToLogin() {
        findNavController().navigate(R.id.action_profile_to_login)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
