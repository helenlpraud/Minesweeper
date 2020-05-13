package com.javarush.games.minesweeper;
import com.javarush.engine.cell.*;

public class GameObject {
    public boolean isMine;
    public int x,y, countMineNeighbors;
    public boolean isOpen;
    public boolean isFlag;
    public GameObject(int x, int y, boolean isMine){
        this.x = x;
        this.y = y;
        this.isMine = isMine;
    }
}