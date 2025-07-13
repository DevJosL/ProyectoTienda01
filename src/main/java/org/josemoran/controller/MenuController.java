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
public class MenuController implements Initializable {
    private Main principal;

    public void setPrincipal(Main principal) {
        this.principal = principal;
    }
    
    public void Volver(){
        principal.inicio();
    }
    
    public void UsuariosView(){
        principal.UsuariosScene();
    }
    
    public void CategoriasView(){
        principal.CategoriaScene();
    }
    
    public void ProveedoresView(){
        principal.ProveedorScene();
    }
    
    public void CarrosView(){
        principal.CarrosScene();
    }
    
    public void FacturasView(){
        principal.FacturaScene();
    }
    
    public void DetallesFacturasView(){
        principal.DetalleScene();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
