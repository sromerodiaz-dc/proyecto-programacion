package com.game.entity.factory;

public enum EntityType {
    VIELLO("Viello"),
    DINOSETO("Dinoseto_elegante");
    // passvigo etc...

    private final String id;

    EntityType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
