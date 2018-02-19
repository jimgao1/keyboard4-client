import com.fazecast.jSerialComm.SerialPort;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class KeyboardConnection {

    public static class LogListener extends Thread {
        private BufferedReader reader;

        public LogListener(BufferedReader r) {
            this.reader = r;
        }

        public void run() {
            while (true) {
                try {
                    String line = reader.readLine();
                    if (line != null) {
                        System.out.println(line);
                    }
                } catch (Exception ex) {
                }
            }
        }
    }

    private SerialPort serialPort;
    private BufferedReader reader;
    private PrintWriter writer;

    private double[] colorCorrectionFactor = {1.0, 1.0, 1.0};

    public KeyboardConnection(SerialPort port) throws IOException, InterruptedException {
        // Initialize Connection
        this.serialPort = port;
        this.serialPort.openPort();
        this.serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0);

        this.reader = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
        this.writer = new PrintWriter(serialPort.getOutputStream(), true);

        new LogListener(this.reader).start();

        // Test Connection
        this.setColorCorrectionFactor(0.5, 0.5, 0.6);
        this.updateColorAll(Color.GREEN);
        Thread.sleep(1000);
        this.updateColorAll(Color.BLACK);
    }

    public void setColorCorrectionFactor(double rr, double gg, double bb) {
        colorCorrectionFactor = new double[]{rr, gg, bb};
    }

    public void updateColor(int id, Color c) {
        if (c == null) return;
        this.writer.print(new char[]{
                (char) 1,
                (char) id,
                (char) (c.getRed() * colorCorrectionFactor[0]),
                (char) (c.getGreen() * colorCorrectionFactor[1]),
                (char) (c.getBlue() * colorCorrectionFactor[2])
        });
        this.writer.flush();
    }

    public void updateColorAll(Color c) {
        for (int i = 0; i < 4; i++)
            updateColor(i, c);
    }

    public void updateKeyCode(int id, int code) {
        this.writer.print(new char[]{
                (char) 2,
                (char) id,
                (char) code
        });
        this.writer.flush();
    }
}
