package me.reckter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: mediacenter
 * Date: 09.07.13
 * Time: 14:09
 * To change this template use File | Settings | File Templates.
 */
public class ImageConvertion extends JPanel {

	public static final int CANVAS_WIDTH = 255;
	public static final int CANVAS_HEIGHT = 255;
	public static final int FACTOR = 255;
	public static final String TITLE = "...Title...";


	protected static final short START_SIZE_TILE = 16;
	protected static final short SIZE = 4096;
	protected static final float MIN_PROCENT = 0.3f;

	protected static final int R = 0;
	protected static final int G = 1;
	protected static final int B = 2;

	protected static final int X = 0;
	protected static final int Y = 1;

	private short[][][] pixel;
	private short[][][] pixelShould;
	Graphics g;

	public ImageConvertion() {

		try {
			Thread.sleep(20 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
		Thread showThread = new Thread(new showThread());
		showThread.start();


		Util.c_log("getting pixel Integer array");
		pixelShould = new short[SIZE][SIZE][3];

		readImage();

		pixel = new short[SIZE][SIZE][3];


		Util.c_log("getting image resources");
		BufferedImage bi = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
		g = bi.getGraphics();

		Util.c_log("started processing...");
		processImage();

		Util.c_log("drawing it.");
		for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++) {
				g.setColor(new Color(pixel[x][y][R], pixel[x][y][G], pixel[x][y][B]));
				g.drawLine(x, y, x, y);
			}
		}

		try {
			ImageIO.write(bi, "PNG", new File("out.png"));
		} catch (IOException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
	}


	private void readImage() {
		Util.c_log("reading in.png ...");

		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("in.png"));
		} catch (IOException e) {
		}
		int[] result = new int[SIZE * SIZE * 3 + 1];

		img.getData().getPixels(0, 0, SIZE, SIZE, result);
		short k = 0;
		for (int i = 1; i < SIZE * SIZE; i++) {
			pixelShould[i % SIZE][(int) ((float) i / (float) SIZE)][R] = (short) result[3 * i + R];
			pixelShould[i % SIZE][(int) ((float) i / (float) SIZE)][G] = (short) result[3 * i + G];
			pixelShould[i % SIZE][(int) ((float) i / (float) SIZE)][B] = (short) result[3 * i + B];
			if ((float) i / (float) (SIZE * SIZE) * 100 > k + 10) {
				k += 10;
				Util.c_log(k + "%");
			}
		}

		img = null;
		Util.c_log("finished.");
	}


	private boolean[][][] getAllColors() {
		boolean[][][] ret = new boolean[256][256][256];
		for (int i = 0; i < 256; i++) {
			for (int j = 0; j < 256; j++) {
				for (int k = 0; k < 256; k++) {
					ret[i][j][k] = false;
				}
			}
		}
		return ret;
	}

	private void randomizePixel() {
		Util.c_log("randomizing Pixel");
		int random, r, g, b;

		ArrayList<Integer> colors = new ArrayList<Integer>();
		for (int i = 0; i <= SIZE * SIZE; i++) {
			colors.add(i);
		}

		int k = 0;
		for (int x = 0; x < SIZE; x++) {
			if ((float) x / (float) (SIZE) * 100 > k + 10) {
				k += 10;
				Util.c_log(k + "%");
			}
			for (int y = 0; y < SIZE; y++) {
				random = (int) Math.random() * colors.size();
				r = colors.get(random) % 256;
				g = (int) (((float) colors.get(random) / 256) % 256);
				b = (int) ((float) colors.get(random) / (256f * 256f));
				colors.remove(random);

				pixel[x][y][R] = (short) r;
				pixel[x][y][G] = (short) g;
				pixel[x][y][B] = (short) b;
			}
		}
		Util.c_log("finished.");
	}

	private float getDifferences(int x, int y) {
		return getSwitchedDifferences(x, y, x, y);
	}


	private float getSwitchedDifferences(int x1, int y1, int x2, int y2) {
		return ((pixel[x1][y1][0] - pixelShould[x2][y2][0]) / 256 + (pixel[x1][y1][1] - pixelShould[x2][y2][1]) / 256 + (pixel[x1][y1][2] - pixelShould[x2][y2][2]) / 256);
	}


	public ArrayList<Integer[]> getPixelToProcess(short x0, short y0, int size) {
		ArrayList<Integer[]> ret = new ArrayList<Integer[]>();

		int numPixelToProxess = 0;
		for (short x = 0; x < size; x++) {
			for (short y = 0; y < size; y++) {
				if (getDifferences(x0 + x, y0 + y) > 0.3f) {
					Integer[] integ = new Integer[2];
					integ[X] = (x0 + x);
					integ[Y] = (y0 + y);

					ret.add(integ);
				}
			}
		}

		return ret;
	}

	public void processImage() {
		randomizePixel();
		short currentTileSize = START_SIZE_TILE;

		Util.c_log("started Tile rendering.");
		while (currentTileSize <= SIZE) {
			Util.c_log("Tilesize: " + currentTileSize);
			for (int x = 0; x < SIZE / currentTileSize; x++) {
				for (int y = 0; y < SIZE / currentTileSize; y++) {
					processImageTile((short) (x * currentTileSize), (short) (y * currentTileSize), currentTileSize);
				}
			}

			currentTileSize *= 2;
		}
	}

	public void processImageTile(short x, short y, short size) {
		if (x + size > SIZE) {
			size = (short) (SIZE - x);
		}
		if (y + size > SIZE) {
			size = (short) (SIZE - y);
		}

		ArrayList<Integer[]> processPixel = getPixelToProcess(x, y, size);

		for (Integer[] aPixel : processPixel) {
			for (Integer[] bPixel : processPixel) {
				if (getSwitchedDifferences(aPixel[X], aPixel[Y], bPixel[X], bPixel[Y]) < getDifferences(aPixel[X], aPixel[Y])
						&& getSwitchedDifferences(bPixel[X], bPixel[Y], aPixel[X], aPixel[Y]) < getDifferences(bPixel[X], bPixel[Y])
						) { //TODO more switch cases (?)
					short r = pixel[aPixel[X]][aPixel[Y]][R];
					short g = pixel[aPixel[X]][aPixel[Y]][G];
					short b = pixel[aPixel[X]][aPixel[Y]][B];

					pixel[aPixel[X]][aPixel[Y]][R] = pixel[bPixel[X]][bPixel[Y]][R];
					pixel[aPixel[X]][aPixel[Y]][G] = pixel[bPixel[X]][bPixel[Y]][G];
					pixel[aPixel[X]][aPixel[Y]][B] = pixel[bPixel[X]][bPixel[Y]][B];

					pixel[bPixel[X]][bPixel[Y]][R] = r;
					pixel[bPixel[X]][bPixel[Y]][G] = g;
					pixel[bPixel[X]][bPixel[Y]][B] = b;
				}
			}
		}

	}

	protected class showThread extends JPanel implements Runnable {
		@Override

		public void run() {
			Util.c_log("started ShowThread");
			JFrame frame = new JFrame(TITLE);
			// frame.setContentPane(new SwingTemplateJPanel());
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.pack();             // "this" JFrame packs its components
			// frame.setLocationRelativeTo(null); // center the application window
			frame.setSize(CANVAS_HEIGHT, CANVAS_WIDTH);
			frame.setVisible(true);
		}


		public void paintComponent(Graphics g) {
			super.paintComponent(g);  // paint background
			setBackground(Color.WHITE);


			for (int x = 0; x < CANVAS_WIDTH; x++) {
				for (int y = 0; y < CANVAS_HEIGHT; y++) {
					g.setColor(new Color(pixel[x * FACTOR][y * FACTOR][0], pixel[x * FACTOR][y * FACTOR][1], pixel[x * FACTOR][y * FACTOR][2]));
					g.drawLine(x, y, x, y);
				}
			}
			// Your custom painting codes
			// ......
		}
	}
}
