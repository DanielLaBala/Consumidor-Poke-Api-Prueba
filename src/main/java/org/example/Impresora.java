package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Impresora extends JPanel {
    private float escalaMaxima = 0.3f;
    private BufferedImage image;
    private boolean blancoYNegro;

    // Perdonme gente, fue mi primer proyecto con swing, esto es un desastre. i will call it a day
    // 😭😭😭😭😭😭😭😭😭😭😭😭😭😭😭😭😭😭😭😭😭😭😭😭😭😭😭😭😭😭😭😭😭😭😭😭😭😭

    private int pixelsDrawn = 0;
    private int totalPixels;
    private int escala = 1;

    private static Impresora instance;

    private JFrame frame;

    public void crearImpresora(float porcentajeEnPantalla, BufferedImage imagen) {
        this.escalaMaxima = porcentajeEnPantalla / 100;

        frame = new JFrame("", null);

        dibujar(frame, imagen);
    }

    void dibujar(JFrame frame, BufferedImage imagen) {
        try {
            image = imagen;
        } catch (Exception e) {
            e.printStackTrace();
        }

        pixelsDrawn = 0;

        frame.add(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(null);
        frame.toFront();
        frame.requestFocus();
        frame.setAlwaysOnTop(true);

        totalPixels = image.getWidth() * image.getHeight();

        Timer timer = new Timer(1, e -> {
            pixelsDrawn += image.getWidth();

            if (pixelsDrawn > totalPixels) {
                pixelsDrawn = totalPixels;
                ((Timer)e.getSource()).stop();
            }

            repaint();

        });

        timer.start();
    }

    public Impresora(BufferedImage imagen, float porcentajeEnPantalla, boolean blancoYNegro, boolean singleton) {
        this.blancoYNegro = blancoYNegro;

        if (singleton) {
            if (instance == null) {
                crearImpresora(porcentajeEnPantalla, imagen);
                instance = this;
            } else {
                instance.dibujar(instance.frame, imagen);

                repaint();
            }
        } else {
            crearImpresora(porcentajeEnPantalla, imagen);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        int maxWidth = (int)(screenWidth * escalaMaxima);
        int maxHeight = (int)(screenHeight * escalaMaxima);

        int escalaX = Math.max(1, maxWidth / image.getWidth());
        int escalaY = Math.max(1, maxHeight / image.getHeight());

        int escala = Math.min(escalaX, escalaY); // mantener proporción

        this.escala = escala;

        return new Dimension(image.getWidth() * escala, image.getHeight() * escala);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int count = 0;

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (count > pixelsDrawn) return;

                int rgb = image.getRGB(x, y);
                Color color = new Color(rgb, true);

                int r = color.getRed();
                int g2 = color.getGreen();
                int b = color.getBlue();
                int a = color.getAlpha();

                int gray = (int) (0.299 * r + 0.587 * g2 + 0.114 * b);
                Color colorPainted;

                if (blancoYNegro) {
                    colorPainted = new Color(gray, gray, gray, a);
                } else {
                    colorPainted = new Color(r, g2, b, a);
                }

                g.setColor(colorPainted);
                g.fillRect(x * escala, y * escala, escala, escala);

                count++;
            }
        }
    }
}