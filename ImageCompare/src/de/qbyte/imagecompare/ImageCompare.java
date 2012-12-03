package de.qbyte.imagecompare;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class ImageCompare {

	/* ***** PROPERTIES ***** */

	private final Display	display	= new Display();
	private final Shell		shell;

	/* ***** CONSTRUCTORS ***** */

	private ImageCompare() {

		// initialize shell
		shell = new Shell(display);
		shell.setSize(800, 800);
		shell.setText("Image Compare");

		// set the main grid layout
		GridLayout shellLayout = new GridLayout();
		shellLayout.numColumns = 2;
		shellLayout.marginWidth = 2;
		shellLayout.marginHeight = 2;
		shellLayout.horizontalSpacing = 2;
		shellLayout.verticalSpacing = 2;
		shell.setLayout(shellLayout);

		ImageInputComposite comp1 = new ImageInputComposite(shell);
		ImageInputComposite comp2 = new ImageInputComposite(shell);
		ImageOutputComposite comp3 = new ImageOutputComposite(shell, comp1, comp2);

		// application loop
		shell.open();
		while (!shell.isDisposed())
			if (!display.readAndDispatch())
				display.sleep();
		display.dispose();
	}

	/* ***** MAIN ***** */

	public static void main(String[] args) {
		new ImageCompare();
	}

}
