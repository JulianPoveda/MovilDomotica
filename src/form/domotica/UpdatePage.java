package form.domotica;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class UpdatePage extends AsyncTask<String, Integer, Integer>{ //doInBakGround, Progress, onPostExecute
	
	/**Variables Locales**/
	private Context 			ConnectServerContext;
	//private String LineasSQL[];
	

	private String URL;				//= "http://190.93.133.87:8080/ControlEnergia/WS/AndroidWS.php?wsdl";
	private String NAMESPACE;		//= "http://190.93.133.87:8080/ControlEnergia/WS";
	
	//Variables con la informacion del web service
	private static final String METHOD_NAME	= "CambioEstadoElemento";
	private static final String SOAP_ACTION	= "CambioEstadoElemento";
	SoapPrimitive 	response = null;
	ProgressDialog 	_pDialog;
	
		
	//Contructor de la clase
	public UpdatePage(Context context, String Directorio){
		this.ConnectServerContext 		= context;
	}


	//Operaciones antes de realizar la conexion con el servidor
	protected void onPreExecute(){
		/***Codigo para el cargue desde la base de datos de la ip y puerto de conexion para el web service***/
		/*this.URL 			= "http//190.93.133.87:8080/Domotica/ServerPDA/DomoticaWS.php?wsdl";
		this.NAMESPACE 		= "http//190.93.133.87:8080/Domotica/ServerPDA";*/
		
		this.URL 			= "http://190.93.133.87:8080/Domotica/ServerPDA/DomoticaWS.php?wsdl";
		this.NAMESPACE 		= "http://190.93.133.87:8080/Domotica/ServerPDA";
		
		
		Toast.makeText(this.ConnectServerContext,"Iniciando conexion con el servidor, por favor espere...", Toast.LENGTH_SHORT).show();	
		/*_pDialog = new ProgressDialog(this.ConnectServerContext);
        _pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        _pDialog.setMessage("Ejecutando operaciones...");
        _pDialog.setCancelable(false);
        _pDialog.setProgress(0);
        _pDialog.setMax(100);
        _pDialog.show();*/
	}
	

	
	//Conexion con el servidor en busca de actualizaciones de trabajo programado
	@Override
	protected Integer doInBackground(String... params) {
		int _retorno = 0;
		try{
			SoapObject so=new SoapObject(NAMESPACE, METHOD_NAME);
			so.addProperty("Elemento", params[0]);	
			so.addProperty("Estado", params[1]);	
			SoapSerializationEnvelope sse=new SoapSerializationEnvelope(SoapEnvelope.VER11);
			sse.dotNet=true;
			sse.setOutputSoapObject(so);
			HttpTransportSE htse=new HttpTransportSE(URL);
			htse.call(SOAP_ACTION, sse);
			response=(SoapPrimitive) sse.getResponse();
			
			/**Inicio de tratamiento de la informacion recibida**/
			if(response.toString()==null) {
				_retorno = -1;
			}else if(response.toString().isEmpty()){
				_retorno = -2;
			}else{
				_retorno = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return _retorno;
	}
	
	
	
	//Operaciones despues de finalizar la conexion con el servidor
	@Override
	protected void onPostExecute(Integer rta) {
		if(rta==1){
			Toast.makeText(this.ConnectServerContext,"Actualizado en el servidor.", Toast.LENGTH_LONG).show();
		}else if(rta==-1){
			Toast.makeText(this.ConnectServerContext,"Intento fallido, el servidor no ha respondido.", Toast.LENGTH_SHORT).show();
		}else if(rta==-2){
			Toast.makeText(this.ConnectServerContext,"Intento fallido, el servidor no ha respondido.", Toast.LENGTH_SHORT).show();	
		}else{
			Toast.makeText(this.ConnectServerContext,"Error desconocido.", Toast.LENGTH_SHORT).show();
		}
		//_pDialog.dismiss();
	}	
	
	
	@Override
    protected void onProgressUpdate(Integer... values) {
		int progreso = values[0].intValue();
        _pDialog.setProgress(progreso);
    }
}

