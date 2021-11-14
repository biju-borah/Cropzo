package com.example.cropzo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_registration.*
import java.util.concurrent.TimeUnit

class RegistrationActivity : AppCompatActivity() {
    var mAuth: FirebaseAuth? = null
    var mobileNumber: String = ""
    var verificationID: String = ""
    var token_: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        mAuth = FirebaseAuth.getInstance()

        btnSignIn.setOnClickListener {

            mobileNumber = etNumber.text.toString()

            if (mobileNumber.length > 0) {
                progressBar.visibility = View.VISIBLE
                loginTask()
            } else {
                etNumber.setError("Enter valid phone number")
            }
        }

    }
    private fun loginTask() {

        val mCallBacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                if (credential != null) {
                    signInWithPhoneAuthCredential(credential)
                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                progressBar.visibility = View.GONE
//                toast("Invalid phone number or verification failed.")
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                if (token != null) {
                    super.onCodeSent(verificationId, token)
                }
                progressBar.visibility = View.GONE
                verificationID = verificationId.toString()
                token_ = token.toString()

                etNumber.setText("")

                etNumber.setHint("Enter OTP ")
                btnSignIn.setText("Verify OTP")

                btnSignIn.setOnClickListener {
                    progressBar.visibility = View.VISIBLE
                    verifyAuthentication(verificationID, etNumber.text.toString())
                }

                Log.e("Login : verificationId ", verificationId)
                Log.e("Login : token ", token_)

            }

            override fun onCodeAutoRetrievalTimeOut(verificationId: String) {
                super.onCodeAutoRetrievalTimeOut(verificationId)
                progressBar.visibility = View.GONE
                // toast("Time out")
            }
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            mobileNumber,            // Phone number to verify
            60,                  // Timeout duration
            TimeUnit.SECONDS,        // Unit of timeout
            this,                // Activity (for callback binding)
            mCallBacks);

    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {

        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(this@RegistrationActivity
            ) { task ->
                if (task.isSuccessful()) {
                    val user = task.getResult()?.getUser()
                    progressBar.visibility = View.GONE
                    startActivity(Intent(this@RegistrationActivity, MainActivity::class.java))

                } else {
                    if (task.getException() is FirebaseAuthInvalidCredentialsException) {
                        progressBar.visibility = View.GONE
                        //                            toast("Invalid OPT")
                    }
                }
            }
    }

    private fun verifyAuthentication(verificationID: String, otpText: String) {

        val phoneAuthCredential = PhoneAuthProvider.getCredential(verificationID, otpText) as PhoneAuthCredential
        signInWithPhoneAuthCredential(phoneAuthCredential)
    }
}