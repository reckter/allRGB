package me.reckter.Generation;

/**
 * Created with IntelliJ IDEA.
 * User: reckter
 * Date: 7/31/13
 * Time: 3:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class RandomColum extends BasicGeneration{

    @Override
    public void render() {
        boolean[] color;
        int random;

        color = new boolean[SIZE * SIZE];
        for(int x = 0; x < SIZE; x++) {
            for(int y = 0; y < SIZE; y++) {
                random = (int) (Math.random() * SIZE);
                while(color[x * SIZE + random]) {
                    random = (int) (Math.random() * SIZE);
                }
                color[x * SIZE + random] = true;
                pixel[x][y][R] = (short) ((x * SIZE + random) % 256);
                pixel[x][y][G] = (short) (((x * SIZE + random) / 256) % 256);
                pixel[x][y][B] = (short) (((x * SIZE + random) / 256 / 256));
            }
        }
    }
}
