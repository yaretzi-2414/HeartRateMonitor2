package studio.anverso.heartratemonitor2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public class SettingsAdminActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    TextView settings_option1, settings_option2, settings_option3;
    public static final String[] languages ={"Language","English", "Español"};
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "Text";
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_admin);

        mAuth = FirebaseAuth.getInstance();

        //Esta parte indica qué pantalla se abrirá al presionar algún elemento del menú de navegación inferior
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.ajustes);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()){
                    case R.id.contacto:
                        startActivity(new Intent(SettingsAdminActivity.this, ContactoAdminActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.medida:
                        startActivity(new Intent(SettingsAdminActivity.this, AdminHome.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.historial:
                        startActivity(new Intent(SettingsAdminActivity.this, HistorialAdminActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.info:
                        startActivity(new Intent(SettingsAdminActivity.this, InfoAdminActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.ajustes:

                        return true;


                }

                return false;
            }
        });

        //Declaración de variables TextView
        settings_option1 = findViewById(R.id.settings_usar_chaleco);
        settings_option2= findViewById(R.id.settings_usar_app);
        settings_option3 = findViewById(R.id.settings_ritmo_cardiaco);

        //Código para ver que pantalla se abrirá al presionar sobre el texto ¿Cómo funciona el chaleco?
        settings_option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingsAdminActivity.this, UsarChaleco.class);
                startActivity(i);
            }
        });

        //Código para ver que pantalla se abrirá al presionar sobre el texto ¿Cómo tomar mediciones con la app?
        settings_option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingsAdminActivity.this, UsarApp.class);
                startActivity(i);
            }
        });

        //Código para ver que pantalla se abrirá al presionar sobre el texto ¿Qué es el ritmo cardiaco?
        settings_option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingsAdminActivity.this, RitmoCardiaco.class);
                startActivity(i);
            }
        });

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
                    setLocal(SettingsAdminActivity.this, "es");

                    Intent intent = new Intent(SettingsAdminActivity.this, AdminHome.class);
                    startActivity(intent);
                }
                else if(selectedLang.equals("English")){
                    setLocal(SettingsAdminActivity.this, "en");
                    Intent intent = new Intent(SettingsAdminActivity.this, AdminHome.class);
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
    }

    //Método para cerrar sesión de la cuenta y al completarse nos dirige a la pantalla para ingresar
    public void LogOut(View view){
        if(mAuth.getCurrentUser() != null)
            mAuth.signOut();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);

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
}