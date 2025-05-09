package com.martinez_orozco_moya_cuevas.sleepytraveler

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class MoodActivity : AppCompatActivity() {

    private var emocionSeleccionada: String? = null
    private var valorSeleccionado: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Comprobación de si ya se abrió hoy
        val prefs = getSharedPreferences("moodPrefs", MODE_PRIVATE)
        val ultimaFecha = prefs.getString("ultima_fecha", null)
        val hoy = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

        if (ultimaFecha == hoy) {
            Toast.makeText(this, "Ya registraste tu emoción hoy.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }


        setContentView(R.layout.activity_mood)

        val tvFecha = findViewById<TextView>(R.id.tvFecha)
        val tvValor = findViewById<TextView>(R.id.tvValorSeleccionado)
        val seekBar = findViewById<SeekBar>(R.id.seekBar)
        val listView = findViewById<ListView>(R.id.listaEmociones)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarEmocion)

        tvFecha.text = "Fecha: $hoy"

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                valorSeleccionado = progress + 1
                tvValor.text = "Valor seleccionado: $valorSeleccionado"
            }

            override fun onStartTrackingTouch(sb: SeekBar?) {}
            override fun onStopTrackingTouch(sb: SeekBar?) {}
        })

        val emociones = listOf(
            "Feliz", "Triste", "Enojado",
            "Ansioso", "Estresado", "Aburrido",
            "Emocionado", "Nostálgico"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, emociones)
        listView.adapter = adapter


        listView.setOnItemClickListener { _, _, position, _ ->
            emocionSeleccionada = emociones[position]
            Toast.makeText(this, "Emoción seleccionada: $emocionSeleccionada", Toast.LENGTH_SHORT).show()
        }

        btnGuardar.setOnClickListener {
            if (emocionSeleccionada == null) {
                Toast.makeText(this, "Selecciona una emoción primero", Toast.LENGTH_SHORT).show()
            } else {

                prefs.edit().putString("ultima_fecha", hoy).apply()

                Toast.makeText(
                    this,
                    "Guardado: $emocionSeleccionada ($valorSeleccionado)",
                    Toast.LENGTH_SHORT
                ).show()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}
