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

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    EditText text1, text2, text3, text4, text5;
    Button calcular, limpiar;
    TextView studentsReprove,result, resultForStudent, text6;
    CheckBox save;
    RadioGroup radioGroup;
    RadioButton radioButtonYes, radioButtonNo;
    SharedPreferences sharedPreferences;
    Integer countStudents = 0;
    Double resultAllStudents = 0.0;
    DecimalFormat decimalFormat = new DecimalFormat("0.0");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text1 = (EditText)findViewById(R.id.txt1);
        text2 = (EditText)findViewById(R.id.txt2);
        text3 = (EditText)findViewById(R.id.txt3);
        text4 = (EditText)findViewById(R.id.txt4);
        text5 = (EditText)findViewById(R.id.txt5);
        text6 = (TextView)findViewById(R.id.txt6);
        save = (CheckBox)findViewById(R.id.checkSave);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        radioButtonYes = findViewById(R.id.radioButtonYes);
        radioButtonNo = findViewById(R.id.radioButtonNo);
        studentsReprove = findViewById(R.id.studentsReprove);
        resultForStudent = (TextView)findViewById(R.id.resultForStudent);
        result = (TextView)findViewById(R.id.result);

        SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        text1.setText(preferences.getString("text1", ""));
        text2.setText(preferences.getString("text2", ""));
        text3.setText(preferences.getString("text3", ""));
        text4.setText(preferences.getString("text4", ""));
        text5.setText(preferences.getString("text5", ""));
        text6.setText(preferences.getString("text6", "Ingrese las notas del estudiante 1 (Las notas son de 0 a 5)"));
        save.setChecked(preferences.getBoolean("save", false));
        radioGroup.check(preferences.getInt("radioGroup", -1));
        studentsReprove.setText(preferences.getString("studentsReprove", "ESTUDIANTES QUE PERDIERON"));
        resultForStudent.setText(preferences.getString("resultForStudent", "PROMEDIO DEL ESTUDIANTE ACTUAL"));
        result.setText(preferences.getString("result", "PROMEDIO DE LOS ESTUDIANTES"));
        countStudents = preferences.getInt("countStudents", 0);
        resultAllStudents = Double.parseDouble(preferences.getString("resultAllStudents", "0.0"));
        calcular = findViewById(R.id.calculate);
        limpiar = findViewById(R.id.clear);
            calcular.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    calcularNotaDefinitiva();
                }
            });

            limpiar.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    limpiar();
                }
            });
        }


        private boolean esNumero(String texto) {
            try {
                Double.parseDouble(texto);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        private void calcularNotaDefinitiva() {
            String numEstudiantesText = text1.getText().toString();
            String nota1Text = text2.getText().toString();
            String nota2Text = text3.getText().toString();
            String nota3Text = text4.getText().toString();
            String nota4Text = text5.getText().toString();
            if (numEstudiantesText.isEmpty()) {
                Toast.makeText(this, "El campo número de estudiantes es requerido", Toast.LENGTH_LONG).show();
                return;
            }

            if (!esNumero(numEstudiantesText)) {
                Toast.makeText(this, "Ingrese un número válido para el número de estudiantes", Toast.LENGTH_LONG).show();
                return;
            }

            if ((nota1Text.isEmpty() || nota2Text.isEmpty() || nota3Text.isEmpty() || nota4Text.isEmpty()) && radioButtonYes.isChecked()) {
                Toast.makeText(this, "El campo número de las notas es requerido, verifique las notas ingresadas", Toast.LENGTH_LONG).show();
                return;
            } else if ((!nota1Text.isEmpty() || !nota2Text.isEmpty() || !nota3Text.isEmpty() || !nota4Text.isEmpty()) && !radioButtonYes.isChecked()) {
                Toast.makeText(this, "El check de ingreso de notas está en NO, y existen notas ingresadas, verifique", Toast.LENGTH_LONG).show();
                return;
            }

            if (radioButtonYes.isChecked() && (!esNumero(nota1Text) || !esNumero(nota2Text) || !esNumero(nota3Text) || !esNumero(nota4Text))) {
                Toast.makeText(this, "Ingrese un número válido para las notas, verifique las notas ingresadas", Toast.LENGTH_SHORT).show();
                return;
            }

            int numEstudiantes = Integer.parseInt(numEstudiantesText);
            if(numEstudiantes > 0 && numEstudiantes >= (countStudents + 1)) {
                text1.setEnabled(false);
                try {
                    double nota1 = Double.parseDouble(text2.getText().toString());
                    double nota2 = Double.parseDouble(text3.getText().toString());
                    double nota3 = Double.parseDouble(text4.getText().toString());
                    double nota4 = Double.parseDouble(text5.getText().toString());

                    if (nota1 < 0 || nota1 > 5 || nota2 < 0 || nota2 > 5 || nota3 < 0 || nota3 > 5 || nota4 < 0 || nota4 > 5) {
                        Toast.makeText(this, "Las notas deben estar entre 0 y 5", Toast.LENGTH_LONG).show();
                        return;
                    }
                    countStudents ++;
                    if(numEstudiantes >= (countStudents + 1)) {
                        text6.setText("Ingrese las notas del estudiante "+ (countStudents + 1) +" (Las notas son de 0 a 5)");
                    }
                    double notaForStudent = (nota1 * 0.20) + (nota2 * 0.30) + (nota3 * 0.15) + (nota4 * 0.35);
                    resultForStudent.setText("Nota definitiva estudiante # " + countStudents + " : " + decimalFormat.format(notaForStudent));
                    resultAllStudents += notaForStudent;
                    double notaDefinitiva = resultAllStudents / countStudents;
                    result.setText("El promedio total es " + decimalFormat.format(notaDefinitiva) + " hasta el momento");

                    int estudiantesPerdieron = (notaDefinitiva < 3.0) ? 1 : 0;
                    studentsReprove.setText("Estudiantes que han perdido la final: " + estudiantesPerdieron);

                    if(save.isChecked()) {
                        guardar();
                    }
                }
                catch (NumberFormatException e) {
                    countStudents ++;
                    if(numEstudiantes >= (countStudents + 1)) {
                        text6.setText("Ingrese las notas del estudiante "+ (countStudents + 1) +" (Las notas son de 0 a 5)");
                    }
                    if(save.isChecked()) {
                        guardar();
                    }
                    Toast.makeText(this, "No se añadieron notas al estudiante: " + countStudents, Toast.LENGTH_LONG).show();
                }

            } else if(numEstudiantes < (countStudents + 2)) {
                Toast.makeText(this, "Ya has ingresado todos los estudiantes, limpia por favor el formulario", Toast.LENGTH_LONG).show();
                if(save.isChecked()) {
                    guardar();
                }
            } else {
                Toast.makeText(this, "Ingrese un número de estudiantes válido", Toast.LENGTH_LONG).show();
            }
        }

        public void guardar() {
        SharedPreferences preferencias = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor obj_editor = preferencias.edit();
        obj_editor.putString("text1", text1.getText().toString());
        obj_editor.putString("text2", text2.getText().toString());
        obj_editor.putString("text3", text3.getText().toString());
        obj_editor.putString("text4", text4.getText().toString());
        obj_editor.putString("text5", text5.getText().toString());
        obj_editor.putString("text6", text6.getText().toString());
        obj_editor.putBoolean("save", save.isChecked());
        obj_editor.putInt("radioGroup", radioGroup.getCheckedRadioButtonId());
        obj_editor.putInt("countStudents", countStudents);
        obj_editor.putString("studentsReprove", studentsReprove.getText().toString());
        obj_editor.putString("resultForStudent", resultForStudent.getText().toString());
        obj_editor.putString("result", result.getText().toString());
        obj_editor.putString("resultAllStudents", String.valueOf(resultAllStudents));
        obj_editor.commit();
        Toast.makeText(this, "Se ha guardado con exito", Toast.LENGTH_LONG).show();
        }

        public void limpiar() {
            text1.setEnabled(true);
            text1.setText("");
            text2.setText("");
            text3.setText("");
            text4.setText("");
            text5.setText("");
            text6.setText("Ingrese las notas del estudiante 1 (Las notas son de 0 a 5)");
            save.setChecked(false);
            studentsReprove.setText("ESTUDIANTES QUE PERDIERON");
            resultForStudent.setText("PROMEDIO DEL ESTUDIANTE ACTUAL");
            result.setText("PROMEDIO DE LOS ESTUDIANTES");
            resultAllStudents = 0.0;
            countStudents = 0;
            guardar();
        }
    }
