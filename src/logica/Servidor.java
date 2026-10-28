package logica;

import java.io.FileInputStream;  
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Properties;

import logica.excepciones.PersistenciaException;	;

public class Servidor {

	public static void main(String[] args) throws PersistenciaException{
		try {
			Properties pro = new Properties();
			String nomArch = "config/properties.txt";
			pro.load(new FileInputStream(nomArch));
			String ipServidor = pro.getProperty("ipServidor");
			String numPuerto = pro.getProperty("numPuerto");
			try {
				LocateRegistry.createRegistry(1099);
				Fachada fachada = new Fachada();
				System.out.println("Antes de publicarlo");
				Naming.rebind("//" + ipServidor + ":" + numPuerto + "/fachada", fachada);
				System.out.println("Luego de publicarlo");
			} catch (PersistenciaException | RemoteException e) {
				throw new PersistenciaException("Error en el acceso a datos");
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
