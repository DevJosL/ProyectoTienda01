package org.josemoran.controller;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.josemoran.database.Conexion;
import org.josemoran.model.*;
import org.josemoran.system.Main;

/**
 * FXML Controller class
 *
 * @author josel
 */
public class VenderProductoController implements Initializable {

    private Main principal;
    private ObservableList<Carros> listaCarros;
    private Carros modeloCarro;
    private int idFactura = 0;

    @FXML
    private TableView<Carros> tablaCarros;

    @FXML
    private ComboBox cbxUsu;

    @FXML
    private TableColumn colID, colMarca, colModelo,
            colColor, colDescripcion, colAño,
            colPrecio, colCategoria, colProveedor;

    @FXML
    private Button btnAnterior, btnSiguiente, btnNuevo,
            btnEliminar, btnEditar, btnCancelar,
            btnGuardar, btnBuscar;

    @FXML
    private TextField txtBuscar, txtCantidad;

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
        txtBuscar.setOnAction(eh -> BuscarTabla());
        CargarCbx();
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

    @FXML
    private void btnAnteriorAction() {
        int indice = tablaCarros.getSelectionModel().getSelectedIndex();
        if (indice > 0) {
            tablaCarros.getSelectionModel().select(indice - 1);
        }
    }

    @FXML
    private void btnSiguienteAction() {
        int indice = tablaCarros.getSelectionModel().getSelectedIndex();
        if (indice < listaCarros.size() - 1) {
            tablaCarros.getSelectionModel().select(indice + 1);
        }
    }

    @FXML
    private void BuscarTabla() {
        String texto = txtBuscar.getText().toLowerCase();
        ArrayList<Carros> resultadoBusqueda = new ArrayList<>();
        for (Carros carro : listaCarros) {
            if (String.valueOf(carro.getIdCarro()).contains(texto)
                    || carro.getMarca().toLowerCase().contains(texto)
                    || carro.getModelo().toLowerCase().contains(texto)
                    || carro.getColor().toLowerCase().contains(texto)
                    || carro.getDescripcion().toLowerCase().contains(texto)) {
                resultadoBusqueda.add(carro);
            }
        }
        tablaCarros.setItems(FXCollections.observableList(resultadoBusqueda));
        if (!resultadoBusqueda.isEmpty()) {
            tablaCarros.getSelectionModel().selectFirst();
        }
    }

    
    //Se carga la entidad "Usuarios" para posteriormente cargarse en un combobox
    public ArrayList<Usuarios> ListarUsuario() {
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

    public void CargarCbx() {
        ObservableList<Usuarios> ListaUsuario = FXCollections.observableArrayList(ListarUsuario());
        cbxUsu.setItems(ListaUsuario);
    }

    //Metodo donde se insertará una Factura para completar la venta 
    public void AgregarFactura() {
        LocalDate tiempo = LocalDate.now();

        Usuarios usuariosSeleccionado = (Usuarios) cbxUsu.getSelectionModel().getSelectedItem();
        int codigoUsu = usuariosSeleccionado != null ? usuariosSeleccionado.getIdUser() : 0;

        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_AgregarFactura(?,?);");
            enunciado.setDate(1, Date.valueOf(tiempo));
            enunciado.setInt(2, codigoUsu);
            ResultSet resultado = enunciado.executeQuery();
            if (resultado.next()) {
                idFactura = resultado.getInt("idFacturaJ");
                System.out.println("Factura creada con ID: " + idFactura);
            }
        } catch (SQLException e) {
            System.out.println("Error al agregar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Metodo donde se insertará DetalleFactura para completar la venta 
    public void AgregarDetalle() {
        Carros carroSeleccionado = tablaCarros.getSelectionModel().getSelectedItem();
        int idCarro = carroSeleccionado != null ? carroSeleccionado.getIdCarro() : 0;

        double precio = carroSeleccionado != null ? carroSeleccionado.getPrecio() : 0.0;
        double subtotal = precio * Double.parseDouble(txtCantidad.getText());

        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_AgregarDetalleFactura(?,?,?,?);");
            enunciado.setInt(1, idFactura);
            enunciado.setInt(2, idCarro);
            enunciado.setInt(3, Integer.parseInt(txtCantidad.getText()));
            enunciado.setDouble(4, subtotal);
            enunciado.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al agregar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void CompletarVenta() {
        //Comprobación si el combo box esta vacio o no: Verdadero dará una alerta para evitar errores
        if (cbxUsu.getSelectionModel().isEmpty()) {
            //Se crea un ALERT de tipo WARNING que advertira que faltan datos
            Alert advertenciaUsu = new Alert(Alert.AlertType.WARNING);
            advertenciaUsu.setTitle("¡Advertencia!");
            advertenciaUsu.setHeaderText("¡Usuario no encontrado!");
            advertenciaUsu.setContentText("Selecciona un usuario antes de continua.");
            advertenciaUsu.show();
            return;
            //Misma comprobación pero para el textField de cantidad
        } else if (txtCantidad.getText().isEmpty()) {
            //Se crea un ALERT de tipo WARNING que advertira que faltan datos
            Alert advertenciaCant = new Alert(Alert.AlertType.WARNING);
            advertenciaCant.setTitle("¡Advertencia!");
            advertenciaCant.setHeaderText("¡Sin cantidad asignada!");
            advertenciaCant.setContentText("Por favor, agrega una cantidad");
            advertenciaCant.show();
            //Comprobación para verificar si está seleccionada una fila de la tabla
        } else if (tablaCarros.getSelectionModel().isEmpty()) {
            //Se crea un ALERT de tipo WARNING que advertira que faltan datos
            Alert advertenciaTbl = new Alert(Alert.AlertType.WARNING);
            advertenciaTbl.setTitle("¡Advertencia!");
            advertenciaTbl.setHeaderText("¡Carro no seleccionado!");
            advertenciaTbl.setContentText("Por favor, selecciona un carro");
            advertenciaTbl.show();
            //Si lo anterior es falso se agregara una factura y un detalle factura de la venta realizada
        } else {
            AgregarFactura();
            //Se realiza una verificación si la idfactura se esta guardando correctamente
            if (idFactura <= 0) {
                System.out.println("IDFactura no encontrada");
            } else {
                //Se ejecutara el metodo AgregarDetalle y lanzara un ALERT de tipo CONFIRMATION
                //para avisar de la realización de la venta.
                AgregarDetalle();
                Alert confirmacionCr = new Alert(Alert.AlertType.CONFIRMATION);
                confirmacionCr.setTitle("¡CREADO!");
                confirmacionCr.setHeaderText("Completaste la venta");
                confirmacionCr.setContentText("La venta ha sido completado, puedes revisar"
                        + "\"Factura\" y \"DetalleFactura\" para ver las facturas creadas. ");
            }
        }
    }
}
