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
    
    public void VenderView(){
        principal.VenderScene();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
}
