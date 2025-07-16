package org.josemoran.controller;

import org.josemoran.system.Main;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.josemoran.database.Conexion;
import org.josemoran.model.Carros;
import org.josemoran.model.Categorias;
import org.josemoran.model.Proveedores;

public class CarrosController implements Initializable {
    private Main principal;
    private ObservableList<Carros> listaCarros;
    private Carros modeloCarro;

    private enum EstadoFormulario {
        AGREGAR, EDITAR, ELIMINAR, NINGUNO
    };
    EstadoFormulario estadoActual = EstadoFormulario.NINGUNO;

    @FXML
    private TableView<Carros> tablaCarros;

    @FXML
    private TableColumn colID, colMarca, colModelo,
            colColor, colDescripcion, colAño,
            colPrecio, colCategoria, colProveedor;

    @FXML
    private TextField txtBuscar, txtId, txtColor, txtDescripcion,
            txtAño, txtPrecio, txtMarca, txtModelo;

    @FXML
    private ComboBox cbxCategoria, cbxProveedor;

    @FXML
    private Button btnAnterior, btnSiguiente, btnNuevo,
            btnEliminar, btnEditar, btnCancelar,
            btnGuardar, btnVolver, btnBuscar;

    public void setPrincipal(Main principal) {
        this.principal = principal;
    }

    public void volver() {
        principal.MenuScene();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumna();
        cargarTabla();
        tablaCarros.setOnMouseClicked(event -> cargarEnTextoField());
        txtBuscar.setOnAction(eh -> BuscarTabla());

        cargarCategoriasComboBox();
        cargarProveedoresComboBox();
    }


    public void configurarColumna() {
        colID.setCellValueFactory(new PropertyValueFactory<Carros, Integer>("idCarro"));
        colMarca.setCellValueFactory(new PropertyValueFactory<Carros, String>("marca"));
        colModelo.setCellValueFactory(new PropertyValueFactory<Carros, String>("modelo"));
        colColor.setCellValueFactory(new PropertyValueFactory<Carros, String>("color"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Carros, String>("descripcion"));
        colAño.setCellValueFactory(new PropertyValueFactory<Carros, Integer>("año"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<Carros, Double>("precio"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<Carros, Integer>("idCategoria"));
        colProveedor.setCellValueFactory(new PropertyValueFactory<Carros, Integer>("idProveedor"));
    }

    public void cargarTabla() {
        listaCarros = FXCollections.observableList(ListarTabla());
        tablaCarros.setItems(listaCarros);
        tablaCarros.getSelectionModel().selectFirst();
        if (tablaCarros.getSelectionModel().getSelectedItem() != null) {
            cargarEnTextoField();
        }
    }

    public void cargarEnTextoField() {
        Carros carroSeleccionado = tablaCarros.getSelectionModel().getSelectedItem();
        txtId.setText(String.valueOf(carroSeleccionado.getIdCarro()));
        txtColor.setText(carroSeleccionado.getColor());
        txtDescripcion.setText(carroSeleccionado.getDescripcion());
        txtAño.setText(String.valueOf(carroSeleccionado.getAño()));
        txtPrecio.setText(String.valueOf(carroSeleccionado.getPrecio()));
        txtMarca.setText(String.valueOf(carroSeleccionado.getMarca()));
        txtModelo.setText(String.valueOf(carroSeleccionado.getModelo()));
    }
    
    private ArrayList<Categorias> cargarModeloCategorias() {
        ArrayList<Categorias> categorias = new ArrayList<>();
        try {
            CallableStatement cs = Conexion.getInstancia().getConexion()
                    .prepareCall("CALL sp_ListarCategorias();");
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                categorias.add(new Categorias(
                        rs.getInt("idCategoria"),
                        rs.getString("nombreCategoria")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categorias;
    }

    private void cargarCategoriasComboBox() {
        ObservableList<Categorias> listaCategorias = FXCollections.observableArrayList(cargarModeloCategorias());
        cbxCategoria.setItems(listaCategorias);
    }

    private ArrayList<Proveedores> cargarModeloProveedores() {
        ArrayList<Proveedores> proveedores = new ArrayList<>();
        try {
            CallableStatement cs = Conexion.getInstancia().getConexion()
                    .prepareCall("CALL sp_ListarProveedores();");
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                proveedores.add(new Proveedores(
                        rs.getInt("idProveedor"),
                        rs.getString("nombreProveedor")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return proveedores;
    }

    private void cargarProveedoresComboBox() {
        ObservableList<Proveedores> listaProveedores = FXCollections.observableArrayList(cargarModeloProveedores());
        cbxProveedor.setItems(listaProveedores);
    }


    public ArrayList<Carros> ListarTabla() {
        ArrayList<Carros> carros = new ArrayList<>();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_ListarCarros();");
            ResultSet resultado = enunciado.executeQuery();

            while (resultado.next()) {
                carros.add(new Carros(
                        resultado.getInt("idCarro"),
                        resultado.getString("marca"),
                        resultado.getString("modelo"),
                        resultado.getString("color"),
                        resultado.getString("descripcion"),
                        resultado.getInt("año"),
                        resultado.getDouble("precio"),
                        resultado.getInt("idCategoria"),
                        resultado.getInt("idProveedor")));
            }
        } catch (SQLException ex) {
            System.out.println("Error al cargar de MySQL la entidad Carros: " + ex.getMessage());
            ex.printStackTrace();
        }
        return carros;
    }

    public Carros obtenerModeloCarros() {
        int idCarro = txtId.getText().isEmpty() ? 0 : Integer.parseInt(txtId.getText());
        String marca = txtMarca.getText();
        String modelo = txtModelo.getText();
        String color = txtColor.getText();
        String descripcion = txtDescripcion.getText();
        int año = Integer.parseInt(txtAño.getText());
        double precio = Double.parseDouble(txtPrecio.getText());

        Categorias categoriaSeleccionada = (Categorias) cbxCategoria.getValue();
        Proveedores proveedorSeleccionado = (Proveedores) cbxProveedor.getValue();

        int idCategoria = categoriaSeleccionada.getId();
        int idProveedor = proveedorSeleccionado.getId();

        return new Carros(idCarro, marca, modelo, color, descripcion, año, precio, idCategoria, idProveedor);
    }


    public void agregarCarro() {
        modeloCarro = obtenerModeloCarros();
        System.out.println("Agregando carro: " + modeloCarro);
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_AgregarCarro(?,?,?,?,?,?,?,?);");
            enunciado.setString(1, modeloCarro.getMarca());
            enunciado.setString(2, modeloCarro.getModelo());
            enunciado.setString(3, modeloCarro.getColor());
            enunciado.setInt(4, modeloCarro.getAño());
            enunciado.setDouble(5, modeloCarro.getPrecio());
            enunciado.setString(6, modeloCarro.getDescripcion());
            enunciado.setInt(7, modeloCarro.getIdCategoria());
            enunciado.setInt(8, modeloCarro.getIdProveedor());
            enunciado.executeQuery();
            cargarTabla();
        } catch (SQLException e) {
            System.out.println("Error al agregar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void actualizarCarro() {
        modeloCarro = obtenerModeloCarros();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_ActualizarCarro(?,?,?,?,?,?,?,?,?);");
            enunciado.setInt(1, modeloCarro.getIdCarro());
            enunciado.setString(2, modeloCarro.getMarca());
            enunciado.setString(3, modeloCarro.getModelo());
            enunciado.setString(4, modeloCarro.getColor());
            enunciado.setString(5, modeloCarro.getDescripcion());
            enunciado.setInt(6, modeloCarro.getAño());
            enunciado.setDouble(7, modeloCarro.getPrecio());
            enunciado.setInt(8, modeloCarro.getIdCategoria());
            enunciado.setInt(9, modeloCarro.getIdProveedor());
            enunciado.executeQuery();
            cargarTabla();
        } catch (Exception e) {
            System.out.println("Error al actualizar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void eliminarCarro() {
        modeloCarro = obtenerModeloCarros();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_EliminarCarro(?);");
            enunciado.setInt(1, modeloCarro.getIdCarro());
            enunciado.executeQuery();
            cargarTabla();
        } catch (Exception e) {
            System.out.println("Error al eliminar: " + e.getMessage());
        }
    }

    public void limpiarFormulario() {
        txtId.clear();
        txtColor.clear();
        txtDescripcion.clear();
        txtAño.clear();
        txtPrecio.clear();
        txtMarca.clear();
        txtModelo.clear();
        cbxCategoria.setValue(null);
        cbxProveedor.setValue(null);
    }

    public void estadoFormulario(EstadoFormulario est) {
        estadoActual = est;
        boolean activo = (est == EstadoFormulario.AGREGAR || est == EstadoFormulario.EDITAR);

        txtMarca.setDisable(!activo);
        txtModelo.setDisable(!activo);
        txtColor.setDisable(!activo);
        txtDescripcion.setDisable(!activo);
        txtAño.setDisable(!activo);
        txtPrecio.setDisable(!activo);
        cbxCategoria.setDisable(!activo);
        cbxProveedor.setDisable(!activo);
        btnGuardar.setDisable(!activo);
        btnCancelar.setDisable(!activo);

        tablaCarros.setDisable(activo);
        btnBuscar.setDisable(activo);
        txtBuscar.setDisable(activo);

        btnVolver.setDisable(activo);
        btnEditar.setDisable(activo);
        btnAnterior.setDisable(activo);
        btnSiguiente.setDisable(activo);
        btnNuevo.setDisable(activo);
        btnEliminar.setDisable(activo);
    }

    @FXML
    private void btnAnteriorAction() {
        int indice = tablaCarros.getSelectionModel().getSelectedIndex();
        if (indice > 0) {
            tablaCarros.getSelectionModel().select(indice - 1);
            cargarEnTextoField();
        }
    }

    @FXML
    private void btnSiguienteAction() {
        int indice = tablaCarros.getSelectionModel().getSelectedIndex();
        if (indice < listaCarros.size() - 1) {
            tablaCarros.getSelectionModel().select(indice + 1);
            cargarEnTextoField();
        }
    }

    @FXML
    private void btnNuevoAction() {
        limpiarFormulario();
        estadoActual = EstadoFormulario.AGREGAR;  // <- Esto es clave
        estadoFormulario(EstadoFormulario.AGREGAR);
    }

    @FXML
    private void btnEditarAction() {
        estadoFormulario(EstadoFormulario.EDITAR);
    }

    @FXML
    private void btnEliminarAction() {
        eliminarCarro();
        cargarTabla();
    }

    @FXML
    private void btnCancelarAction() {
        if (tablaCarros.getSelectionModel().getSelectedItem() != null) {
            cargarEnTextoField();
        }
        estadoFormulario(EstadoFormulario.NINGUNO);
    }

    @FXML
    private void btnGuardarAction() {
        System.out.println("Guardar botón presionado. Estado actual: " + estadoActual);
        if (estadoActual == EstadoFormulario.AGREGAR) {
            agregarCarro();
        } else if (estadoActual == EstadoFormulario.EDITAR) {
            actualizarCarro();
        }
        estadoFormulario(EstadoFormulario.NINGUNO);
    }

    
    @FXML
    private void BuscarTabla() {
        String texto = txtBuscar.getText().toLowerCase();
        ArrayList<Carros> resultadoBusqueda = new ArrayList<>();
        for (Carros carro : listaCarros) {
            if (String.valueOf(carro.getIdCarro()).contains(texto) ||
                carro.getMarca().toLowerCase().contains(texto) ||
                carro.getModelo().toLowerCase().contains(texto) ||
                carro.getColor().toLowerCase().contains(texto) ||
                carro.getDescripcion().toLowerCase().contains(texto)) {
                resultadoBusqueda.add(carro);
            }
        }
        tablaCarros.setItems(FXCollections.observableList(resultadoBusqueda));
        if (!resultadoBusqueda.isEmpty()) {
            tablaCarros.getSelectionModel().selectFirst();
        }
    }
} 
