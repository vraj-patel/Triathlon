package com.example.triathlon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

public class GridView extends View {
    private boolean readyToDraw = false;
    private GridTile[][] grid;
    private int gridHeight = 0;
    private int gridWidth = 0;
    List<GridTile> chosenPath = new ArrayList<GridTile>();
    int weightOfChosenPath = 0;

    int swimColor = 0xff99ccff;
    int runColor = 0xff99ff99;
    int bicycleColor = 0xffbf8040;
    int startFinishColor = 0xff4d4d4d;

    int darkSwimColor = 0xff008ae6;
    int darkRunColor = 0xff009933;
    int darkBicycleColor = 0xff86592d;
    int darkStartFinishColor = 0xff000000;



    public GridView(Context context) {
        super(context);
        init(null);
    }

    public GridView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public GridView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GridView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }


    private void init (@Nullable AttributeSet set) {

    }

    public void setReadyToDraw(boolean isReady) {
        readyToDraw = isReady;
    }

    public void resetChosenPath() {
        for (int i = 0; i < chosenPath.size(); i++) {
            int color = chosenPath.get(i).color;

            if (color == darkSwimColor) chosenPath.get(i).color = swimColor;
            else if (color == darkRunColor) chosenPath.get(i).color = runColor;
            else if (color == darkBicycleColor) chosenPath.get(i).color = bicycleColor;
            else if (color == darkStartFinishColor) chosenPath.get(i).color = startFinishColor;
        }
        chosenPath = new ArrayList<GridTile>();
        weightOfChosenPath = 0;
    }

    public void setGrid(GridTile[][] g, int gridH, int gridW) {
        grid = g;
        gridHeight = gridH;
        gridWidth = gridW;
    }

    int selectColor(int color) {

        if (color == swimColor) return darkSwimColor;
        if (color == runColor) return darkRunColor;
        if (color == bicycleColor) return darkBicycleColor;
        if (color == startFinishColor) return darkStartFinishColor;

        if (color == darkSwimColor) return swimColor;
        if (color == darkRunColor) return runColor;
        if (color == darkBicycleColor) return bicycleColor;
        if (color == darkStartFinishColor) return startFinishColor;

        return 0;
    }

    void deleteFromChosen(int row, int column) {
        for (int i = 0; i < chosenPath.size(); i++) {
            if (chosenPath.get(i).col == column && chosenPath.get(i).row == row) {
                chosenPath.remove(i);
                break;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            float x = e.getX();
            float y = e.getY();

            for (int i = 0; i < gridHeight; i++) {
                for (int j = 0; j < gridWidth; j++) {
                    Rect rect = new Rect(
                            (int)grid[i][j].left,
                            (int)grid[i][j].top,
                            (int)grid[i][j].right,
                            (int)grid[i][j].bottom);

                    if (rect.contains((int)x, (int)y)) {
                        grid[i][j].color = selectColor(grid[i][j].color);

                        if (grid[i][j].chosen) {
                            grid[i][j].chosen = false;
                            deleteFromChosen(grid[i][j].row, grid[i][j].col);
                            weightOfChosenPath -= grid[i][j].weight;
                        } else {
                            grid[i][j].chosen = true;
                            chosenPath.add(grid[i][j]);
                            weightOfChosenPath += grid[i][j].weight;
                        }

                        invalidate();
                        break;
                    }

                }
            }
            return true;
        }
        return false;
    }



    @Override
    protected void onDraw(Canvas canvas) {

        if (readyToDraw) {
            for (int i = 0; i < gridHeight; i++) {
                for (int j = 0; j < gridWidth; j++) {

                    Paint fillPaint = new Paint();
                    fillPaint.setColor(grid[i][j].color);

                    Paint borderPaint = new Paint();
                    borderPaint.setStyle(Paint.Style.STROKE);
                    borderPaint.setStrokeWidth(grid[i][j].borderStrokeWidth);

                    canvas.drawRect(grid[i][j].left, grid[i][j].top, grid[i][j].right, grid[i][j].bottom, fillPaint);
                    canvas.drawRect(grid[i][j].left, grid[i][j].top, grid[i][j].right, grid[i][j].bottom, borderPaint);
                }
            }
        }
    }
}

