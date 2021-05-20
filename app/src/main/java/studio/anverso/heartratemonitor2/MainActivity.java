package studio.anverso.heartratemonitor2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public class MainActivity extends AppCompatActivity {


    Spinner spinner;
    public static final String[] languages ={"Language","English", "Español"};
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "Text";
    private FirebaseAuth mAuth;
    private String langCode, langCode2;
    private String tipoUsuario;
    String currentUserID;
    DatabaseReference mRootReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        //Declaración de variables tipo SHARED PREFERENCES para guardar el idioma preferido a utilizar en la app
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Spinner que contiene los idiomas disponibles que están en la app
        spinner = findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedLang = adapterView.getItemAtPosition(i).toString();
                //Al elegir el idioma con la siguiente condición enviamos el código del idioma para que se realice el método setLocal
                //y al finalizar se abre la pantalla de inicio
                if (selectedLang.equals("Español")){
                    setLocal(MainActivity.this, "es");

                    Intent intent = new Intent(MainActivity.this, RegisterLoginActivity.class);
                    startActivity(intent);
                }
                else if(selectedLang.equals("English")){
                    setLocal(MainActivity.this, "en");
                    Intent intent = new Intent(MainActivity.this, RegisterLoginActivity.class);
                    startActivity(intent);
                }
                //Guarda en las preferencias de usuario el idioma elegido para la app
                editor.putString(TEXT, selectedLang);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mRootReference = FirebaseDatabase.getInstance().getReference();
    }


    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        //updateUI(currentUser);
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        langCode = sharedPreferences.getString(TEXT, "");
        if(currentUser != null){

            if (langCode.equals("Español")){
                setLocal(MainActivity.this, "es");
                startActivity(new Intent(this, LoginActivity.class));

            }
            else if(langCode.equals("English")){
                setLocal(MainActivity.this, "en");
                startActivity(new Intent(this, LoginActivity.class));

            }
        }
        checkSMSStatePermission();
    }



    //Método para configurar el idioma en la app
    public void setLocal(Activity activity, String langCode){
        Locale locale = new Locale(langCode);
        locale.setDefault(locale);

        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    //Método para revisar que este permitido el permiso de enviar SMS
    private void checkSMSStatePermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.SEND_SMS);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.i("Mensaje", "No se tiene permiso para enviar SMS.");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 225);
        } else {
            Log.i("Mensaje", "Se tiene permiso para enviar SMS!");
        }
    }

}