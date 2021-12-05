package common;

import java.awt.Color;

public class Constants {
    private Constants(){}

    public enum State 
    {
        INIT,
        UPDATE,
        END,   
    }

    public static final String IP = "192.168.3.9";
    public static final int PORT = 80;

    public static final int WIDTH_CANVAS = 600;
    public static final int HEIGHT_CANVAS = 600;

    public static final int WIDTH_RECT = 750;
    public static final int HEIGHT_RECT = 750;

    public static final int SIZE_OBJ = 10;
    public static final int OFFSET_NAME = 10;

    public static final Color COLOR_STR1 = Color.BLACK;
    public static final Color COLOR_MYSELF = Color.RED;
    public static final Color COLOR_ENEMY = Color.BLUE;
    public static final Color COLOR_ENERGY = Color.ORANGE;
    public static final Color COLOR_BACKGROUND = Color.WHITE;

    public static final long SLEEP_TIME = 500;

    public static final String DB_SERVER_INFO_PATH = "./servers/db_connect_info.pelo";

    
    
}
