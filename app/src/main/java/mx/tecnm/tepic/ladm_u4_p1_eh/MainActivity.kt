package mx.tecnm.tepic.ladm_u4_p1_eh

import android.content.pm.PackageManager
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var listaID = ArrayList<String>()
    val siPermisoLlamadas = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_PHONE_STATE,
                    android.Manifest.permission.SEND_SMS,android.Manifest.permission.READ_CALL_LOG),siPermisoLlamadas)
        }
        agregar.setOnClickListener {
            try {
                var baseDatos = BaseDatos(this, "telefonia", null, 1)
                var insertar = baseDatos.writableDatabase
                var SQL =
                        "INSERT INTO TELEFONIA VALUES('${telefono.text.toString()}','${mensaje.text.toString()}', '${nombre.text.toString()}', '${deseado.text.toString()}')"
                insertar.execSQL(SQL)
                insertar.close()
                baseDatos.close()
                telefono.setText("")
                mensaje.setText("")
                nombre.setText("")
                deseado.setText("")
            } catch (e: SQLiteException) {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
            cargarLista()
        }
        cargarLista()
    }

    private fun cargarLista() {
        try {
            var baseDatos = BaseDatos(this, "telefonia",null,1)
            var select = baseDatos.readableDatabase
            var SQL ="SELECT * FROM TELEFONIA"

            var cursor = select.rawQuery(SQL, null)
            if(cursor.count>0){
                var arreglo =ArrayList<String>()
                this.listaID = ArrayList<String>()
                cursor.moveToFirst()
                var cantidad = cursor.count-1
                (0..cantidad).forEach {
                    var data ="Nombre: ${cursor.getString(2)}" +
                            "\nTelefono: ${cursor.getString(0)}" +
                            "\nTipo: ${cursor.getString(3)}" +
                            "\n Mensaje: ${cursor.getString(1)}"
                    arreglo.add(data)
                    cursor.moveToNext()
                }
                lista.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arreglo)
            }
            select.close()
            baseDatos.close()
        }catch (e: SQLiteException){
            Toast.makeText(this,e.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==siPermisoLlamadas){
        }
    }
}
