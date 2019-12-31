package com.example.triathlon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class Algorithms {
    int dijkstra(int gridHeight, int gridWidth, int startRow, int startCol, int endRow, int endCol, GridTile[][] grid) {
        int numOfNodes = gridHeight * gridWidth;
        int dist[] = new int[numOfNodes];

        for (int i = 0; i < numOfNodes; i++) {
            dist[i] = Integer.MAX_VALUE - 1;
        }

        int vertex = startRow*gridWidth + startCol;
        dist[vertex] = 0;

        List<Integer> H = makeList(dist, gridWidth, gridHeight);

        int size = H.size();
        while (size > 0) {
            int u = H.indexOf(Collections.min(H));
            H.set(u, Integer.MAX_VALUE);
            size--;

            int[] neighbours = getNeighbours_Dijkstra(u, gridWidth, gridHeight);
            for (int i = 0; i < neighbours.length; i++) {
                int weightUV = weight(u, neighbours[i], gridWidth, grid);
                if (dist[u] +  weightUV < dist[neighbours[i]]){
                    dist[neighbours[i]] = dist[u] + weightUV;
                    H = decreaseKey(H, neighbours[i], dist[neighbours[i]]);
                }
            }
        }

        int v = endRow*gridWidth + endCol;
        return dist[v];
    }

    private int[] getNeighbours_Dijkstra(int u, int gridWidth, int gridHeight) {
        int[] neighbours = {};
        int uRow = u / gridWidth;
        int uCol = u % gridWidth;

        if ((uRow > 0) && (uRow < gridHeight - 1) && (uCol > 0) && (uCol < gridWidth - 1)) {
            neighbours = new int[] {
                    (uRow-1)*gridWidth + uCol,
                    (uRow+1)*gridWidth + uCol,
                    uRow*gridWidth + uCol + 1,
                    uRow*gridWidth + uCol - 1,
            };
        } else if (uRow == 0 && (uCol > 0) && (uCol < gridWidth - 1)) {
            neighbours = new int[] {
                    (uRow+1)*gridWidth + uCol,
                    uRow*gridWidth + uCol + 1,
                    uRow*gridWidth + uCol - 1,
            };
        } else if ((uRow == gridHeight - 1) && (uCol > 0) && (uCol < gridWidth - 1)) {
            neighbours = new int[] {
                    (uRow-1)*gridWidth + uCol,
                    uRow*gridWidth + uCol + 1,
                    uRow*gridWidth + uCol - 1,
            };
        } else if (uCol == 0 && (uRow > 0) && (uRow < gridHeight - 1)) {
            neighbours = new int[] {
                    (uRow-1)*gridWidth + uCol,
                    (uRow+1)*gridWidth + uCol,
                    uRow*gridWidth + uCol + 1,
            };
        } else if ((uCol == gridWidth - 1) && (uRow > 0) && (uRow < gridHeight - 1)) {
            neighbours = new int[] {
                    (uRow-1)*gridWidth + uCol,
                    (uRow+1)*gridWidth + uCol,
                    uRow*gridWidth + uCol - 1,
            };
        } else if (uCol == 0 && uRow == 0) {
            neighbours = new int[] {
                    (uRow+1)*gridWidth + uCol,
                    uRow*gridWidth + uCol + 1,
            };
        } else if (uCol == 0 && uRow == gridHeight-1) {
            neighbours = new int[] {
                    (uRow-1)*gridWidth + uCol,
                    uRow*gridWidth + uCol + 1,
            };
        } else if (uCol == gridWidth-1 && uRow == 0) {
            neighbours = new int[] {
                    (uRow+1)*gridWidth + uCol,
                    uRow*gridWidth + uCol - 1,
            };

        } else if ((uCol == gridWidth-1) && (uRow == gridHeight-1)) {
            neighbours = new int[] {
                    (uRow-1)*gridWidth + uCol,
                    uRow*gridWidth + uCol - 1,
            };
        }

        return neighbours;
    }

    private List<Integer> decreaseKey(List<Integer> H, int v, int distV) {
        H.set(v, distV);
        return H;
    }

    private int weight(int u, int v, int gridWidth, GridTile[][] grid) {
        int vRow = v / gridWidth;
        int vCol = v % gridWidth;

        return grid[vRow][vCol].weight;
    }

    private List<Integer> makeList(int[] dist, int gridWidth, int gridHeight) {
        int n = gridWidth * gridHeight;
        List<Integer> H = new ArrayList<Integer>();
        for (int i = 0; i < n; i++) {
            H.add(dist[i]);
        }
        return H;
    }

    private List<Integer> getNeighbours_DFS(int v, int gridWidth, GridView gridView) {
        int vRow = v / gridWidth;;
        int vCol = v % gridWidth;
        List<Integer> neighbours = new ArrayList<>();

        for (int i = 0; i < gridView.chosenPath.size(); i++) {
            if (i == v) continue;
            if (((gridView.chosenPath.get(i).row == vRow - 1) && (gridView.chosenPath.get(i).col == vCol)) ||
                    ((gridView.chosenPath.get(i).row == vRow + 1) && (gridView.chosenPath.get(i).col == vCol)) ||
                    ((gridView.chosenPath.get(i).col == vCol - 1) && (gridView.chosenPath.get(i).row == vRow)) ||
                    ((gridView.chosenPath.get(i).col == vCol + 1) && (gridView.chosenPath.get(i).row == vRow))) {
                int vertex = gridView.chosenPath.get(i).row*gridWidth + gridView.chosenPath.get(i).col;
                neighbours.add(vertex);
            }
        }

        return neighbours;

    }

    boolean chosenPathIsConnected(int gridHeight, int gridWidth, GridView gridView) {
        if (gridView.chosenPath.isEmpty()) return false;

        int amountDiscovered = 0;
        int n = gridHeight * gridWidth;

        boolean[] discovered = new boolean[n];
        Arrays.fill(discovered, false);

        int s = gridView.chosenPath.get(0).row*gridWidth + gridView.chosenPath.get(0).col;
        discovered[s] = true;

        Queue<Integer> Q = new LinkedList<>();
        Q.add(s);
        amountDiscovered++;

        while (!Q.isEmpty()) {
            int v = Q.remove();
            List<Integer> neighbours = getNeighbours_DFS(v, gridWidth, gridView);
            for (int i = 0; i < neighbours.size(); i++) {
                if (!discovered[neighbours.get(i)]) {
                    discovered[neighbours.get(i)] = true;
                    Q.add(neighbours.get(i));
                    amountDiscovered++;
                }
            }

        }

        if (amountDiscovered == gridView.chosenPath.size()) return true;
        return false;
    }

    boolean startAndEndIncluded(GridTile[][] grid, int startRow, int startCol, int endRow, int endCol) {
        if (grid[startRow][startCol].chosen && grid[endRow][endCol].chosen) return true;
        return false;
    }
}
