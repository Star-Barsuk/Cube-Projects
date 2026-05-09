package gui.panels;

import logging.ILogger;
import logging.Loggers;
import timer.Statistics;

import javax.swing.*;
import java.awt.*;

public class StatisticsPanel extends JPanel {
    private static final ILogger logger = Loggers.get(TimerPanel.class);

    // Ссылка на панель таймера для получения статистики
    private final Statistics statistics;

    // Метки для отображения статистики
    private JLabel workTimeLabel;
    private JLabel breakTimeLabel;
    private JLabel cyclesLabel;

    public StatisticsPanel() {
        this.statistics = Statistics.getInstance();
        logger.info("Initializing Statistics Panel");

        setLayout(new GridBagLayout());
        setBackground(new Color(240, 240, 240));

        initComponents();
    }

    private void initComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Заголовок
        JLabel titleLabel = new JLabel("Статистика за сегодня");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // Метки для отображения статистики
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        add(new JLabel("Время работы:"), gbc);

        workTimeLabel = new JLabel("0 мин");
        workTimeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 1;
        add(workTimeLabel, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        add(new JLabel("Время отдыха:"), gbc);

        breakTimeLabel = new JLabel("0 мин");
        breakTimeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 1;
        add(breakTimeLabel, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        add(new JLabel("Завершенных циклов:"), gbc);

        cyclesLabel = new JLabel("0");
        cyclesLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 1;
        add(cyclesLabel, gbc);

        // Кнопка обновления статистики
        JButton refreshButton = new JButton("Обновить");
        refreshButton.addActionListener(e -> updateStatistics());

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        add(refreshButton, gbc);
    }

    // Метод для обновления статистики
    public void updateStatistics() {
        if (statistics != null) {
            workTimeLabel.setText(statistics.getTotalWorkTime() + " мин");
            breakTimeLabel.setText(statistics.getTotalBreakTime() + " мин");
            cyclesLabel.setText(String.valueOf(statistics.getCompletedCycles()));
        }
    }
}
