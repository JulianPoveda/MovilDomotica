package form.domotica;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;

public class FormPrincipal extends Activity implements OnClickListener{
	private Bluetooth 				MB;
	private ArrayAdapter<String> 	AdapLstImpresoras;	
	private ArrayList<String> 		_listaImpresoras;
	private Switch 					_lampara, _puerta, _potencia;
	private Spinner 				_bluetooth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_form_principal);
		
		this.MB = new Bluetooth(this);
		
		this._lampara 	= (Switch) findViewById(R.id.btnLampara);
		this._puerta 	= (Switch) findViewById(R.id.btnPuerta);
		this._potencia 	= (Switch) findViewById(R.id.btnPotencia);
		this._bluetooth	= (Spinner) findViewById(R.id.CmbBluetooth);
		
		this._listaImpresoras 	= this.MB.GetDeviceBluetooth();
		this.AdapLstImpresoras 	= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,this._listaImpresoras);
		this._bluetooth.setAdapter(this.AdapLstImpresoras);
		
		this._lampara.setOnClickListener(this);
		this._puerta.setOnClickListener(this);
		this._potencia.setOnClickListener(this);		
	}
	

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.btnLampara:
				if(this._lampara.isActivated()){
					this.MB.IntentPrint(this._bluetooth.getSelectedItem().toString(), "encender 1\r\n");
				}else{
					this.MB.IntentPrint(this._bluetooth.getSelectedItem().toString(), "apagar 1\r\n");
				}
				break;
			
			case R.id.btnPuerta:
				if(this._puerta.isActivated()){
					this.MB.IntentPrint(this._bluetooth.getSelectedItem().toString(), "encender 2\r\n");
				}else{
					this.MB.IntentPrint(this._bluetooth.getSelectedItem().toString(), "apagar 2\r\n");
				}
				break;
			
			case R.id.btnPotencia:
				if(this._potencia.isActivated()){
					this.MB.IntentPrint(this._bluetooth.getSelectedItem().toString(), "encender 3\r\n");
				}else{
					this.MB.IntentPrint(this._bluetooth.getSelectedItem().toString(), "apagar 3\r\n");
				}
				break;	
		}
		
	}
}
