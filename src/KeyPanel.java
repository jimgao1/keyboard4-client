import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class KeyPanel extends JPanel implements ActionListener{

    public KeyboardConnection conn;

    public KeyPanel(KeyboardConnection c) {
        this.conn = c;

        this.setLayout(new GridLayout(4, 3));

        for (int i = 0; i < 4; i++) {
            this.add(new JLabel("Button " + i));

            JButton btnChangeColor = new JButton("Update Color");
            btnChangeColor.setActionCommand("color " + i);
            btnChangeColor.addActionListener(this);
            this.add(btnChangeColor);

            JButton btnChangeKey = new JButton("Update Key Code");
            btnChangeKey.setActionCommand("keycode " + i);
            btnChangeKey.addActionListener(this);
            this.add(btnChangeKey);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().startsWith("color")) {
            int key = Integer.parseInt(e.getActionCommand().split(" ")[1]);
            conn.updateColor(key, JColorChooser.showDialog(this, "Select New Color", Color.BLACK));
        } else if (e.getActionCommand().startsWith("keycode")) {
            int key = Integer.parseInt(e.getActionCommand().split(" ")[1]);
            int keyCode = Integer.parseInt(JOptionPane.showInputDialog(this, "Input Key Code"));
            conn.updateKeyCode(key, keyCode);
        }
    }
}
