package com.example.crud_ejemplo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private EditText ETCodigo, ETEtiqueta, ETPrecio;
    private Button BTNRegistrar, BTNConsultar, BTNModificar, BTNEliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ETCodigo = findViewById(R.id.CampoCode);
        ETEtiqueta = findViewById(R.id.CampoEtiqueta);
        ETPrecio = findViewById(R.id.CampoPrecio);

        BTNRegistrar = findViewById(R.id.btnRegistrar);
        BTNConsultar = findViewById(R.id.btnBuscar);
        BTNModificar = findViewById(R.id.btnEditar);
        BTNEliminar = findViewById(R.id.btnEliminar);

        BTNRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try
                {
                    AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(MainActivity.this, "administracion", null, 1 );
                    SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
                    String codigo = ETCodigo.getText().toString();
                    String etiqueta = ETEtiqueta.getText().toString();
                    String precio = ETPrecio.getText().toString();

                    if(!codigo.isEmpty() && !etiqueta.isEmpty() && !precio.isEmpty())
                    {
                        Cursor fila = baseDeDatos.rawQuery("select descripcion, precio from articulos where codigo=" +codigo, null);
                        if(fila.moveToFirst())
                        {
                            Toast.makeText(MainActivity.this, "El codigo ingresado ya existe en la base de datos", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        ContentValues registro = new ContentValues();
                        registro.put("codigo", codigo);
                        registro.put("descripcion", etiqueta);
                        registro.put("precio", precio);

                        baseDeDatos.insert("articulos", null, registro);
                        baseDeDatos.close();

                        ETCodigo.setText("");
                        ETEtiqueta.setText("");
                        ETPrecio.setText("");

                        Toast.makeText(MainActivity.this, "Registro creado exitosamente!", Toast.LENGTH_SHORT).show();
                    }

                    else
                    {
                        Toast.makeText(MainActivity.this, "Todos los campos deben tener información", Toast.LENGTH_SHORT).show();
                    }


                }

                catch(Exception e)
                {
                    Toast.makeText(MainActivity.this, "Error al crear base de datos", Toast.LENGTH_SHORT).show();
                }

            }
        });

        BTNConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try
                {
                    AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(MainActivity.this, "administracion", null, 1 );
                    SQLiteDatabase baseDeDatos = admin.getReadableDatabase();

                    String codigo = ETCodigo.getText().toString();

                    if(codigo.isEmpty())
                    {
                        Toast.makeText(MainActivity.this, "Se debe ingresar un codigo", Toast.LENGTH_SHORT).show();
                    }

                    else
                    {
                        Cursor fila = baseDeDatos.rawQuery("select descripcion, precio from articulos where codigo=" +codigo, null);
                        if(fila.moveToFirst())
                        {
                            ETEtiqueta.setText(fila.getString(0));
                            ETPrecio.setText(fila.getString(1));
                        }

                        else
                        {
                            Toast.makeText(MainActivity.this, "Codigo no encontrado", Toast.LENGTH_SHORT).show();
                            ETEtiqueta.setText("");
                            ETPrecio.setText("");
                        }
                    }

                    baseDeDatos.close();
                }

                catch (Exception e)
                {
                    Toast.makeText(MainActivity.this, "Error al cargar base de datos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        BTNEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try
                {
                    AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(MainActivity.this, "administracion", null, 1 );
                    SQLiteDatabase baseDeDatos = admin.getWritableDatabase();


                    String codigo = ETCodigo.getText().toString();

                    if(!codigo.isEmpty())
                    {
                        int cantidad = baseDeDatos.delete("articulos", "codigo="+codigo,null);

                        if(cantidad>0)
                        {
                            Toast.makeText(MainActivity.this, "Articulo eliminado exitosamente", Toast.LENGTH_LONG).show();
                            ETCodigo.setText("");
                            ETEtiqueta.setText("");
                            ETPrecio.setText("");
                        }

                        else
                        {
                            Toast.makeText(MainActivity.this, "No se encontro el codigo", Toast.LENGTH_LONG).show();
                        }
                    }

                    else
                    {
                        Toast.makeText(MainActivity.this, "Se debe ingresar un codigo", Toast.LENGTH_LONG).show();
                    }

                    baseDeDatos.close();
                }

                catch (Exception e)
                {
                    Toast.makeText(MainActivity.this, "Error al cargar base de datos", Toast.LENGTH_LONG).show();
                }
            }
        });

        BTNModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try
                {
                    AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(MainActivity.this, "administracion", null, 1 );
                    SQLiteDatabase baseDeDatos = admin.getWritableDatabase();

                    String codigo = ETCodigo.getText().toString();
                    String etiqueta = ETEtiqueta.getText().toString();
                    String precio = ETPrecio.getText().toString();

                    if(!codigo.isEmpty())
                    {
                        Cursor fila = baseDeDatos.rawQuery("select descripcion, precio from articulos where codigo=" +codigo, null);
                        if(!fila.moveToFirst())
                        {
                            Toast.makeText(MainActivity.this, "El codigo no existe en la base de datos", Toast.LENGTH_LONG).show();
                            return;
                        }

                        ContentValues registro = new ContentValues();

                        if(etiqueta.isEmpty() && precio.isEmpty())
                        {
                            Toast.makeText(MainActivity.this, "Debe ingresar valores en el campo que desea modificar", Toast.LENGTH_LONG).show();
                            return;
                        }

                        else
                        {
                            if(!etiqueta.isEmpty() && precio.isEmpty())
                            {
                                registro.put("descripcion", etiqueta);
                            }

                            else
                            {
                                if(etiqueta.isEmpty() && !precio.isEmpty())
                                {
                                    registro.put("precio", precio);
                                }

                                else
                                {
                                    registro.put("descripcion", etiqueta);
                                    registro.put("precio", precio);
                                }
                            }

                            int cantidad = baseDeDatos.update("articulos",registro, "codigo="+codigo,null);

                            if(cantidad>0)
                            {
                                Toast.makeText(MainActivity.this, "Articulo actualizado exitosamente", Toast.LENGTH_LONG).show();
                                ETCodigo.setText("");
                                ETEtiqueta.setText("");
                                ETPrecio.setText("");
                            }

                            else
                            {
                                Toast.makeText(MainActivity.this, "No se encontro el codigo", Toast.LENGTH_LONG).show();
                            }
                        }
                }

                else
                {
                    Toast.makeText(MainActivity.this, "Se debe ingresar un codigo", Toast.LENGTH_LONG).show();
                }

                baseDeDatos.close();
                }

                catch (Exception e)
                {
                    Toast.makeText(MainActivity.this, "Error al cargar base de datos", Toast.LENGTH_LONG).show();
                }
            }
        });

        ETCodigo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ETEtiqueta.setText("");
                ETPrecio.setText("");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}