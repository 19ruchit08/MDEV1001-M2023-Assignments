package com.example.mdev1001_m2023_ice11b

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.mdev1001_m2023_ice11b.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Register : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    private lateinit var firebaseauth : FirebaseAuth
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseauth = FirebaseAuth.getInstance()

        binding.registeredButton.setOnClickListener{
            val firstname =binding.firstnameEditText.text.toString()
            val lastname =binding.lastnameEditText.text.toString()
            val email =binding.emailEditText.text.toString()
            val userename =binding.usernameEditText.text.toString()
            val password =binding.passwordEditText.text.toString()
            val conformpassword =binding.confirmPasswordEditText.text.toString()

            if(firstname.isNotEmpty() && lastname.isNotEmpty() && email.isNotEmpty() && userename.isNotEmpty() && password.isNotEmpty() && conformpassword.isNotEmpty())
            {
                if (password == conformpassword){
                    firebaseauth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{ task->
                        if (task.isSuccessful){
                            // Store the username and email mapping in Firestore
                            val db = FirebaseFirestore.getInstance()
                            db.collection("usernames").document(userename)
                                .set(hashMapOf("email" to email))
                                .addOnSuccessListener {
                                    println("Document successfully written!")
                                }
                                .addOnFailureListener { e ->
                                    println("Error writing document: $e")
                                }

                            val intent = Intent(this,LoginActivity::class.java)
                            startActivity(intent)
                        }else{
                            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(this, "Feild cannot be Empty", Toast.LENGTH_SHORT).show()
            }

        }
        binding.loginRedirectText.setOnClickListener{
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }



}