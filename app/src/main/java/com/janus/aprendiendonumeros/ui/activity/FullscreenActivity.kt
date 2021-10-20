package com.janus.aprendiendonumeros.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.janus.aprendiendonumeros.R

class FullscreenActivity : AppCompatActivity() {

    // Este método es el primero en ser llamado cuando la activity es iniciada
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* setContentView establece la vista de la actividad mediante un recurso de diseño,
         * Con la calse R obtenemos la referencia al recurso activity_main.xml del directorio res/layout
         * Ejemplo: R.layout.activity_main */
        setContentView(R.layout.activity_main)
    }
}