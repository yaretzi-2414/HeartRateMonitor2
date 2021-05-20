package studio.anverso.heartratemonitor2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class UsarApp extends AppCompatActivity {

    TextView settings_usar_app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usar_app);

        //Instrucciones para habilitar el scroll en TextView donde se muestra todo el texto
        settings_usar_app = findViewById(R.id.info_usar_app);
        settings_usar_app.setMovementMethod(new ScrollingMovementMethod());
    }
}