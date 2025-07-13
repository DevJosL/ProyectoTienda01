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
 * @author josel
 */
public class InicioSesionController implements Initializable {
    private Main principal;

    public void setPrincipal(Main principal) {
        this.principal = principal;
    }
    
    public void MenuView(){
        principal.MenuScene();
    }
    
    public void Volver(){
        principal.inicio();
    }
    
    public void RegistroView(){
        principal.RegistroScene();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
