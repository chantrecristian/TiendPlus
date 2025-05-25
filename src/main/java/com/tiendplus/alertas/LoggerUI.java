package com.tiendplus.alertas;

import com.vaadin.flow.component.UI;

public class LoggerUI {
    public static void logError(String mensaje) {
        UI.getCurrent().getPage().executeJs("console.error($0)", mensaje);
    }

    public static void logInfo(String mensaje) {
        UI.getCurrent().getPage().executeJs("console.log($0)", mensaje);
    }
}
