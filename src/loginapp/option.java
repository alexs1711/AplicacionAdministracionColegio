package loginapp;

public enum option {
    Profesor , Estudiante , Admin;

    private option(){}

    public String value(){

        return name();
    }

    public static option fromvalue (String v) { /*v para valor*/
        return valueOf(v);
    }
}
