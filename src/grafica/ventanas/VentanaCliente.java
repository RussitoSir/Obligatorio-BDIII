package grafica.ventanas;

import java.awt.BorderLayout;  
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import grafica.controladores.Controladora;


public class VentanaCliente extends JFrame {

	private Controladora controlador;
	private JTabbedPane tabbedPane;
	


	public VentanaCliente() {
		
			// Inicializa la controladora
			controlador = new Controladora(this);

			setTitle("BD III - Practico 4");
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setBounds(100, 100, 1050, 700);

			tabbedPane = new JTabbedPane();
			getContentPane().add(tabbedPane, BorderLayout.CENTER);
			tabbedPane.setForeground(Color.WHITE);
			Color gris = new Color(45, 45, 45);
			tabbedPane.setBackground(gris);

			// Llamada a todos los paneles solicitados
			initPaneles();
		
	}

	private void initPaneles() {
		initPanelNuevoAlumno(); // 1: Alta
		initPanelRegistrarNota(); // 2: Detalle Atributos
	}
	
	public void mostrarMensaje(String mensaje ) { 
		
		JOptionPane.showMessageDialog(null, mensaje);
		
	}
	


	// --- PESTAÑA 1: ALTA DE POSTRE (Req. 1) ---
	private void initPanelNuevoAlumno() {
		// Inicio el Panel
		JPanel panel = new JPanel(null);
		//Modificamos el color
		Color azul = new Color(85, 110, 145);
		panel.setBackground(azul);
		// Agrego el panel el tabbedPanel
		tabbedPane.addTab("Ingreso Alumno", panel);
		// Campos de texto para los atributos básicos
		JTextField txtCedula = new JTextField();
		txtCedula.setBounds(20, 40, 80, 25);
		panel.add(txtCedula);
		JTextField txtNombre = new JTextField();
		txtNombre.setBounds(110, 40, 150, 25);
		panel.add(txtNombre);
		JTextField txtApellido = new JTextField();
		txtApellido.setBounds(270, 40, 80, 25);
		panel.add(txtApellido);

		// DEFINICION BOTÓN:
		JButton btnReg = new JButton("INGRESAR ALUMNO");
		btnReg.setBounds(410, 40, 160, 65); 
		panel.add(btnReg);

		btnReg.addActionListener(e -> {
			
				controlador.nuevoAlumno(Integer.parseInt(txtCedula.getText()), txtNombre.getText(), txtApellido.getText());
			
		});
		
		Color blanco = new Color(236, 240, 241);
		
		JLabel lblNewLabel = new JLabel("Cedula");
		lblNewLabel.setBounds(31, 15, 46, 14);
		lblNewLabel.setForeground(blanco);
		panel.add(lblNewLabel);

		JLabel lblNombre = new JLabel("Nombre");
		lblNombre.setBounds(137, 15, 46, 14);

		lblNombre.setForeground(blanco);
		panel.add(lblNombre);

		JLabel lblPrecio = new JLabel("Apellido");
		lblPrecio.setBounds(284, 15, 46, 14);
		lblPrecio.setForeground(blanco);
		panel.add(lblPrecio);
		
		

	}

	// --- PESTAÑA 2: LISTADO DETALLADO POSTRES ---
	private void initPanelRegistrarNota() {
		// Inicio el Panel
		JPanel panel = new JPanel(null);
		//Modificamos el color
		Color azul = new Color(85, 110, 145);
		panel.setBackground(azul);
		// Agrego el panel el tabbedPanel
		tabbedPane.addTab("Registrar Nota", panel);
		// Campos de texto para los atributos básicos
		JTextField txtCedula = new JTextField();
		txtCedula.setBounds(20, 40, 80, 25);
		panel.add(txtCedula);
		JTextField txtActividad = new JTextField();
		txtActividad.setBounds(110, 40, 150, 25);
		panel.add(txtActividad);
		JTextField txtNota = new JTextField();
		txtNota.setBounds(270, 40, 80, 25);
		panel.add(txtNota);

		// DEFINICION BOTÓN:
		JButton btnReg = new JButton("REGISTRAR NOTA");
		btnReg.setBounds(410, 40, 160, 65); 
		panel.add(btnReg);

		btnReg.addActionListener(e -> {
			
				controlador.registrarNota(Integer.parseInt(txtCedula.getText()), txtActividad.getText(), Integer.parseInt(txtNota.getText()));
			
		});
		
		Color blanco = new Color(236, 240, 241);
		
		JLabel lblNewLabel = new JLabel("Cedula");
		lblNewLabel.setBounds(31, 15, 46, 14);
		lblNewLabel.setForeground(blanco);
		panel.add(lblNewLabel);

		JLabel lblNombre = new JLabel("Actividad");
		lblNombre.setBounds(137, 15, 46, 14);

		lblNombre.setForeground(blanco);
		panel.add(lblNombre);

		JLabel lblPrecio = new JLabel("Nota");
		lblPrecio.setBounds(284, 15, 46, 14);
		lblPrecio.setForeground(blanco);
		panel.add(lblPrecio);
		
		

	}
	
}