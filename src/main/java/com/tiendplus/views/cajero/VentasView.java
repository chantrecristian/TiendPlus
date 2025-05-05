package com.tiendplus.views.cajero;

// Importación de clases necesarias para la vista
import com.tiendplus.views.admin.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase representa la vista de ventas donde se muestran las ventas realizadas
 * en formato de tabla y se permite exportar los datos a un archivo Excel.
 */
@Route(value = "ventas", layout = MainView.class) // Ruta asociada a esta vista
public class VentasView extends VerticalLayout {

    private final Grid<Venta> grid = new Grid<>(Venta.class); // Grid que muestra las ventas

    // Constructor de la clase
    public VentasView() {
        setPadding(true); // Añadir padding al layout
        setSpacing(true); // Añadir espaciado entre los componentes

        // Crear botón para volver al menú principal
        Button volverBtn = new Button("Volver al Menú");
        volverBtn.addClickListener(e -> volverBtn.getUI().ifPresent(ui -> ui.navigate("menu-admin")));

        // Layout superior con el botón alineado a la izquierda
        HorizontalLayout topBar = new HorizontalLayout(volverBtn);
        topBar.setWidthFull(); // Hacer que ocupe todo el ancho disponible
        topBar.setJustifyContentMode(JustifyContentMode.START); // Alineación a la izquierda

        // Lista simulada de ventas
        List<Venta> ventas = new ArrayList<>();
        ventas.add(new Venta("2025-05-01", "Producto 1", 2, 5000, 10000)); // Añadir ventas simuladas
        ventas.add(new Venta("2025-05-02", "Producto 2", 1, 3000, 3000));
        ventas.add(new Venta("2025-05-03", "Producto 3", 5, 4000, 20000));

        // Configurar las columnas de la tabla
        grid.setItems(ventas); // Asignar las ventas a la tabla
        grid.setColumns("fecha", "producto", "cantidad", "precio", "total"); // Definir las columnas de la tabla
        grid.getColumnByKey("fecha").setHeader("Fecha"); // Encabezado de la columna Fecha
        grid.getColumnByKey("producto").setHeader("Producto"); // Encabezado de la columna Producto
        grid.getColumnByKey("cantidad").setHeader("Cantidad"); // Encabezado de la columna Cantidad
        grid.getColumnByKey("precio").setHeader("Precio"); // Encabezado de la columna Precio
        grid.getColumnByKey("total").setHeader("Total"); // Encabezado de la columna Total

        // Crear icono de Excel para el botón
        Icon excelIcon = VaadinIcon.FILE_TABLE.create();
        excelIcon.getStyle().set("margin-right", "8px").set("color", "white"); // Estilo para el icono

        // Crear botón para exportar las ventas a un archivo Excel
        Button exportButton = new Button("Exportar a Excel", excelIcon, e -> exportToExcel(ventas));
        exportButton.getStyle()
            .set("background-color", "#1e7e34") // Color de fondo del botón
            .set("color", "white") // Color del texto
            .set("border-radius", "8px") // Bordes redondeados
            .set("margin-top", "20px"); // Margen superior

        // Añadir los componentes al layout principal
        add(topBar, grid, exportButton);
    }

    // Método para exportar las ventas a un archivo Excel
    private void exportToExcel(List<Venta> ventas) {
        Workbook workbook = new XSSFWorkbook(); // Crear un libro de Excel
        Sheet sheet = workbook.createSheet("Ventas"); // Crear una hoja llamada "Ventas"

        // Crear la fila de encabezado
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Fecha"); // Columna Fecha
        headerRow.createCell(1).setCellValue("Producto"); // Columna Producto
        headerRow.createCell(2).setCellValue("Cantidad"); // Columna Cantidad
        headerRow.createCell(3).setCellValue("Precio"); // Columna Precio
        headerRow.createCell(4).setCellValue("Total"); // Columna Total

        // Añadir las filas de datos para cada venta
        int rowNum = 1;
        for (Venta venta : ventas) {
            Row row = sheet.createRow(rowNum++); // Crear una nueva fila para cada venta
            row.createCell(0).setCellValue(venta.getFecha());
            row.createCell(1).setCellValue(venta.getProducto());
            row.createCell(2).setCellValue(venta.getCantidad());
            row.createCell(3).setCellValue(venta.getPrecio());
            row.createCell(4).setCellValue(venta.getTotal());
        }

        // Ajustar el tamaño de las columnas automáticamente
        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }

        try (ByteArrayOutputStream fileOut = new ByteArrayOutputStream()) {
            workbook.write(fileOut); // Escribir el contenido del libro a un flujo de salida
            byte[] bytes = fileOut.toByteArray(); // Convertir a bytes

            // Crear un recurso de descarga para el archivo Excel
            StreamResource resource = new StreamResource("ventas.xlsx", () -> new ByteArrayInputStream(bytes));
            Anchor downloadLink = new Anchor(resource, "Descargar Excel"); // Enlace para descargar el archivo
            downloadLink.getElement().setAttribute("download", true);

            // Forzar el clic para iniciar la descarga automáticamente
            downloadLink.getElement().executeJs("this.click()");

            // Añadir el enlace al layout para ejecutar el JavaScript
            add(downloadLink);

            // Mostrar notificación de éxito
            Notification.show("Exportación a Excel exitosa", 3000, Notification.Position.BOTTOM_START)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } catch (IOException e) {
            // Mostrar notificación de error si ocurre un problema al exportar
            Notification.show("Error al exportar a Excel", 3000, Notification.Position.BOTTOM_START)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    // Clase interna para representar una venta
    public static class Venta {
        private String fecha;
        private String producto;
        private int cantidad;
        private double precio;
        private double total;

        public Venta(String fecha, String producto, int cantidad, double precio, double total) {
            this.fecha = fecha;
            this.producto = producto;
            this.cantidad = cantidad;
            this.precio = precio;
            this.total = total;
        }

        // Métodos getter para acceder a los datos de la venta
        public String getFecha() {
            return fecha;
        }

        public String getProducto() {
            return producto;
        }

        public int getCantidad() {
            return cantidad;
        }

        public double getPrecio() {
            return precio;
        }

        public double getTotal() {
            return total;
        }
    }
}
