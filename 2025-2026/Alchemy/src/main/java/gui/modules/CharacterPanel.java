package gui.modules;

import gui.Settings;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class CharacterPanel extends JPanel {
    private final JTextArea dialogueArea;
    private Timer typingTimer;

    public CharacterPanel() {
        // Устанавливаем прозрачный фон для самого JPanel
        setOpaque(false);
        setLayout(new BorderLayout(15, 0));
        setPreferredSize(new Dimension(450, 150));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Панель для гифки (слева)
        JPanel gifContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        gifContainer.setOpaque(false);
        gifContainer.add(createCharacterLabel());

        // 2. Классное облако диалога (центр)
        SpeechBubblePanel bubble = new SpeechBubblePanel();

        dialogueArea = new JTextArea();
        dialogueArea.setFont(Settings.getFontDialogue());
        dialogueArea.setForeground(new Color(50, 50, 50));
        dialogueArea.setWrapStyleWord(true);
        dialogueArea.setLineWrap(true);
        dialogueArea.setEditable(false);
        dialogueArea.setOpaque(false);

        // Отступы текста внутри облака
        dialogueArea.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 15));

        bubble.add(dialogueArea, BorderLayout.CENTER);

        add(gifContainer, BorderLayout.WEST);
        add(bubble, BorderLayout.CENTER);

        // Запускаем приветствие
        updateText("Привет! Я твой помощник.\nРад тебя видеть! Давай играть?");
    }

    /**
     * Метод для плавного обновления текста (эффект печати)
     */
    public void updateText(String text) {
        if (typingTimer != null && typingTimer.isRunning()) {
            typingTimer.stop();
        }

        dialogueArea.setText("");
        final int[] charIndex = {0};

        typingTimer = new Timer(40, e -> {
            if (charIndex[0] < text.length()) {
                dialogueArea.append(String.valueOf(text.charAt(charIndex[0])));
                charIndex[0]++;
            } else {
                typingTimer.stop();
            }
        });
        typingTimer.start();
    }

    private JLabel createCharacterLabel() {
        java.net.URL gifUrl = getClass().getClassLoader().getResource("character.gif");
        Dimension size = new Dimension(110, 110);

        if (gifUrl != null) {
            ImageIcon icon = new ImageIcon(gifUrl);
            JLabel label = new JLabel(icon) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    // Рисуем гифку 640x640, вписывая её в 110x110
                    g2d.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
                    g2d.dispose();
                }
            };
            label.setPreferredSize(size);
            return label;
        } else {
            return createPlaceholderLabel(size);
        }
    }

    private JLabel createPlaceholderLabel(Dimension size) {
        JLabel label = new JLabel("🎮");
        label.setFont(Settings.getFontPlaceholder());
        label.setPreferredSize(size);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    /**
     * Внутренний класс для рисования "облака" с хвостиком
     */
    private static class SpeechBubblePanel extends JPanel {
        public SpeechBubblePanel() {
            setLayout(new BorderLayout());
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int arrowSize = 12; // Размер хвостика
            int x = arrowSize;
            int y = 0;
            int w = getWidth() - arrowSize - 2;
            int h = getHeight() - 2;

            // Рисуем тень (чуть смещенная черная рамка)
            g2.setColor(new Color(0, 0, 0, 20));
            g2.fillRoundRect(x + 2, y + 2, w, h, 25, 25);

            // Основной фон облака (белый с прозрачностью)
            g2.setColor(new Color(255, 255, 255, 240));
            g2.fill(new RoundRectangle2D.Float(x, y, w, h, 25, 25));

            // Рисуем хвостик (треугольник), указывающий на гифку
            int[] px = {x, x - arrowSize, x};
            int[] py = {h / 2 - 10, h / 2, h / 2 + 10};
            g2.fillPolygon(px, py, 3);

            // Контур облака
            g2.setColor(new Color(210, 210, 210));
            g2.setStroke(new BasicStroke(1.5f));
            g2.draw(new RoundRectangle2D.Float(x, y, w, h, 25, 25));
            // Дорисовываем часть контура хвостика
            g2.drawLine(x, h / 2 - 10, x - arrowSize, h / 2);
            g2.drawLine(x - arrowSize, h / 2, x, h / 2 + 10);

            g2.dispose();
        }
    }
}
