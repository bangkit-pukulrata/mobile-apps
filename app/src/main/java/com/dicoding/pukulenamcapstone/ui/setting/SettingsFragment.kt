package com.dicoding.pukulenamcapstone.ui.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.dicoding.pukulenamcapstone.R
import com.dicoding.pukulenamcapstone.ui.auth.AuthenticationActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SettingsFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inisialisasi Firebase Auth
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi views
        val logoutTextView: TextView = view.findViewById(R.id.textView28)
        val userNameTextView: TextView = view.findViewById(R.id.textView17)
        val userEmailTextView: TextView = view.findViewById(R.id.textView26)

        // Set detail pengguna
        currentUser?.let { user ->
            userNameTextView.text = user.displayName ?: "No display name"
            userEmailTextView.text = user.email ?: "No email"
        }

        logoutTextView.setOnClickListener {
            logoutUser()
        }
    }

    private fun logoutUser() {
        // Hapus status login dari SharedPreferences
        val sharedPreferences = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("isLoggedIn", false)
            apply()
        }

        // Logout dari Firebase
        auth.signOut()

        // Arahkan pengguna ke AuthenticationActivity
        val intent = Intent(activity, AuthenticationActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }
}
