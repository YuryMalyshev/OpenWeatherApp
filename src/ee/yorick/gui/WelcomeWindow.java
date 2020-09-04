package ee.yorick.gui;

import java.awt.BorderLayout;
import java.awt.Desktop;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JCheckBox;
import javax.swing.JTextPane;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.prefs.Preferences;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;

public class WelcomeWindow extends JDialog
{
	private static final long serialVersionUID = -8530219190761853670L;
	public static final int OK = 0;
	public static final int NOK = -1;
	
	private static final Preferences prefs = Preferences.userNodeForPackage(WelcomeWindow.class);
	
	
	private final JPanel contentPanel = new JPanel();
	private JTextPane textField;
	private JTextField fieldAPIkey;
	
	private int status = NOK;
	
	private String sAPIkey;

	/**
	 * Create the dialog.
	 */
	public WelcomeWindow()
	{
		
		sAPIkey = prefs.get("apikey", null);
		boolean donotshow = prefs.getBoolean("donotshow", false);
		if(donotshow)
		{
			if(sAPIkey == null)
			{
				donotshow = false;
			}
		}
		
		this.setTitle("Welcome!");
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		setBounds(100, 100, 565, 448);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setOpaque(false);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		JPanel panelAPI = new JPanel();
		panelAPI.setOpaque(false);
		contentPanel.add(panelAPI, BorderLayout.NORTH);
		panelAPI.setLayout(new GridLayout(2, 0, 0, 0));
		JPanel panelAPIkey = new JPanel();
		panelAPIkey.setOpaque(false);
		panelAPI.add(panelAPIkey);
		panelAPIkey.setLayout(new BorderLayout(0, 0));
		JLabel lblAPIKey = new JLabel(" API key: ");
		lblAPIKey.setFont(new Font("Calibri", Font.PLAIN, 17));
		panelAPIkey.add(lblAPIKey, BorderLayout.WEST);
		fieldAPIkey = new JTextField();
		fieldAPIkey.setFont(new Font("Consolas", Font.PLAIN, 17));
		panelAPIkey.add(fieldAPIkey);
		fieldAPIkey.setColumns(10);
		textField = new JTextPane();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		textField.addHyperlinkListener(new HyperlinkListener()
		{
			public void hyperlinkUpdate(HyperlinkEvent e)
			{

				if (e.getEventType() == EventType.ACTIVATED)
				{
					try
					{
						Desktop.getDesktop().browse(e.getURL().toURI());
					}
					catch (IOException e1)
					{
						e1.printStackTrace();
					}
					catch (URISyntaxException e1)
					{
						e1.printStackTrace();
					}
				}
			}
		});
		textField.setContentType("text/html");
		textField.setText(
				"<p style = \"font-family:Calibri;font-size:13px;\"> Get your API key here: <a\r\nhref=\"https://home.openweathermap.org/api_keys\">https://home.openweathermap.org/api_keys</a>\r\n</p>");
		textField.setEditable(false);
		textField.setOpaque(false);
		panelAPI.add(textField);
		JTextPane mentionsTextField = new JTextPane();
		mentionsTextField.setEditable(false);
		mentionsTextField.setOpaque(false);
		mentionsTextField.setContentType("text/html");
		contentPanel.add(mentionsTextField, BorderLayout.CENTER);
		JPanel buttonPane = new JPanel();
		buttonPane.setOpaque(false);
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		buttonPane.setLayout(new BorderLayout(0, 0));
		JButton continueBtn = new JButton("Continue");
		continueBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				status = OK;
				setVisible(false);
			}
		});
		continueBtn.setEnabled(false);
		continueBtn.setFont(new Font("Tahoma", Font.PLAIN, 16));
		continueBtn.setActionCommand("OK");
		buttonPane.add(continueBtn, BorderLayout.EAST);
		getRootPane().setDefaultButton(continueBtn);
		JCheckBox chboxDoNotShow = new JCheckBox("Do not show again");
		chboxDoNotShow.setEnabled(false);
		chboxDoNotShow.setSelected(donotshow);
		chboxDoNotShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				prefs.putBoolean("donotshow", chboxDoNotShow.isSelected());
			}
		});
		chboxDoNotShow.setFont(new Font("Tahoma", Font.PLAIN, 16));
		buttonPane.add(chboxDoNotShow, BorderLayout.WEST);
		JLabel lblWelcome = new JLabel("Welcome to Weather App");
		lblWelcome.setFont(new Font("Calibri", Font.PLAIN, 24));
		lblWelcome.setRequestFocusEnabled(false);
		lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(lblWelcome, BorderLayout.NORTH);
		
		if(sAPIkey != null)
		{
			fieldAPIkey.setText(sAPIkey);
			continueBtn.setEnabled(true);
			chboxDoNotShow.setEnabled(true);
		}
		
		fieldAPIkey.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) 
			{
				if(fieldAPIkey.getText().length() == 32)
				{
					sAPIkey = fieldAPIkey.getText();
					prefs.put("apikey", sAPIkey);
					continueBtn.setEnabled(true);
					chboxDoNotShow.setEnabled(true);
				}
				else
				{
					continueBtn.setEnabled(false);
				}
			}
		});
		
		this.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent windowEvent)
			{
				status = NOK;
			}
		});
		setVisible(false);
	}
	
	public int showDialog()
	{
		this.setModal(true);
		this.setVisible(true);
		return status;
	}
	
	public int showFirstTime()
	{
		boolean donotshow = prefs.getBoolean("donotshow", false);
		if(donotshow && sAPIkey != null)
		{
			return OK;
		}
		else
		{
			int result = showDialog();
			if(result == NOK)
			{
				if(this.sAPIkey != null)
					return OK;
			}
			return result;
		}
	}
	
	public String getAPIkey()
	{
		if(sAPIkey == null)
		{
			
		}
		return sAPIkey;
	}
}
