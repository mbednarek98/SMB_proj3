package smb.s18579.mb.shoplist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import smb.s18579.mb.shoplist.databinding.ActivityCredentialsBinding

class CredentialsActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private val binding by lazy { ActivityCredentialsBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()



        binding.registerBtn.setOnClickListener {
            auth.createUserWithEmailAndPassword(binding.emailValue.text.toString(), binding.passwordValue.text.toString()).addOnCompleteListener {
                onComplete(it)
            }
        }

        binding.loginBtn.setOnClickListener {
            auth.signInWithEmailAndPassword(binding.emailValue.text.toString(), binding.passwordValue.text.toString()).addOnCompleteListener {
                onComplete(it)
            }
        }
    }

    private fun onComplete(auth : Task<AuthResult>){
        when {
            auth.isSuccessful -> {
                startActivity(Intent(this,MainActivity::class.java).putExtra("uid",auth.result.user?.uid))
            }
            else -> {
                Toast.makeText(this,  auth.exception.toString(), Toast.LENGTH_SHORT).show()
            }

        }
    }
}