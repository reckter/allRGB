package me.reckter.Generation.Picture;

import com.sun.istack.internal.NotNull;
import me.reckter.Generation.Utilities.Color;
import me.reckter.Generation.Utilities.Coordinates;
import me.reckter.Log;
import me.reckter.UtilSort;

import java.util.*;

/**
 * Created by hannes on 23/12/14.
 */
public class ColorSort extends BasicPicture{

    public ColorSort(String file) {
        super(file);
    }


    public void render() {

        int k = 0;
        int times = 0;
        Map<String, ColorPixels> analysedPixel = new HashMap<>(COLORS * COLORS * COLORS);
        List<Color> colors = new ArrayList<>(COLORS * COLORS * COLORS);

        Log.info("creating analysed array");
        k = 0;
        times = 0;
        for(int r = 0; r < COLORS; r++) {
            k++;

            if(k == 10) {
                k = 0;
                times += 10;
                Log.info("[" + times + "/" + COLORS + "]");
            }

            for(int g = 0; g < COLORS; g++) {
                for(int b = 0; b < COLORS; b++) {
                    Color color = new Color(r,g,b);
                    colors.add(color);
                    analysedPixel.put((byte) r + "_" + (byte) g + "_" +(byte)  b, new ColorPixels(color));
                }
            }
        }

        Log.info("analysing picture");
        k = 0;
        times = 0;
        for(int x = 0; x < SIZE; x++) {
            k++;
            if(k == 100) {
                k = 0;
                times += 100;
                Log.info("[" + times + "/" + SIZE + "]");
            }
            for(int y = 0; y < SIZE; y++) {
                analysedPixel.get(pixelShould[x][y][R] + "_" + pixelShould[x][y][G] + "_" + pixelShould[x][y][B]).add(new Coordinates(x, y));
            }
        }


        Log.info("sorting pixels");

        colors.removeIf(color -> analysedPixel.get(color.r + "_" + color.g + "_" + color.b).size() > 0);
        List<ColorPixels> pixelList = new ArrayList<>(analysedPixel.values());
        pixelList.sort(ColorPixels::compareTo);



        Log.info("distribute colors");

        k = 0;
        times = 0;

        int colorsSorted = 0;
        int count = 0;
        float sumFactor = 8;

        float pixelPerMinute = 1;
        int pixelPerMinuteCounter = 0;
        long startMinute = System.currentTimeMillis();


        for(ColorPixels colorPixel: pixelList) {
            k++;
            if (k == 100) {
                k = 0;
                times += 100;
                if (colorPixel.size() > 0) {
                    Log.info("[" + times + "/" + SIZE * SIZE + "] size: " + colorPixel.size());
                }
            }

            count++;

            if (colorPixel.size() <= 1) {
                continue;
            }

            int colorSize = colorPixel.size();


            Log.info("[" + count + "][" + colorsSorted + "/" + (4096 * 4096) + "]: sorting: " + colorSize + " of " + colors.size());


            UtilSort.sortFirst(colorSize, colors, (c1, c2) -> {
                double value = c1.distanceSquared(colorPixel.color) - c2.distanceSquared(colorPixel.color);

                return value > 0 ? 1 : (value == 0 ? 0 : -1);
            });

            Log.info("[" + pixelPerMinute+ "px/m][" + (colors.size() / pixelPerMinute) + "m]: distributing " + colorPixel.size());
            int i;

            for (i = 0; i < colors.size(); i++) {

                Color color = colors.get(i);

                if (colorPixel.size() <= 1) {
                    break;
                }

                String colorString = color.r + "_" + color.g + "_" + color.b;
                if (analysedPixel.get(colorString).size() == 0) {

                    analysedPixel.get(colorString).add(colorPixel.get(0));

                    Coordinates coordinates = colorPixel.get(0);
                    pixel[coordinates.getX()][coordinates.getY()][R] = colorPixel.color.r;
                    pixel[coordinates.getX()][coordinates.getY()][G] = colorPixel.color.g;
                    pixel[coordinates.getX()][coordinates.getY()][B] = colorPixel.color.b;

                    colorPixel.remove(0);
                } else {
                    Log.error("found allready filled color!");
                }
            }

            colors = colors.subList(colorSize, colors.size());
            colorsSorted += colorSize;

            pixelPerMinuteCounter += colorSize;
            if(System.currentTimeMillis() - startMinute > 60 * 1000) {
                pixelPerMinute = (float) pixelPerMinuteCounter / ((float) (System.currentTimeMillis() - startMinute) / (60f * 1000f));
                pixelPerMinuteCounter = 0;
                startMinute = System.currentTimeMillis();
            }

            Coordinates coordinates = colorPixel.get(0);
            pixel[coordinates.getX()][coordinates.getY()][R] = colorPixel.color.r;
            pixel[coordinates.getX()][coordinates.getY()][G] = colorPixel.color.g;
            pixel[coordinates.getX()][coordinates.getY()][B] = colorPixel.color.b;
        }

        Log.info("painting ");
        for(ColorPixels colorPixel: pixelList) {
            Coordinates coordinates = colorPixel.get(0);

            pixel[coordinates.getX()][coordinates.getY()][R] = colorPixel.color.r;
            pixel[coordinates.getX()][coordinates.getY()][G] = colorPixel.color.g;
            pixel[coordinates.getX()][coordinates.getY()][B] = colorPixel.color.b;
        }

        Log.info("done.");

    }



    private class ColorPixels implements List<Coordinates>, Comparable<ColorPixels> {
        Color color;
        ArrayList<Coordinates> coordinates;

        public ColorPixels(Color color) {
            this.color = color;
            coordinates = new ArrayList<>();
        }

        @Override
        public int size() {
            return coordinates.size();
        }

        @Override
        public boolean isEmpty() {
            return coordinates.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            return coordinates.contains(o);
        }

        @Override
        public Iterator<Coordinates> iterator() {
            return coordinates.iterator();
        }

        @Override
        public Object[] toArray() {
            return coordinates.toArray();
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return coordinates.toArray(a);
        }

        @Override
        public boolean add(Coordinates coordinates) {
            return this.coordinates.add(coordinates);
        }

        @Override
        public boolean remove(Object o) {
            return coordinates.remove(o);
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return coordinates.containsAll(c);
        }

        @Override
        public boolean addAll(Collection<? extends Coordinates> c) {
            return coordinates.addAll(c);
        }

        @Override
        public boolean addAll(int index, Collection<? extends Coordinates> c) {
            return coordinates.addAll(c);
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return coordinates.removeAll(c);
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return coordinates.retainAll(c);
        }

        @Override
        public void clear() {
            coordinates.clear();
        }

        @Override
        public Coordinates get(int index) {
            return coordinates.get(index);
        }

        @Override
        public Coordinates set(int index, Coordinates element) {
            return coordinates.set(index,element);
        }

        @Override
        public void add(int index, Coordinates element) {
            coordinates.add(index,element);
        }

        @Override
        public Coordinates remove(int index) {
            return coordinates.remove(index);
        }

        @Override
        public int indexOf(Object o) {
            return coordinates.indexOf(o);
        }

        @Override
        public int lastIndexOf(Object o) {
            return coordinates.lastIndexOf(o);
        }

        @Override
        public ListIterator<Coordinates> listIterator() {
            return coordinates.listIterator();
        }

        @Override
        public ListIterator<Coordinates> listIterator(int index) {
            return coordinates.listIterator(index);
        }

        @Override
        public List<Coordinates> subList(int fromIndex, int toIndex) {
            return coordinates.subList(fromIndex,toIndex);
        }

        @Override
        public int compareTo(ColorPixels o) {
            return Integer.compare(o.size(), this.size());
        }
    }
}
