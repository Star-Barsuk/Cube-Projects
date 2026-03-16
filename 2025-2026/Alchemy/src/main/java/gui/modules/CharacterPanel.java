package gui.modules;

import gui.Settings;
import javax.swing.*;
import java.awt.*;

public class CharacterPanel extends JPanel {

    public CharacterPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(400, 150));
        setBackground(new Color(255, 255, 255, 200));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JPanel gifPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        gifPanel.setOpaque(false);

        JLabel gifLabel = createCharacterLabel();
        gifPanel.add(gifLabel);

        JPanel dialoguePanel = new JPanel(new BorderLayout());
        dialoguePanel.setOpaque(false);

        JTextArea dialogueArea = new JTextArea(3, 25);
        dialogueArea.setText("Привет! Я твой помощник.\nКак у тебя дела?\nДавай играть вместе!");
        dialogueArea.setFont(Settings.getFontDialogue());
        dialogueArea.setWrapStyleWord(true);
        dialogueArea.setLineWrap(true);
        dialogueArea.setEditable(false);
        dialogueArea.setOpaque(false);
        dialogueArea.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        dialoguePanel.add(dialogueArea, BorderLayout.CENTER);

        add(gifPanel, BorderLayout.WEST);
        add(dialoguePanel, BorderLayout.CENTER);
    }

    private JLabel createCharacterLabel() {
        java.net.URL gifUrl = getClass().getClassLoader().getResource("character.gif");

        if (gifUrl != null) {
            return new JLabel(new ImageIcon(gifUrl));
        } else {
            return createPlaceholderLabel();
        }
    }

    private JLabel createPlaceholderLabel() {
        JLabel label = new JLabel("🎮");
        label.setFont(Settings.getFontPlaceholder());
        label.setForeground(new Color(255, 200, 100));
        label.setPreferredSize(new Dimension(80, 80));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }
}
