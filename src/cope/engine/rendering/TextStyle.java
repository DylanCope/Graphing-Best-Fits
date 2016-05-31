package cope.engine.rendering;


public class TextStyle {

	private int m_size, m_style;
	private Colour m_colour;
	private String m_font;

	public void setFont(String font)	 { m_font = font;     }
	public void setSize(int size)		 { m_size = size;     }
	public void setStyle(int style)		 { m_style = style;   }
	public void setColor(Colour colour)  { m_colour = colour; }
	
	public String getFont()   { return m_font;   }
	public int getSize()	  { return m_size;   }
	public int getStyle()	  { return m_style;  }
	public Colour getColour() { return m_colour; }

}
