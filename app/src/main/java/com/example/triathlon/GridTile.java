package com.example.triathlon;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;


public class GridTile {
    float left;
    float top;
    float right;
    float bottom;
    float z;
    int color;
    int weight;
    int borderStrokeWidth = 3;
    boolean chosen = false;
    int row;
    int col;



    public GridTile(float left, float top, float right, float bottom, float z, int color) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.z = z;
        this.color = color;

    }

}
