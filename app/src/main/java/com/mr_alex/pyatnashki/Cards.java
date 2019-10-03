package com.mr_alex.pyatnashki;

import android.app.Activity;

public class Cards {
    private int[][] board;
    private int n, m;

    public Cards(int N, int M) {
        n = N;
        m = M;
        board = new int[n][m];
    }

    private boolean isSolvable() {
        int rank = 0;
        int inversions = 0;
        for (int i = 0; i < m * n; i++) {
            int tile = board[i % n][i / m];
            if (tile == 0) {
                rank = i % n;
                continue;
            }
            for (int j = i; j < m * n; j++) {
                if (board[i % n][i / m] > tile) {
                    inversions++;
                }
            }
        }
        if (n % 2 == 0) {
            return ((rank + inversions) % 2 == 0);
        } else {
            return inversions % 2 == 0;
        }
    }

    public void getNewCards() {
        board = new int [n][m];
        int boardX, boardY;
        for (int i = 0; i < (n * m); i++) {
            boardX = (int)(Math.random() * n);
            boardY = (int)(Math.random() * m);
            while(!(board[boardX][boardY] == 0)) {
                boardX = (int)(Math.random() * n);
                boardY = (int)(Math.random() * m);
            }
            board[boardX][boardY] = i;
        }
        while (!isSolvable()) {
            getNewCards();
        }
    }

    private boolean result;
    public void moveCards(int boardX, int boardY){
        int X0 = -1, Y0 = -1;
        for(int i = 0; i < n; i++)
            for(int j = 0; j < m; j++)
                if (board[i][j] == 0) {
                    X0 = i;
                    Y0 = j;
                }

        result = false;
        if (X0 == boardX || Y0 == boardY)
            if (!(X0 == boardX && Y0 == boardY)) {
                if (X0 == boardX)
                    if (Y0 < boardY)
                        for (int i = Y0 + 1; i <= boardY; i++)
                            board[boardX][i - 1] = board[boardX][i];
                    else
                        for (int i = Y0; i > boardY; i--)
                            board[boardX][i] = board[boardX][i - 1];

                if (Y0 == boardY)
                    if (X0 < boardX)
                        for (int i = X0 + 1; i <= boardX; i++)
                            board[i-1][boardY] = board[i][boardY];
                    else
                        for (int i = X0; i > boardX; i--)
                            board[i][boardY] = board[i - 1][boardY];

                board[boardX][boardY] = 0;
                result = true;
            }
    }

    public boolean resultMove() {
        return result;
    }

    public boolean finished(int N, int M){
        boolean finish = false;
        if (board[N - 1][M - 1] == 0) {
            int a = 0;
            int b = 1;
            for (int i = 0; i < N; i++)
                for (int j = 0; j < M; j++) {
                    a++;
                    if (board[i][j] == a) b++;
                }
            if (b == (N * M)) finish = true;
        }
        return finish;
    }

    public int getValueBoard(int i, int j) {
        return board[i][j];
    }

    public void setValueBoard(int i, int j, int value) {
        board[i][j] = value;
    }
}

