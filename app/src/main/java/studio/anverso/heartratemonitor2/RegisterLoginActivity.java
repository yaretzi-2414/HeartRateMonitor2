package studio.anverso.heartratemonitor2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RegisterLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_login);
    }

    //Método para dirigirnos a la pantalla de iniciar sesión
    public void irIniciar(View view){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    //Método para dirigirnos a la pantalla de registrar persona que tiene chaleco o persona que va a monitorear ritmo cardiaco de otra
    public void irRegistrar(View view){
        Intent i = new Intent(this, AdminOrUser.class);
        startActivity(i);

    }


}