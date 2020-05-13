package com.javarush.games.minesweeper;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;

import java.util.ArrayList;
import java.util.List;


public class MinesweeperGame extends Game {
    private static final int SIDE = 9;
    private GameObject[][] gameField = new GameObject[SIDE][SIDE];
    private int countMinesOnField;
    private static final String MINE="(>_<)";
    private static final String FLAG="(⊙_⊙)";
    private static final String MessageStart =
            "Щёлкни по экрану, чтобы начать";
    private static final String MessageDialog =
    "Ты проиграл, но следующая попытка обязательно приведёт к успеху";
    private static final String MessageWin =
    "Ты выиграл, ура!";
    private int countFlags;
    private boolean isGameStopped;
    private int countClosedTiles=SIDE*SIDE;
    private int score;
    private int step;

    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
    }

    private void createGame() {
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                setCellValue(y, x, "");
                boolean isMine = getRandomNumber(7) < 1;
                if (isMine) {
                    countMinesOnField++;
                }
                gameField[y][x] = new GameObject(x, y, isMine);
                setCellColor(x, y, Color.PALEGOLDENROD);
                countFlags = countMinesOnField;
            }
            
        }
        countMineNeighbors();
    }
    
    private void gameOver(){
        isGameStopped = true;
        showMessageDialog(Color.ANTIQUEWHITE, MessageDialog, Color.BLACK, 14);
    }
    
    private void win(){
        isGameStopped = true;
        showMessageDialog(Color.ANTIQUEWHITE, MessageWin, Color.BLACK, 14);
    }
    
    private void restart(){
        isGameStopped = false;
        countClosedTiles = SIDE*SIDE;
        score = 0;
        step = 0;
        setScore(score);
        countMinesOnField = 0;
        createGame();
        
    }

    private List<GameObject> getNeighbors(GameObject gameObject) {
        List<GameObject> result = new ArrayList<>();
        for (int y = gameObject.y - 1; y <= gameObject.y + 1; y++) {
            for (int x = gameObject.x - 1; x <= gameObject.x + 1; x++) {
                if (y < 0 || y >= SIDE) {
                    continue;
                }
                if (x < 0 || x >= SIDE) {
                    continue;
                }
                if (gameField[y][x] == gameObject) {
                    continue;
                }
                result.add(gameField[y][x]);
            }
        }
        return result;
    }
    private void countMineNeighbors(){
        for (int y = 0; y < SIDE; y++){
            for (int x = 0; x < SIDE; x++){
                if(!gameField[y][x].isMine){
                    List<GameObject> result = new ArrayList<>();
                    result=getNeighbors(gameField[y][x]);
                    for (GameObject o:result){
                        if (o.isMine){
                            gameField[y][x].countMineNeighbors++;
                        }
                    }
                }
            }
        }
    }
    private void openTile(int y, int x){
        if(gameField[y][x].isOpen || gameField[y][x].isFlag || isGameStopped)
        {
        }
        
        else{
            gameField[y][x].isOpen=true;
            if(!gameField[y][x].isMine){
                score = score +5;
                step++;
                setScore(score);
            }
            countClosedTiles--;
        
            if(gameField[y][x].isMine){
                if(step==0){
                    restart();
                }
                else {
                    setCellValueEx(y, x, Color.DEEPSKYBLUE, MINE);
                    gameOver();
                }
            }
        
            else
            {
                setCellColor(y, x, Color.MEDIUMSEAGREEN);
                if (gameField[y][x].countMineNeighbors==0)
                {
                setCellValue(y, x,"");
                
                List<GameObject> result = new ArrayList<>();
                result=getNeighbors(gameField[y][x]);
                    for (GameObject o:result)
                    {
                        if (!o.isOpen)
                        {
                        openTile(o.y,o.x);
                        setCellColor(o.y, o.x, Color.MEDIUMSEAGREEN);
                        }
                    }
                }
                else
                {
                    setCellNumber(y, x, gameField[y][x].countMineNeighbors);
                }
            }
        }
        if(countClosedTiles == countMinesOnField&& !gameField[y][x].isMine){
            win();
        }
    }
    private void markTile(int y, int x){
        if (isGameStopped){
            
        }
        else if (gameField[y][x].isOpen){
            
        }
        else if(countFlags ==0&& !gameField[y][x].isFlag){
            
        }
        else{
            if(!gameField[y][x].isFlag){
                gameField[y][x].isFlag=true;
                countFlags--;
                setCellValue(y, x, FLAG);
                setCellColor(y, x, Color.GOLD);
            }
            else{
                gameField[y][x].isFlag=false;
                countFlags++;
                setCellValue(y, x, "");
                setCellColor(y, x, Color.PALEGOLDENROD);
            }
        }
    }
    
    public void onMouseLeftClick(int y, int x){
        if (isGameStopped){
        restart();
        }
        else{
        openTile(y, x);
        }
    }
    public void onMouseRightClick(int y, int x){
        markTile(y, x);
    }
}