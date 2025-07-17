package org.josemoran.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.josemoran.database.Conexion;
import org.josemoran.model.*;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.josemoran.system.Main;

public class DetalleFacturasController {

    private Main principal;

    @FXML
    private TableView<DetalleFactura> tblDetalles;
    @FXML
    private TableColumn<DetalleFactura, Integer> colIdDetalle, colCantidad;
    @FXML
    private TableColumn<DetalleFactura, Double> colSubtotal;
    @FXML
    private TableColumn<DetalleFactura, String> colFactura, colCarro;

    @FXML
    private ComboBox<Factura> cbFactura;
    @FXML
    private ComboBox<Carros> cbCarro;
    @FXML
    private Spinner<Integer> spnCantidad;
    @FXML
    private TextField txtBuscar, txtsubtotal;

    @FXML
    private Button btnAgregar, btnEditar, btnEliminar, btnGuardar, btnCancelar;

    public void setPrincipal(Main principal) {
        this.principal = principal;
    }

    private enum Estado {
        NUEVO, EDITAR, NINGUNO
    }
    private Estado estadoActual = Estado.NINGUNO;

    private ObservableList<DetalleFactura> listaDetalles;
    private ObservableList<Factura> listaFacturas;
    private ObservableList<Carros> listaCarros;

    private DetalleFactura detalleSeleccionado;

    @FXML
    public void initialize() {
        cargarFacturas();
        cargarCarros();
        configurarTabla();
        cargarDetalles();

        spnCantidad.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));

        btnGuardar.setDisable(true);
        btnCancelar.setDisable(true);
        habilitarCampos(false);

        // Mostrar detalle al seleccionar en la tabla
        tblDetalles.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            detalleSeleccionado = newSel;
            mostrarDetalle(detalleSeleccionado);
        });

        cbCarro.setOnAction(e -> actualizarSubtotal());
        spnCantidad.valueProperty().addListener((obs, oldVal, newVal) -> actualizarSubtotal());
    }

    public void volver() {
        principal.MenuScene();
    }

    private void configurarTabla() {
        colIdDetalle.setCellValueFactory(new PropertyValueFactory<>("idDetalle"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        colFactura.setCellValueFactory(cellData
                -> javafx.beans.binding.Bindings.createStringBinding(()
                        -> obtenerNombreFactura(cellData.getValue().getIdFactura())));

        colCarro.setCellValueFactory(cellData
                -> javafx.beans.binding.Bindings.createStringBinding(()
                        -> obtenerNombreCarro(cellData.getValue().getIdCarro())));
    }

    private String obtenerNombreFactura(int id) {
        return listaFacturas.stream()
                .filter(f -> f.getIdFactura() == id)
                .map(f -> "Factura #" + f.getIdFactura())
                .findFirst().orElse("");
    }

    private String obtenerNombreCarro(int id) {
        return listaCarros.stream()
                .filter(c -> c.getIdCarro() == id)
                .map(c -> c.getMarca() + " " + c.getModelo())
                .findFirst().orElse("");
    }

    private void actualizarSubtotal() {
        Carros carro = cbCarro.getSelectionModel().getSelectedItem();
        Integer cantidad = spnCantidad.getValue();
        if (carro != null && cantidad != null) {
            double subtotal = carro.getPrecio() * cantidad;
            txtsubtotal.setText(String.format("%.2f", subtotal));
        } else {
            txtsubtotal.clear();
        }
    }

    private void cargarFacturas() {
        listaFacturas = FXCollections.observableArrayList();
        try (CallableStatement cs = Conexion.getInstancia().getConexion().prepareCall("call sp_ListarFacturas();")) {
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                listaFacturas.add(new Factura(rs.getInt("idFactura"), rs.getDate("fecha"), rs.getInt("idUser")));
            }
            cbFactura.setItems(listaFacturas);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cargarCarros() {
        listaCarros = FXCollections.observableArrayList();
        try (CallableStatement cs = Conexion.getInstancia().getConexion().prepareCall("call sp_ListarCarros();")) {
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                listaCarros.add(new Carros(
                        rs.getInt("idCarro"), rs.getString("marca"), rs.getString("modelo"),
                        rs.getString("color"), rs.getString("descripcion"),
                        rs.getInt("año"), rs.getDouble("precio"),
                        rs.getInt("idCategoria"), rs.getInt("idProveedor")));
            }
            cbCarro.setItems(listaCarros);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cargarDetalles() {
        listaDetalles = FXCollections.observableArrayList();
        try (CallableStatement cs = Conexion.getInstancia().getConexion().prepareCall("call sp_ListarDetalles();")) {
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                listaDetalles.add(new DetalleFactura(
                        rs.getInt("idDetalle"),
                        rs.getInt("idFactura"),
                        rs.getInt("idCarro"),
                        rs.getInt("cantidad"),
                        rs.getDouble("subtotal")));
            }
            tblDetalles.setItems(listaDetalles);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void habilitarCampos(boolean habilitar) {
        cbFactura.setDisable(!habilitar);
        cbCarro.setDisable(!habilitar);
        spnCantidad.setDisable(!habilitar);
    }

    private void limpiarCampos() {
        cbFactura.getSelectionModel().clearSelection();
        cbCarro.getSelectionModel().clearSelection();
        spnCantidad.getValueFactory().setValue(1);
        txtsubtotal.clear();
    }

    private void mostrarDetalle(DetalleFactura d) {
        if (d != null) {
            cbFactura.getSelectionModel().select(
                    listaFacturas.stream().filter(f -> f.getIdFactura() == d.getIdFactura()).findFirst().orElse(null)
            );
            cbCarro.getSelectionModel().select(
                    listaCarros.stream().filter(c -> c.getIdCarro() == d.getIdCarro()).findFirst().orElse(null)
            );
            spnCantidad.getValueFactory().setValue(d.getCantidad());
        } else {
            limpiarCampos();
        }
    }

    @FXML
    private void btnAnteriorAction() {
        int index = tblDetalles.getSelectionModel().getSelectedIndex();
        if (index > 0) {
            tblDetalles.getSelectionModel().select(index - 1);
        }
    }

    @FXML
    private void btnSiguienteAction() {
        int index = tblDetalles.getSelectionModel().getSelectedIndex();
        if (index < tblDetalles.getItems().size() - 1) {
            tblDetalles.getSelectionModel().select(index + 1);
        }
    }

    @FXML
    private void btnAgregarAction() {
        estadoActual = Estado.NUEVO;
        limpiarCampos();
        habilitarCampos(true);
        actualizarBotones(true);
    }

    @FXML
    private void btnEditarAction() {
        if (detalleSeleccionado != null) {
            estadoActual = Estado.EDITAR;
            habilitarCampos(true);
            actualizarBotones(true);
        }
    }

    @FXML
    private void btnEliminarAction() {
        if (detalleSeleccionado != null) {
            try (CallableStatement cs = Conexion.getInstancia().getConexion().prepareCall("call sp_EliminarDetalle(?);")) {
                cs.setInt(1, detalleSeleccionado.getIdDetalle());
                cs.execute();
                listaDetalles.remove(detalleSeleccionado);
                limpiarCampos();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void btnGuardarAction() {
        Factura factura = cbFactura.getSelectionModel().getSelectedItem();
        Carros carro = cbCarro.getSelectionModel().getSelectedItem();
        int cantidad = spnCantidad.getValue();

        if (factura == null || carro == null || cantidad <= 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Por favor, complete todos los campos requeridos (Factura, Carro, Cantidad).");
            alert.showAndWait();
            return;
        }

        double subtotal = carro.getPrecio() * cantidad;

        try {
            if (estadoActual == Estado.NUEVO) {
                CallableStatement cs = Conexion.getInstancia().getConexion().prepareCall("call sp_AgregarDetalleFactura(?, ?, ?, ?);");
                cs.setInt(1, factura.getIdFactura());
                cs.setInt(2, carro.getIdCarro());
                cs.setInt(3, cantidad);
                cs.setDouble(4, subtotal);
                cs.execute();
            } else if (estadoActual == Estado.EDITAR) {
                CallableStatement cs = Conexion.getInstancia().getConexion().prepareCall("call sp_ActualizarDetalle(?, ?, ?, ?, ?);");
                cs.setInt(1, detalleSeleccionado.getIdDetalle());
                cs.setInt(2, factura.getIdFactura());
                cs.setInt(3, carro.getIdCarro());
                cs.setInt(4, cantidad);
                cs.setDouble(5, subtotal);
                cs.execute();
            }
            cargarDetalles();
            cancelarOperacion();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnCancelarAction() {
        cancelarOperacion();
    }

    private void cancelarOperacion() {
        estadoActual = Estado.NINGUNO;
        limpiarCampos();
        habilitarCampos(false);
        actualizarBotones(false);
        detalleSeleccionado = null;
        tblDetalles.getSelectionModel().clearSelection();
    }

    private void actualizarBotones(boolean editando) {
        btnAgregar.setDisable(editando);
        btnEditar.setDisable(editando);
        btnEliminar.setDisable(editando);
        btnGuardar.setDisable(!editando);
        btnCancelar.setDisable(!editando);
        tblDetalles.setDisable(editando);
    }

    @FXML
    private void buscarAction() {
        String texto = txtBuscar.getText().trim().toLowerCase();
        if (texto.isEmpty()) {
            tblDetalles.setItems(listaDetalles);
            return;
        }
        ObservableList<DetalleFactura> filtrado = FXCollections.observableArrayList();
        for (DetalleFactura d : listaDetalles) {
            String nombreCarro = obtenerNombreCarro(d.getIdCarro()).toLowerCase();
            if (nombreCarro.contains(texto) || String.valueOf(d.getIdDetalle()).contains(texto)) {
                filtrado.add(d);
            }
        }
        tblDetalles.setItems(filtrado);
    }
}