package form.domotica;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.widget.Toast;


public class Bluetooth {
	Context 			context;
	private String 		mydeviceaddress;
	private String 		mydevicename;
	BluetoothAdapter 	bluetooth = BluetoothAdapter.getDefaultAdapter();
	private BluetoothDevice 	mmDevice= null;
	BluetoothSocket 	mmSocket;
	OutputStream 		mmOutputStream;
	//private UIHelper helper = new UIHelper(this);
	
	public Bluetooth(Context context){
		this.context = context;
	}
	
	//Funcion para detectar si el equipo tiene bluetooth
	public boolean ExistBluetooth(){
		boolean result = false;
		if(bluetooth != null){
			EnabledBluetoth();
			result = true;
		}
		return result;
	}
	
	
	//Funcion para detectar si el bluetooth esta encendido
	public void EnabledBluetoth(){
		if(!bluetooth.isEnabled()){
			bluetooth.enable();
		}
	}
	
	//Funcion para mostrar el nombre del dispositivo y la direccion
	public void GetOurDevice(){
		String status;
		if (ExistBluetooth()) {		
		    mydeviceaddress = bluetooth.getAddress();
		    mydevicename = bluetooth.getName();
		    status = mydevicename + " : " + mydeviceaddress;
		}else{
		    status = "Bluetooth is not Enabled.";
		}
		Toast.makeText(context, status, Toast.LENGTH_LONG).show();
	}
	
	
	//Funcion para mostrar el estado del bluetooth
	public void StatusBluetooth(){
		int state = bluetooth.getState();
		Toast.makeText(context, mydevicename + ":" + mydeviceaddress + " : " + state, Toast.LENGTH_LONG).show();
	}
	
	
	//Funcion para listar los dispositivos bluetooth asociados con el equipo
	public void ListDevice(){
		Set<BluetoothDevice> pairedDevices = bluetooth.getBondedDevices();
		if (pairedDevices.size() > 0) {
			for (BluetoothDevice device : pairedDevices) {
				Toast.makeText(context, "Equipo " +  device.getName() + " : " + device.getAddress(), Toast.LENGTH_LONG).show();
		    }
		}
	}
	
	
	
	//Funcion para realizar el intento de impresion
	public void IntentPrint(String Impresora, String txtvalue){
		try{
			byte[] buffer = txtvalue.getBytes();
			InitPrinter(Impresora);
			for(int i=0;i<=buffer.length-1;i++){
				mmOutputStream.write(buffer[i]);
			}
			//mmOutputStream.close();
			//mmSocket.close();
		}catch(Exception ex){
		}
	} 
	
	
	private void setUUID(String _uuid){
		try {
			UUID uuid = UUID.fromString(_uuid); 
			mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
			bluetooth.cancelDiscovery();
			mmSocket.connect();
			mmOutputStream = mmSocket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private boolean getUUID(){
		if(mmSocket == null){
			return false;
		}else{
			return mmSocket.isConnected();
		}
	}
	
	private int getStateBluetooth(){
		return mmDevice.getBondState();
	}
	
	private void setPairedBluetooth(){
		//mmDevice.createBond();
	}
	
	private void getAddressBluetoothByName(String _name){
		Set<BluetoothDevice> pairedDevices = bluetooth.getBondedDevices();
		if(pairedDevices.size() > 0){
			for(BluetoothDevice device : pairedDevices){
				if(device.getName().equals(_name)){ 
					this.mmDevice = device;
				  	break;
				}
			}
		}else{
			this.mmDevice = null;
			Toast.makeText(this.context, "No se detectaron dispositivos bluetooth sincronizados.", Toast.LENGTH_LONG).show();
		}
	}
	
	
	//Funcion para imprimir 
	private void InitPrinter(String Impresora){
		try{
			getAddressBluetoothByName(Impresora);
			if(mmDevice!=null){
				
				//UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
				//mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
				//bluetooth.cancelDiscovery();
				if(!getUUID()){
					setUUID("00001101-0000-1000-8000-00805F9B34FB");
				}
				
				switch(getStateBluetooth()){
					case BluetoothDevice.BOND_NONE:
						setPairedBluetooth();
						break;
						
					case BluetoothDevice.BOND_BONDING:
						break;
						
					case BluetoothDevice.BOND_BONDED:
						try{
							//mmOutputStream = mmSocket.getOutputStream();
						}catch(Exception e){
							Toast.makeText(this.context, e.toString(), Toast.LENGTH_LONG).show();
						}
						break;
				}
				
				/*if(mmDevice.getBondState()==12){	//Condicion para saber si esta apareada la impresora
					try{
						//mmSocket.connect();
						if(!mmSocket.isConnected()){
							mmSocket.connect();
							mmOutputStream = mmSocket.getOutputStream();
						}
					}catch(Exception e){
						Toast.makeText(this.context, e.toString(), Toast.LENGTH_LONG).show();
					}
				}else{
					Toast.makeText(this.context, "No se encontro la impresora no esta sincronizada.", Toast.LENGTH_LONG).show();
				}*/
			}else{
				Toast.makeText(this.context, "No se encontro la impresora en la lista de dispositivos apareados.", Toast.LENGTH_LONG).show();
			}				
		}catch(Exception ex){
			Toast.makeText(this.context, "No se detecto bluetooth en el equipo.", Toast.LENGTH_LONG).show();
		}
	}
	
	
	public ArrayList<String> GetDeviceBluetooth(){
		ArrayList<String> _lstDevice= new ArrayList<String>();
		_lstDevice.add("");
		try{
			Set<BluetoothDevice> pairedDevices = bluetooth.getBondedDevices();
			if(pairedDevices.size() > 0){
				for(BluetoothDevice device : pairedDevices){
					_lstDevice.add(device.getName());
				}
			}
		}catch(Exception e){
			
		}
		return _lstDevice;
	}
}