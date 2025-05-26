package com.tiendplus.views.cajero;

import com.tiendplus.models.Venta;
import com.tiendplus.repositories.VentaRepository;
import com.tiendplus.views.admin.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Route(value = "ventas", layout = MainView.class)
public class VentasView extends VerticalLayout {

    private final Grid<Venta> grid = new Grid<>(Venta.class);
    private final VentaRepository ventaRepository;

    public VentasView(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;

        setPadding(true);
        setSpacing(true);

        // Botón para volver al menú principal
        Button volverBtn = new Button("Volver al Menú");
        volverBtn.addClickListener(e -> volverBtn.getUI().ifPresent(ui -> {
            ui.navigate("menu-admin");
            ui.getPage().executeJs("console.log('Navegando al menú principal');");
        }));

        HorizontalLayout topBar = new HorizontalLayout(volverBtn);
        topBar.setWidthFull();
        topBar.setJustifyContentMode(JustifyContentMode.START);

        // Obtener ventas desde la base de datos
        List<Venta> ventas = ventaRepository.findAll();

        // Configurar columnas personalizadas (sin la de cajero)
        grid.removeAllColumns();
        grid.addColumn(Venta::getFechaVenta).setHeader("Fecha");
        grid.addColumn(Venta::getMetodoPago).setHeader("Método de Pago");
        grid.addColumn(producto -> {
            NumberFormat formato = NumberFormat.getNumberInstance(new Locale("es", "CO"));
            return formato.format(producto.getTotal());
        }).setHeader("Total");

        grid.setItems(ventas);

        // Botón para exportar a Excel
        Icon excelIcon = VaadinIcon.FILE_TABLE.create();
        excelIcon.getStyle().set("margin-right", "8px").set("color", "white");

        Button exportButton = new Button("Exportar a Excel", excelIcon, e -> exportToExcel(ventas));
        exportButton.getStyle()
            .set("background-color", "#1e7e34")
            .set("color", "white")
            .set("border-radius", "8px")
            .set("margin-top", "20px");

        add(topBar, grid, exportButton);
    }

    private void exportToExcel(List<Venta> ventas) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Ventas");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Fecha");
        headerRow.createCell(1).setCellValue("Método de Pago");
        headerRow.createCell(2).setCellValue("Total");

        int rowNum = 1;
        for (Venta venta : ventas) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(venta.getFechaVenta().toString());
            row.createCell(1).setCellValue(venta.getMetodoPago());
            row.createCell(2).setCellValue(venta.getTotal());
        }

        for (int i = 0; i < 4; i++) {
            sheet.autoSizeColumn(i);
        }

        try (ByteArrayOutputStream fileOut = new ByteArrayOutputStream()) {
            workbook.write(fileOut);
            byte[] bytes = fileOut.toByteArray();

            StreamResource resource = new StreamResource("ventas.xlsx", () -> new ByteArrayInputStream(bytes));
            Anchor downloadLink = new Anchor(resource, "");
            downloadLink.getElement().setAttribute("download", true);
            downloadLink.getElement().executeJs("this.click()");
            add(downloadLink);

            // Mostrar notificación y mensaje en consola
            Notification.show("Exportación a Excel exitosa", 3000, Notification.Position.BOTTOM_START)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            getUI().ifPresent(ui -> ui.getPage().executeJs("console.log('Exportación a Excel exitosa')"));

        } catch (IOException e) {
            Notification.show("Error al exportar a Excel", 3000, Notification.Position.BOTTOM_START)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            getUI().ifPresent(ui -> ui.getPage().executeJs("console.error('Error al exportar a Excel: ' + $0)", e.getMessage()));
        }
    }
}
