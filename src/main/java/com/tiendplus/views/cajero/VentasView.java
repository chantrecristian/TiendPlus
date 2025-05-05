package com.tiendplus.views.cajero;

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

@Route(value = "ventas", layout = MainView.class)
public class VentasView extends VerticalLayout {

    private final Grid<Venta> grid = new Grid<>(Venta.class);

    public VentasView() {
        setPadding(true);
        setSpacing(true);

        // Botón para volver al menú
        Button volverBtn = new Button("Volver al Menú");
        volverBtn.addClickListener(e -> volverBtn.getUI().ifPresent(ui -> ui.navigate("menu-admin")));

        // Layout superior con el botón alineado a la izquierda
        HorizontalLayout topBar = new HorizontalLayout(volverBtn);
        topBar.setWidthFull();
        topBar.setJustifyContentMode(JustifyContentMode.START);

        // Lista de ventas simuladas
        List<Venta> ventas = new ArrayList<>();
        ventas.add(new Venta("2025-05-01", "Producto 1", 2, 5000, 10000));
        ventas.add(new Venta("2025-05-02", "Producto 2", 1, 3000, 3000));
        ventas.add(new Venta("2025-05-03", "Producto 3", 5, 4000, 20000));

        // Configurar tabla
        grid.setItems(ventas);
        grid.setColumns("fecha", "producto", "cantidad", "precio", "total");
        grid.getColumnByKey("fecha").setHeader("Fecha");
        grid.getColumnByKey("producto").setHeader("Producto");
        grid.getColumnByKey("cantidad").setHeader("Cantidad");
        grid.getColumnByKey("precio").setHeader("Precio");
        grid.getColumnByKey("total").setHeader("Total");

        // Icono de Excel
        Icon excelIcon = VaadinIcon.FILE_TABLE.create();
        excelIcon.getStyle().set("margin-right", "8px").set("color", "white");

        // Botón exportar a Excel con ícono
        Button exportButton = new Button("Exportar a Excel", excelIcon, e -> exportToExcel(ventas));
        exportButton.getStyle()
            .set("background-color", "#1e7e34")
            .set("color", "white")
            .set("border-radius", "8px")
            .set("margin-top", "20px");

        // Añadir al layout
        add(topBar, grid, exportButton);
    }

    private void exportToExcel(List<Venta> ventas) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Ventas");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Fecha");
        headerRow.createCell(1).setCellValue("Producto");
        headerRow.createCell(2).setCellValue("Cantidad");
        headerRow.createCell(3).setCellValue("Precio");
        headerRow.createCell(4).setCellValue("Total");

        int rowNum = 1;
        for (Venta venta : ventas) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(venta.getFecha());
            row.createCell(1).setCellValue(venta.getProducto());
            row.createCell(2).setCellValue(venta.getCantidad());
            row.createCell(3).setCellValue(venta.getPrecio());
            row.createCell(4).setCellValue(venta.getTotal());
        }

        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }

        try (ByteArrayOutputStream fileOut = new ByteArrayOutputStream()) {
            workbook.write(fileOut);
            byte[] bytes = fileOut.toByteArray();
            StreamResource resource = new StreamResource("ventas.xlsx", () -> new ByteArrayInputStream(bytes));
            Anchor downloadLink = new Anchor(resource, "Descargar Excel");
            downloadLink.getElement().setAttribute("download", true);

            // Forzar el clic para iniciar la descarga automáticamente
            downloadLink.getElement().executeJs("this.click()");

            // Añadir al layout para que se ejecute el JavaScript
            add(downloadLink);

            // Mostrar notificación de éxito
            Notification.show("Exportación a Excel exitosa", 3000, Notification.Position.BOTTOM_START)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } catch (IOException e) {
            // Mostrar notificación de error
            Notification.show("Error al exportar a Excel", 3000, Notification.Position.BOTTOM_START)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

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
