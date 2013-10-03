package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

/**
 * A numeric key pad panel using <code>Action</code> with key bindings.
 * 
 * @author John B. Matthews; distribution per LGPL
 * 
 * And then Patrick came along and turned it into a calculator!
 */
public class KeyPadPanel extends JPanel {

    private int maxDigits = 10;
    private final JTextArea text = new JTextArea(1, maxDigits);
    private final JButton clear = new JButton(new Clear("C"));
    private final JButton enter = new JButton(new Enter("="));
    private final JButton plus = new JButton(new Plus("+"));
    private final JButton minus = new JButton(new Minus("-"));
    private final List<NumberButton> numbers = new ArrayList<NumberButton>();
    private final float workingVal = 0;
    private float storedVal = 0;
    private String state;

    /**
     * Construct a numeric key pad that accepts up to ten digits.
     */
    public KeyPadPanel() {
        super(new BorderLayout());

        JPanel display = new JPanel();
        text.setFont(new Font("Monaco", Font.BOLD, 30));
        text.setEditable(false);
        text.setFocusable(false);
        display.add(text);
        display.setBackground(Color.black);
        this.add(display, BorderLayout.NORTH);

        JPanel pad = new JPanel(new GridLayout(4, 3));
        // Add number button
        for (int i = 0; i < 10; i++) {
            NumberButton n = new NumberButton(i);
            numbers.add(n);
            if (i > 0) {
                pad.add(n);
            }
        }
        // Add clear button
        pad.add(clear);
        clear.setFont(new Font("Futura", Font.PLAIN, 20));
        clear.setFocusable(false);
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_CLEAR, 0), clear.getText());
        this.getActionMap().put(clear.getText(), new Click(clear));
        pad.add(numbers.get(0));
        // Add enter button
        pad.add(enter);
        enter.setFont(new Font("Futura", Font.PLAIN, 20));
        enter.setFocusable(false);
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), enter.getText());
        this.getActionMap().put(enter.getText(), new Click(enter));
        // Add plus button
        pad.add(plus);
        plus.setFont(new Font("Futura", Font.PLAIN, 20));
        plus.setFocusable(false);
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK), plus.getText());
        this.getActionMap().put(plus.getText(), new Click(plus));
        this.add(pad, BorderLayout.CENTER);
        // Add minus button
        pad.add(minus);
        minus.setFont(new Font("Futura", Font.PLAIN, 20));
        minus.setFocusable(false);
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, 0), minus.getText());
        this.getActionMap().put(minus.getText(), new Click(minus));
        this.add(pad, BorderLayout.CENTER);
        

    }

    /**
     * Construct a numeric key pad that accepts up to <code>maxDigits<code>.
     */
    public KeyPadPanel(int maxDigits) {
        this();
        this.maxDigits = maxDigits;
        this.text.setColumns(maxDigits);
    }

    private class Clear extends AbstractAction {

        public Clear(String name) {
            super(name);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            text.setText("");
            storedVal = 0;
            for (NumberButton n : numbers) {
                n.setEnabled(true);
            }
        }
    }

    private class Enter extends AbstractAction {

        public Enter(String name) {
        	super(name);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
        	text.setText(calculate(state,text.getText()));
        	//text.setText(Float.toString(storedVal + Float.parseFloat(text.getText())));
        }
    }
    
    private class Plus extends AbstractAction {

        public Plus(String name) {
            super(name);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
        	storedVal = Float.parseFloat(text.getText());
            text.setText("");
            state = "plus";
            for (NumberButton n : numbers) {
                n.setEnabled(true);
            }
        }
    }
    
    private class Minus extends AbstractAction {

        public Minus(String name) {
            super(name);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
        	storedVal = Float.parseFloat(text.getText());
            text.setText("");
            state = "minu";
            for (NumberButton n : numbers) {
                n.setEnabled(true);
            }
        }
    }

    private class Click extends AbstractAction {

        JButton button;

        public Click(JButton button) {
            this.button = button;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            button.doClick();
        }
    }
    
    private String calculate (String state, String display) {
    	String result = "";
    	
    	if ("plus".equals(state)) {
    		result = Float.toString(storedVal + Float.parseFloat(display));
    	} else if ("minus".equals(state)) {
    		result = Float.toString(storedVal - Float.parseFloat(display));
    	}
    	storedVal = 0;
    	return result;
    }

    /*
     * A numeric button with digit key bindings that appends to
     * <code>text<code>, accepting no more than <code>maxDigits<code>.
     */
    private class NumberButton extends JButton {

        public NumberButton(int number) {
            super(String.valueOf(number));
            this.setFocusable(false);
            this.setFont(new Font("Futura", Font.PLAIN, 20));
            this.setAction(new AbstractAction(this.getText()) {

                @Override
                public void actionPerformed(ActionEvent e) {
                    String cmd = e.getActionCommand();
                    if (text.getText().length() < maxDigits) {
                        text.append(cmd);
                    }
                    if (text.getText().length() == maxDigits) {
                        for (NumberButton n : numbers) {
                            n.setEnabled(false);
                        }
                    }
                }
            });
            KeyPadPanel.this.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_0 + number, 0), this.getText());
            KeyPadPanel.this.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_NUMPAD0 + number, 0), this.getText());
            KeyPadPanel.this.getActionMap().put(this.getText(), new Click(this));
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                JFrame f = new JFrame();
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.add(new KeyPadPanel(7));
                f.pack();
                f.setLocationRelativeTo(null);
                f.setVisible(true);
            }
        });
    }
}
