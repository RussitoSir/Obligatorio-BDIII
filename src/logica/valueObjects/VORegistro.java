package logica.valueObjects;

import java.io.Serializable;

public class VORegistro implements Serializable{
    private String actividad;
    private int nota;

    public VORegistro(String actividad, int nota) {
        this.actividad = actividad;
        this.nota = nota;
    }


    public String getActividad() {
        return actividad;
    }

    public int getNota() {
        return nota;
    }

 
}
