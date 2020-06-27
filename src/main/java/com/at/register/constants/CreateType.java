package com.at.register.constants;

public enum CreateType {
    FaceBook(0),
    Google(1),
    Local(2);
    /**
     * An integer or 0 to identify the email platform.
     */
    private int id;

    CreateType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
