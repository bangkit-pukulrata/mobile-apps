package com.dicoding.pukulenamcapstone

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val toolbar: Toolbar = findViewById (R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)

//        val buttonStart: Button = findViewById(R.id.button_home)
//        buttonStart.setOnClickListener {
//            val intent = Intent(this, HomeDetailActivity::class.java)
//            startActivity(intent)
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        if (menu != null) {
            for (i in 0 until menu.size()) {
                val item = menu.getItem(i)
                val icon = item.icon
                icon?.let {
                    val size = dpToPx(64) // Desired size in dp
                    it.setBounds(0, 0, size, size)
                    item.icon = it
                }
            }
        }
        return true
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }
}
