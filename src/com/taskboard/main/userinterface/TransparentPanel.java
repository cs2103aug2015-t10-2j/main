package com.taskboard.main.userinterface;

import javax.swing.JPanel;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class TransparentPanel extends JPanel {

	static final long serialVersionUID = 2;
	private float _transparency;

	public TransparentPanel(float transparency) {
		super();
		_transparency = transparency;
		setOpaque(false);
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
