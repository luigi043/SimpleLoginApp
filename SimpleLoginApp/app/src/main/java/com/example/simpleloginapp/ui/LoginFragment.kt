package com.example.simpleloginapp.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.simpleloginapp.R
import com.example.simpleloginapp.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar

/**
 * Fragmento de login com validação local
 * 
 * Funcionalidades:
 * - Validação de email e senha
 * - Login fake com credenciais demo
 * - Navegação para perfil
 * - Animações futuristas
 * - Persistência com SharedPreferences
 */
class LoginFragment : Fragment() {
    
    // View Binding
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    
    // SharedPreferences para armazenamento local
    private lateinit var sharedPreferences: SharedPreferences
    
    // Credenciais de demonstração
    private val demoEmail = "demo@example.com"
    private val demoPassword = "password123"
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflar layout usando view binding
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Inicializar SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences(
            "user_prefs",
            Context.MODE_PRIVATE
        )
        
        // Verificar se usuário já está logado
        checkIfUserIsLoggedIn()
        
        // Configurar animações
        setupAnimations()
        
        // Configurar listeners
        setupTextWatchers()
        setupLoginButton()
    }
    
    /**
     * Verifica se o usuário já está logado
     * Se estiver, navega direto para o perfil
     */
    private fun checkIfUserIsLoggedIn() {
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            // Pequeno delay para melhor experiência
            Handler(Looper.getMainLooper()).postDelayed({
                navigateToProfile()
            }, 1000)
        }
    }
    
    /**
     * Configura animações iniciais
     */
    private fun setupAnimations() {
        // Animação de fade in para o título
        val fadeIn = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        binding.tvTitle.startAnimation(fadeIn)
        
        // Animação de slide up para o card
        val slideUp = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_right)
        binding.cardLogin.startAnimation(slideUp)
        
        // Animação de pulso para o botão
        val pulse = AnimationUtils.loadAnimation(requireContext(), R.anim.pulse)
        binding.btnLogin.startAnimation(pulse)
    }
    
    /**
     * Configura TextWatchers para validação em tempo real
     */
    private fun setupTextWatchers() {
        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateEmail(s.toString())
            }
            
            override fun afterTextChanged(s: Editable?) {}
        })
        
        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validatePassword(s.toString())
            }
            
            override fun afterTextChanged(s: Editable?) {}
        })
    }
    
    /**
     * Valida formato do email
     */
    private fun validateEmail(email: String) {
        when {
            email.isEmpty() -> {
                binding.tilEmail.error = null
                binding.tilEmail.isErrorEnabled = false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.tilEmail.error = getString(R.string.invalid_email)
            }
            else -> {
                binding.tilEmail.error = null
                binding.tilEmail.isErrorEnabled = false
            }
        }
    }
    
    /**
     * Valida força da senha
     */
    private fun validatePassword(password: String) {
        when {
            password.isEmpty() -> {
                binding.tilPassword.error = null
                binding.tilPassword.isErrorEnabled = false
            }
            password.length < 6 -> {
                binding.tilPassword.error = getString(R.string.password_short)
            }
            else -> {
                binding.tilPassword.error = null
                binding.tilPassword.isErrorEnabled = false
            }
        }
    }
    
    /**
     * Configura botão de login
     */
    private fun setupLoginButton() {
        binding.btnLogin.setOnClickListener {
            attemptLogin()
        }
    }
    
    /**
     * Tenta realizar login
     */
    private fun attemptLogin() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        
        // Validar inputs
        if (!validateInputs(email, password)) {
            return
        }
        
        // Autenticação fake
        val isAuthenticated = authenticateUser(email, password)
        
        if (isAuthenticated) {
            // Salvar estado de login
            saveLoginState(email)
            
            // Mostrar feedback positivo
            showLoginSuccess()
            
            // Navegar para perfil após delay
            Handler(Looper.getMainLooper()).postDelayed({
                navigateToProfile()
            }, 1500)
        } else {
            // Mostrar erro
            showLoginError()
        }
    }
    
    /**
     * Valida todos os campos de input
     */
    private fun validateInputs(email: String, password: String): Boolean {
        var isValid = true
        
        // Validar email
        if (email.isEmpty()) {
            binding.tilEmail.error = getString(R.string.email_required)
            shakeView(binding.tilEmail)
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = getString(R.string.invalid_email)
            shakeView(binding.tilEmail)
            isValid = false
        }
        
        // Validar senha
        if (password.isEmpty()) {
            binding.tilPassword.error = getString(R.string.password_required)
            shakeView(binding.tilPassword)
            isValid = false
        } else if (password.length < 6) {
            binding.tilPassword.error = getString(R.string.password_short)
            shakeView(binding.tilPassword)
            isValid = false
        }
        
        return isValid
    }
    
    /**
     * Autenticação fake do usuário
     */
    private fun authenticateUser(email: String, password: String): Boolean {
        // Credenciais demo
        if (email == demoEmail && password == demoPassword) {
            return true
        }
        
        // Qualquer email com senha de pelo menos 6 caracteres (para teste)
        if (email.isNotEmpty() && password.length >= 6) {
            return true
        }
        
        return false
    }
    
    /**
     * Salva estado de login no SharedPreferences
     */
    private fun saveLoginState(email: String) {
        sharedPreferences.edit().apply {
            putBoolean("isLoggedIn", true)
            putString("userEmail", email)
            putString("userName", email.substringBefore("@"))
            putLong("loginTimestamp", System.currentTimeMillis())
            apply()
        }
    }
    
    /**
     * Mostra feedback de login bem-sucedido
     */
    private fun showLoginSuccess() {
        // Desabilitar botão durante animação
        binding.btnLogin.isEnabled = false
        
        // Mudar texto do botão
        binding.btnLogin.text = "✓ ACCESS GRANTED"
        
        // Mudar cor do botão
        binding.btnLogin.setBackgroundColor(
            ContextCompat.getColor(requireContext(), R.color.success)
        )
        
        // Mostrar Toast
        Toast.makeText(
            requireContext(),
            "Login successful!",
            Toast.LENGTH_SHORT
        ).show()
    }
    
    /**
     * Mostra feedback de erro no login
     */
    private fun showLoginError() {
        // Animação de shake
        val shake = AnimationUtils.loadAnimation(requireContext(), R.anim.shake)
        binding.cardLogin.startAnimation(shake)
        
        // Snackbar de erro
        Snackbar.make(
            binding.root,
            "Invalid credentials. Try: demo@example.com / password123",
            Snackbar.LENGTH_LONG
        ).setBackgroundTint(
            ContextCompat.getColor(requireContext(), R.color.error)
        ).setTextColor(
            ContextCompat.getColor(requireContext(), R.color.white)
        ).show()
    }
    
    /**
     * Navega para a tela de perfil
     */
    private fun navigateToProfile() {
        findNavController().navigate(R.id.action_login_to_profile)
    }
    
    /**
     * Animação de shake para indicar erro
     */
    private fun shakeView(view: View) {
        val shake = AnimationUtils.loadAnimation(requireContext(), R.anim.shake)
        view.startAnimation(shake)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
