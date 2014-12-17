package me.reckter.Generation.Picture;

import me.reckter.Util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by reckter on 17.12.2014.
 */
public class PictureRandomBiasedSort extends PictureRandomSort {
    public PictureRandomBiasedSort(String file) {
        super(file);
    }


    @Override
    public void render(){
        randomizePixel();
        Util.c_log("repainting image");

        int k = 0;
        for(int i = 0; i < SIZE; i++) {

            Util.c_log( (((float) i / (float)SIZE) * 100f) + "%: " + calculateFittnes());
            for(int j = 0; j < SIZE; j++) {

                List<int[]> pixelToSwitch = Stream.iterate(0, n -> n + 1).limit(SIZE * SIZE).parallel().map(n -> {
                    int ret[]  =  {n % SIZE, (int) n / SIZE};
                    return ret;
                }).sorted((a, b) -> {
                    int value = getDifferences(b[0],b[1]) - getDifferences(a[0], a[1]);
                    if(value > 0 ) {
                        return 1;
                    } else if(value == 0) {
                        return  0;
                    } else {
                        return  -1;
                    }
                }).limit(SIZE).collect(Collectors.toList());

                List<int[]> firsthalf = pixelToSwitch.subList(0, pixelToSwitch.size() / 2);
                pixelToSwitch = pixelToSwitch.subList(firsthalf.size(), pixelToSwitch.size());
                final List<int[]> finalPixelToSwitch = pixelToSwitch;

                final int[] counter = {0};
                firsthalf.stream().sequential().map(a -> {
                    int[] b = finalPixelToSwitch.get(counter[0]++);
                    int ret[] = {a[0],a[1], b[0], b[1]};
                    return ret;
                }).parallel().forEach(point -> {
                    if (isChangedBetter(point[0], point[1], point[2], point[3])) {

                        byte tmpR = pixel[point[0]][point[1]][R];
                        byte tmpG = pixel[point[0]][point[1]][G];
                        byte tmpB = pixel[point[0]][point[1]][B];

                        pixel[point[0]][point[1]][R] = pixel[point[2]][point[3]][R];
                        pixel[point[0]][point[1]][G] = pixel[point[2]][point[3]][G];
                        pixel[point[0]][point[1]][B] = pixel[point[2]][point[3]][B];

                        pixel[point[2]][point[3]][R] = tmpR;
                        pixel[point[2]][point[3]][G] = tmpG;
                        pixel[point[2]][point[3]][B] = tmpB;
                    }
                });
            }
        }
    }
}
