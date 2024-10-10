package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    var widget: TextView = findViewById(R.id.hello);
    val text:String = "Hello Erdenee";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        widget.setOnClickListener {
            widget.setText(text)
        }
    }
}