package com.taskboard.main;

import javax.swing.JScrollPane;
import javax.swing.JPanel;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class TransparentScrollPane extends JScrollPane {

	static final long serialVersionUID = 3;
	private float _transparency;

	public TransparentScrollPane(JPanel panel, float transparency) {
		super(panel);
		_transparency = transparency;
		setOpaque(false);
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, _transparency));
		super.paint(g2);
		g2.dispose();
	}

}
