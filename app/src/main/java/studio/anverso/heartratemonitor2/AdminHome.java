package studio.anverso.heartratemonitor2;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spark.submitbutton.SubmitButton;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import studio.anverso.heartratemonitor2.Prevalent.Prevalent;

public class AdminHome extends AppCompatActivity {

    //Declaración de variables para la conexión BT
    private static final String TAG = "bluetooth1";

    private final String DEVICE_ADDRESS="00:20:10:00:0D:15";
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");//Serial Port Service ID
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    //Variables de los elementos que se muestran en la pantalla
    Button conectar;
    SubmitButton mBtnMedidaAnim;
    private TextView textView;
    boolean deviceConnected=false;
    Thread thread;
    byte buffer[];
    int bufferPosition;
    boolean stopThread;
    String medida;
    FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    String historialRandomKey;
    Integer medida_int;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        //Esta parte indica qué pantalla se abrirá al presionar algún elemento del menú de navegación inferior
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.medida);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()){
                    case R.id.contacto:
                        startActivity(new Intent(AdminHome.this, ContactoAdminActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.medida:
                        return true;
                    case R.id.historial:
                        startActivity(new Intent(AdminHome.this, HistorialAdminActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.info:
                        startActivity(new Intent(AdminHome.this, InfoAdminActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.ajustes:
                        startActivity(new Intent(AdminHome.this, SettingsAdminActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }

                return false;
            }
        });

        textView = findViewById(R.id.txtReceive);
        mBtnMedidaAnim = findViewById(R.id.btnMedidaAnim);
        conectar = findViewById(R.id.btnConectar);
        setUiEnabled(false);

        //Variables Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //Al presionar el boton de Medida se inician diversos métodos para obtener la medida del ritmo cardiaco brindado por el chaleco
        mBtnMedidaAnim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Se inicia el método que establece la conexión con el módulo Bluetooth del chaleco
               beginListenForData();

               //Hilo para obtener la medición del chaleco
                Thread hilo = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(5500);
                            guardarMedicion();
                        } catch (InterruptedException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                hilo.start();
            }
        });


    }

    //Método para desactivar el botón de conectar y mensaje si no se ha establecido la conexión con el módulo BT
    public void setUiEnabled(boolean bool)
    {
        conectar.setEnabled(!bool);
        mBtnMedidaAnim.setEnabled(bool);
        textView.setEnabled(bool);

    }

    //Método con el que se realiza la conexión del módulo bluetooth al celular
    public boolean BTinit()
    {
        boolean found=false;
        BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(),"Device doesnt Support Bluetooth",Toast.LENGTH_SHORT).show();
        }
        if(!bluetoothAdapter.isEnabled())
        {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter, 0);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        if(bondedDevices.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Please Pair the Device first",Toast.LENGTH_SHORT).show();
        }
        else
        {
            for (BluetoothDevice iterator : bondedDevices)
            {
                if(iterator.getAddress().equals(DEVICE_ADDRESS))
                {
                    device=iterator;
                    found=true;
                    break;
                }
            }
        }
        return found;
    }

    //Método para recibir y enviar datos a traves del modulo de BT
    public boolean BTconnect()
    {
        boolean connected=true;
        try {
            socket = device.createRfcommSocketToServiceRecord(PORT_UUID);
            socket.connect();
        } catch (IOException e) {
            e.printStackTrace();
            connected=false;
        }
        if(connected)
        {
            try {
                outputStream=socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStream=socket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        return connected;
    }

    //Método que muestra que la conexión fue exitosa
    public void onClickStart(View view) {
        if(BTinit())
        {
            if(BTconnect())
            {
                setUiEnabled(true);
                deviceConnected=true;
                textView.append("\nConnection Opened!\n");
            }

        }
    }

    //Método que obtiene los valores del chaleco
    public void beginListenForData()
    {
        final Handler handler = new Handler();
        stopThread = false;
        buffer = new byte[1024];
        Thread thread  = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopThread)
                {
                    try
                    {
                        int byteCount = inputStream.available();
                        if(byteCount > 0)
                        {
                            byte[] rawBytes = new byte[byteCount];
                            inputStream.read(rawBytes);
                            final String string=new String(rawBytes,"UTF-8");
                            handler.post(new Runnable() {
                                public void run()
                                {
                                    textView.setText(string);
                                    medida = textView.getText().toString();
                                    medida_int = Integer.parseInt(getNumeros(medida));
                                }
                            });

                        }
                    }
                    catch (IOException ex)
                    {
                        stopThread = true;
                    }
                }
            }
        });

        thread.start();


    }



    //Método para guardar los datos de la medición en la base de datos
    public void guardarMedicion() throws IOException{
        stopThread = true;
        outputStream.close();
        inputStream.close();

        //Condición para enviar SMS al contacto de emergencia registrado si existe un ritmo cardiaco fuera del rango definido como normal
        if(medida_int < 60 || medida_int > 100){
            String phone_emergency = Prevalent.currentOnlineUser.getPhone_contact();
            String text = getString(R.string.msj_texto_emergencia);

            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phone_emergency, null, text, null, null);
        }

        //Código para obtener el día y hora en la que se realizan las mediciones para posteriormente guardarlo en la BD
        String saveCurrentTime, saveCurrentDate;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calendar.getTime());

        historialRandomKey = saveCurrentDate + saveCurrentTime;


        //Referencia a la BD donde se guardará la información de la medición
        final DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Admin").child(Prevalent.currentOnlineUser.getId());

        //Datos de la medición que se guardarán en la BD
        final HashMap<String, Object> medicionesMap = new HashMap<>();
        medicionesMap.put("date", saveCurrentDate);
        medicionesMap.put("time", saveCurrentTime);
        medicionesMap.put("medicion", medida);

        //Agregar una nueva medición a la BD
        UsersRef.child("mediciones").child(historialRandomKey).updateChildren(medicionesMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(AdminHome.this, MedicionInfoActivity.class);
                    intent.putExtra("medida", medida);
                    intent.putExtra("medida_int", medida_int);
                    startActivity(intent);
                }
            }
        });


    }

    //Método para obtener los números de la medición que brinda el chaleco para poder realizar operaciones con ella
    public static String getNumeros(String cadena){
        char [] cadena_div = cadena.toCharArray();
        String n= "";
        for(int i = 0; i <cadena_div.length; i++){
            if(Character.isDigit(cadena_div[i])){
                n+=cadena_div[i];
            }
        }
        return n;
    }

}