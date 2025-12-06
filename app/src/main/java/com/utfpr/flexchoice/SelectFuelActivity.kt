package com.utfpr.flexchoice

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.utfpr.flexchoice.databinding.ActivitySelectFuelBinding

class SelectFuelActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectFuelBinding
    private val fuelEnums = listOf(Fuel.GASOLINE, Fuel.ETHANOL)
    private val fuelOptions: List<String> = fuelEnums.map { it.description }
    private var callerId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySelectFuelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.listViewFuel)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // id do button que chamou pra sabermos onde preencher
        callerId = intent.getIntExtra(IntentKeys.REQUEST_ID, IntentKeys.DEFAULT_RETURN_VALUE)
        initFuelList()
    }

    private fun initFuelList() {
        val listView: ListView = binding.listViewFuel

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            fuelOptions
        )
        listView.adapter = adapter

        listView.setOnItemClickListener { parent, view, position, id ->
            val selectedFuel = fuelEnums[position]
            val fuelName = selectedFuel.description

            val resultIntent = Intent()
            resultIntent.putExtra(IntentKeys.FUEL_SELECTED, fuelName)
            resultIntent.putExtra(IntentKeys.RETURN_ID, callerId)

            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}