package grafica.controladores;

import java.io.FileInputStream;  
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.JOptionPane;

import grafica.ventanas.*;
import logica.IFachada;
import logica.excepciones.*;
import logica.valueObjects.*;


public class Controladora {
	private IFachada fachada;
	private VentanaCliente ven;

	public Controladora(VentanaCliente ven) {
		this.ven = ven;

		try {

			Properties pro = new Properties();
			String nomArch = "config/properties.txt";
			pro.load(new FileInputStream(nomArch));
			String ipServidor = pro.getProperty("ipServidor");
			String numPuerto = pro.getProperty("numPuerto");

			fachada = (IFachada) Naming.lookup("//" + ipServidor + ":" + numPuerto + "/fachada");

		} catch (NotBoundException | IOException e) {
			ven.mostrarMensaje("No se pudo conectar al servidor");
		}

	}

	public void nuevoAlumno(int cedula, String nombre, String apellido) {
		try {
			VOAlumno alumno = new VOAlumno(cedula, nombre, apellido);
			fachada.nuevoAlumno(alumno);
			ven.mostrarMensaje("Alumno registrado con éxito.");
		} catch (PersistenciaException e) {
			ven.mostrarMensaje(e.getMessage());
		} catch (YaExisteAlumnoException e) {
			ven.mostrarMensaje(e.getMessage());
		} catch (RemoteException e) {
			ven.mostrarMensaje("No se pudo conectar al servidor");

		}
	}
	
	public void registrarNota(int cedula, String actividad, int nota) {
		try {
			VORegistro registro = new VORegistro(actividad, nota);
			fachada.registrarNota(cedula, registro);
			ven.mostrarMensaje("Registro ingresado exitosamente");
		} catch (PersistenciaException e) {
			ven.mostrarMensaje(e.getMessage());
		} catch (CedulaInexistenteException e) {
			ven.mostrarMensaje(e.getMessage());
		} catch (RemoteException e) {
			ven.mostrarMensaje("No se pudo conectar al servidor");

		}
	}

	

}