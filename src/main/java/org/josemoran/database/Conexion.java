package org.josemoran.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author informatica
 */
public class Conexion {
    private static Conexion instancia;
    private Connection conexion;
    private static final String URL = "useSSL=false";
    private static final String USER = "quintom";
    private static final String PASSWORD = "admin";
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    
    public Conexion(){
        
    }
    
    public void conectar(){
        try{
            Class.forName(DRIVER).newInstance();
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexion realizada con exito");
        } catch(ClassNotFoundException | InstantiationException
                |IllegalAccessException | SQLException ex) {
            System.out.println("Error al conectar");
        }
    }
    
    public static Conexion getInstancia(){
        if (instancia == null) {
            instancia  = new Conexion();
        }
        return instancia;
    }
    
    public Connection getConexion(){
        try{
            if (conexion == null || conexion.isClosed()) {
            conectar(); // reconectar
            }        
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conexion;
    }
}
