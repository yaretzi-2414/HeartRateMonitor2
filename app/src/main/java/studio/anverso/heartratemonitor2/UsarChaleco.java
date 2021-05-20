package studio.anverso.heartratemonitor2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class UsarChaleco extends AppCompatActivity {

    TextView settings_usar_chaleco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usar_chaleco);

        //Instrucciones para habilitar el scroll en TextView donde se muestra todo el texto
        settings_usar_chaleco = findViewById(R.id.info_usar_chaleco);
        settings_usar_chaleco.setMovementMethod(new ScrollingMovementMethod());
    }
}