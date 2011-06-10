package com.creepsmash.client.gui.panels;

import java.awt.Color;
import java.io.IOException;
import java.text.DateFormat;

import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import com.creepsmash.common.IConstants;


public class ChatPane extends JEditorPane {

	private static final long serialVersionUID = 1L;

	private Boolean ShowDate = true;
	
	
	/**
	 * Create the chat text area.
	 */
	public ChatPane() {

		this.setContentType( "text/html" );
		this.setText( "<html><body link='#FFFFFF' vlink='#FFFFFF' alink='#FFFFFF' text='#FFFFFF' bgcolor='#000000'></body></html>" );
		
		this.setEditable( false );
		
		this.setBackground(Color.BLACK);
		this.setForeground(Color.WHITE);
		//this.revalidate();
	}
	
	/**
	 * Send a Chat MSG.
	 * 
	 */
	public void sendChatText(String from, String msg) {
		
		String From = "";
		String Date = "";
		if (from.equalsIgnoreCase("system")) {
			From = "<span style='color:#C0C0C0;'>&#60;"+ from + "&#62;: </span>";
		} else {
			From = "<span style='color:#FFFF00; font-weight:700'>&#60;"+ from + "&#62;: </span>";
			msg = this.escapeHTML(msg);					   		
		}
		
		java.net.URL imageURL = null;
		
		imageURL = getClass().getClassLoader().getResource(IConstants.SIMLEY_URL+"0021.gif");
		msg = msg.replace(":)", "<img src='"+imageURL+"'>");
		msg = msg.replace(":-)", "<img src='"+imageURL+"'>");
		
		imageURL = getClass().getClassLoader().getResource(IConstants.SIMLEY_URL+"0009.gif");
		msg = msg.replace(":D", "<img src='"+imageURL+"'>");
		msg = msg.replace(":-D", "<img src='"+imageURL+"'>");
		
		imageURL = getClass().getClassLoader().getResource(IConstants.SIMLEY_URL+"0022.gif");
		msg = msg.replace(":(", "<img src='"+imageURL+"'>");
		msg = msg.replace(":-(", "<img src='"+imageURL+"'>");
		
		imageURL = getClass().getClassLoader().getResource(IConstants.SIMLEY_URL+"0020.gif");
		msg = msg.replace(";(", "<img src='"+imageURL+"'>");
		msg = msg.replace(";-(", "<img src='"+imageURL+"'>");
		
		imageURL = getClass().getClassLoader().getResource(IConstants.SIMLEY_URL+"0018.gif");
		msg = msg.replace(":kiss:", "<img src='"+imageURL+"'>");

		imageURL = getClass().getClassLoader().getResource(IConstants.SIMLEY_URL+"0019.gif");
		msg = msg.replace(":con:", "<img src='"+imageURL+"'>");

		imageURL = getClass().getClassLoader().getResource(IConstants.SIMLEY_URL+"0023.gif");
		msg = msg.replace(":x", "<img src='"+imageURL+"'>");
		msg = msg.replace(":-x", "<img src='"+imageURL+"'>");
		
		imageURL = getClass().getClassLoader().getResource(IConstants.SIMLEY_URL+"0016.gif");
		msg = msg.replace(":crazy:", "<img src='"+imageURL+"'>");
			
		imageURL = getClass().getClassLoader().getResource(IConstants.SIMLEY_URL+"0004.gif");
		msg = msg.replace(":shock:", "<img src='"+imageURL+"'>");
		
		imageURL = getClass().getClassLoader().getResource(IConstants.SIMLEY_URL+"0015.gif");
		msg = msg.replace(":cool:", "<img src='"+imageURL+"'>");
		msg = msg.replace("8:", "<img src='"+imageURL+"'>");
		
		imageURL = getClass().getClassLoader().getResource(IConstants.SIMLEY_URL+"0001.gif");
		msg = msg.replace(":mad:", "<img src='"+imageURL+"'>");

		imageURL = getClass().getClassLoader().getResource(IConstants.SIMLEY_URL+"0008.gif");
		msg = msg.replace(":lol:", "<img src='"+imageURL+"'>");
		
		imageURL = getClass().getClassLoader().getResource(IConstants.SIMLEY_URL+"0006.gif");
		msg = msg.replace(":hrhr:", "<img src='"+imageURL+"'>");
		
		imageURL = getClass().getClassLoader().getResource(IConstants.SIMLEY_URL+"0017.gif");
		msg = msg.replace(":bad:", "<img src='"+imageURL+"'>");

		imageURL = getClass().getClassLoader().getResource(IConstants.SIMLEY_URL+"0007.gif");
		msg = msg.replace(":grr:", "<img src='"+imageURL+"'>");
		
		imageURL = getClass().getClassLoader().getResource(IConstants.SIMLEY_URL+"0002.gif");
		msg = msg.replace(":hmm:", "<img src='"+imageURL+"'>");

		imageURL = getClass().getClassLoader().getResource(IConstants.SIMLEY_URL+"0029.gif");
		msg = msg.replace(":P", "<img src='"+imageURL+"'>");
		
		
		HTMLDocument doc = (HTMLDocument) this.getDocument();
		try {

			if (this.getShowDatum()) {
				DateFormat df = DateFormat.getTimeInstance(DateFormat.SHORT);
				Date = "<span style='color:#C0C0C0;'>" + "["
						+ df.format(System.currentTimeMillis()) + "]</span> ";

			}

			((HTMLEditorKit) this.getEditorKit()).insertHTML(doc, doc
					.getLength(), "<div>" + Date + From + msg + "</div>", 0, 0,
					null);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

		this.setCaretPosition(doc.getLength());

	}

	/**
	 * @see http://www.rgagnon.com/javadetails/java-0306.html
	 */
	public final String escapeHTML(String s){
		
		StringBuffer sb = new StringBuffer();
		   int n = s.length();
		   for (int i = 0; i < n; i++) {
		      char c = s.charAt(i);
		      switch (c) {
		         case '<': sb.append("&lt;"); break;
		         case '>': sb.append("&gt;"); break;
		         case '"': sb.append("&quot;"); break;
		         default:  sb.append(c); break;
		      }
		   }
		   return sb.toString();
		}
	/**
	 * Set the Date on/off in Chat MSG
	 * 
	 * @return the scrollpane
	 */
	public void setShowDatum (Boolean b){
		this.ShowDate = b;
		
	}
	/**
	 * Set get state
	 * 
	 * @return the scrollpane
	 */
	public Boolean getShowDatum (){
		return this.ShowDate;
		
	}
}
