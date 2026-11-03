package com.chat_hub.chat_hub.client;

import java.awt.Color;

import javax.swing.border.EmptyBorder;

public class Utilities {
    public static final Color TRANSPARENT_COLOR = new Color(0, 0, 0, 0);
    public static final Color PRIMARY_COLOR = Color.decode("#2F2D2D");
    public static final Color SECONDARY_COLOR = Color.decode("#484444");
    public static final Color THERID_COLOR = Color.decode("#83a9e8");
    public static final Color TEXT_COLOR = Color.WHITE;
    public static final Color TEXT_COLOR_INPUT = Color.BLACK;

    public static EmptyBorder addPadding(int top, int left, int bottom, int right){
        return new EmptyBorder(top, left, bottom, right);
    }
}
