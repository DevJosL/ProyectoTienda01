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
public class InicioSesionController implements Initializable {

    private Main principal;

    @FXML
    private TextField txtCorreo;
    
    @FXML
    private PasswordField pssContraseña;
    
    @FXML
    private Text txtError;

    public void setPrincipal(Main principal) {
        this.principal = principal;
    }

    public void Volver() {
        principal.inicio();
    }

    public void RegistroView() {
        principal.RegistroScene();
    }

    public void ingresar() {
        try {
            //Se hara una comprobación con un SP de la DB donde verificara si "Correo" y "Contraseña"
            //existan dentro de la DB
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_loginUsuario(?,?);");
            enunciado.setString(1, txtCorreo.getText());
            enunciado.setString(2, pssContraseña.getText());
            ResultSet resultado = enunciado.executeQuery();
            if (resultado.next()) {
                String mensaje = resultado.getString(1);

                if ("Autorizado".equals(mensaje)) {
                    principal.MenuScene();
                    System.out.println("Autorizado");
                } else {
                    System.out.println("Acceso denegado");
                    txtError.setVisible(true);
                }
            }
        } catch (SQLException ex) {
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Expresion lambda runLater (Funciona luego de cargar toda la escena) donde al entrar a esta escena se seleccionara el campo correo.
        Platform.runLater(() -> txtCorreo.requestFocus());
        
        //Desde el textField correo se podra dar enter para pasar al siguiente requisito
        txtCorreo.setOnAction(eh -> pssContraseña.requestFocus());
        
        //Desde el PasswordField contraseña se podra dar enter para ingresar al menú
        pssContraseña.setOnAction(eh -> ingresar());
    }

}
