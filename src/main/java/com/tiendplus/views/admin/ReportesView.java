package com.tiendplus.views.admin;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Reportes")
@Route(value = "reportes", layout = MainView.class)
public class ReportesView extends Div {
    public ReportesView() {
        setText("Esta vista es de reportes.");
    }
}