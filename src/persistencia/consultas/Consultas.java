package persistencia.consultas;

public class Consultas {
	
	public String existeAlumno() {
		
		return "SELECT * FROM Alumnos WHERE cedula = ?";
	}
	
	public String ingresoAlumno() {
		
		return "INSERT INTO Alumnos VALUES (?,?,?)";
	}
	
	public String numRegistro() {
		
		return "SELECT MAX(numero) from Registros WHERE cedAlu = ?";
	}
	
	public String ingresarRegistro() {
		
		return "INSERT INTO Registros VALUES (?,?,?,?)";
	}
	
	public String devolverAlumnosOrdenados() {
		
		return "SELECT * FROM Alumnos ORDER BY cedula";
	}
	
	public String existeRegistro() {
		
		return "SELECT * FROM Registros WHERE numero = ? AND cedAlu = ?";
	}
	
	public String existeAlgunRegistroAlumno() {
		
		return "SELECT * FROM Registros WHERE cedAlu = ?";
	}
	
	public String obtenerRegistrosAlumno() {
		
		return "SELECT * FROM Registros WHERE cedAlu = ? ORDER BY numero";
	}
	
	public String obtenerCantidadAlumnosActividadConNota() {
		
		return "SELECT COUNT(DISTINCT cedAlu) FROM Registros WHERE actividad = ? AND nota = ?";
	}
	
	public String obtenerActividadMasCursadaPorUnAlumno() {
		
		return "SELECT actividad, COUNT(*) FROM Registros WHERE cedAlu = ? GROUP BY actividad ORDER BY COUNT(*) DESC LIMIT 1";
	}

}
