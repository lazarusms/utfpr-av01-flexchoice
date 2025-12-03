package com.utfpr.flexchoice

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.utfpr.flexchoice.databinding.ActivityMainBinding

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
        binding.buttonCalculateFuel.setOnClickListener { calculateBestFuel() }

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

            when (returnId) {
                binding.buttonSearchFuel1.id -> {
                    if (selectedFuel == binding.textLabelInputFuel2.text.toString()) {
                        showDuplicateMessage(selectedFuel) {
                            binding.textLabelInputFuel1.setText(selectedFuel)
                        }
                    } else {
                        binding.textLabelInputFuel1.setText(selectedFuel)
                    }
                }
                binding.buttonSearchFuel2.id -> {
                    if (selectedFuel == binding.textLabelInputFuel1.text.toString()) {
                        showDuplicateMessage(selectedFuel) {
                            binding.textLabelInputFuel2.setText(selectedFuel)
                        }
                    } else {
                        binding.textLabelInputFuel2.setText(selectedFuel)
                    }
                }
            }
        }
    }

    private fun showDuplicateMessage(fuelName: String?, onConfirm: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle("Opaaa!")
            .setMessage("Você está selecionando $fuelName nos dois campos.\n\nVocê quer mesmo comparar dois combustíveis iguais?")
            .setPositiveButton("Continuar") { _, _ ->
                onConfirm()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun validateSelectFuelResponse(fuelName: String?, returnId: Int?) {
        if (fuelName.isNullOrBlank()) {
            Toast.makeText(this, "Erro ao selecionar nome do combustível.", Toast.LENGTH_SHORT).show()
            return
        }

        if (returnId == -1 || returnId == null) {
            Toast.makeText(this, "ID de retorno inválido.", Toast.LENGTH_SHORT).show()
            return
        }
    }

    private fun clearScreen() {
        binding.textLabelInputFuel1.setText("")
        binding.textLabelInputFuel2.setText("")

        binding.textInputFuel1Consumption.setText("")
        binding.textInputFuel2Consumption.setText("")

        binding.textInputFuel1Price.setText("")
        binding.textInputFuel2Price.setText("")
    }

    private fun calculateBestFuel() {
        val fuel1Name = binding.textLabelInputFuel1.text.toString()
        val fuel2Name = binding.textLabelInputFuel2.text.toString()

        val consumption1 = binding.textInputFuel1Consumption.text.toString().toDoubleOrNull()
        val consumption2 = binding.textInputFuel2Consumption.text.toString().toDoubleOrNull()

        val price1 = binding.textInputFuel1Price.text.toString().toDoubleOrNull()
        val price2 = binding.textInputFuel2Price.text.toString().toDoubleOrNull()

        if (fuel1Name.isBlank() || fuel2Name.isBlank() ||
            consumption1 == null || consumption2 == null ||
            price1 == null || price2 == null) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            return
        }

        val costPerKm1 = price1 / consumption1
        val costPerKm2 = price2 / consumption2

        val result = if (costPerKm1 < costPerKm2) {
            "$fuel1Name vale bemmm mais a pena!\nOlha o custo: R$ %.2f/km".format(costPerKm1)
        } else {
            "$fuel2Name vale bemmm mais a pena!\nOlha o custo: R$ %.2f/km".format(costPerKm2)
        }

        Snackbar.make(binding.root, result,  Snackbar.LENGTH_LONG).show()
      //  Toast.makeText(this, result, Toast.LENGTH_LONG).show()
    }



}