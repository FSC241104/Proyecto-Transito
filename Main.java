import java.sql.*;
import javax.swing.*;

public class Main {
    private static final String URL = "jdbc:postgresql://localhost:5432/Transito";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1023523004";

    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            while (true) {
                int choice = Integer.parseInt(JOptionPane.showInputDialog(
                        "\nBienvenido al Sistema de Tránsito (Medellín)\nSeleccione la opción que desea: \n1. Conductores\n2. Vehículos\n3. Multas\n4. Salir"));

                switch (choice) {
                    case 1:
                        conductoresCRUD(connection);
                        break;
                    case 2:
                        vehiculosCRUD(connection);
                        break;
                    case 3:
                        multasCRUD(connection);
                        break;
                    case 4:
                        connection.close();
                        return;
                    default:
                        JOptionPane.showMessageDialog(null, "Opción inválida");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    private static void conductoresCRUD(Connection connection) throws SQLException {
        while (true) {
            int choice = JOptionPane.showOptionDialog(null,
                    "__Menú Conductores__\nSeleccione la acción que desea realizar:",
                    "Conductores",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new String[] { "Agregar Conductor", "Visualizar Conductores", "Actualizar Conductor",
                            "Eliminar Conductor", "Volver atrás" },
                    null);

            switch (choice) {
                case 0:
                    crearConductor(connection);
                    break;
                case 1:
                    leerConductores(connection);
                    break;
                case 2:
                    actualizarConductor(connection);
                    break;
                case 3:
                    eliminarConductor(connection);
                    break;
                case 4:
                    return;
                default:
                    JOptionPane.showMessageDialog(null, "Opción inválida");
            }
        }
    }

    private static void crearConductor(Connection connection) throws SQLException {
        String nombre = JOptionPane.showInputDialog("Ingrese el nombre del conductor:");
        String apellido = JOptionPane.showInputDialog("Ingrese el apellido del conductor:");
        String fechaNacimiento = JOptionPane
                .showInputDialog("Ingrese la fecha de nacimiento del conductor (YYYY-MM-DD):");
        String licenciaNumero = JOptionPane.showInputDialog("Ingrese el número de licencia del conductor:");

        String query = "INSERT INTO conductores (nombre, apellido, fecha_nacimiento, licencia_numero) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, nombre);
        statement.setString(2, apellido);
        statement.setDate(3, Date.valueOf(fechaNacimiento));
        statement.setString(4, licenciaNumero);
        statement.executeUpdate();
        JOptionPane.showMessageDialog(null, "Conductor creado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void leerConductores(Connection connection) throws SQLException {
        String query = "SELECT * FROM conductores";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        StringBuilder sb = new StringBuilder();
        while (resultSet.next()) {
            sb.append("ID: ").append(resultSet.getInt("id"))
                    .append(", Nombre: ").append(resultSet.getString("nombre"))
                    .append(", Apellido: ").append(resultSet.getString("apellido"))
                    .append(", Fecha de Nacimiento: ").append(resultSet.getDate("fecha_nacimiento"))
                    .append(", Licencia: ").append(resultSet.getString("licencia_numero"))
                    .append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Conductores", JOptionPane.PLAIN_MESSAGE);
    }

    private static void actualizarConductor(Connection connection) throws SQLException {
        try {
            int id = Integer.parseInt(
                    JOptionPane.showInputDialog("Ingrese el ID del conductor a actualizar (o -1 para cancelar):"));
            if (id == -1)
                return; // Cancelar
            String nombre = JOptionPane.showInputDialog("Ingrese el nuevo nombre del conductor:");
            String apellido = JOptionPane.showInputDialog("Ingrese el nuevo apellido del conductor:");
            String fechaNacimiento = JOptionPane
                    .showInputDialog("Ingrese la nueva fecha de nacimiento del conductor (YYYY-MM-DD):");
            String licenciaNumero = JOptionPane.showInputDialog("Ingrese el nuevo número de licencia del conductor:");

            String query = "UPDATE conductores SET nombre=?, apellido=?, fecha_nacimiento=?, licencia_numero=? WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, nombre);
            statement.setString(2, apellido);
            statement.setDate(3, Date.valueOf(fechaNacimiento));
            statement.setString(4, licenciaNumero);
            statement.setInt(5, id);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Conductor actualizado exitosamente", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró ningún conductor con ese ID", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID de conductor inválido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void eliminarConductor(Connection connection) throws SQLException {
        try {
            int id = Integer.parseInt(
                    JOptionPane.showInputDialog("Ingrese el ID del conductor a eliminar (o -1 para cancelar):"));
            if (id == -1)
                return; // Cancelar
            String query = "DELETE FROM conductores WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(null, "Conductor eliminado exitosamente", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró ningún conductor con ese ID", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID de conductor inválido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void vehiculosCRUD(Connection connection) throws SQLException {
        while (true) {
            int choice = JOptionPane.showOptionDialog(null,
                    "__Menú Vehículos__\nSeleccione la acción que desea realizar:",
                    "Vehículos",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new String[] { "Agregar Vehículo", "Visualizar Vehículos", "Actualizar Vehículo",
                            "Eliminar Vehículo", "Volver atrás" },
                    null);

            switch (choice) {
                case 0:
                    crearVehiculo(connection);
                    break;
                case 1:
                    leerVehiculos(connection);
                    break;
                case 2:
                    actualizarVehiculo(connection);
                    break;
                case 3:
                    eliminarVehiculo(connection);
                    break;
                case 4:
                    return;
                default:
                    JOptionPane.showMessageDialog(null, "Opción inválida");
            }
        }
    }

    private static void crearVehiculo(Connection connection) throws SQLException {
        String placa = JOptionPane.showInputDialog("Ingrese la placa del vehículo:");
        String marca = JOptionPane.showInputDialog("Ingrese la marca del vehículo:");
        String modelo = JOptionPane.showInputDialog("Ingrese el modelo del vehículo:");
        int anio = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el año del vehículo:"));
        String color = JOptionPane.showInputDialog("Ingrese el color del vehículo:");
        int propietarioId = Integer
                .parseInt(JOptionPane.showInputDialog("Ingrese el ID del propietario del vehículo:"));

        String query = "INSERT INTO vehiculos (placa, marca, modelo, anio, color, propietario_id) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, placa);
        statement.setString(2, marca);
        statement.setString(3, modelo);
        statement.setInt(4, anio);
        statement.setString(5, color);
        statement.setInt(6, propietarioId);
        statement.executeUpdate();
        JOptionPane.showMessageDialog(null, "Vehículo creado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void leerVehiculos(Connection connection) throws SQLException {
        String query = "SELECT * FROM vehiculos";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        StringBuilder sb = new StringBuilder();
        while (resultSet.next()) {
            sb.append("ID: ").append(resultSet.getInt("id"))
                    .append(", Placa: ").append(resultSet.getString("placa"))
                    .append(", Marca: ").append(resultSet.getString("marca"))
                    .append(", Modelo: ").append(resultSet.getString("modelo"))
                    .append(", Año: ").append(resultSet.getInt("anio"))
                    .append(", Color: ").append(resultSet.getString("color"))
                    .append(", Propietario ID: ").append(resultSet.getInt("propietario_id"))
                    .append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Vehículos", JOptionPane.PLAIN_MESSAGE);
    }

    private static void actualizarVehiculo(Connection connection) throws SQLException {
        try {
            int id = Integer.parseInt(
                    JOptionPane.showInputDialog("Ingrese el ID del vehículo a actualizar (o -1 para cancelar):"));
            if (id == -1)
                return; // Cancelar
            String placa = JOptionPane.showInputDialog("Ingrese la nueva placa del vehículo:");
            String marca = JOptionPane.showInputDialog("Ingrese la nueva marca del vehículo:");
            String modelo = JOptionPane.showInputDialog("Ingrese el nuevo modelo del vehículo:");
            int anio = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el nuevo año del vehículo:"));
            String color = JOptionPane.showInputDialog("Ingrese el nuevo color del vehículo:");
            int propietarioId = Integer
                    .parseInt(JOptionPane.showInputDialog("Ingrese el nuevo ID del propietario del vehículo:"));

            String query = "UPDATE vehiculos SET placa=?, marca=?, modelo=?, anio=?, color=?, propietario_id=? WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, placa);
            statement.setString(2, marca);
            statement.setString(3, modelo);
            statement.setInt(4, anio);
            statement.setString(5, color);
            statement.setInt(6, propietarioId);
            statement.setInt(7, id);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Vehículo actualizado exitosamente", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró ningún vehículo con ese ID", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID de vehículo inválido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void eliminarVehiculo(Connection connection) throws SQLException {
        try {
            int id = Integer.parseInt(
                    JOptionPane.showInputDialog("Ingrese el ID del vehículo a eliminar (o -1 para cancelar):"));
            if (id == -1)
                return; // Cancelar
            String query = "DELETE FROM vehiculos WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(null, "Vehículo eliminado exitosamente", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró ningún vehículo con ese ID", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID de vehículo inválido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void multasCRUD(Connection connection) throws SQLException {
        while (true) {
            int choice = JOptionPane.showOptionDialog(null,
                    "__Menú Multas__\nSeleccione la acción que desea realizar:",
                    "Multas",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new String[] { "Agregar Multa", "Visualizar Multas", "Actualizar Multa", "Eliminar Multa",
                            "Volver atrás" },
                    null);

            switch (choice) {
                case 0:
                    crearMulta(connection);
                    break;
                case 1:
                    leerMultas(connection);
                    break;
                case 2:
                    actualizarMulta(connection);
                    break;
                case 3:
                    eliminarMulta(connection);
                    break;
                case 4:
                    return;
                default:
                    JOptionPane.showMessageDialog(null, "Opción inválida");
            }
        }
    }

    private static void crearMulta(Connection connection) throws SQLException {
        int vehiculoId = Integer
                .parseInt(JOptionPane.showInputDialog("Ingrese el ID del vehículo asociado a la multa:"));
        int conductorId = Integer
                .parseInt(JOptionPane.showInputDialog("Ingrese el ID del conductor asociado a la multa:"));
        String fecha = JOptionPane.showInputDialog("Ingrese la fecha de la multa (YYYY-MM-DD):");
        double monto = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el monto de la multa:"));
        String descripcion = JOptionPane.showInputDialog("Ingrese la descripción de la multa:");

        String query = "INSERT INTO multas (vehiculo_id, conductor_id, fecha, monto, descripcion) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, vehiculoId);
        statement.setInt(2, conductorId);
        statement.setDate(3, Date.valueOf(fecha));
        statement.setDouble(4, monto);
        statement.setString(5, descripcion);
        statement.executeUpdate();
        JOptionPane.showMessageDialog(null, "Multa creada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void leerMultas(Connection connection) throws SQLException {
        String query = "SELECT * FROM multas";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        StringBuilder sb = new StringBuilder();
        while (resultSet.next()) {
            sb.append("ID: ").append(resultSet.getInt("id"))
                    .append(", Vehículo ID: ").append(resultSet.getInt("vehiculo_id"))
                    .append(", Conductor ID: ").append(resultSet.getInt("conductor_id"))
                    .append(", Fecha: ").append(resultSet.getDate("fecha"))
                    .append(", Monto: ").append(resultSet.getDouble("monto"))
                    .append(", Descripción: ").append(resultSet.getString("descripcion"))
                    .append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Multas", JOptionPane.PLAIN_MESSAGE);
    }

    private static void actualizarMulta(Connection connection) throws SQLException {
        try {
            int id = Integer.parseInt(
                    JOptionPane.showInputDialog("Ingrese el ID de la multa a actualizar (o -1 para cancelar):"));
            if (id == -1)
                return; // Cancelar
            int vehiculoId = Integer
                    .parseInt(JOptionPane.showInputDialog("Ingrese el nuevo ID del vehículo asociado a la multa:"));
            int conductorId = Integer
                    .parseInt(JOptionPane.showInputDialog("Ingrese el nuevo ID del conductor asociado a la multa:"));
            String fecha = JOptionPane.showInputDialog("Ingrese la nueva fecha de la multa (YYYY-MM-DD):");
            double monto = Double.parseDouble(JOptionPane.showInputDialog("Ingrese el nuevo monto de la multa:"));
            String descripcion = JOptionPane.showInputDialog("Ingrese la nueva descripción de la multa:");

            String query = "UPDATE multas SET vehiculo_id=?, conductor_id=?, fecha=?, monto=?, descripcion=? WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, vehiculoId);
            statement.setInt(2, conductorId);
            statement.setDate(3, Date.valueOf(fecha));
            statement.setDouble(4, monto);
            statement.setString(5, descripcion);
            statement.setInt(6, id);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Multa actualizada exitosamente", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró ninguna multa con ese ID", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID de multa inválido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void eliminarMulta(Connection connection) throws SQLException {
        try {
            int id = Integer.parseInt(
                    JOptionPane.showInputDialog("Ingrese el ID de la multa a eliminar (o -1 para cancelar):"));
            if (id == -1)
                return; // Cancelar
            String query = "DELETE FROM multas WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(null, "Multa eliminada exitosamente", "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró ninguna multa con ese ID", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "ID de multa inválido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
