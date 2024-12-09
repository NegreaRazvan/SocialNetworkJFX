package Ui.controller;

import Ui.socialnetworkjfx.ApplicationManager;

public abstract class Controller {

    protected ApplicationManager manager;

    public void setApplicationManager(ApplicationManager manager) {
        this.manager = manager;
    }
}

