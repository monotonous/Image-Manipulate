/*
 * Author:  Joshua Parker
 *
 * This is the JFrame
 * for the image manipulation
*/


import java.awt.*;
import javax.swing.*;

public class ImageManipulateJFrame extends JFrame {
		
	public ImageManipulateJFrame(String fileName, String title, int x, int y, int width, int height) {
	 	// Set the title, top left location, and close operation for the frame
		setTitle(title);
		setLocation(x, y);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setPreferredSize(new Dimension(width, height));

		Container visibleArea = getContentPane();
		
		// declare a JPanel variable
		JPanel myJPanel = null;
		myJPanel = new ImageManipulateJPanel(this, fileName);
		
		if(myJPanel != null) {
			visibleArea.add(myJPanel);

			// Set the size of the JPanel inside content pane of the window,
			// resize and validate the
			// window to suit, obtain keyboard focus, and then make the window visible
			
			pack();
			myJPanel.requestFocusInWindow();
		}

		// make the window visible
		setVisible(true);
	}
//---------------------------------------------------------------------
// Set JFrame preferred size
//---------------------------------------------------------------------
	public void setPreferredSize(Dimension dim) {
		super.setPreferredSize(dim);
	}
}