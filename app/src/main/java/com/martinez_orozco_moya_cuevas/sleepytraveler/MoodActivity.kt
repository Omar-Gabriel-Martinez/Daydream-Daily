package com.martinez_orozco_moya_cuevas.sleepytraveler

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class MoodActivity : AppCompatActivity() {

    private var emocionSeleccionada: String? = null
    private var valorSeleccionado: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("moodPrefs", MODE_PRIVATE)
        val ultimaFecha = prefs.getString("ultima_fecha", null)
        val hoy = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

        // Validar si ya se registró emoción hoy
        if (ultimaFecha == hoy) {
            Toast.makeText(this, "Ya registraste tu emoción hoy.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_mood)

        // Obtener el nombre del usuario de SharedPreferences (asumiendo que lo guardas aquí)
        val nombreUsuario = prefs.getString("nombre_usuario", "Usuario")

        val tvSaludoUsuario = findViewById<TextView>(R.id.tvSaludoUsuario)
        tvSaludoUsuario.text = "Hola, $nombreUsuario"

        val tvFecha = findViewById<TextView>(R.id.tvFecha)
        tvFecha.text = hoy // Establecer fecha actual

        // Aquí implementa la selección de emociones y lógica adicional
        val spinnerEmociones = findViewById<Spinner>(R.id.spinnerEmociones)
        val emociones = listOf(
            "Feliz", "Triste", "Enojado",
            "Ansioso", "Estresado", "Aburrido",
            "Emocionado", "Nostálgico"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, emociones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEmociones.adapter = adapter

        spinnerEmociones.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View, position: Int, id: Long) {
                emocionSeleccionada = emociones[position]
                valorSeleccionado = position + 1
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                emocionSeleccionada = emociones[0]
                valorSeleccionado = 1
            }
        }

        val btnGuardarEmocion = findViewById<Button>(R.id.btnGuardarEmocion)
        btnGuardarEmocion.setOnClickListener {
            prefs.edit().putString("ultima_fecha", hoy).apply()
            prefs.edit().putString("emocion", emocionSeleccionada).apply()
            prefs.edit().putInt("valor_emocion", valorSeleccionado).apply()

            Toast.makeText(this, "Emoción guardada: $emocionSeleccionada", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
