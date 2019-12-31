package com.example.triathlon;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private int gridWidth = 15;
    private int gridHeight = 0;
    private GridTile[][] grid;
    private int startCol = 0;
    private int startRow = 0;
    private int endCol = 0;
    private int endRow = 0;
    GridView gridView;
    int swimWeight = 3;
    int runWeight = 2;
    int bicycleWeight = 1;
    int canvasHeight;
    int canvasWidth;
    int swimColor = 0xff99ccff;
    int runColor = 0xff99ff99;
    int bicycleColor = 0xffbf8040;
    int startFinishColor = 0xff4d4d4d;

    void initGrid(int canvasWidth, int canvasHeight) {
        float tileLength = canvasWidth / gridWidth;
        gridHeight = (int) ((float)canvasHeight / tileLength);
        float yOffsetShift = canvasHeight - gridHeight * tileLength;
        float extraWidthSpace = canvasWidth - tileLength*gridWidth;

        grid = new GridTile[gridHeight][gridWidth];

        for (int i = 0; i < gridHeight; i++ ) {
            for (int j = 0; j < gridWidth; j++) {
                float left = j*tileLength;
                float top = i*tileLength + yOffsetShift;
                float right = left+tileLength;
                float bottom = top + tileLength;
                if (j == gridWidth - 1) right += extraWidthSpace;

                GridTile gridTile = new GridTile(left, top, right, bottom, 0, Color.WHITE);
                gridTile.row = i;
                gridTile.col = j;
                grid[i][j] = gridTile;
            }
        }
    }

    void assignStartAndEnd() {
        Random random = new Random();

        startRow = random.nextInt(gridHeight);
        startCol = random.nextInt(gridWidth);

        endRow = random.nextInt(gridHeight);
        endCol = random.nextInt(gridWidth);

        while (endCol == startCol && endRow == startRow) {
            endRow = random.nextInt(gridHeight);
            endCol = random.nextInt(gridWidth);
        }

        grid[startRow][startCol].color = startFinishColor;
        grid[startRow][startCol].weight = 0;

        grid[endRow][endCol].color = startFinishColor;
        grid[endRow][endCol].weight = 0;
    }

    void assignLandscapes() {
        Random r = new Random();
        for (int i = 0; i < gridHeight; i++) {
            for (int j = 0; j < gridWidth; j++) {
                int temp = r.nextInt(3);
                if (temp == 0){
                    grid[i][j].color = runColor;
                    grid[i][j].weight = runWeight;
                } else if (temp == 1) {
                    grid[i][j].color = bicycleColor;
                    grid[i][j].weight = bicycleWeight;
                } else if (temp == 2) {
                    grid[i][j].color = swimColor;
                    grid[i][j].weight = swimWeight;
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Algorithms algorithms = new Algorithms();

        final TextView resultTV = (TextView) findViewById(R.id.resultTV);

        gridView = (GridView)findViewById(R.id.gridview);

        gridView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {

            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight,
                                       int oldBottom) {

                if (left == 0 && top == 0 && right == 0 && bottom == 0) {
                    return;
                }

                canvasWidth = right - left;
                canvasHeight = bottom - top;

                initGrid(canvasWidth, canvasHeight);
                assignLandscapes();
                assignStartAndEnd();

                gridView.setGrid(grid, gridHeight, gridWidth);
                gridView.setReadyToDraw(true);
                gridView.invalidate();
            }
        });

        Button resetBtn = (Button) findViewById(R.id.resetBtn);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridView.resetChosenPath();
                gridView.invalidate();
            }
        });

        Button newLocationsBtn = (Button) findViewById(R.id.newLocationsBtn);
        newLocationsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initGrid(canvasWidth, canvasHeight);
                assignLandscapes();
                assignStartAndEnd();

                gridView.setGrid(grid, gridHeight, gridWidth);
                gridView.setReadyToDraw(true);
                gridView.resetChosenPath();
                gridView.invalidate();
            }
        });

        Button checkDistance = (Button) findViewById(R.id.checkDistanceBtn);
        checkDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int optimalSolutionWeight = algorithms.dijkstra(gridHeight, gridWidth, startRow, startCol, endRow, endCol, grid);

                if (gridView.weightOfChosenPath != optimalSolutionWeight) {
                    resultTV.setText("Not Optimal Solution");

                } else {

                    if (algorithms.startAndEndIncluded(grid, startRow, startCol, endRow, endCol) && algorithms.chosenPathIsConnected(gridHeight, gridWidth, gridView)) {
                        resultTV.setText("Correct!");
                    } else {
                        resultTV.setText("Not Optimal Solution");
                    }
                }
            }
        });

    }
}
