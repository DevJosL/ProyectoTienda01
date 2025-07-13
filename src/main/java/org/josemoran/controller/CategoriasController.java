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

/**
 * FXML Controller class
 *
 * @author josel
 */
public class CategoriasController implements Initializable {

    private Main principal;
    private ObservableList<Categorias> listaCategorias;
    private Categorias modeloCategoria;

    private enum EstadoFormulario {
        AGREGAR, EDITAR, ELIMINAR, NINGUNO
    };
    EstadoFormulario estadoActual = EstadoFormulario.NINGUNO;

    @FXML
    private TableView<Categorias> tablaCategorias;

    @FXML
    private TableColumn colID, colNombre;

    @FXML
    private TextField txtBuscar, txtId, txtNombre;

    @FXML
    private Button btnAnterior, btnSiguiente, btnNuevo,
            btnEliminar, btnEditar, btnCancelar,
            btnGuardar, btnVolver, btnBuscar;

    public void setPrincipal(Main principal) {
        this.principal = principal;
    }
    
    public void Volver(){
        principal.MenuScene();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumna();
        cargarTabla();
        tablaCategorias.setOnMouseClicked(eventHandler -> cargarEnTextoField());
        txtBuscar.setOnAction(eh -> BuscarTabla());
    }

    public void configurarColumna() {
        colID.setCellValueFactory(new PropertyValueFactory<Usuarios, Integer>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Usuarios, String>("nombre"));
    }

    public void cargarTabla() {
        listaCategorias = FXCollections.observableList(ListarTabla());
        tablaCategorias.setItems(listaCategorias);
        tablaCategorias.getSelectionModel().selectFirst();
        if (tablaCategorias.getSelectionModel().getSelectedItem() != null) {
            cargarEnTextoField();
        }
    }

    public void cargarEnTextoField() {
        Categorias categoriaSeleccionado = tablaCategorias.getSelectionModel().getSelectedItem();
        txtId.setText(String.valueOf(categoriaSeleccionado.getId()));
        txtNombre.setText(categoriaSeleccionado.getNombre());
    }

    public ArrayList<Categorias> ListarTabla() {
        ArrayList<Categorias> categoria = new ArrayList<>();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_ListarCategorias();");
            ResultSet resultado = enunciado.executeQuery();

            while (resultado.next()) {
                categoria.add(new Categorias(resultado.getInt(1),
                        resultado.getString(2)));
            }
        } catch (SQLException ex) {
            System.out.println("Error al cargar de MySQL la entidad" + ex.getMessage());
            ex.printStackTrace();
        }
        return categoria;
    }

    public Categorias obtenerModeloCategorias() {
        int id;
        if (txtId.getText().isEmpty()) {
            id = 1;
        } else {
            id = Integer.parseInt(txtId.getText());
        }
        String nombre = txtNombre.getText();
        Categorias categoria = new Categorias(id, nombre);
        return categoria;
    }

    public void agregarCategoria() {
        modeloCategoria = obtenerModeloCategorias();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_AgregarCategoria(?);");
            enunciado.setString(1, modeloCategoria.getNombre());
            enunciado.executeQuery();
            cargarTabla();
        } catch (SQLException e) {
            System.out.println("Error al agregar " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void actualizarCategoria() {
        modeloCategoria = obtenerModeloCategorias();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_ActualizarCategoria(?,?);");
            enunciado.setInt(1, modeloCategoria.getId());
            enunciado.setString(2, modeloCategoria.getNombre());
            enunciado.executeQuery();
            cargarTabla();
        } catch (Exception e) {
            System.out.println("Error al actualizar" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void eliminarCategoria() {
        modeloCategoria = obtenerModeloCategorias();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_EliminarCategoria(?);");
            enunciado.setInt(1, modeloCategoria.getId());
            enunciado.executeQuery();
            cargarTabla();
        } catch (Exception e) {
            System.out.println("Error al eliminar " + e.getMessage());
        }
    }

    public void limpiarFormulario() {
        txtId.clear();
        txtNombre.clear();
    }

    public void estadoFormulario(EstadoFormulario est) {
        estadoActual = est;
        boolean activo = (est == EstadoFormulario.AGREGAR || est == EstadoFormulario.EDITAR);

        txtNombre.setDisable(!activo);
        btnGuardar.setVisible(activo);
        btnCancelar.setVisible(activo);

        tablaCategorias.setDisable(activo);
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
        int indice = tablaCategorias.getSelectionModel().getSelectedIndex();
        if (indice > 0) {
            tablaCategorias.getSelectionModel().select(indice - 1);
            cargarEnTextoField();
        }
    }

    @FXML
    private void btnSiguienteAction() {
        int indice = tablaCategorias.getSelectionModel().getSelectedIndex();
        if (indice < listaCategorias.size() - 1) {
            tablaCategorias.getSelectionModel().select(indice + 1);
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
        eliminarCategoria();
    }

    @FXML
    private void btnCancelarAction() {
        if (tablaCategorias.getSelectionModel().getSelectedItem() != null) {
            cargarEnTextoField();
        }
        estadoActual = EstadoFormulario.NINGUNO;
        estadoFormulario(estadoActual);
    }

    @FXML
    private void btnGuardarAction() {
        if (estadoActual == EstadoFormulario.AGREGAR) {
            agregarCategoria();
            estadoActual = EstadoFormulario.NINGUNO;
        } else if (estadoActual == EstadoFormulario.EDITAR) {
            actualizarCategoria();
            estadoActual = EstadoFormulario.NINGUNO;
        }
        estadoFormulario(estadoActual);
    }

    @FXML
    private void BuscarTabla() {
        ArrayList<Categorias> resultadoBusqueda = new ArrayList<>();
        String nombre = txtBuscar.getText();
        String id = txtBuscar.getText();
        for (Categorias cat : listaCategorias) {
            if (String.valueOf(cat.getNombre()).toLowerCase().contains(nombre.toLowerCase())
                    || String.valueOf(cat.getId()).toLowerCase().contains(id.toLowerCase())) {
                resultadoBusqueda.add(cat);
            }
        }
        tablaCategorias.setItems(FXCollections.observableList(resultadoBusqueda));
        if (!resultadoBusqueda.isEmpty()) {
            tablaCategorias.getSelectionModel().selectFirst();
        }
    }
}
