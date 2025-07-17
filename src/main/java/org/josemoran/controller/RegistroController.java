package org.josemoran.controller;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.josemoran.database.Conexion;
import org.josemoran.system.Main;

/**
 * FXML Controller class
 *
 * @author josel
 */
public class RegistroController implements Initializable {

    private Main principal;

    @FXML
    private TextField txtNombre, txtApellido,
            txtCorreo, txtTelefono, txtDireccion;

    @FXML
    private PasswordField pssContrasena;

    @FXML
    private Text txtError;

    public void setPrincipal(Main principal) {
        this.principal = principal;
    }

    public void LoginScene() {
        principal.InicioSesionScene();
    }

    public void volver() {
        principal.inicio();
    }

    public void crearCuenta() {
        //Primero se comprobara si algunos (o todos) los campos estan vacios
        if (txtNombre.getText().isBlank()
                || txtApellido.getText().isBlank()
                || txtCorreo.getText().isBlank()
                || pssContrasena.getText().isBlank()
                || txtTelefono.getText().isBlank()
                || txtDireccion.getText().isBlank()) {
            txtError.setVisible(true);
        } else {
            //Se realizara un SP_Agregar para insertar los datos del usuario nuevo.
            try {
                CallableStatement enunciado = Conexion.getInstancia().getConexion()
                        .prepareCall("call sp_AgregarUsuario(?,?,?,?,?,?);");
                enunciado.setString(1, txtNombre.getText());
                enunciado.setString(2, txtApellido.getText());
                enunciado.setString(3, txtCorreo.getText());
                enunciado.setString(4, pssContrasena.getText());
                enunciado.setString(5, txtTelefono.getText());
                enunciado.setString(6, txtDireccion.getText());
                enunciado.executeQuery();

                System.out.println("Usuarios agregados correctamente");
            } catch (SQLException ex) {
                System.out.println("Error al agregar Usuarios " + ex.getMessage());
                ex.printStackTrace();
            }
            principal.MenuScene();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Expresion lambda runLater (Funciona luego de cargar toda la escena) 
        //donde al entrar a esta escena se seleccionara el campo correo.
        Platform.runLater(() -> txtNombre.requestFocus());

        //Desde el textField  se podra dar enter para pasar al siguiente requisito
        txtNombre.setOnAction(eh -> txtApellido.requestFocus());
        txtApellido.setOnAction(eh -> txtCorreo.requestFocus());
        txtCorreo.setOnAction(eh -> pssContrasena.requestFocus());
        pssContrasena.setOnAction(eh -> txtTelefono.requestFocus());
        txtTelefono.setOnAction(eh -> txtDireccion.requestFocus());

        //Desde el textField se podra dar enter para ingresar al menú
        txtDireccion.setOnAction(eh -> crearCuenta());

    }

}
