package me.reckter.Generation;

import me.reckter.Generation.Utilities.Pixel;
import me.reckter.Util;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * User: reckter
 * Date: 7/31/13
 * Time: 12:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class PictureRandomSort extends BasicPicture {
    protected ArrayList<Integer[]>[] processPixel;
    protected volatile boolean[] threadIsFinished;

    protected static final float ERROR_ACEPTANCE = 0.5f;

    public PictureRandomSort(String file) {
        super(file);

        int cores = Runtime.getRuntime().availableProcessors();
        processPixel = new ArrayList[cores];

        for(int i = 0; i < processPixel.length; i++){
            processPixel[i] = new ArrayList<Integer[]>();
        }
        threadIsFinished = new boolean[cores];
    }


    @Override
    public void render(){
        randomizePixel();

        Util.c_log("getting Pixel to process");
        // ArrayList<Integer[]> pixelToProcess = getPixelToProcess();

        ArrayList<Pixel> pixelArrayList= new ArrayList<Pixel>();

        Util.c_log("creating pixel Array");

        for(int x = 0; x < SIZE; x++) {
            for(int y = 0; y < SIZE; y++) {
                pixelArrayList.add(new Pixel(pixel[x][y][R],pixel[x][y][G],pixel[x][y][B],  pixelShould[x][y][R],pixelShould[x][y][G],pixelShould[x][y][B]));
            }
        }
        Util.c_log("sorting ( this might take a looong time!)");
        Collections.sort(pixelArrayList);

        Util.c_log("repainting image");

        int x,y;
        Pixel singlePixel;
        for(int i = 0; i < pixelArrayList.size(); i++) {
            x = i % SIZE;
            y = i / SIZE;
            singlePixel = pixelArrayList.get(i);

            pixel[x][y][R] = (short) singlePixel.r;
            pixel[x][y][G] = (short) singlePixel.g;
            pixel[x][y][B] = (short) singlePixel.b;
        }

        /*
        Util.c_log("splitting up " + pixelToProcess.size() + "(of " + SIZE * SIZE + ") pixel to match cores");
        int cores = Runtime.getRuntime().availableProcessors();
        for(int core = 0; core < cores; core++){
            if(core == cores - 1){
                int i = 0;
                while(core * (pixelToProcess.size() / cores) + i < pixelToProcess.size()) {
                    processPixel[core].add(pixelToProcess.get(core * (pixelToProcess.size() / cores) + i));
                    i++;
                }
            } else {
                for(int i = 0; i < pixelToProcess.size() / cores; i++){
                    processPixel[core].add(pixelToProcess.get(core * (pixelToProcess.size() / cores) + i));
                }
            }
        }

        Util.c_log("Starting Threads to process pixel");
        Thread[] processors = new ProcessThread[cores];
        for(int i = 0; i < cores; i++) {
            processors[i] = new ProcessThread(i);
            processors[i].start();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        while(!AreThreadsFinished(processors)){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        Util.c_log("All Threads are finished.");
        pixelToProcess.clear();
        for(int core = 0; core < cores; core++){
            for(int i = 0; i < processPixel[core].size(); i++){
                pixelToProcess.add(processPixel[core].get(i));
            }
        }
        processors = null;
*/
    }

    /**
     * checks if all process Threads are finished
     * @return
     */
    private boolean AreThreadsFinished(Thread[] processors) {
        for(int i = 0; i < processors.length; i++) {
            if(processors[i].isAlive()) {
                   return false;
            }
        }
        return true;
    }


    /**
     * randomizes the pixel. Takes care of allRGB things
     */
    private void randomizePixel() {
        Util.c_log("randomizing Pixel");

        boolean[][] colors = new boolean[SIZE][SIZE];
        int x,y;
        int k = 0;
        short r,g,b;
        for( r = 0; r < 256; r++) {
            if((float) r / 256f * 100 > k + 10) {
                k += 10;
                Util.c_log(k + "%");
            }
            for( g = 0; g < 256; g++) {
                for( b = 0; b < 256; b++) {
                    x = (int) ( Math.random() * SIZE);
                    y = (int) ( Math.random() * SIZE);
                    while(colors[x][y]){
                        x = (int) ( Math.random() * SIZE);
                        y = (int) ( Math.random() * SIZE);
                    }
                    colors[x][y] = true;
                    pixel[x][y][R] = r;
                    pixel[x][y][G] = g;
                    pixel[x][y][B] = b;
                }
            }
        }
        Util.c_log("finished.");
    }

    /**
     * Compares if changing is better and returns true if so
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    private boolean isChangedBetter(int x1, int y1, int x2, int y2) {
        if( (getDifferences(x1, y1) - getSwitchedDifferences(x2, y2, x1, y1))  +  (getDifferences(x2, y2) - getSwitchedDifferences(x1, y1, x2, y2)) > 0 ){
            return true;
        }
        return false;
    }

    /**
     * returns the diffrence between pixel[x][y] and pixelshould[x][y]
     * @param x
     * @param y
     * @return
     */
    private float getDifferences(int x, int y) {
        return getSwitchedDifferences(x, y, x, y);
    }

    /**
     *  returns the diffrence between pixel[x1][y1] and pixelshould[x2][y2]
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    private float getSwitchedDifferences(int x1, int y1, int x2, int y2) {
        return ( ((float)pixel[x1][y1][R] - (float)pixelShould[x2][y2][R]) / 256 + ((float)pixel[x1][y1][G] - (float)pixelShould[x2][y2][G]) / 256 + ((float)pixel[x1][y1][B] - (float)pixelShould[x2][y2][B]) / 256) / 3;
    }


    /**
     * returns an ArrayList of all picel to process
     * @return
     */
    public ArrayList<Integer[]> getPixelToProcess() {
        ArrayList<Integer[]> ret = new ArrayList<Integer[]>();

        int numPixelToProxess = 0;
        for(int x = 0; x < SIZE; x++) {
            for(int y = 0; y < SIZE; y++) {
                if(getDifferences(x , y) >= ERROR_ACEPTANCE) {
                    Integer[] integ = new Integer[2];
                    integ[X] = (x);
                    integ[Y] = (y);

                    ret.add(integ);
                }
            }
        }

        return ret;
    }


    class ProcessThread extends Thread {
        private int id;
        public ProcessThread(int id) {
            this.id = id;
        }

        public void run(){
            threadIsFinished[id] = false;
            short r, g, b;
            int x1, y1, x2, y2;

            int k = 0;
            for(int i = 0; i < processPixel[id].size(); i++) {
                if((float) i / (float) (processPixel[id].size()) * 100 > k + 10) {
                    k += 10;
                    Util.c_log("Thread " + id + ": " + k + "%");
                }
                for(int j = i; j < processPixel[id].size(); j++) {
                    x1 = processPixel[id].get(i)[X];
                    y1 = processPixel[id].get(i)[Y];
                    x2 = processPixel[id].get(j)[X];
                    y2 = processPixel[id].get(j)[Y];

                    if(i != j && isChangedBetter(x1, y1, x2, y2) ){
                        r = pixel[x1][y1][R];
                        g = pixel[x1][y1][G];
                        b = pixel[x1][y1][B];

                        pixel[x1][y1][R] = pixel[x2][y2][R];
                        pixel[x1][y1][G] = pixel[x2][y2][G];
                        pixel[x1][y1][B] = pixel[x2][y2][B];

                        pixel[x2][y2][R] = r;
                        pixel[x2][y2][G] = g;
                        pixel[x2][y2][B] = b;
                    }
                }
            }
            Util.c_log("Thread " + id + " finished");
            threadIsFinished[id] = true;
        }

        /**
         * Compares if changing is better and returns true if so
         * @param x1
         * @param y1
         * @param x2
         * @param y2
         * @return
         */
        private boolean isChangedBetter(int x1, int y1, int x2, int y2) {
            if( (getDifferences(x1, y1) - getSwitchedDifferences(x2, y2, x1, y1))  +  (getDifferences(x2, y2) - getSwitchedDifferences(x1, y1, x2, y2)) > 0 ){
                return true;
            }
            return false;
        }

        /**
         * returns the diffrence between pixel[x][y] and pixelshould[x][y]
         * @param x
         * @param y
         * @return
         */
        private float getDifferences(int x, int y) {
            return getSwitchedDifferences(x, y, x, y);
        }

        /**
         *  returns the diffrence between pixel[x1][y1] and pixelshould[x2][y2]
         * @param x1
         * @param y1
         * @param x2
         * @param y2
         * @return
         */
        private float getSwitchedDifferences(int x1, int y1, int x2, int y2) {
            return ( ((float)pixel[x1][y1][R] - (float)pixelShould[x2][y2][R]) / 256 + ((float)pixel[x1][y1][G] - (float)pixelShould[x2][y2][G]) / 256 + ((float)pixel[x1][y1][B] - (float)pixelShould[x2][y2][B]) / 256) / 3;
        }
    }
}
