package controller;

import com.beginsecure.socialnetworkjfx.ApplicationManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import map.service.Service;

import java.io.IOException;

public abstract class Controller {

    protected ApplicationManager manager;

    public void setApplicationManager(ApplicationManager manager) {
        this.manager = manager;
    }
}

