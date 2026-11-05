package com.example.simpleloginapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.content.Context
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.simpleloginapp.R
import com.example.simpleloginapp.auth.DemoAuth
import com.example.simpleloginapp.auth.DemoSession
import com.example.simpleloginapp.databinding.FragmentLoginBinding

/** Public offline demo login. No account is created and no password is stored. */
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var session: DemoSession

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        androidx.core.view.ViewCompat.setAccessibilityHeading(binding.tvTitle, true)
        session = DemoSession(requireContext())
        if (session.isActive) {
            navigateToProfile()
            return
        }
        session.clear() // Discard sessions created by older versions with arbitrary credentials.
        binding.etEmail.doAfterTextChanged {
            binding.tilEmail.error = null
            binding.tvLoginError.visibility = View.GONE
        }
        binding.etPassword.doAfterTextChanged {
            binding.tilPassword.error = null
            binding.tvLoginError.visibility = View.GONE
        }
        binding.btnLogin.setOnClickListener { attemptLogin() }
        binding.etPassword.setOnEditorActionListener { _, action, _ ->
            if (action == EditorInfo.IME_ACTION_DONE) {
                attemptLogin()
                true
            } else false
        }
    }

    private fun attemptLogin() {
        if (!binding.btnLogin.isEnabled) return
        val email = binding.etEmail.text?.toString().orEmpty()
        val password = binding.etPassword.text?.toString().orEmpty()
        val emailError = DemoAuth.emailError(email)
        val passwordError = DemoAuth.passwordError(password)
        binding.tilEmail.error = when (emailError) {
            DemoAuth.Error.REQUIRED -> getString(R.string.email_required)
            DemoAuth.Error.INVALID_EMAIL -> getString(R.string.invalid_email)
            else -> null
        }
        binding.tilPassword.error = when (passwordError) {
            DemoAuth.Error.REQUIRED -> getString(R.string.password_required)
            DemoAuth.Error.SHORT_PASSWORD -> getString(R.string.password_short)
            else -> null
        }
        binding.tvLoginError.visibility = View.GONE
        if (emailError != null || passwordError != null) {
            if (emailError != null) binding.etEmail.requestFocus() else binding.etPassword.requestFocus()
            return
        }
        if (!DemoAuth.accepts(email, password)) {
            binding.tvLoginError.visibility = View.VISIBLE
            return
        }
        binding.btnLogin.isEnabled = false
        (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(binding.root.windowToken, 0)
        session.start()
        navigateToProfile()
    }

    private fun navigateToProfile() {
        if (findNavController().currentDestination?.id == R.id.loginFragment) {
            findNavController().navigate(R.id.action_login_to_profile)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
