package com.example.examen_2p;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import id.zelory.compressor.Compressor;

public class MainActivity extends AppCompatActivity {

    static final int RESULT_GALLERY = 101;

    private Bitmap photo;
    private TextView txbName, txbTel, txbLat, txbLong;

    private Button btnSave, btnContactos, btnfoto;
    private LocationManager ubicacion;
    TextView mensaje1;
    TextView mensaje2;
    private DatabaseReference mDataBase;
    private String nombre;
    private String telefono;
    private String latitud;
    private String longitud;

    private ImageView foto;
    private StorageReference storageReference;
    private ProgressDialog cargando;
    Bitmap thumb_bitmap = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        foto = findViewById(R.id.img_foto);
        //btnContactos = findViewById(R.id.btnContactos);
        btnSave = findViewById(R.id.btnSave);
        btnContactos = findViewById(R.id.btnContactos);
        btnfoto = findViewById(R.id.btnfoto);
        txbName = findViewById(R.id.txbName);
        txbTel = findViewById(R.id.txbTel);
        txbLat = findViewById(R.id.txbLatitud);
        txbLong = findViewById(R.id.txbLongitud);

        mensaje1 = (TextView) findViewById(R.id.txbLatitud);
        mensaje2 = (TextView) findViewById(R.id.txbLongitud);

        mDataBase = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference().child("img_comprimidas");
        cargando = new ProgressDialog(this);



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }


        if(Accion.bandera){
            String NombreUpdate = getIntent().getStringExtra("NombreUpdate");
            String NumeroTelUpdate = getIntent().getStringExtra("NumeroTelUpdate");
            /*String LatitudUpdate = getIntent().getStringExtra("LatitudUpdate");
            String LongitudUpdate = getIntent().getStringExtra("LongitudUpdate");*/

            txbName.setText(NombreUpdate);
            txbTel.setText(NumeroTelUpdate);
            /*txbLat.setText(LatitudUpdate);
            txbLong.setText(LongitudUpdate);*/

            btnSave.setText("Actualizar");
            btnContactos.setText("Cancelar");
        }
/*
        imvPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GaleriaImagenes();
            }
        });

        imvPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GaleriaImagenes();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recogerVal();
                Toast.makeText(MainActivity.this, "Guardado", Toast.LENGTH_SHORT).show();

            }
        });

          btnContactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Accion.bandera){
                    btnSave.setText("Agregar Contactos");
                    btnContactos.setText("Contactos");
                    Accion.bandera = false;
                    ClearScreen();
                }else{
                    Intent intent = new Intent(getApplicationContext(), ContactsActivity.class);
                    startActivity(intent);
                }
            }
        });
*/
        btnContactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(getApplicationContext(), ContactsActivity.class);
                    startActivity(intent);

            }
        });
        btnfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.startPickImageActivity(MainActivity.this);
            }
        });


    }

/*/
    private void recogerVal() {
        nombre= txbName.getText().toString();
        telefono= txbTel.getText().toString();
        longitud= txbLong.getText().toString();
        latitud= txbLat.getText().toString();
        usuarios user= new usuarios(nombre, telefono, latitud, longitud);
        mDataBase.child("usuario").push().setValue(user);
    }
*/

    private void AgregarContactos() {
        //datos de aws
        Toast.makeText(getApplicationContext(), "Contacto Guardado", Toast.LENGTH_SHORT).show();
        ClearScreen();
    }

    private void ClearScreen() {
        txbName.setText("");
        txbTel.setText("");
        txbLat.setText("");
        txbLong.setText("");
    }


/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri imageUri;
        imageUri = data.getData();
        imvPhoto.setImageURI(imageUri);

        try {
            photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

        }catch (Exception ex){
        }
    }
*/
    private void sendImage() {
        String url = "";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{

                }catch (Exception ex){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Respuesta", error.toString());
            }
        })
        {
            protected Map<String, String> getParams(){
                String image = getStringImage(photo);
                Map<String, String> parametros = new HashMap<>();
                parametros.put("IMAGEN", image);
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private String getStringImage(Bitmap photo) {
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, ba);
        byte[] imgByte = ba.toByteArray();
        String encode = Base64.encodeToString(imgByte, Base64.DEFAULT);
        return encode;
    }


    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setMainActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);

        mensaje1.setText("Localizacion agregada");
        mensaje2.setText("");
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }

    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    //  mensaje2.setText("Mi direccion es: \n"
                    //          + DirCalle.getAddressLine(0));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /* Aqui empieza la Clase Localizacion */
    public class Localizacion implements LocationListener {
        MainActivity mainActivity;

        public MainActivity getMainActivity() {
            return mainActivity;
        }

        public void setMainActivity(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion

            loc.getLatitude();
            loc.getLongitude();


            mensaje1.setText(String.valueOf(loc.getLatitude()));
            mensaje2.setText(String.valueOf(loc.getLongitude()));
            this.mainActivity.setLocation(loc);
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            mensaje1.setText("GPS Desactivado");
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            mensaje1.setText("GPS Activado");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            Uri imgaeuri = CropImage.getPickImageResultUri(this, data);

            //Recortar la IMG

            CropImage.activity(imgaeuri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setRequestedSize(500, 500)
                    .setAspectRatio(2, 1).start(MainActivity.this);
// 648, 480
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                File url = new File(resultUri.getPath());

                Picasso.with(this).load(url).into(foto);

                //comprimiendo imagen

                try {
                    thumb_bitmap = new Compressor(this).setMaxWidth(640).setMaxHeight(480).setQuality(90).compressToBitmap(url);
                } catch (IOException e) {

                    e.printStackTrace();
                }
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                final byte[] thumb_byte = byteArrayOutputStream.toByteArray();

                //fin del compresor


                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cargando.setTitle("Subiendo foto...");
                        cargando.setTitle("Espere por favor...");
                        cargando.show();

                        String fecha = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                        StorageReference ref = storageReference.child(fecha);
                        UploadTask uploadTask = ref.putBytes(thumb_byte);

                        //subir imagen en storage....

                        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw Objects.requireNonNull(task.getException());
                                }

                                return ref.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {


                                Uri dowloaduri = task.getResult();


                                nombre= txbName.getText().toString();
                                telefono= txbTel.getText().toString();
                                longitud= txbLong.getText().toString();
                                latitud= txbLat.getText().toString();
                                String url = dowloaduri.toString();

                                if (!nombre.isEmpty() && !telefono.isEmpty() && !longitud.isEmpty() && !latitud.isEmpty()) {

                                    usuarios user= new usuarios(nombre, telefono, latitud, longitud,url);
                                    mDataBase.child("usuario").push().setValue(user);

                                    Toast.makeText(MainActivity.this, "Datos exitosos", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "Debe completar los campos", Toast.LENGTH_LONG).show();
                                }


                                cargando.dismiss();

                                Toast.makeText(MainActivity.this, "Imagen cargada con exito", Toast.LENGTH_SHORT).show();


                            }
                        });


                    }
                });


            }

        }


    }
}