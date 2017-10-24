package com.android.alejandra.a07_3_androidmanifest_gestionpermisos_ej1;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int MY_REQUEST_CAMERA = 1;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;

    private ImageView fotoTomada;
    private Button btnTomarFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //obtener referencias
        initReferencias();


    }

    /**
     * Método que obtiene las referencias del xml
     */
    private void initReferencias() {
        fotoTomada = (ImageView) findViewById(R.id.ivFotoTomada);
        btnTomarFoto = (Button) findViewById(R.id.btnTomarFoto);
        btnTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lanzarCamara();
            }
        });
    }


    /**
     * Método que lanza la cámara del teléfono
     */
    private void lanzarCamara() {
        //COMPRUEBO SI TENGO PERMISO

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            //permiso  concedido
            lanzarIntentCamara();

        } else {
            //permiso no concedido
            solicitarPermisoCamara();
        }

    }


    /**
     * Método que solicita el permiso para usar la cámara
     */
    private void solicitarPermisoCamara() {
        // Mostrar una explicación de por qué necesito el permiso???
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            mostrarDialogo(this, " Sin el permiso de la cámara no se pueden tomar fotos", Manifest.permission.CAMERA);


        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);

            // MY_PERMISSIONS_REQUEST_CAMERA es una constante definida en mi app.
            // The callback method gets the
            // result of the request.
        }

    }


    /**
     * Método que muestra un cuadro de díalogo informando de por qué se necesita ese permiso
     *
     * @param activity           Activity sobre la cual se solicita el permiso
     * @param textoJustificacion Texto explicativo que justifica por qué se necesita ese permiso
     * @param permiso            el permiso que se está pidiendo
     */
    private void mostrarDialogo(final Activity activity, String textoJustificacion, final String permiso) {
        new AlertDialog.Builder(activity)
                .setTitle("Solicitud de permiso")
                .setMessage(textoJustificacion)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        ActivityCompat.requestPermissions(activity,
                                new String[]{permiso}, MainActivity.MY_PERMISSIONS_REQUEST_CAMERA);
                    }
                })
                .show();
    }


    /**
     * Método que realmente lanza el intent que abre la cámara
     */

    private void lanzarIntentCamara() {
        Intent iCamara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (iCamara.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(iCamara, MY_REQUEST_CAMERA);
        }
    }


    /**
     * Método que se ejecuta cada vez que el usuario acepta/rechaza un permiso
     *
     * @param requestCode  petición de permiso a la que se responde
     * @param permissions  permiso aceptado/rechazado
     * @param grantResults resultado de la concesión (granted or not)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted!
                    lanzarIntentCamara();


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "PERMISO DENEGADO", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


    /**
     * Método que se ejecuta cuando se vuelve de tomar una foto
     *
     * @param requestCode código de la petición a la que responde
     * @param resultCode código que indica cómo fue la operación
     * @param data       datos de la operación. Aquí vendrá una foto miniatura
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_REQUEST_CAMERA && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            fotoTomada.setImageBitmap((Bitmap)extras.get("data"));
        }
    }
}
