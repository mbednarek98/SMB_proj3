package smb.s18579.mb.shoplist

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import smb.s18579.mb.shoplist.database.AppDB
import smb.s18579.mb.shoplist.database.Helper
import smb.s18579.mb.shoplist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Helper.db = AppDB.open(applicationContext)
        val pref = getSharedPreferences("save",Context.MODE_PRIVATE)
        preferencesSetUp(pref)
        radioButtonSetUp(pref)
        onClickRadioButton(pref)
        binding.shoplistbutton.setOnClickListener {
            startActivity(Intent(this, ProductListActivity::class.java))
        }
    }

    private fun preferencesSetUp(preferences: SharedPreferences){
        with(binding) {
            val textSize = preferences.getFloat("TEXTSIZE", 12F)
            val textColor = preferences.getInt("TEXTCOLOR", Color.BLACK)
            radioButton12dp.textSize = textSize
            radioButton16dp.textSize = textSize
            colorblack.textSize = textSize
            colorpurple.textSize = textSize

            radioButton12dp.setTextColor(textColor)
            radioButton16dp.setTextColor(textColor)
            colorblack.setTextColor(textColor)
            colorpurple.setTextColor(textColor)
        }
    }

    private fun radioButtonSetUp(preferences: SharedPreferences){
        with(binding) {
            val textSize = preferences.getFloat("TEXTSIZE", 12F)
            val textColor = preferences.getInt("TEXTCOLOR", Color.BLACK)
            when (textSize) {
                12F -> {
                    radioButton12dp.isChecked = true
                    radioButton16dp.isChecked = false
                }
                else -> {
                    radioButton12dp.isChecked = false
                    radioButton16dp.isChecked = true
                }
            }

            when (textColor) {
                Color.BLACK -> {
                    colorblack.isChecked = true
                    colorpurple.isChecked = false
                }
                else -> {
                    colorblack.isChecked = false
                    colorpurple.isChecked = true
                }
            }
        }
    }

    private fun onClickRadioButton(preferences: SharedPreferences){
        val edit = preferences.edit()
        with(binding){
            radioButton12dp.setOnClickListener {
                edit.putFloat("TEXTSIZE", 12F)
                edit.apply()
                preferencesSetUp(preferences)
            }
            radioButton16dp.setOnClickListener {
                edit.putFloat("TEXTSIZE", 16F)
                edit.apply()
                preferencesSetUp(preferences)
            }
            colorblack.setOnClickListener {
                edit.putInt("TEXTCOLOR", Color.BLACK)
                edit.apply()
                preferencesSetUp(preferences)
            }
            colorpurple.setOnClickListener {
                edit.putInt("TEXTCOLOR", Color.MAGENTA)
                edit.apply()
                preferencesSetUp(preferences)
            }
        }
    }
}