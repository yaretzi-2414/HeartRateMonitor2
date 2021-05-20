package studio.anverso.heartratemonitor2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MedicionInfoActivity extends AppCompatActivity {

    //Declaración de variables
    TextView txtBPM, txtInfoBPM;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicion_info);

        //Obtiene los datos de la pantalla anterior donde se obtuvo la medida del ritmo cardiaco
        Bundle datos = this.getIntent().getExtras();
        String medida = datos.getString("medida");
        Integer medida_int = datos.getInt("medida_int");

        txtBPM = findViewById(R.id.txtBPM);
        txtInfoBPM = findViewById(R.id.txtInfoBPM);
        fab = findViewById(R.id.fab);
        txtBPM.setText(medida);

        //Condición para saber que mensaje mostrar respecto al ritmo cardiaco obtenido del sensor
        if(medida_int < 60){
            txtInfoBPM.setText(getString(R.string.txt_info_bajo));
        }else if (medida_int > 100){
            txtInfoBPM.setText(getString(R.string.txt_info_alto));
        }else{
            txtInfoBPM.setText(getString(R.string.txt_info_normal));
        }

        //Al presionar sobre el icono de check se inicia la pantalla de historial
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MedicionInfoActivity.this, HistorialAdminActivity.class);
                startActivity(intent);
            }
        });

    }
}