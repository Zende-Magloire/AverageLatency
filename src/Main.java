import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;

public class Main {

    public static double[] main(String[] args) throws IOException, InterruptedException {
        int j = 2;
        double[] avg_diff_0 = new double[9];
        double total_0 = 0.0;

        for (int i = 0; i < 9; i++) {

            String line = "";
            String splitBy = " ";
            //for (int i = 0; i < 9; i++) {
            System.out.println("chat" + j);
            try {
                BufferedReader br = new BufferedReader(new FileReader("C:\\Downloads\\chats_csv\\chat-" + j + ".csv"));
                String[] packets = new String[br.readLine().length()];
                String[] ports = new String[packets.length];
                while ((line = br.readLine()) != null) {
                    packets = line.split(splitBy);
                    //System.out.println("[Time=" + packets[0] + ", Ports=" + packets[1] + "]");
                    ports = packets[1].split(",");
                    //System.out.println("[Src=" + ports[0] + ", Dst=" + ports[1] + "]");
                }

                double[] time_0 = new double[j];
                if (ports[1]=="5000") {
                    time_0[0] = Double.valueOf(packets[0]);
                }
                else if (ports[0]=="5000"){
                    time_0[i] = Double.valueOf(packets[0]);
                }

                time_0[i] = Double.valueOf(packets[0]);
                double[] diff_0 = new double[j];
                for (int t = 1; t < time_0.length - 1; t++) {
                    diff_0[t] = time_0[t] - time_0[0];
                    total_0 = total_0 + diff_0[t];
                }
                avg_diff_0[i] = total_0 / j;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            j += 2;
            System.out.println("diff" + i + ": " + avg_diff_0[i]);
        }
        return avg_diff_0;
    }

    public static class ChartPanel extends JPanel {
        private double[] values;

        private String[] names;

        private String title;

        public ChartPanel(double[] v, String[] n, String t) {
            names = n;
            values = v;
            title = t;
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (values == null || values.length == 0)
                return;
            double minValue = 0;
            double maxValue = 0;
            for (int i = 0; i < values.length; i++) {
                if (minValue > values[i])
                    minValue = values[i];
                if (maxValue < values[i])
                    maxValue = values[i];
            }

            Dimension d = getSize();
            int clientWidth = d.width;
            int clientHeight = d.height;
            int barWidth = clientWidth / values.length;

            Font titleFont = new Font("SansSerif", Font.BOLD, 20);
            FontMetrics titleFontMetrics = g.getFontMetrics(titleFont);
            Font labelFont = new Font("SansSerif", Font.PLAIN, 10);
            FontMetrics labelFontMetrics = g.getFontMetrics(labelFont);

            int titleWidth = titleFontMetrics.stringWidth(title);
            int y = titleFontMetrics.getAscent();
            int x = (clientWidth - titleWidth) / 2;
            g.setFont(titleFont);
            g.drawString(title, x, y);

            int top = titleFontMetrics.getHeight();
            int bottom = labelFontMetrics.getHeight();
            if (maxValue == minValue)
                return;
            double scale = (clientHeight - top - bottom) / (maxValue - minValue);
            y = clientHeight - labelFontMetrics.getDescent();
            g.setFont(labelFont);

            for (int i = 0; i < values.length; i++) {
                int valueX = i * barWidth + 1;
                int valueY = top;
                int height = (int) (values[i] * scale);
                if (values[i] >= 0)
                    valueY += (int) ((maxValue - values[i]) * scale);
                else {
                    valueY += (int) (maxValue * scale);
                    height = -height;
                }

                g.setColor(Color.red);
                g.fillRect(valueX, valueY, barWidth - 2, height);
                g.setColor(Color.black);
                g.drawRect(valueX, valueY, barWidth - 2, height);
                int labelWidth = labelFontMetrics.stringWidth(names[i]);
                x = i * barWidth + (barWidth - labelWidth) / 2;
                g.drawString(names[i], x, y);
            }
        }

        public static void main(String[] argv) throws IOException, InterruptedException {
            JFrame f = new JFrame();
            f.setSize(400, 300);
            double[] values = Main.main(null);
            String[] names = new String[9];

            names[0] = "2";
            names[1] = "4";
            names[2] = "6";
            names[3] = "8";
            names[4] = "10";
            names[5] = "12";
            names[6] = "14";
            names[7] = "16";
            names[8] = "18";

            f.getContentPane().add(new ChartPanel(values, names, "Average Latency"));

            WindowListener wndCloser = new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            };
            f.addWindowListener(wndCloser);
            f.setVisible(true);
        }
    }
}

