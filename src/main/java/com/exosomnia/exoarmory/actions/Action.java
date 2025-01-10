package com.exosomnia.exoarmory.actions;

public abstract class Action {

    protected ActionManager manager;
    public boolean active = true;

    public Action(ActionManager manager) {
        this.manager = manager;
    }

    public abstract boolean isValid();
    public abstract void action();

    public void execute() {
        if (!active || !isValid()) { return; }
        action();
    }
}
