package me.reckter.Generation.Picture;


import me.reckter.Log;
import me.reckter.Util;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: reckter
 * Date: 7/31/13
 * Time: 2:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class PictureAverage extends BasicPicture {
	protected boolean[][][] colors;
	protected int[][][] analyzedColors;

	protected Thread showGraph;

	public PictureAverage(String file) {
		super(file);

		colors = new boolean[COLORS][COLORS][COLORS];
		analyzedColors = new int[COLORS][COLORS][COLORS];
		showGraph = new Thread(new ShowGraph());
		showGraph.start();

	}

	@Override
	public void render() {
		Log.info("started analyzing picture...");
		int r, g, b;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				r = pixelShould[y][x][R];
				g = pixelShould[y][x][G];
				b = pixelShould[y][x][B];

				r = r >= 0 ? r : 127 - r;
				g = g >= 0 ? g : 127 - g;
				b = b >= 0 ? b : 127 - b;

				analyzedColors[r][g][b]++;
			}
		}
		Log.info("finished analyzing");

	}


	public class ShowGraph extends JPanel implements Runnable {


		public void run() {
			Util.c_log("started ShowGraph");
			JFrame frame = new JFrame("allRGBGraphs");
			// frame.setContentPane(new SwingTemplateJPanel());
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.pack();             // "this" JFrame packs its components
			// frame.setLocationRelativeTo(null); // center the application window
			frame.setSize(CANVAS_SIZE, CANVAS_SIZE);
			frame.setVisible(true);
			paintComponent(frame.getGraphics());
		}


		public void paintComponent(Graphics graphics) {
			setBackground(Color.black);
			super.paintComponent(graphics);  // paint background

			int r, g, b;

			graphics.setColor(Color.white);

			int lastMax = 1000;
			int currentMax;

			while (true) {
				currentMax = lastMax;
				lastMax = 0;
				graphics.setColor(Color.black);
				graphics.clearRect(0, 0, CANVAS_SIZE, CANVAS_SIZE);

				graphics.setColor(Color.black);
				try {
					Thread.sleep(0);
				} catch (InterruptedException e) {
					e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
				}
				for (int x = 0; x < CANVAS_SIZE - 1; x++) {
					if (analyzedColors[(int) ((float) x / (COLORS * COLORS))][(int) (((float) x / COLORS) % COLORS)][x % COLORS] > lastMax) {
						lastMax = analyzedColors[(int) ((float) x / (COLORS * COLORS))][(int) (((float) x / COLORS) % COLORS)][x % COLORS];
					}
					graphics.drawLine(x, (int) (CANVAS_SIZE * currentMax / (float) analyzedColors[(int) ((float) x / (COLORS * COLORS))][(int) (((float) x / COLORS) % COLORS)][x % COLORS]),
							x + 1, (int) (CANVAS_SIZE * currentMax / (float) analyzedColors[(int) ((float) (x + 1) / (COLORS * COLORS))][(int) (((float) (x + 1) / COLORS) % COLORS)][(x + 1) % COLORS]));
				}
			}
		}
	}


}
