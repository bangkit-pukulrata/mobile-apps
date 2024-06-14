package com.dicoding.pukulenamcapstone.ui.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.pukulenamcapstone.R
import com.dicoding.pukulenamcapstone.ui.MainActivity
import com.dicoding.pukulenamcapstone.ui.customview.EditTextEmail
import com.dicoding.pukulenamcapstone.ui.customview.EditTextPassword
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class AuthenticationActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailField: EditTextEmail
    private lateinit var passwordField: EditTextPassword
    private lateinit var loginButton: Button
    private lateinit var registerTextView: TextView
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var progressBar: ProgressBar
    private lateinit var rememberMeCheckbox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        // Inisialisasi Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Inisialisasi views
        emailField = findViewById(R.id.login_email)
        passwordField = findViewById(R.id.login_password)
        loginButton = findViewById(R.id.button_login)
        registerTextView = findViewById(R.id.textView6)
        progressBar = findViewById(R.id.progressBar)
        rememberMeCheckbox = findViewById(R.id.checkbox_remember_me)

        // Periksa status login
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        loginButton.setOnClickListener {
            val email = emailField.text.toString()
            val password = passwordField.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                progressBar.visibility = ProgressBar.VISIBLE // Tampilkan loading
                loginUser(email, password)
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        registerTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        findViewById<Button>(R.id.button_google).setOnClickListener {
            googleSignIn()
        }
    }

    private fun googleSignIn() {
        progressBar.visibility = ProgressBar.VISIBLE // Tampilkan loading
        val signInClient = googleSignInClient.signInIntent
        launcher.launch(signInClient)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        Log.d("AuthenticationActivity", "Result: $result")
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            manageResult(task)
        } else {
            progressBar.visibility = ProgressBar.GONE // Sembunyikan loading jika gagal
            Toast.makeText(this, "Google sign-in failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun manageResult(task: Task<GoogleSignInAccount>) {
        val account: GoogleSignInAccount? = task.result
        if (account != null) {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential).addOnCompleteListener {
                progressBar.visibility = ProgressBar.GONE // Sembunyikan loading setelah autentikasi
                if (task.isSuccessful) {
                    saveLoginStatus()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            progressBar.visibility = ProgressBar.GONE // Sembunyikan loading jika gagal
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                progressBar.visibility = ProgressBar.GONE // Sembunyikan loading setelah autentikasi
                if (task.isSuccessful) {
                    saveLoginStatus()
                    // Login success, update UI with the signed-in user's information
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If login fails, display a message to the user.
                    Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveLoginStatus() {
        if (rememberMeCheckbox.isChecked) {
            val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                putBoolean("isLoggedIn", true)
                apply()
            }
        }
    }
}
