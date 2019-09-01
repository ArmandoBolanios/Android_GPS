package com.mfast.appgps;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.mfast.appgps.MainActivity;
import com.mfast.appgps.R;
import com.mfast.appgps.MainActivity.Localizacion;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity {

	TextView mensaje1;
	TextView mensaje2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mensaje1 = (TextView) findViewById(R.id.mensaje_id);
		mensaje2 = (TextView) findViewById(R.id.mensaje_id2);

		/* Uso de la clase LocationManager para obtener la localizacion del GPS */
		LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Localizacion Local = new  Localizacion();
		Local.setMainActivity(this);
		mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
				(LocationListener) Local);

		mensaje1.setText("Localizacion agregada");
		mensaje2.setText("");
	}



	public void setLocation(Location loc) {
		//Obtener la direccion de la calle a partir de la latitud y la longitud 
		if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
			try {
				Geocoder geocoder = new Geocoder(this, Locale.getDefault());
				List<Address> list = geocoder.getFromLocation(
						loc.getLatitude(), loc.getLongitude(), 1);
				if (!list.isEmpty()) {
					Address DirCalle = list.get(0);
					mensaje2.setText("Mi direccion es: \n"
							+ DirCalle.getAddressLine(0));
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/* Aqui empieza la Clase Localizacion */
	public class Localizacion implements LocationListener {
		MainActivity mainActivity;

		public MainActivity getMainActivity() {
			return mainActivity;
		}

		public void setMainActivity(MainActivity mainActivity) {
			this.mainActivity = mainActivity;
		}

		@Override
		public void onLocationChanged(Location loc) {
			// Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
			// debido a la deteccion de un cambio de ubicacion
			
			loc.getLatitude();
			loc.getLongitude();
			String Text = "Mi ubicacion actual es: " + "\n Lat = "
					+ loc.getLatitude() + "\n Long = " + loc.getLongitude();
			mensaje1.setText(Text);
			this.mainActivity.setLocation(loc);
		}

		@Override
		public void onProviderDisabled(String provider) {
			// Este metodo se ejecuta cuando el GPS es desactivado
			mensaje1.setText("GPS Desactivado");
		}

		@Override
		public void onProviderEnabled(String provider) {
			// Este metodo se ejecuta cuando el GPS es activado
			mensaje1.setText("GPS Activado");
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// Este metodo se ejecuta cada vez que se detecta un cambio en el
			// status del proveedor de localizacion (GPS)
			// Los diferentes Status son:
			// OUT_OF_SERVICE -> Si el proveedor esta fuera de servicio
			// TEMPORARILY_UNAVAILABLE -> Temporalmente no disponible pero se
			// espera que este disponible en breve
			// AVAILABLE -> Disponible
		}

	}/* Fin de la clase localizacion */

}
