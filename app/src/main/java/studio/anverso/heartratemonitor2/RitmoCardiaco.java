package studio.anverso.heartratemonitor2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class RitmoCardiaco extends AppCompatActivity {

    TextView settings_ritmo_cardiaco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ritmo_cardiaco);

        //Instrucciones para habilitar el scroll en TextView donde se muestra todo el texto
        settings_ritmo_cardiaco = findViewById(R.id.info_ritmo_cardiaco);
        settings_ritmo_cardiaco.setMovementMethod(new ScrollingMovementMethod());


    }
}