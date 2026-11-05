package com.example.simpleloginapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.simpleloginapp.R
import com.example.simpleloginapp.auth.DemoAuth
import com.example.simpleloginapp.auth.DemoSession
import com.example.simpleloginapp.databinding.FragmentProfileBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Displays the locally saved demo session and supports confirmed sign-out. */
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var session: DemoSession
    private var logoutDialog: AlertDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        androidx.core.view.ViewCompat.setAccessibilityHeading(binding.tvProfileTitle, true)
        session = DemoSession(requireContext())
        if (!session.isActive) {
            navigateToLogin()
            return
        }
        binding.tvUserEmail.text = DemoAuth.EMAIL
        binding.tvMemberSince.text = SimpleDateFormat("MMM d, yyyy 'at' HH:mm", Locale.ENGLISH)
            .format(Date(session.startedAt))
        binding.btnLogout.setOnClickListener { showLogoutConfirmation() }
    }

    private fun showLogoutConfirmation() {
        if (logoutDialog?.isShowing == true) return
        logoutDialog = com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.logout_title)
            .setMessage(R.string.logout_confirmation)
            .setPositiveButton(R.string.logout_button) { _, _ ->
                session.clear()
                navigateToLogin()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun navigateToLogin() {
        if (findNavController().currentDestination?.id == R.id.profileFragment) {
            findNavController().navigate(R.id.action_profile_to_login)
        }
    }

    override fun onDestroyView() {
        logoutDialog?.dismiss()
        logoutDialog = null
        _binding = null
        super.onDestroyView()
    }
}
