package org.josemoran.system;

import org.josemoran.controller.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.JSONObject;

public class Main extends Application{
    private Stage stage;
    private static String URL_VIEW = "/view/";
    
    public static void main(String[] args) {
//        JSONObject persona = new JSONObject();
//        
//        persona.put("Nombre: ", "José");
//        persona.put("Apellido: ", "Morán");
//        persona.put("Edad: ", 17);
//        persona.put("Valido: ", true);
//        
//        
//        System.out.println("Contenido de JSON:");
//        System.out.println(persona.toString(4));

        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        inicio();
//        FXMLLoader cargador = new FXMLLoader(getClass().getResource(
//                "/view/InicioView.fxml"));
//        Parent raiz = cargador.load();
//        Scene escena = new Scene(raiz);
//        stage.setScene(escena);
        stage.show();
    }
    
        public FXMLLoader cambiarEscena(String fxml, double ancho, double alto){
            FXMLLoader cargadorFXML = null;
        try {
            cargadorFXML = new FXMLLoader(getClass().getResource(URL_VIEW+fxml));
            Parent archivoFXML = cargadorFXML.load();
            Scene escena = new Scene(archivoFXML,ancho,alto);
            stage.setScene(escena);
        } catch (Exception ex) {
            System.out.println("Error al cambiar: "+ ex.getMessage());
            ex.printStackTrace();
        }
        return cargadorFXML;
    }
        
    public void inicio(){
        InicioController ic = cambiarEscena("InicioView.fxml",600,400).getController();
        ic.setPrincipal(this);
    }
    
    public void CarrosScene(){
        CarrosController cc = cambiarEscena("CarrosView.fxml", 900, 700).getController();
        cc.setPrincipal(this);
    }
}
