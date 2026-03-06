package gui.panels;

import logging.ILogger;
import logging.Loggers;
import timer.Statistics;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class TimerPanel extends JPanel {
    private static final ILogger logger = Loggers.get(TimerPanel.class);

    // Пути к иконкам
    private static final String PROJECT_ROOT = System.getProperty("user.dir");
    private static final String PNG_PATH = PROJECT_ROOT + "/src/main/resources/icon.png";
    private static final String SVG_PATH = PROJECT_ROOT + "/src/main/resources/icon.svg";

    // Компоненты интерфейса
    private JSpinner workTimeSpinner;      // Поле для ввода времени работы
    private JSpinner breakTimeSpinner;      // Поле для ввода времени отдыха

    private JLabel timerLabel;              // Большой дисплей таймера
    private JButton startButton;             // Кнопка Старт
    private JButton pauseButton;              // Кнопка Пауза
    private JButton stopButton;               // Кнопка Стоп
    private JButton trayButton;               // Кнопка свернуть в трей

    // Переменные для работы таймера
    private Timer swingTimer;                 // Таймер Swing для отсчета времени
    private int currentTimeInSeconds;          // Текущее оставшееся время в секундах
    private int workTimeInSeconds;              // Время работы в секундах
    private int breakTimeInSeconds;             // Время отдыха в секундах
    private boolean isWorkPhase;                 // true - фаза работы, false - фаза отдыха
    private boolean isRunning;                    // Запущен ли таймер
    private boolean isPaused;                      // На паузе ли таймер

    // Ссылка на класс статистики
    private final Statistics statistics;

    // Ссылка на главное окно для работы с треем
    private final JFrame mainFrame;

    // Системный трей
    private SystemTray tray;
    private TrayIcon trayIcon;

    public TimerPanel(JFrame frame) {
        logger.info("Initializing Timer Panel");

        this.mainFrame = frame;
        this.statistics = Statistics.getInstance();

        // Устанавливаем менеджер компоновки
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 240, 240));

        // Инициализация компонентов
        initComponents();

        // Настройка системного трея
        setupSystemTray();

        // Настройка таймера Swing
        setupSwingTimer();
    }

    private void initComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // === Поля для ввода времени ===
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Время работы (мин):"), gbc);

        workTimeSpinner = new JSpinner(new SpinnerNumberModel(25, 1, 120, 1));
        workTimeSpinner.setPreferredSize(new Dimension(80, 30));
        gbc.gridx = 1;
        add(workTimeSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Время отдыха (мин):"), gbc);

        breakTimeSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 60, 1));
        breakTimeSpinner.setPreferredSize(new Dimension(80, 30));
        gbc.gridx = 1;
        add(breakTimeSpinner, gbc);

        // === Большой дисплей таймера ===
        timerLabel = new JLabel("25:00", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Monospaced", Font.BOLD, 48));
        timerLabel.setForeground(new Color(50, 50, 150));
        timerLabel.setPreferredSize(new Dimension(200, 100));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(timerLabel, gbc);

        // === Кнопки управления ===
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);

        startButton = new JButton("Старт");
        startButton.setPreferredSize(new Dimension(80, 35));
        startButton.addActionListener(e -> startTimer());

        pauseButton = new JButton("Пауза");
        pauseButton.setPreferredSize(new Dimension(80, 35));
        pauseButton.setEnabled(false); // Изначально неактивна
        pauseButton.addActionListener(e -> pauseTimer());

        stopButton = new JButton("Стоп");
        stopButton.setPreferredSize(new Dimension(80, 35));
        stopButton.setEnabled(false); // Изначально неактивна
        stopButton.addActionListener(e -> stopTimer());

        buttonPanel.add(startButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(stopButton);

        gbc.gridy = 3;
        add(buttonPanel, gbc);

        // === Кнопка свернуть в трей ===
        trayButton = new JButton("Свернуть в трей");
        trayButton.setPreferredSize(new Dimension(150, 35));
        trayButton.addActionListener(e -> minimizeToTray());

        gbc.gridy = 4;
        add(trayButton, gbc);

        // Инициализация значений
        resetToWorkPhase();
    }

    // Настройка таймера Swing
    private void setupSwingTimer() {
        // Таймер будет срабатывать каждую секунду (1000 мс)
        swingTimer = new Timer(1000, e -> {
            // Уменьшаем текущее время на 1 секунду
            currentTimeInSeconds--;

            // Обновляем отображение таймера
            updateTimerDisplay();

            // Проверяем, не истекло ли время
            if (currentTimeInSeconds <= 0) {
                // Время истекло!
                swingTimer.stop(); // Останавливаем таймер
                handlePhaseEnd();   // Обрабатываем окончание фазы
            }
        });
    }

    // Обновление отображения таймера
    private void updateTimerDisplay() {
        // Форматируем время в формате ММ:СС
        int minutes = currentTimeInSeconds / 60;
        int seconds = currentTimeInSeconds % 60;
        String timeString = String.format("%02d:%02d", minutes, seconds);

        // Обновляем метку в потоке Swing (безопасно)
        SwingUtilities.invokeLater(() -> timerLabel.setText(timeString));
    }

    // Обработка окончания фазы (работа или отдых)
    private void handlePhaseEnd() {
        if (isWorkPhase) {
            // Завершилась фаза работы
            logger.info("Work phase ended");

            // Добавляем время работы в статистику
            statistics.addTotalWorkTime(workTimeInSeconds / 60);

            // Показываем уведомление
            showNotification("Время работать! Начинается перерыв?");

            // Запускаем фазу отдыха
            startBreakPhase();
        } else {
            // Завершилась фаза отдыха
            logger.info("Break phase ended");

            // Добавляем время отдыха в статистику
            statistics.addTotalBreakTime(breakTimeInSeconds / 60);

            // Увеличиваем счетчик завершенных циклов
            statistics.addCompletedCycles(1);

            // Показываем уведомление
            showNotification("Перерыв окончен! Пора работать!");

            // Запускаем новую фазу работы
            startWorkPhase();
        }
    }

    // Запуск фазы работы
    private void startWorkPhase() {
        isWorkPhase = true;
        currentTimeInSeconds = workTimeInSeconds;
        updateTimerDisplay();

        // Запускаем таймер, если не на паузе
        if (!isPaused) {
            swingTimer.start();
            isRunning = true;
        }
    }

    // Запуск фазы отдыха
    private void startBreakPhase() {
        isWorkPhase = false;
        currentTimeInSeconds = breakTimeInSeconds;
        updateTimerDisplay();

        // Запускаем таймер, если не на паузе
        if (!isPaused) {
            swingTimer.start();
            isRunning = true;
        }
    }

    // Сброс к фазе работы
    private void resetToWorkPhase() {
        isWorkPhase = true;
        isRunning = false;
        isPaused = false;

        // Получаем значения из полей ввода
        workTimeInSeconds = (Integer) workTimeSpinner.getValue() * 60;
        breakTimeInSeconds = (Integer) breakTimeSpinner.getValue() * 60;

        currentTimeInSeconds = workTimeInSeconds;
        updateTimerDisplay();
    }

    // Обработчик кнопки Старт
    private void startTimer() {
        logger.info("Start button clicked");

        if (!isRunning) {
            // Если таймер не запущен
            if (isPaused) {
                // Если был на паузе - просто продолжаем
                isPaused = false;
            } else {
                // Если новый запуск - сбрасываем с текущими значениями
                workTimeInSeconds = (Integer) workTimeSpinner.getValue() * 60;
                breakTimeInSeconds = (Integer) breakTimeSpinner.getValue() * 60;
                resetToWorkPhase();
            }

            // Запускаем таймер
            swingTimer.start();
            isRunning = true;

            // Обновляем состояние кнопок
            startButton.setEnabled(false);
            pauseButton.setEnabled(true);
            stopButton.setEnabled(true);

            // Блокируем поля ввода времени
            workTimeSpinner.setEnabled(false);
            breakTimeSpinner.setEnabled(false);
        }
    }

    // Обработчик кнопки Пауза
    private void pauseTimer() {
        logger.info("Pause button clicked");

        if (isRunning && !isPaused) {
            swingTimer.stop();
            isPaused = true;
            isRunning = false;

            // Обновляем состояние кнопок
            startButton.setEnabled(true);
            pauseButton.setEnabled(false);
            stopButton.setEnabled(true);
        }
    }

    // Обработчик кнопки Стоп
    private void stopTimer() {
        logger.info("Stop button clicked");

        // Останавливаем таймер
        swingTimer.stop();

        // Сбрасываем все переменные
        resetToWorkPhase();
        isRunning = false;
        isPaused = false;

        // Обновляем состояние кнопок
        startButton.setEnabled(true);
        pauseButton.setEnabled(false);
        stopButton.setEnabled(false);

        // Разблокируем поля ввода
        workTimeSpinner.setEnabled(true);
        breakTimeSpinner.setEnabled(true);
    }

    // Настройка системного трея
    private void setupSystemTray() {
        if (!SystemTray.isSupported()) {
            logger.warn("System tray is not supported");
            trayButton.setEnabled(false);
            return;
        }

        try {
            tray = SystemTray.getSystemTray();

            // Создаем иконку для трея
            Image image = loadIconFromPng();

            // Создаем всплывающее меню для трея
            PopupMenu popup = new PopupMenu();

            MenuItem restoreItem = new MenuItem("Восстановить");
            restoreItem.addActionListener(e -> restoreFromTray());

            MenuItem exitItem = new MenuItem("Выход");
            exitItem.addActionListener(e -> {
                // Закрываем приложение
                mainFrame.dispose();
                System.exit(0);
            });

            popup.add(restoreItem);
            popup.add(exitItem);

            // Создаем иконку в трее
            trayIcon = new TrayIcon(image, "Pomodoro Timer", popup);
//            trayIcon.setImageAutoSize(true);

            // Добавляем обработчик двойного клика для восстановления
            trayIcon.addActionListener(e -> restoreFromTray());

            logger.info("System tray setup completed");
        } catch (Exception e) {
            logger.error("Failed to setup system tray", e);
            trayButton.setEnabled(false);
        }
    }

    private Image loadIconFromPng() {
        try {
            // Получаем корень проекта (рабочую директорию)
            String projectRoot = System.getProperty("user.dir");
            logger.info("Project root: " + projectRoot);

            // Собираем полный путь до иконки
            String iconPath = projectRoot + "/src/main/resources/icon1.png";
            logger.info("Full icon path: " + iconPath);

            // Проверяем существует ли файл
            File iconFile = new File(iconPath);
            if (iconFile.exists()) {
                logger.info("Icon file found! Size: " + iconFile.length() + " bytes");

                // Загружаем иконку
                BufferedImage img = ImageIO.read(iconFile);
                if (img != null) {
                    logger.info("Icon loaded successfully!");
                    return img;
                } else {
                    logger.error("Failed to read icon file (ImageIO returned null)");
                }
            }
        } catch (Exception e) {
            logger.error("Error loading icon: " + e.getMessage());
        }

        return null;
    }

    // Свернуть в трей
    private void minimizeToTray() {
        logger.info("Minimizing to tray");

        try {
            // Добавляем иконку в трей
            tray.add(trayIcon);

            // Скрываем главное окно
            mainFrame.setVisible(false);

            logger.info("Window minimized to tray");
        } catch (Exception e) {
            logger.error("Failed to minimize to tray", e);
            JOptionPane.showMessageDialog(this,
                    "Не удалось свернуть в трей",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Восстановить из трея
    private void restoreFromTray() {
        logger.info("Restoring from tray");

        // Показываем окно
        mainFrame.setVisible(true);
        mainFrame.setState(Frame.NORMAL); // Восстанавливаем, если было свернуто

        // Убираем иконку из трея
        tray.remove(trayIcon);
    }

    // Показать системное уведомление
    private void showNotification(String message) {
        logger.info("Showing notification: " + message);

        // Пробуем показать через системный трей
        if (trayIcon != null) {
            trayIcon.displayMessage("Pomodoro Timer", message, TrayIcon.MessageType.INFO);
        } else {
            // Если трей недоступен, показываем диалоговое окно
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                    message,
                    "Pomodoro Timer",
                    JOptionPane.INFORMATION_MESSAGE));
        }
    }
}
