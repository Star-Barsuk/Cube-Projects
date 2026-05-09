package modules.laws;

import core.PhysicsModule;
import settings.Settings;
import javax.swing.*;
import java.awt.*;

public class LawDisplayModule implements PhysicsModule {
    private final String name;
    private final String formula;
    private final Settings settings;

    public LawDisplayModule(String name, String formula) {
        this.name = name;
        this.formula = formula;
        this.settings = Settings.getInstance();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public JPanel getInterface() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(settings.getCOLOR_PANEL_BG());

        // Лейбл с названием закона
        JLabel titleLabel = new JLabel(name);
        titleLabel.setFont(new Font(settings.getFONT_FAMILY(), Font.BOLD, settings.getHEADER_FONT_SIZE()));
        titleLabel.setForeground(Color.BLACK);

        // Лейбл с формулой (огромный шрифт)
        JLabel formulaLabel = new JLabel(formula);
        formulaLabel.setFont(new Font("Serif", Font.ITALIC, 92)); // Serif лучше для формул
        formulaLabel.setForeground(Color.red); // Или любой яркий акцент

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);
        panel.add(titleLabel, gbc);

        gbc.gridy = 1;
        panel.add(formulaLabel, gbc);

        return panel;
    }
}