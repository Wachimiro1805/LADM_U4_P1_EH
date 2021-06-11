package mx.tecnm.tepic.ladm_u4_p1_eh

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteException
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.widget.Toast

class CallReceiver : BroadcastReceiver(){
    var ring = false
    var callReceived = false
    var number :String?=null
    private var incoming_number: String? = null
    var mensajeEnvio=""
    override fun onReceive(context: Context, intent: Intent) {

        var state = intent.extras!!.getString(TelephonyManager.EXTRA_STATE)

        val tm = context.getSystemService(Service.TELEPHONY_SERVICE) as TelephonyManager

        when(tm.callState){
            TelephonyManager.CALL_STATE_RINGING->{
                incoming_number = intent.getStringExtra("incoming_number")
                Toast.makeText(context,"INCOMING: ${incoming_number}", Toast.LENGTH_LONG).show()
                if(incoming_number!=null){
                    try{
                        val cursor=BaseDatos(context,"telefonia",null,1)
                            .readableDatabase
                            .rawQuery("SELECT * FROM TELEFONIA WHERE CELULAR = '${incoming_number}'",null)
                        if(cursor.count>0){
                            cursor.moveToFirst()
                            mensajeEnvio = cursor.getString(1)
                            SmsManager.getDefault().sendTextMessage(incoming_number,null,mensajeEnvio,null,null)
                        }
                    }catch (e: SQLiteException){
                        Toast.makeText(context, e.message, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
            TelephonyManager.CALL_STATE_IDLE->{
            }
        }
    }
}
