package de.qbyte.imagecompare;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class ImageOutputComposite extends Composite {

	/* ***** PROPERTIES ***** */

	private final Button	compare;
	private final Canvas	canvas;
	private Image			img;
	ImageInputComposite		comp1;
	ImageInputComposite		comp2;

	/* ***** CONSTRUCTORS ***** */

	public ImageOutputComposite(Shell parent, ImageInputComposite input1, ImageInputComposite input2) {

		// create container
		super(parent, SWT.NONE);
		comp1 = input1;
		comp2 = input2;

		// set layout
		setLayout(new FillLayout());

		// set layout data
		GridData containerData = new GridData();
		containerData.horizontalAlignment = GridData.FILL;
		containerData.grabExcessHorizontalSpace = true;
		containerData.horizontalSpan = 2;
		containerData.verticalAlignment = GridData.FILL;
		containerData.grabExcessVerticalSpace = true;
		containerData.verticalSpan = 1;
		setLayoutData(containerData);

		// create image canvas
		canvas = new Canvas(this, SWT.BORDER);
		canvas.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				if (img != null)
					e.gc.drawImage(img, 0, 0);
			}
		});

		// register mouseover listener
		canvas.addListener(SWT.MouseEnter, new Listener() {
			@Override
			public void handleEvent(Event event) {
				compare.setVisible(true);
			}
		});
		canvas.addListener(SWT.MouseExit, new Listener() {
			@Override
			public void handleEvent(Event event) {
				compare.setVisible(false);
			}
		});

		// create compare button
		compare = new Button(canvas, SWT.PUSH);
		compare.setText("Compare");
		compare.setBounds(10, 10, 100, 30);
		compare.setVisible(false);
		
		// compare images
		compare.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {

				ImageData imgData1 = comp1.img.getImageData();
				ImageData imgData2 = comp2.img.getImageData();
				ImageData imgData3 = new ImageData(imgData1.width, imgData1.height, imgData1.depth, imgData1.palette);

				System.out.println(imgData1.depth + ":" + imgData2.depth);

				// byte[] data1 = imgData1.data;
				// byte[] data2 = imgData2.data;
				//
				// // long maxLength = Math.max(data1.length, data2.length);
				// byte[] data3 = new byte[data1.length];

				for (int x = 0; x < imgData1.width; x++) {
					for (int y = 0; y < imgData1.height; y++) {
						int val1 = imgData1.getPixel(x, y);
						int val2 = imgData2.getPixel(x, y);

						int r1 = val1 & 0xFF;
						int r2 = val2 & 0xFF;
						int g1 = (val1 & 0xFF00) >> 8;
						int g2 = (val2 & 0xFF00) >> 8;
						int b1 = (val1 & 0xFF0000) >> 16;
						int b2 = (val2 & 0xFF0000) >> 16;

						int r = Math.abs(r1 - r2);
						int g = Math.abs(g1 - g2);
						int b = Math.abs(b1 - b2);

						int distance = (int) ((r + b + g) / (3.0d * 0xFF) * 0xFF);

						int val3 = (distance & 0xFF) << 8 | ((distance & 0xFF) << 16) | ((distance & 0xFF) << 24);
						imgData3.setPixel(x, y, val3);
					}

				}

				// for (int i = 0; i < data1.length; i += 3) {
				// int r = Math.abs(data1[i + 0] - data2[i + 0]);
				// int g = Math.abs(data1[i + 1] - data2[i + 1]);
				// int b = Math.abs(data1[i + 2] - data2[i + 2]);
				//
				// int distance = (int) ((r + b + g) / (3.0d * 255) * 255);
				// // System.out.println(r + ":" + g + ":" + b + ":" + distance);
				//
				// data3[i + 0] = (byte) distance;
				// data3[i + 1] = (byte) distance;
				// data3[i + 2] = (byte) distance;
				// }

				// ImageData imgData = new ImageData(imgData1.width, imgData1.height, imgData1.depth, imgData1.palette,
				// 1,
				// data3);
				img = new Image(getDisplay(), imgData3);
				canvas.redraw();
			}
		});

		// trick to stay visible on entering button and leaving canvas
		compare.addListener(SWT.MouseEnter, new Listener() {
			@Override
			public void handleEvent(Event event) {
				compare.setVisible(true);
			}
		});
	}

}
