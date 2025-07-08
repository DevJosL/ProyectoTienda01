package org.josemoran.system;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.JSONObject;

public class Main extends Application{

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
        FXMLLoader cargador = new FXMLLoader(getClass().getResource(
                "/view/InicioView.fxml"));
        Parent raiz = cargador.load();
        Scene escena = new Scene(raiz);
        stage.setScene(escena);
        stage.show();
    }
}
