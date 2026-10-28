package logica.valueObjects;

import java.io.Serializable;

public class VOAlumnoProm extends VOAlumno implements Serializable{
	private double promedio;

    public VOAlumnoProm(int cedula, String nombre, String apellido, double promedio) {
    	super(cedula, nombre, apellido);
    	this.promedio = promedio;
    }
    
    public double getPromedio() {
        return promedio;
    }


 
}
