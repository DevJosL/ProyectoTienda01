/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.josemoran.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.josemoran.system.Main;

/**
 * FXML Controller class
 *
 * @author informatica
 */
public class InicioController implements Initializable {
    private Main principal;

    public void setPrincipal(Main principal) {
        this.principal = principal;
    }
    
    public void inicioSesion(){
        principal.InicioSesionScene();
    }
    
//    public void CambioCarros(){
//        principal.CarrosScene();
//    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
