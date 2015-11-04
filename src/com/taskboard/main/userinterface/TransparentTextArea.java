//@@author A0129526B
package com.taskboard.main.userinterface;
import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JTextArea;

public class TransparentTextArea extends JTextArea {
	
	static final long serialVersionUID = 4;
	private float _transparency;

	public TransparentTextArea(float transparency) {
		super();
		_transparency = transparency;
		setOpaque(false);
		setEditable(false);
	}

	public void setTransparency(float newTransparency) {
		_transparency = newTransparency;
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, _transparency));
		super.paint(g2);
		g2.dispose();
	} 
	
}
