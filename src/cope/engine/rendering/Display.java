package cope.engine.rendering;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.swing.JFrame;

import cope.engine.input.Input;

public class Display extends Canvas 
{
	private static final long serialVersionUID = -5731461407498870427L;
	private final JFrame         m_frame;
	private final Bitmap         m_frameBuffer;
	private final BufferStrategy m_bufferStrategy;
	private final Graphics       m_graphics;
	
	private final Input m_input;
	private Screen m_screen;
	
	public Display(int width, int height, String title)
	{
		Dimension size = new Dimension(width, height);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		
		m_frameBuffer = new Bitmap(width, height);
		
		m_frame = new JFrame();
		m_frame.add(this);
		m_frame.pack();
		m_frame.setResizable(false);
		m_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		m_frame.setVisible(true);
		m_frame.setLocationRelativeTo(null);
		m_frame.setTitle(title);
		
		createBufferStrategy(2);
		m_bufferStrategy = getBufferStrategy();
		m_graphics = m_bufferStrategy.getDrawGraphics();
		
		setFocusable(true);
		requestFocus();
		
		m_input = new Input();
		addKeyListener(m_input);
		addFocusListener(m_input);
		addMouseListener(m_input);
		addMouseMotionListener(m_input);
		
		m_screen = new Screen();
		
	}
	
	public void showScreen(Screen screen)
	{
		m_screen = screen;
	}
	
	public Input getInput() 
	{
		return m_input;
	}
	
	public void showBufferStrategy() 
	{
		drawBitmap(0, 0, m_frameBuffer);
		m_screen.render(this);
		m_bufferStrategy.show();
	}
	
	public void fill(int shade) 
	{
		m_frameBuffer.clear(shade);
	}
	
	public void drawBitmap(int x, int y, Bitmap bitmap) 
	{
		
		BufferedImage image = new BufferedImage(
				bitmap.getWidth(), bitmap.getHeight(), 
				BufferedImage.TYPE_4BYTE_ABGR);
		
		byte[] displayComponents = ((DataBufferByte) image
				.getRaster()
				.getDataBuffer()
				).getData();
		
		bitmap.copyToByteArray(displayComponents);
		
		m_graphics.drawImage(image, x, y, 
				bitmap.getWidth(), bitmap.getHeight(), null);
	}
	
	public void drawText(int x, int y, String text, Font font, Color color) 
	{
		Graphics2D g2d = (Graphics2D) m_graphics;
		
		g2d.setRenderingHint(
		        RenderingHints.KEY_TEXT_ANTIALIASING,
		        RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);

		g2d.setFont(font);
		g2d.setColor(color);
		g2d.drawString(text, x, y);
	}
	
	public Bitmap getFrameBuffer()
	{
		return m_frameBuffer;
	}
	
}

