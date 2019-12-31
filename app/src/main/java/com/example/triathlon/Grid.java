package com.example.triathlon;

import java.util.ArrayList;
import java.util.List;

public class Grid {
    int width;
    int height;
    //List<List<GridTile>> grid;
    GridTile[][] grid;

    public Grid(int h, int w) {
        height = h;
        width = w;
        grid = new GridTile[h][w];

        /*for (int i = 0; i < h; i++) {
            List<GridTile> row = new ArrayList<GridTile>();
            for (int j = 0; j < w; j++) {
                GridTile tile = new GridTile(i, j,0);
                row.add(tile);
            }
            grid.add(row);
        }*/

    }
}
