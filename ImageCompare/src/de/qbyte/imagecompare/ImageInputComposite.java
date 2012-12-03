package de.qbyte.imagecompare;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class ImageInputComposite extends Composite {

	/* ***** PROPERTIES ***** */

	private final Shell		shell;
	private final Button	load;
	public final Canvas		canvas;
	public Image			img;

	/* ***** CONSTRUCTORS ***** */

	public ImageInputComposite(Shell parent) {

		// create container
		super(parent, SWT.NONE);
		shell = parent;

		// set layout
		setLayout(new FillLayout());

		// set layout data
		GridData containerData = new GridData();
		containerData.horizontalAlignment = GridData.FILL;
		containerData.grabExcessHorizontalSpace = true;
		containerData.horizontalSpan = 1;
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
				load.setVisible(true);
			}
		});
		canvas.addListener(SWT.MouseExit, new Listener() {
			@Override
			public void handleEvent(Event event) {
				load.setVisible(false);
			}
		});

		// create load button
		load = new Button(canvas, SWT.PUSH);
		load.setText("Load");
		load.setBounds(10, 10, 100, 30);
		load.setVisible(false);

		// load image
		load.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				// file dialog
				FileDialog fileDialog = new FileDialog(shell, SWT.OPEN);
				fileDialog.setText("Open image file");
				fileDialog.setFilterExtensions(new String[] {
					"*.gif; *.jpg; *.png; *.ico; *.bmp"
				});
				fileDialog.setFilterNames(new String[] {
					"SWT image" + " (gif, jpeg, png, ico, bmp)"
				});

				// show dialog
				String filename = fileDialog.open();

				if (filename != null) {
					if (img != null && !img.isDisposed()) {
						img.dispose();
						img = null;
					}
					img = new Image(getDisplay(), filename);
					canvas.redraw();
					load.setVisible(false);
				}
			}
		});

		// trick to stay visible on entering button and leaving canvas
		load.addListener(SWT.MouseEnter, new Listener() {
			@Override
			public void handleEvent(Event event) {
				load.setVisible(true);
			}
		});
	}

}
