
import com.fazecast.jSerialComm.*;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame implements ActionListener {

    public KeyboardConnection conn;

    public KeyPanel keyPanel;

    public Main(SerialPort port) {
        try {
            UIManager.setLookAndFeel(new PlasticLookAndFeel());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        this.setSize(375, 200);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle(String.format("[%s] - Allen Keys Configuration Utility", port.getSystemPortName()));

        this.setLayout(new GridLayout(1, 1));

        try {
            this.conn = new KeyboardConnection(port);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error connecting: " + ex.getLocalizedMessage());
        }

        keyPanel = new KeyPanel(this.conn);
        keyPanel.setBorder(BorderFactory.createTitledBorder("Key Configuration"));
        this.add(keyPanel);

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public static void main(String[] args) throws Exception {
        SerialPort[] ports = SerialPort.getCommPorts();

        String[] portNames = new String[ports.length];
        for (int i = 0; i < ports.length; i++)
            portNames[i] = ports[i].getSystemPortName();

        String selectedPort = (String) JOptionPane.showInputDialog(
                null,
                "Select serial device from list:",
                "Select Device",
                JOptionPane.QUESTION_MESSAGE,
                null,
                portNames,
                portNames[0]);

        for (int i = 0; i < ports.length; i++) {
            if (ports[i].getSystemPortName().equals(selectedPort)) {
                new Main(ports[i]);
            }
        }
    }
}
