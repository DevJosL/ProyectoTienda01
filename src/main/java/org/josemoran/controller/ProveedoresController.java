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

public class ProveedoresController implements Initializable {

    private Main principal;
    private ObservableList<Proveedores> listaProveedores;
    private Proveedores modeloProveedor;

    private enum EstadoFormulario {
        AGREGAR, EDITAR, ELIMINAR, NINGUNO
    };
    EstadoFormulario estadoActual = EstadoFormulario.NINGUNO;

    @FXML
    private TableView<Proveedores> tablaProveedores;

    @FXML
    private TableColumn colID, colNombre, colTelefono,
            colDireccion;

    @FXML
    private TextField txtBuscar, txtId, txtNombre,
            txtTelefono, txtDireccion;

    @FXML
    private Button btnAnterior, btnSiguiente, btnNuevo,
            btnEliminar, btnEditar, btnCancelar,
            btnGuardar, btnVolver, btnBuscar;

    public void setPrincipal(Main principal) {
        this.principal = principal;
    }

    public void Volver() {
        principal.MenuScene();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumna();
        cargarTabla();
        tablaProveedores.setOnMouseClicked(eventHandler -> cargarEnTextoField());
        txtBuscar.setOnAction(eh -> BuscarTabla());
    }

    public void configurarColumna() {
        colID.setCellValueFactory(new PropertyValueFactory<Usuarios, Integer>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Usuarios, String>("nombre"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Usuarios, String>("telefono"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Usuarios, String>("direccion"));
    }

    public void cargarTabla() {
        listaProveedores = FXCollections.observableList(ListarTabla());
        tablaProveedores.setItems(listaProveedores);
        tablaProveedores.getSelectionModel().selectFirst();
        if (tablaProveedores.getSelectionModel().getSelectedItem() != null) {
            cargarEnTextoField();
        }
    }

    public void cargarEnTextoField() {
        Proveedores proveedorSeleccionado = tablaProveedores.getSelectionModel().getSelectedItem();
        txtId.setText(String.valueOf(proveedorSeleccionado.getId()));
        txtNombre.setText(proveedorSeleccionado.getNombre());
    }

    public ArrayList<Proveedores> ListarTabla() {
        ArrayList<Proveedores> proveedor = new ArrayList<>();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_ListarProveedores();");
            ResultSet resultado = enunciado.executeQuery();

            while (resultado.next()) {
                proveedor.add(new Proveedores(resultado.getInt(1),
                        resultado.getString(2),
                        resultado.getString(3),
                        resultado.getString(4)));
            }
        } catch (SQLException ex) {
            System.out.println("Error al cargar de MySQL la entidad" + ex.getMessage());
            ex.printStackTrace();
        }
        return proveedor;
    }

    public Proveedores obtenerModeloProveedores() {
        int id;
        if (txtId.getText().isEmpty()) {
            id = 1;
        } else {
            id = Integer.parseInt(txtId.getText());
        }
        String nombre = txtNombre.getText();
        String telefono = txtTelefono.getText();
        String direccion = txtDireccion.getText();
        Proveedores proveedor = new Proveedores(id, nombre, telefono, direccion);
        return proveedor;
    }

    public void agregarProveedor() {
        modeloProveedor = obtenerModeloProveedores();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_AgregarProveedor(?,?,?);");
            enunciado.setString(1, modeloProveedor.getNombre());
            enunciado.setString(2, modeloProveedor.getTelefono());
            enunciado.setString(3, modeloProveedor.getDireccion());
            enunciado.executeQuery();
            cargarTabla();
        } catch (SQLException e) {
            System.out.println("Error al agregar " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void actualizarProveedor() {
        modeloProveedor = obtenerModeloProveedores();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_ActualizarProveedor(?,?,?,?);");
            enunciado.setInt(1, modeloProveedor.getId());
            enunciado.setString(2, modeloProveedor.getNombre());
            enunciado.setString(3, modeloProveedor.getTelefono());
            enunciado.setString(4, modeloProveedor.getDireccion());
            enunciado.executeQuery();
            cargarTabla();
        } catch (Exception e) {
            System.out.println("Error al actualizar" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void eliminarProveedor() {
        modeloProveedor = obtenerModeloProveedores();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_EliminarProveedor(?);");
            enunciado.setInt(1, modeloProveedor.getId());
            enunciado.executeQuery();
            cargarTabla();
        } catch (Exception e) {
            System.out.println("Error al eliminar " + e.getMessage());
        }
    }

    public void limpiarFormulario() {
        txtId.clear();
        txtNombre.clear();
        txtTelefono.clear();
        txtDireccion.clear();
    }

    public void estadoFormulario(EstadoFormulario est) {
        estadoActual = est;
        boolean activo = (est == EstadoFormulario.AGREGAR || est == EstadoFormulario.EDITAR);

        txtNombre.setDisable(!activo);
        txtTelefono.setDisable(!activo);
        txtDireccion.setDisable(!activo);
        btnGuardar.setVisible(activo);
        btnCancelar.setVisible(activo);

        tablaProveedores.setDisable(activo);
        btnBuscar.setDisable(activo);
        txtBuscar.setDisable(activo);

        btnVolver.setDisable(activo);
        btnEditar.setDisable(activo);
        btnAnterior.setDisable(activo);
        btnSiguiente.setDisable(activo);
        btnNuevo.setDisable(activo);
        btnEliminar.setDisable(activo);
        btnVolver.setDisable(activo);
    }

    @FXML
    private void btnAnteriorAction() {
        int indice = tablaProveedores.getSelectionModel().getSelectedIndex();
        if (indice > 0) {
            tablaProveedores.getSelectionModel().select(indice - 1);
            cargarEnTextoField();
        }
    }

    @FXML
    private void btnSiguienteAction() {
        int indice = tablaProveedores.getSelectionModel().getSelectedIndex();
        if (indice < listaProveedores.size() - 1) {
            tablaProveedores.getSelectionModel().select(indice + 1);
            cargarEnTextoField();
        }
    }

    @FXML
    private void btnAgregarAction() {
        limpiarFormulario();
        estadoActual = EstadoFormulario.AGREGAR;
        estadoFormulario(estadoActual);
    }

    @FXML
    private void btnEditarAction() {
        estadoActual = EstadoFormulario.EDITAR;
        estadoFormulario(estadoActual);
    }

    @FXML
    private void btnEliminarAction() {
        eliminarProveedor();
    }

    @FXML
    private void btnCancelarAction() {
        if (tablaProveedores.getSelectionModel().getSelectedItem() != null) {
            cargarEnTextoField();
        }
        estadoActual = EstadoFormulario.NINGUNO;
        estadoFormulario(estadoActual);
    }

    @FXML
    private void btnGuardarAction() {
        if (estadoActual == EstadoFormulario.AGREGAR) {
            agregarProveedor();
            estadoActual = EstadoFormulario.NINGUNO;
        } else if (estadoActual == EstadoFormulario.EDITAR) {
            actualizarProveedor();
            estadoActual = EstadoFormulario.NINGUNO;
        }
        estadoFormulario(estadoActual);
    }

    @FXML
    private void BuscarTabla() {
        ArrayList<Proveedores> resultadoBusqueda = new ArrayList<>();
        String nombre = txtBuscar.getText();
        String id = txtBuscar.getText();
        for (Proveedores pro : listaProveedores) {
            if (String.valueOf(pro.getNombre()).toLowerCase().contains(nombre.toLowerCase())
                    || String.valueOf(pro.getId()).toLowerCase().contains(id.toLowerCase())) {
                resultadoBusqueda.add(pro);
            }
        }
        tablaProveedores.setItems(FXCollections.observableList(resultadoBusqueda));
        if (!resultadoBusqueda.isEmpty()) {
            tablaProveedores.getSelectionModel().selectFirst();
        }
    }
}
