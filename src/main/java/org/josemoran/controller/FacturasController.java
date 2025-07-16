package org.josemoran.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent; // Se puede eliminar si no se utiliza en ningún método
import org.josemoran.model.Factura;
import org.josemoran.model.Usuarios;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.Date; 
import javafx.scene.control.cell.PropertyValueFactory;
import org.josemoran.database.Conexion;
import org.josemoran.system.Main;

public class FacturasController {

    private Main principal;

    @FXML private TableView<Factura> tblFacturas;
    @FXML private TableColumn<Factura, Integer> colIdFactura;
    @FXML private TableColumn<Factura, LocalDate> colFecha;
    @FXML private TableColumn<Factura, String> colUsuario;
    // @FXML private TableColumn<Factura, String> colDireccion; // ¡ELIMINA ESTA LÍNEA!

    @FXML private DatePicker dpFecha;
    @FXML private ComboBox<Usuarios> cbUsuarios;
    @FXML private TextField txtBuscar, txtIdFactura;
    // @FXML private TextField txtDireccion; // ¡ELIMINA ESTA LÍNEA!
    @FXML private Button btnAgregar, btnEditar, btnEliminar, btnGuardar, btnCancelar, btnAnterior, btnSiguiente;

    private enum Estado { NUEVO, EDITAR, NINGUNO }
    private Estado estadoActual = Estado.NINGUNO;

    private ObservableList<Factura> listaFacturas;
    private ObservableList<Usuarios> listaUsuarios; 
    private Factura facturaSeleccionada;

    public void setPrincipal(Main principal) {
        this.principal = principal;
    }

    @FXML
    public void initialize() {
        cargarUsuarios();
        configurarTabla();
        cargarFacturas();

        btnGuardar.setDisable(true);
        btnCancelar.setDisable(true);
        habilitarCampos(false);

        tblFacturas.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            facturaSeleccionada = newSel;
            mostrarFactura(facturaSeleccionada);
        });

        // ¡ELIMINA TODO ESTE BLOQUE DE CÓDIGO RELACIONADO CON txtDireccion!
        /*
        cbUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtDireccion.setText(newVal.getDireccion());
            } else {
                txtDireccion.clear();
            }
        });
        */
    }

    public void volver() {
        principal.MenuScene();
    }

    private void configurarTabla() {
        colIdFactura.setCellValueFactory(new PropertyValueFactory<>("idFactura"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        // ¡ELIMINA ESTA LÍNEA! colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colUsuario.setCellValueFactory(cellData -> {
            int idUser = cellData.getValue().getIdUser();
            return new javafx.beans.property.SimpleStringProperty(obtenerNombreUsuario(idUser));
        });
    }

    private String obtenerNombreUsuario(int idUser) {
        for (Usuarios u : listaUsuarios) {
            if (u.getIdUser() == idUser) {
                return u.getNombre() + " " + u.getApellido();
            }
        }
        return "";
    }

    private void cargarUsuarios() {
        listaUsuarios = FXCollections.observableArrayList();
        try {
            CallableStatement cs = Conexion.getInstancia().getConexion().prepareCall("call sp_ListarUsuarios();");
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                // AQUÍ USAMOS EL CONSTRUCTOR DE 6 PARÁMETROS QUE ACABAMOS DE CREAR EN Usuarios.java
                listaUsuarios.add(new Usuarios(
                    rs.getInt("idUser"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("correo"),
                    rs.getString("contraseña"),
                    rs.getString("telefono")
                ));
            }
            cbUsuarios.setItems(listaUsuarios);
        } catch (SQLException e) {
            System.err.println("Error al cargar usuarios: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void cargarFacturas() {
        listaFacturas = FXCollections.observableArrayList();
        try {
            CallableStatement cs = Conexion.getInstancia().getConexion().prepareCall("call sp_ListarFacturas();");
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                // Si la clase Factura no tiene propiedad 'direccion' en la BD ni en el modelo,
                // asegúrate de que el constructor de Factura aquí SÓLO tenga 3 parámetros
                listaFacturas.add(new Factura(
                    rs.getInt("idFactura"),
                    rs.getDate("fecha"),
                    rs.getInt("idUser")
                    // ¡ELIMINA ESTA LÍNEA si Factura no tiene direccion! , rs.getString("direccion")
                ));
            }
            tblFacturas.setItems(listaFacturas);
        } catch (SQLException e) {
            System.err.println("Error al cargar facturas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void habilitarCampos(boolean habilitar) {
        dpFecha.setDisable(!habilitar);
        cbUsuarios.setDisable(!habilitar);
        txtIdFactura.setDisable(true);
        // ¡ELIMINA ESTA LÍNEA! txtDireccion.setDisable(true);
    }

    private void limpiarCampos() {
        txtIdFactura.clear();
        dpFecha.setValue(null);
        cbUsuarios.getSelectionModel().clearSelection();
        // ¡ELIMINA ESTA LÍNEA! txtDireccion.clear();
    }

    private void mostrarFactura(Factura f) {
        if (f != null) {
            txtIdFactura.setText(String.valueOf(f.getIdFactura()));
            dpFecha.setValue(f.getFecha().toLocalDate());
            for (Usuarios u : listaUsuarios) {
                if (u.getIdUser() == f.getIdUser()) {
                    cbUsuarios.getSelectionModel().select(u);
                    // ¡ELIMINA ESTA LÍNEA! txtDireccion.setText(u.getDireccion());
                    break;
                }
            }
        } else {
            limpiarCampos();
        }
    }

    @FXML
    private void btnAnteriorAction() {
        int index = tblFacturas.getSelectionModel().getSelectedIndex();
        if (index > 0) {
            tblFacturas.getSelectionModel().select(index - 1);
        }
    }

    @FXML
    private void btnSiguienteAction() {
        int index = tblFacturas.getSelectionModel().getSelectedIndex();
        if (index < tblFacturas.getItems().size() - 1) {
            tblFacturas.getSelectionModel().select(index + 1);
        }
    }

    @FXML
    private void btnAgregarAction() {
        estadoActual = Estado.NUEVO;
        limpiarCampos();
        habilitarCampos(true);
        btnAgregar.setDisable(true);
        btnEditar.setDisable(true);
        btnEliminar.setDisable(true);
        btnGuardar.setDisable(false);
        btnCancelar.setDisable(false);
        tblFacturas.setDisable(true);
    }

    @FXML
    private void btnEditarAction() {
        if (facturaSeleccionada == null) return;
        estadoActual = Estado.EDITAR;
        habilitarCampos(true);
        btnAgregar.setDisable(true);
        btnEditar.setDisable(true);
        btnEliminar.setDisable(true);
        btnGuardar.setDisable(false);
        btnCancelar.setDisable(false);
        tblFacturas.setDisable(true);
    }

    @FXML
    private void btnEliminarAction() {
        if (facturaSeleccionada == null) return;
        try {
            CallableStatement cs = Conexion.getInstancia().getConexion().prepareCall("call sp_EliminarFactura(?);");
            cs.setInt(1, facturaSeleccionada.getIdFactura());
            cs.execute();
            listaFacturas.remove(facturaSeleccionada);
            limpiarCampos();
        } catch (SQLException e) {
            System.err.println("Error al eliminar factura: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void btnGuardarAction() {
        LocalDate fecha = dpFecha.getValue();
        Usuarios usuario = cbUsuarios.getSelectionModel().getSelectedItem();
        if (fecha == null || usuario == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Por favor, complete la fecha y seleccione un usuario.");
            alert.showAndWait();
            return;
        }

        try {
            if (estadoActual == Estado.NUEVO) {
                CallableStatement cs = Conexion.getInstancia().getConexion().prepareCall("call sp_AgregarFactura(?, ?);");
                cs.setDate(1, java.sql.Date.valueOf(fecha));
                cs.setInt(2, usuario.getIdUser());
                cs.execute();
            } else if (estadoActual == Estado.EDITAR) {
                CallableStatement cs = Conexion.getInstancia().getConexion().prepareCall("call sp_ActualizarFactura(?, ?, ?);");
                cs.setInt(1, facturaSeleccionada.getIdFactura());
                cs.setDate(2, java.sql.Date.valueOf(fecha));
                cs.setInt(3, usuario.getIdUser());
                cs.execute();
            }
            cargarFacturas();
            cancelarOperacion();
        } catch (SQLException e) {
            System.err.println("Error al guardar factura: " + e.getMessage());
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
        btnAgregar.setDisable(false);
        btnEditar.setDisable(false);
        btnEliminar.setDisable(false);
        btnGuardar.setDisable(true);
        btnCancelar.setDisable(true);
        tblFacturas.setDisable(false);
        facturaSeleccionada = null;
        tblFacturas.getSelectionModel().clearSelection();
    }

    @FXML
    private void buscarAction() {
        String texto = txtBuscar.getText().trim().toLowerCase();
        if (texto.isEmpty()) {
            tblFacturas.setItems(listaFacturas);
            return;
        }
        ObservableList<Factura> filtrado = FXCollections.observableArrayList();
        for (Factura f : listaFacturas) {
            String nombreUsuario = obtenerNombreUsuario(f.getIdUser()).toLowerCase();
            if (nombreUsuario.contains(texto) || String.valueOf(f.getIdFactura()).contains(texto)) {
                filtrado.add(f);
            }
        }
        tblFacturas.setItems(filtrado);
    }
}