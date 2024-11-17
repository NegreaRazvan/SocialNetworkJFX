package controller;

import javafx.stage.Stage;
import map.service.Service;

public abstract class Controller {
    protected Stage stage;
    protected Service service;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setService(Service service) {
        this.service = service;
    }
}
