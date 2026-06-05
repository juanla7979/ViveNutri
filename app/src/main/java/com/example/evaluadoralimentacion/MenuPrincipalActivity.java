package com.example.evaluadoralimentacion;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MenuPrincipalActivity extends AppCompatActivity {

    private Button btnNuevaEncuesta, btnVerHistorial, btnSalir, btnImportar;
    private static final int PICK_CSV_FILE = 1000;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private UserStorage userStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        userStorage = new UserStorage(this);

        btnNuevaEncuesta = findViewById(R.id.btnNuevaEncuesta);
        btnVerHistorial = findViewById(R.id.btnVerHistorial);
        btnSalir = findViewById(R.id.btnSalir);
        btnImportar = findViewById(R.id.btnImportar);
        Button btnEditarDatos = findViewById(R.id.btnEditarDatos);

        btnNuevaEncuesta.setOnClickListener(v -> {
            Intent intent = new Intent(MenuPrincipalActivity.this, MainActivity.class);
            startActivity(intent);
        });

        btnVerHistorial.setOnClickListener(v -> {
            Intent intent = new Intent(MenuPrincipalActivity.this, HistorialActivity.class);
            startActivity(intent);
        });

        btnSalir.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Salir")
                    .setMessage("¿Estás seguro de que quieres salir de la aplicación?")
                    .setPositiveButton("Sí", (dialog, which) -> finishAffinity())
                    .setNegativeButton("Cancelar", null)
                    .setCancelable(false)
                    .show();
        });

        btnImportar.setOnClickListener(v -> {
            if (verificarPermisos()) {
                abrirSelectorCSV();
            } else {
                solicitarPermisos();
            }
        });

        btnEditarDatos.setOnClickListener(v -> {
            mostrarFormularioEditarDatos();
        });

        verificarDatosUsuario();
    }

    private void verificarDatosUsuario() {
        if (!userStorage.existeUserData()) {
            mostrarDialogoDatosPersonales();
        }
    }

    private void mostrarDialogoDatosPersonales() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Datos Personales");
        builder.setMessage("Para continuar, necesitamos que ingreses tus datos personales.\n\nEsta informacion se guardara localmente y se usara en tus encuestas.");

        builder.setPositiveButton("Ingresar datos", (dialog, which) -> {
            mostrarFormularioDatos();
        });

        builder.setNegativeButton("Salir", (dialog, which) -> {
            finishAffinity();
        });

        builder.setCancelable(false);
        builder.show();
    }

    private void mostrarFormularioDatos() {
        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText etNombre = new EditText(this);
        etNombre.setHint("Nombre completo");
        layout.addView(etNombre);

        final EditText etEdad = new EditText(this);
        etEdad.setHint("Edad ('18' por ejemplo)");
        layout.addView(etEdad);

        final EditText etCarrera = new EditText(this);
        etCarrera.setHint("Carrera");
        layout.addView(etCarrera);

        final EditText etLocalidad = new EditText(this);
        etLocalidad.setHint("Lugar de origen");
        layout.addView(etLocalidad);

        final android.widget.RadioGroup rgSexo = new android.widget.RadioGroup(this);
        rgSexo.setOrientation(android.widget.RadioGroup.HORIZONTAL);

        android.widget.RadioButton rbHombre = new android.widget.RadioButton(this);
        rbHombre.setText("Hombre");
        rbHombre.setId(android.view.View.generateViewId());

        android.widget.RadioButton rbMujer = new android.widget.RadioButton(this);
        rbMujer.setText("Mujer");
        rbMujer.setId(android.view.View.generateViewId());

        android.widget.RadioButton rbOtro = new android.widget.RadioButton(this);
        rbOtro.setText("Otro");
        rbOtro.setId(android.view.View.generateViewId());

        rgSexo.addView(rbHombre);
        rgSexo.addView(rbMujer);
        rgSexo.addView(rbOtro);

        android.widget.TextView tvSexo = new android.widget.TextView(this);
        tvSexo.setText("Sexo:");
        tvSexo.setTextSize(14);
        tvSexo.setPadding(0, 16, 0, 8);
        layout.addView(tvSexo);
        layout.addView(rgSexo);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ingresa tus datos");
        builder.setView(layout);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nombre = etNombre.getText().toString().trim();
            String edad = etEdad.getText().toString().trim();
            String carrera = etCarrera.getText().toString().trim();
            String localidad = etLocalidad.getText().toString().trim();

            String sexo = "";
            int selectedId = rgSexo.getCheckedRadioButtonId();
            if (selectedId == rbHombre.getId()) {
                sexo = "Hombre";
            } else if (selectedId == rbMujer.getId()) {
                sexo = "Mujer";
            } else if (selectedId == rbOtro.getId()) {
                sexo = "Otro";
            }

            if (nombre.isEmpty() || edad.isEmpty() || carrera.isEmpty() || localidad.isEmpty() || sexo.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                mostrarFormularioDatos();
                return;
            }

            UserData userData = new UserData(nombre, edad, carrera, localidad, sexo);
            userStorage.guardarUserData(userData);
            Toast.makeText(this, "Datos guardados correctamente", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Salir", (dialog, which) -> {
            finishAffinity();
        });

        builder.setCancelable(false);
        builder.show();
    }

    private boolean verificarPermisos() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES)
                    == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void solicitarPermisos() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_MEDIA_IMAGES},
                    PERMISSION_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
        }
    }

    private void mostrarFormularioEditarDatos() {
        UserData userData = userStorage.obtenerUserData();

        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText etNombre = new EditText(this);
        etNombre.setHint("Nombre completo");
        etNombre.setText(userData.getNombre() != null ? userData.getNombre() : "");
        layout.addView(etNombre);

        final EditText etEdad = new EditText(this);
        etEdad.setHint("Edad (ej: 17-25, 26-35, etc)");
        etEdad.setText(userData.getEdad() != null ? userData.getEdad() : "");
        layout.addView(etEdad);

        final EditText etCarrera = new EditText(this);
        etCarrera.setHint("Carrera");
        etCarrera.setText(userData.getCarrera() != null ? userData.getCarrera() : "");
        layout.addView(etCarrera);

        final EditText etLocalidad = new EditText(this);
        etLocalidad.setHint("Lugar de origen");
        etLocalidad.setText(userData.getLocalidad() != null ? userData.getLocalidad() : "");
        layout.addView(etLocalidad);

        final android.widget.RadioGroup rgSexo = new android.widget.RadioGroup(this);
        rgSexo.setOrientation(android.widget.RadioGroup.HORIZONTAL);

        android.widget.RadioButton rbHombre = new android.widget.RadioButton(this);
        rbHombre.setText("Hombre");
        rbHombre.setId(android.view.View.generateViewId());

        android.widget.RadioButton rbMujer = new android.widget.RadioButton(this);
        rbMujer.setText("Mujer");
        rbMujer.setId(android.view.View.generateViewId());

        android.widget.RadioButton rbOtro = new android.widget.RadioButton(this);
        rbOtro.setText("Otro");
        rbOtro.setId(android.view.View.generateViewId());

        rgSexo.addView(rbHombre);
        rgSexo.addView(rbMujer);
        rgSexo.addView(rbOtro);

        String sexoActual = userData.getSexo() != null ? userData.getSexo() : "";
        if (sexoActual.equals("Hombre")) {
            rgSexo.check(rbHombre.getId());
        } else if (sexoActual.equals("Mujer")) {
            rgSexo.check(rbMujer.getId());
        } else if (sexoActual.equals("Otro")) {
            rgSexo.check(rbOtro.getId());
        }

        android.widget.TextView tvSexo = new android.widget.TextView(this);
        tvSexo.setText("Sexo:");
        tvSexo.setTextSize(14);
        tvSexo.setPadding(0, 16, 0, 8);
        layout.addView(tvSexo);
        layout.addView(rgSexo);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Mis Datos");
        builder.setView(layout);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nombre = etNombre.getText().toString().trim();
            String edad = etEdad.getText().toString().trim();
            String carrera = etCarrera.getText().toString().trim();
            String localidad = etLocalidad.getText().toString().trim();

            String sexo = "";
            int selectedId = rgSexo.getCheckedRadioButtonId();
            if (selectedId == rbHombre.getId()) {
                sexo = "Hombre";
            } else if (selectedId == rbMujer.getId()) {
                sexo = "Mujer";
            } else if (selectedId == rbOtro.getId()) {
                sexo = "Otro";
            }

            if (nombre.isEmpty() || edad.isEmpty() || carrera.isEmpty() || localidad.isEmpty() || sexo.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            UserData newUserData = new UserData(nombre, edad, carrera, localidad, sexo);
            userStorage.guardarUserData(newUserData);
            Toast.makeText(this, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void abrirSelectorCSV() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/csv");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, PICK_CSV_FILE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                abrirSelectorCSV();
            } else {
                Toast.makeText(this, "Permiso denegado. No se puede importar.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CSV_FILE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            CSVImporter importer = new CSVImporter(this);
            importer.importarDesdeCSV(uri);
        }
    }
}