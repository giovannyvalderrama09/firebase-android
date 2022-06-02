package com.example.firebase_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText jetidentificacion, jetnombre, jetcarrera, jetsemestre;
    Button jbtadicionar, jbtconsultar, jbtmodificar, jbteliminar, jbtcancelar;
    String identificacion, nombre, carrera, semestre;
    String iduser;
    FirebaseFirestore xt = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jetidentificacion = findViewById(R.id.etidentificacion);
        jetnombre = findViewById(R.id.etnombre);
        jetcarrera = findViewById(R.id.etcarrera);
        jetsemestre = findViewById(R.id.etsemestre);
        jbtadicionar = findViewById(R.id.btadicionar);
        jbtconsultar = findViewById(R.id.btconsultar);
        jbtmodificar = findViewById(R.id.btmodificar);
        jbteliminar = findViewById(R.id.bteliminar);
        jbtcancelar = findViewById(R.id.btcancelar);



        jbtadicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Adicionar();
            }
        });

        jbtconsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
         Consultar();
            }
        });

        jbtmodificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Modificar();
            }
        });


    }

    private void Adicionar() {
        identificacion = jetidentificacion.getText().toString();
        nombre = jetnombre.getText().toString();
        carrera = jetcarrera.getText().toString();
        semestre = jetsemestre.getText().toString();
        if (identificacion.isEmpty() || nombre.isEmpty() || carrera.isEmpty() || semestre.isEmpty()) {
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            jetidentificacion.requestFocus();
        } else {
            // Create a new user with a first and last name
            Map<String, Object> user = new HashMap<>();
            user.put("Identificacion", identificacion);
            user.put("Nombre", nombre);
            user.put("Carerra", carrera);
            user.put("Semestre", semestre);

// Add a new document with a generated ID
            xt.collection("Registro estudiantes")
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(MainActivity.this, "Datos guardados", Toast.LENGTH_SHORT).show();
                            Limpiar_campos();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Error guardando datos", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void Consultar(){
        identificacion = jetidentificacion.getText().toString();
        if (identificacion.isEmpty()){
            Toast.makeText(this, "La identificacion es requerida", Toast.LENGTH_SHORT).show();
            jetidentificacion.requestFocus();
        }
        xt.collection("Registro estudiantes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                iduser=document.getId();
                                jetnombre.setText(document.getString("Nombre"));
                                jetcarrera.setText(document.getString("Carerra"));
                                jetsemestre.setText(document.getString("Semestre"));
                                Toast.makeText(MainActivity.this, "Registro encontrado", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(MainActivity.this, "El registro no existe", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }




   private void Modificar(){
        identificacion = jetidentificacion.getText().toString();
        nombre = jetnombre.getText().toString();
        carrera = jetcarrera.getText().toString();
        semestre = jetsemestre.getText().toString();
        if (identificacion.isEmpty() || nombre.isEmpty() || carrera.isEmpty() || semestre.isEmpty()) {
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            jetidentificacion.requestFocus();
        } else {
            Map<String, Object> user = new HashMap<>();
            user.put("Identificacion", identificacion);
            user.put("Nombre", nombre);
            user.put("Carerra", carrera);
            user.put("Semestre", semestre);

            xt.collection("Registro estudiantes").document(iduser)
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MainActivity.this, "Estudiante actualizada correctamente", Toast.LENGTH_SHORT).show();
                            Limpiar_campos();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Error actualizando estudiante", Toast.LENGTH_SHORT).show();
                        }

                    });
        }
}




    private void Limpiar_campos() {
        jetidentificacion.setText("");
        jetnombre.setText("");
        jetcarrera.setText("");
        jetsemestre.setText("");
        jetidentificacion.requestFocus();
    }
}
