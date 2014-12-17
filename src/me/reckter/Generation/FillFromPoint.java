package me.reckter.Generation;

import me.reckter.Log;

import java.util.LinkedList;

/**
 * Created by reckter on 1/19/14.
 */
public class FillFromPoint extends BasicGeneration {

    LinkedList<int[]> pointsToCheck;

    boolean[][] isInList;

    int number;

    public void render(){
        isInList = new boolean[SIZE][SIZE];
        pointsToCheck = new LinkedList<int[]>();

        int[] start = {SIZE / 2, SIZE / 2};

        int bigStep = 13;
        int smallStep = 11;

        pointsToCheck.add(start);

        number = 0;
        while(pointsToCheck.size() > 0){
            int[] pointToCheck = pointsToCheck.pollFirst();

            //pointsToCheck.remove(0);
            //pointsChecked.put(pointToCheck[0] + "|" + pointToCheck[1], 1);

            pixel[pointToCheck[0]][pointToCheck[1]] = Gray.to_gray(256, 3, ++number);

            /*
            pixel[pointToCheck[0]][pointToCheck[1]][R] = (byte) (++number % 256);
            pixel[pointToCheck[0]][pointToCheck[1]][G] = (byte) ((number / 256) % 256);
            pixel[pointToCheck[0]][pointToCheck[1]][B] = (byte) (number / 256 / 256);
            */



            addPointToCheck(pointToCheck[0] + smallStep, pointToCheck[1] + smallStep);
            addPointToCheck(pointToCheck[0] + bigStep, pointToCheck[1] + 0);
            addPointToCheck(pointToCheck[0] + smallStep, pointToCheck[1] - smallStep);

            addPointToCheck(pointToCheck[0] + 0, pointToCheck[1] + bigStep);
            //addPointToCheck(pointToCheck[0] + 0, pointToCheck[1] + 0);  //not needed ^^
            addPointToCheck(pointToCheck[0] + 0, pointToCheck[1] - bigStep);

            addPointToCheck(pointToCheck[0] - smallStep, pointToCheck[1] + smallStep);
            addPointToCheck(pointToCheck[0] - bigStep, pointToCheck[1] + 0);
            addPointToCheck(pointToCheck[0] - smallStep, pointToCheck[1] - smallStep);
        }
        if(number == SIZE * SIZE + 1){
            Log.info("distributed all colors! ");
        } else {
            Log.error("not all colors distributed! " + number + "& " + SIZE * SIZE);
        }
    }

    protected void addPointToCheck(int x, int y){
        int[] point = {x,y};
        if(x < 0 || x >= SIZE || y < 0 || y >= SIZE){
            return;
        }
        if(!isInList[x][y]){
            pointsToCheck.add(point);
            isInList[x][y] = true;
        }
    }
}
