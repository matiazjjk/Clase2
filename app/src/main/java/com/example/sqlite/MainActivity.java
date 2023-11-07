package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

import kotlin.collections.MapsKt;

public class MainActivity extends AppCompatActivity {
    EditText ID, Nombre, Area;
    ListView Lista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ID = findViewById(R.id.txtidUsuario);
        Nombre = findViewById(R.id.txtNombreUsuario);
        Area = findViewById(R.id.txtAreaUsuario);
        Lista = findViewById(R.id.ListaUsuarios);
        CargaUsuarios();
    }

    public void Registrarusuario(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Produccion", null, 1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();
        String IDUsuario = ID.getText().toString();
        String NombreUsuario = Nombre.getText().toString();
        String AreaUsuario = Area.getText().toString();
        if (!IDUsuario.isEmpty() && !NombreUsuario.isEmpty()
                && !AreaUsuario.isEmpty()){
            ContentValues DatosUsuario = new ContentValues();
            DatosUsuario.put("idUsuario", IDUsuario);
            DatosUsuario.put("NombreUsuario", NombreUsuario);
            DatosUsuario.put("AreaUsuario", AreaUsuario);
            BaseDatos.insert("Usuarios", null, DatosUsuario);
            BaseDatos.close();
            ID.setText("");
            Nombre.setText("");
            Area.setText("");
            Toast.makeText(this, "Usuario registrado exitosamente",
                    Toast.LENGTH_SHORT).show();
            CargaUsuarios();
        } else {
            Toast.makeText(this, "No pueden haber campos vacios",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void BuscarUsuario(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Produccion", null, 1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();
        String IDUsuario = ID.getText().toString();

        if(!IDUsuario.isEmpty()){
            Cursor fila = BaseDatos.rawQuery("Select NombreUsuario, AreaUsuario from Usuarios where idUsuario="+ IDUsuario, null);
            if(fila.moveToFirst()){
                Nombre.setText(fila.getString(0));
                Area.setText(fila.getString(1));
                BaseDatos.close();
            } else {
                Toast.makeText(this, "No se encontro el ID ingresado",
                        Toast.LENGTH_SHORT).show();
                BaseDatos.close();
            }

        } else {
            Toast.makeText(this, "Campo ID no puede estar vacio",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void ActualizarUsuario(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Produccion", null, 1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();
        String IDUsuario = ID.getText().toString();
        String NombreUsuario = Nombre.getText().toString();
        String AreaUsuario = Area.getText().toString();
        if (!NombreUsuario.isEmpty() && !AreaUsuario.isEmpty()){
            ContentValues DatosUsuario = new ContentValues();
            DatosUsuario.put("NombreUsuario", NombreUsuario);
            DatosUsuario.put("AreaUsuario", AreaUsuario);
            int Cantidad = BaseDatos.update("Usuarios", DatosUsuario,
                    "idUsuario="+ IDUsuario, null);
            BaseDatos.close();
            if (Cantidad == 1){
                Toast.makeText(this, "El registro se actualizo correctamente",
                        Toast.LENGTH_SHORT).show();
                ID.setText("");
                Nombre.setText("");
                Area.setText("");
                CargaUsuarios();
            } else {
                Toast.makeText(this, "No se encontro el ID ingresado", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No pueden haber campos vacios", Toast.LENGTH_SHORT).show();
        }
    }

    public void EliminarUsuario(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Produccion", null, 1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();
        String IDUsuario = ID.getText().toString();
        if (!IDUsuario.isEmpty()){
            int Eliminar = BaseDatos.delete("Usuarios", "idUsuario="+ IDUsuario, null);
            BaseDatos.close();
            if(Eliminar == 1){
                Toast.makeText(this, "El registro se elimino correctamente",
                        Toast.LENGTH_SHORT).show();
                ID.setText("");
                Nombre.setText("");
                Area.setText("");
                CargaUsuarios();
            } else {
                Toast.makeText(this, "El ID que intenta eloiminar no existe",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Campo ID usuario no puede estar vacio",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void CargaUsuarios(){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "Produccion", null, 1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();
        Cursor fila = BaseDatos.rawQuery("Select * from Usuarios", null);
        ArrayList<String>  ListaUsuarios = new ArrayList<>();
        if (fila.moveToFirst()){
            do {
                String IDusuario = fila.getString(0);
                String NombreUsuario = fila.getString(1);
                String AreaUsuario = fila.getString(2);
                String userInfo = "ID: "+IDusuario + " Nombre usuario: " +
                        NombreUsuario + " Area usuario: " + AreaUsuario;
                ListaUsuarios.add(userInfo);
            } while (fila.moveToNext());
        }
        BaseDatos.close();
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1, ListaUsuarios);
        Lista.setAdapter(adapter);
    }
}