package com.utfpr.flexchoice

import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.utfpr.flexchoice.databinding.ActivityMainBinding
import com.utfpr.flexchoice.databinding.ActivitySelectFuelBinding

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // botoões de busca
        binding.buttonSearchFuel1.setOnClickListener { view -> searchTypeFuel(view) }
        binding.buttonSearchFuel2.setOnClickListener { view -> searchTypeFuel(view) }

        // cálculo

        // limpar
        binding.buttonClearScreen.setOnClickListener { clearScreen() }

    }


    private fun searchTypeFuel(view: View) {
        val intent = Intent(this, SelectFuelActivity::class.java)
        intent.putExtra(IntentKeys.REQUEST_ID, view.id)
        getResult.launch( intent)
    }

    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val fuelName = result.data?.getStringExtra(IntentKeys.FUEL_SELECTED)
            val returnId = result.data?.getIntExtra(IntentKeys.RETURN_ID, -1)

            validateSelectFuelResponse(fuelName, returnId)

            val selectedFuel = fuelName
            val descriptionToDisplay = selectedFuel

            when (returnId) {
                binding.buttonSearchFuel1.id -> {
                    binding.textLabelInputFuel1.setText(descriptionToDisplay)
                }
                binding.buttonSearchFuel2.id -> {
                    binding.textLabelInputFuel2.setText(descriptionToDisplay)
                }
            }
        }
    }

    fun validateSelectFuelResponse(fuelName: String?, returnId: Int?) {
        if (fuelName.isNullOrBlank()) {
            Toast.makeText(this, "Erro ao selecionar nome do combustível.", Toast.LENGTH_SHORT).show()
            return
        }

        if (returnId == -1 || returnId == null) {
            Toast.makeText(this, "ID de retorno inválido.", Toast.LENGTH_SHORT).show()
            return
        }
    }

    fun clearScreen() {
        binding.textLabelInputFuel1.setText("")
        binding.textLabelInputFuel2.setText("")

        binding.textInputFuel1Consumption.setText("")
        binding.textInputFuel2Consumption.setText("")

        binding.textInputFuel1Price.setText("")
        binding.textInputFuel2Price.setText("")
    }

}