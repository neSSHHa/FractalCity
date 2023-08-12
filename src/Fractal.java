import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.*;

public class Fractal extends JFrame implements KeyListener {

    private static final long serialVersionUID = 1L;
    private JPanel fractalPanel;
    private ExecutorService executor;

    public Fractal() {
        super("Fractal");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        getContentPane().setBackground(Color.BLACK);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        fractalPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int x2 = getWidth() / 2;
                int y2 = getHeight();
                Paint2(365, x2, y2, -90, 0, 40, g);
            }
        };
        fractalPanel.setBackground(Color.BLACK);
        add(fractalPanel);

        addKeyListener(this);
        setVisible(true);
        
        executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()); // Create the executor
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Fractal());
    }

    public void Paint2(double length, int x1, int y1, int angle, int numberOfIteration, int limit, Graphics g) {
        if (numberOfIteration != limit) {
            int x2 = x1 + (int) (Math.cos(Math.toRadians(angle)) * length);
            int y2 = y1 + (int) (Math.sin(Math.toRadians(angle)) * length);
            Random r = new Random();
            g.setColor(new Color(r.nextFloat(),r.nextFloat(),r.nextFloat()));
            g.drawLine(x1, y1, x2, y2);

            SwingUtilities.invokeLater(() -> {
                try {
                    Thread.sleep(0, 15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Graphics g2 = fractalPanel.getGraphics();
                executor.submit(() -> Paint2(length / 1.5, x2, y2, angle - 22, numberOfIteration + 1, limit, g2));
                executor.submit(() -> Paint2(length / 1.5, x2, y2, angle + 55, numberOfIteration + 1, limit, g2));
            });
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            executor.shutdown(); // Shutdown the executor before exiting
            System.exit(1);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}