package org.josemoran.controller;

import org.josemoran.system.Main;
import org.josemoran.model.*;
import org.josemoran.database.Conexion;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.ArrayList;

import java.sql.Date;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

/**
 * FXML Controller class
 *
 * @author josel
 */
public class UsuariosController implements Initializable {

    private Main principal;
    private ObservableList<Usuarios> listaUsuarios;
    private Usuarios modeloUsuario;

    private enum EstadoFormulario {
        AGREGAR, EDITAR, ELIMINAR, NINGUNO
    };
    EstadoFormulario estadoActual = EstadoFormulario.NINGUNO;

    @FXML
    private TableView<Usuarios> tablaUsuarios;

    @FXML
    private TableColumn colID, colNombre, colApellido,
            colCorreo, colContrasena, colTelefono,
            colDirección;

    @FXML
    private TextField txtBuscar, txtId, txtNombre, txtApellido,
            txtCorreo, txtContrasena, txtTelefono, txtDireccion;

    @FXML
    private Button btnAnterior, btnSiguiente, btnNuevo,
            btnEliminar, btnEditar, btnCancelar,
            btnGuardar, btnVolver, btnBuscar;

    public void setPrincipal(Main principal) {
        this.principal = principal;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumna();
        cargarTabla();
        tablaUsuarios.setOnMouseClicked(eventHandler -> cargarEnTextoField());
        txtBuscar.setOnAction(eh -> BuscarTabla());
    }

    public void configurarColumna() {
        colID.setCellValueFactory(new PropertyValueFactory<Usuarios, Integer>("idUser"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Usuarios, String>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<Usuarios, String>("apellido"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<Usuarios, String>("correo"));
        colContrasena.setCellValueFactory(new PropertyValueFactory<Usuarios, String>("contrasena"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Usuarios, String>("telefono"));
        colDirección.setCellValueFactory(new PropertyValueFactory<Usuarios, String>("direccion"));
    }

    public void cargarTabla() {
        listaUsuarios = FXCollections.observableList(ListarTabla());
        tablaUsuarios.setItems(listaUsuarios);
        tablaUsuarios.getSelectionModel().selectFirst();
        if (tablaUsuarios.getSelectionModel().getSelectedItem() != null) {
            cargarEnTextoField();
        }
    }

    public void cargarEnTextoField() {
        Usuarios usuarioSeleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        txtId.setText(String.valueOf(usuarioSeleccionado.getIdUser()));
        txtNombre.setText(usuarioSeleccionado.getNombre());
        txtApellido.setText(usuarioSeleccionado.getApellido());
        txtCorreo.setText(usuarioSeleccionado.getCorreo());
        txtContrasena.setText(usuarioSeleccionado.getContrasena());
        txtTelefono.setText(usuarioSeleccionado.getTelefono());
        txtDireccion.setText(usuarioSeleccionado.getDireccion());
    }

    public ArrayList<Usuarios> ListarTabla() {
        ArrayList<Usuarios> usuario = new ArrayList<>();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_ListarUsuarios();");
            ResultSet resultado = enunciado.executeQuery();

            while (resultado.next()) {
                usuario.add(new Usuarios(resultado.getInt(1), 
                        resultado.getString(2), 
                        resultado.getString(3), 
                        resultado.getString(4), 
                        resultado.getString(5), 
                        resultado.getString(6), 
                        resultado.getString(7)));
            }
        } catch (SQLException ex) {
            System.out.println("Error al cargar de MySQL la entidad" + ex.getMessage());
            ex.printStackTrace();
        }
        return usuario;
    }

    public Usuarios obtenerModeloUsuarios() {
        int idUser;
        if (txtId.getText().isEmpty()) {
            idUser = 1;
        } else {
            idUser = Integer.parseInt(txtId.getText());
        }
        String nombre = txtNombre.getText();
        String apellido = txtApellido.getText();
        String correo = txtCorreo.getText();
        String contrasena = txtContrasena.getText();
        String telefono = txtTelefono.getText();
        String direccion = txtDireccion.getText();
        Usuarios usuario = new Usuarios(idUser, nombre, apellido, 
                correo, contrasena, telefono, 
                direccion);
        return usuario;
    }
    
    public void agregarUsuario(){
        modeloUsuario = obtenerModeloUsuarios();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_AgregarUsuario(?,?,?,?,?,?);");
            enunciado.setString(1, modeloUsuario.getNombre());
            enunciado.setString(2, modeloUsuario.getApellido());
            enunciado.setString(3, modeloUsuario.getCorreo());
            enunciado.setString(4, modeloUsuario.getContrasena());
            enunciado.setString(5, modeloUsuario.getTelefono());
            enunciado.setString(6, modeloUsuario.getDireccion());
            enunciado.executeQuery();
            cargarTabla();
        } catch (SQLException e) {
            System.out.println("Error al agregar " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void actualizarUsuario(){
        modeloUsuario = obtenerModeloUsuarios();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_ActualizarUsuario(?,?,?,?,?,?,?);");
            enunciado.setInt(1, modeloUsuario.getIdUser());
            enunciado.setString(2, modeloUsuario.getNombre());
            enunciado.setString(3, modeloUsuario.getApellido());
            enunciado.setString(4, modeloUsuario.getCorreo());
            enunciado.setString(5, modeloUsuario.getContrasena());
            enunciado.setString(6, modeloUsuario.getTelefono());
            enunciado.setString(7, modeloUsuario.getDireccion());
            enunciado.executeQuery();
            cargarTabla();
        } catch (Exception e) {
            System.out.println("Error al actualizar" + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void eliminarUsuario(){
        modeloUsuario = obtenerModeloUsuarios();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_EliminarUsuario(?);");
            enunciado.setInt(1, modeloUsuario.getIdUser());
            enunciado.executeQuery();
            cargarTabla();
        } catch (Exception e) {
            System.out.println("Error al eliminar " + e.getMessage());
        }
    }
    
    public void limpiarFormulario(){
        txtId.clear(); txtNombre.clear(); txtApellido.clear();
        txtCorreo.clear(); txtContrasena.clear(); txtTelefono.clear();
        txtDireccion.clear();
    }
    
    public void estadoFormulario(EstadoFormulario est){
        estadoActual = est;
        boolean activo = (est == EstadoFormulario.AGREGAR || est == EstadoFormulario.EDITAR);
        
        txtNombre.setDisable(!activo);
        txtApellido.setDisable(!activo);
        txtCorreo.setDisable(!activo);
        txtTelefono.setDisable(!activo);
        txtDireccion.setDisable(!activo);
        btnGuardar.setDisable(!activo);
        btnCancelar.setDisable(!activo);
        
        tablaUsuarios.setDisable(activo);
        btnBuscar.setDisable(activo);
        txtBuscar.setDisable(activo);
        
        btnVolver.setDisable(activo);
        btnAnterior.setDisable(activo);
        btnSiguiente.setDisable(activo);
        btnNuevo.setDisable(activo);
        btnEliminar.setDisable(activo);
        btnVolver.setDisable(activo);
    }
    
    @FXML
    private void btnAnteriorAction(){
        int indice = tablaUsuarios.getSelectionModel().getSelectedIndex();
        if (indice > 0) {
            tablaUsuarios.getSelectionModel().select(indice -1);
            cargarEnTextoField();
        }
    }
    
   @FXML
    private void btnSiguienteAction(){
        int indice = tablaUsuarios.getSelectionModel().getSelectedIndex();
        if (indice < listaUsuarios.size() -1) {
            tablaUsuarios.getSelectionModel().select(indice +1);
            cargarEnTextoField();
        }
    }
    
    @FXML
    private void btnAgregarAction(){
        limpiarFormulario();
        estadoActual = EstadoFormulario.AGREGAR;
        estadoFormulario(estadoActual);
    }
    
    @FXML
    private void btnEditarAction(){
        estadoActual = EstadoFormulario.EDITAR;
        estadoFormulario(estadoActual);
    }
    
    @FXML
    private void btnEliminarAction(){
        eliminarUsuario();
    }
    
    @FXML
    private void btnCancelarAction(){
        if (tablaUsuarios.getSelectionModel().getSelectedItem() != null) {
            cargarEnTextoField();
        }
        estadoActual = EstadoFormulario.NINGUNO;
        estadoFormulario(estadoActual);
    }
    
    @FXML
    private void btnGuardarAction(){
        if (estadoActual == EstadoFormulario.AGREGAR) {
            agregarUsuario();
            estadoActual = EstadoFormulario.NINGUNO;
        } else if(estadoActual == EstadoFormulario.EDITAR){
            actualizarUsuario();
            estadoActual = EstadoFormulario.NINGUNO;
        }
        estadoFormulario(estadoActual);
    }
    
    @FXML
    private void BuscarTabla(){
        ArrayList<Usuarios> resultadoBusqueda = new ArrayList<>();
        String id = txtBuscar.getText();
        for (Usuarios usu : listaUsuarios) {
            if (String.valueOf(usu.getIdUser()).toLowerCase().contains(id.toLowerCase())) {
                resultadoBusqueda.add(usu);
            }
        }
        tablaUsuarios.setItems(FXCollections.observableList(resultadoBusqueda));
        if (!resultadoBusqueda.isEmpty()) {
            tablaUsuarios.getSelectionModel().selectFirst();
        }
    }
}
