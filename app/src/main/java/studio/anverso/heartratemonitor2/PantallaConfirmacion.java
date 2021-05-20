package studio.anverso.heartratemonitor2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PantallaConfirmacion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_confirmacion);


    }

    //Método para dirigirnos a la pantalla de inicio de sesión
    public void irLogin(View view){
        Intent i = new Intent(PantallaConfirmacion.this, LoginActivity.class);
        startActivity(i);
    }
}