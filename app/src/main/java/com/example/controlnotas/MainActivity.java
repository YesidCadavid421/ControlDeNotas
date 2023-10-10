package com.example.controlnotas;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText text1, text2, text3, text4, text5;
    Button button;
    TextView studentsReprove,result;
    CheckBox save;
    RadioGroup radioGroup;
    RadioButton radioButtonYes, radioButtonNo;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        text1 = findViewById(R.id.txt1);
        text2 = findViewById(R.id.txt2);
        text3 = findViewById(R.id.txt3);
        text4 = findViewById(R.id.txt4);
        text5 = findViewById(R.id.txt5);
        button = findViewById(R.id.b1);
        studentsReprove = findViewById(R.id.studentsReprove);
        result = findViewById(R.id.tvResultado);
        save = findViewById(R.id.checkSave);
        radioGroup = findViewById(R.id.radioGroup);
        radioButtonYes = findViewById(R.id.radioButtonYes);
        radioButtonNo = findViewById(R.id.radioButtonNo);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                calcularNotaDefinitiva();
            }
             });
        }

        private boolean esNumero(String texto) {
            return TextUtils.isDigitsOnly(texto);
        }

        private void calcularNotaDefinitiva() {
            Integer countStudents = 0;
            String numEstudiantesText = text1.getText().toString();
            String nota1Text = text2.getText().toString();
            String nota2Text = text3.getText().toString();
            String nota3Text = text4.getText().toString();
            String nota4Text = text5.getText().toString();
            if (numEstudiantesText.isEmpty()) {
                Toast.makeText(this, "El campo número de estudiantes es requerido", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!esNumero(numEstudiantesText)) {
                Toast.makeText(this, "Ingrese un número válido para el número de estudiantes", Toast.LENGTH_SHORT).show();
                return;
            }

            if (nota1Text.isEmpty() || nota2Text.isEmpty() || nota3Text.isEmpty() || nota4Text.isEmpty()) {
                Toast.makeText(this, "El campo número de las notas es requerido, verifique las notas ingresadas", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!esNumero(nota1Text) || !esNumero(nota2Text) || !esNumero(nota3Text) || !esNumero(nota4Text)) {
                Toast.makeText(this, "Ingrese un número válido para las notas, verifique las notas ingresadas", Toast.LENGTH_SHORT).show();
                return;
            }

            int numEstudiantes = Integer.parseInt(numEstudiantesText);
            if(numEstudiantes > 0 && numEstudiantes >= countStudents) {
                double nota1 = Double.parseDouble(text2.getText().toString());
                double nota2 = Double.parseDouble(text3.getText().toString());
                double nota3 = Double.parseDouble(text4.getText().toString());
                double nota4 = Double.parseDouble(text5.getText().toString());

                if (nota1 < 0 || nota1 > 5 || nota2 < 0 || nota2 > 5 || nota3 < 0 || nota3 > 5 || nota4 < 0 || nota4 > 5) {
                    Toast.makeText(this, "Las notas deben estar entre 0 y 5", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    countStudents ++;
                    if(save.isActivated()) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                    }
                }

                double notaDefinitiva = (nota1 * 0.20) + (nota2 * 0.30) + (nota3 * 0.15) + (nota4 * 0.35);

                result.setText("Nota definitiva estudiante # " + countStudents + " : " + notaDefinitiva);

                int estudiantesPerdieron = (notaDefinitiva < 3.0) ? 1 : 0;
                studentsReprove.setText("Estudiantes que han perdido la final: " + estudiantesPerdieron);
                text2.setText("");
                text3.setText("");
                text4.setText("");
                text5.setText("");
            } else {
                Toast.makeText(this, "Ingrese un número de estudiantes válido", Toast.LENGTH_SHORT).show();
            }
        }

        public class ObjectToSave {
            private int student;
            private double nota1;
            private double nota2;
            private double nota3;
            private double nota4;

            public ObjectToSave(int student, double nota1, double nota2, double nota3, double nota4) {
                this.student = student;
                this.nota1 = nota1;
                this.nota2 = nota2;
                this.nota3 = nota3;
                this.nota4 = nota4;
            }
        }
    }
