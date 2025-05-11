package com.martinez_orozco_moya_cuevas.sleepytraveler

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar

class MoodActivity : AppCompatActivity() {

    private var emocionSeleccionada: String? = null
    private var valorSeleccionado: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        //Para el saludo
        val hora = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val saludo = when (hora) {
            in 6..11   -> "Buenos días"
            in 12..17  -> "Buenas tardes"
            in 18..21  -> "Buenas noches"
            else       -> "Hola"
        }


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

        // Saludo y fecha
        val nombreUsuario = prefs.getString("nombre_usuario", "Usuario")
        //findViewById<TextView>(R.id.tvSaludoUsuario).text = "Hola, $nombreUsuario"
        findViewById<TextView>(R.id.tvSaludoUsuario).text = "$saludo, $nombreUsuario!"
        findViewById<TextView>(R.id.tvFecha).text = hoy

        // Spinner de emociones (igual que antes) …
        val spinnerEmociones = findViewById<Spinner>(R.id.spinnerEmociones)
        val emociones = listOf(
            "Feliz", "Triste", "Enojado", "Ansioso",
            "Estresado", "Aburrido", "Emocionado", "Nostálgico"
        )
        val adapter = android.widget.ArrayAdapter(
            this, android.R.layout.simple_spinner_item, emociones
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
        spinnerEmociones.adapter = adapter
        spinnerEmociones.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>, view: android.view.View, position: Int, id: Long) {
                emocionSeleccionada = emociones[position]
            }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>) {
                emocionSeleccionada = emociones.first()
            }
        }

        // SeekBar y valor dinámico (igual que antes) …
        val seekBar = findViewById<SeekBar>(R.id.seekBar)
        val tvValorSeleccionado = findViewById<TextView>(R.id.tvValorSeleccionado)
        seekBar.progress = valorSeleccionado
        tvValorSeleccionado.text = valorSeleccionado.toString()
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar, progress: Int, fromUser: Boolean) {
                valorSeleccionado = progress
                tvValorSeleccionado.text = progress.toString()
            }
            override fun onStartTrackingTouch(sb: SeekBar) {}
            override fun onStopTrackingTouch(sb: SeekBar) {}
        })

        // Referencias a los EditText
        val etTitulo = findViewById<EditText>(R.id.etTituloEmocion)
        val etDescripcion = findViewById<EditText>(R.id.etDescripcionEmocion)

        // Botón guardar con validaciones
        findViewById<Button>(R.id.btnGuardarEmocion).setOnClickListener {
            val titulo = etTitulo.text.toString().trim()
            val descripcion = etDescripcion.text.toString().trim()

            if (titulo.length < 5) {
                etTitulo.error = "El título debe tener al menos 5 caracteres"
                etTitulo.requestFocus()
                return@setOnClickListener
            }
            if (descripcion.length < 10) {
                etDescripcion.error = "La descripción debe tener al menos 10 caracteres"
                etDescripcion.requestFocus()
                return@setOnClickListener
            }

            // Guardamos sólo si pasa las validaciones
            prefs.edit()
                .putString("ultima_fecha", hoy)
                .putString("emocion_titulo", titulo)
                .putString("emocion", emocionSeleccionada)
                .putInt("valor_emocion", valorSeleccionado)
                .putString("emocion_descripcion", descripcion)
                .apply()

            Toast.makeText(
                this,
                "Emoción guardada: $emocionSeleccionada\nTítulo: $titulo",
                Toast.LENGTH_SHORT
            ).show()

            val intent = Intent(this, MoodActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
