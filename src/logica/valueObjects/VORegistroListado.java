package logica.valueObjects;

import java.io.Serializable;

public class VORegistroListado extends VORegistro implements Serializable{
    private int numero;


    
    public VORegistroListado(String actividad, int nota, int numero) {
    	super(actividad, nota);
    	this.numero = numero;
    	 }
    
    public int getNumero() {
    	return numero;
    }

 
}
