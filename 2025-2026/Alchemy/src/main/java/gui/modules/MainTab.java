package gui.modules;

import gui.Settings;
import gui.dnd.CombinationSlot;
import gui.dnd.ElementTransferable;
import models.Element;
import services.CombineResponse;
import services.GameService;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.dnd.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainTab extends JPanel implements CombinationSlot.SlotListener {
    private final GameService gameService;

    private JPanel knownElementsPanel;
    private JPanel craftPanel;
    private JPanel recipePanel;

    // Поля для DnD
    private CombinationSlot[] slots = new CombinationSlot[6];
    private JButton combineButton;
    private JLabel resultLabel;

    public MainTab(GameService gameService) {
        this.gameService = gameService;

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        createKnownElementsPanel();
        createCraftPanel();
        createRecipePanel();

        setupLayout();

        refreshKnownElements();
    }

    private void setupLayout() {
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(createTitledBorder("📚 Инвентарь"));
        leftPanel.setPreferredSize(new Dimension(350, 0));

        JScrollPane scrollPane = new JScrollPane(knownElementsPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        leftPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(createTitledBorder("🔮 Алхимическая лаборатория"));
        centerPanel.add(craftPanel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(createTitledBorder("📖 Гримуар рецептов"));
        rightPanel.setPreferredSize(new Dimension(600, 0));

        JScrollPane recipeScrollPane = new JScrollPane(recipePanel);
        recipeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        recipeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        rightPanel.add(recipeScrollPane, BorderLayout.CENTER);

        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }

    private void createKnownElementsPanel() {
        knownElementsPanel = new JPanel();
        knownElementsPanel.setLayout(new BorderLayout());
        knownElementsPanel.setBackground(new Color(250, 250, 250));
        knownElementsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    private void refreshKnownElements() {
        knownElementsPanel.removeAll();

        JPanel levelsPanel = new JPanel();
        levelsPanel.setLayout(new BoxLayout(levelsPanel, BoxLayout.Y_AXIS));
        levelsPanel.setBackground(new Color(250, 250, 250));

        Map<Integer, List<Element>> inventoryByLevel = gameService.getInventoryByLevel();

        for (Map.Entry<Integer, List<Element>> entry : inventoryByLevel.entrySet()) {
            int level = entry.getKey();
            List<Element> elements = entry.getValue();

            JPanel levelPanel = createLevelPanel(level, elements);

            levelPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            levelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, levelPanel.getPreferredSize().height));

            levelsPanel.add(levelPanel);
            levelsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        levelsPanel.add(Box.createVerticalGlue());

        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setBackground(new Color(250, 250, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;

        wrapperPanel.add(levelsPanel, gbc);

        knownElementsPanel.add(wrapperPanel, BorderLayout.CENTER);

        knownElementsPanel.revalidate();
        knownElementsPanel.repaint();
    }

    private JPanel createLevelPanel(int level, List<Element> elements) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(240, 240, 250));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 220), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel levelLabel = new JLabel("⭐ Уровень " + level + " (" + elements.size() + ")");
        levelLabel.setFont(Settings.getFontHeader());
        levelLabel.setForeground(new Color(70, 70, 140));
        levelLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.add(levelLabel, BorderLayout.NORTH);

        JPanel elementsGrid = new JPanel(new GridLayout(0, 3, 8, 8));
        elementsGrid.setBackground(new Color(240, 240, 250));
        elementsGrid.setBorder(new EmptyBorder(5, 5, 5, 5));

        for (Element element : elements) {
            JPanel elementBlock = createElementBlock(element);
            elementsGrid.add(elementBlock);
        }

        int remainder = elements.size() % 3;
        if (remainder > 0) {
            for (int i = 0; i < 3 - remainder; i++) {
                JPanel emptyPanel = new JPanel();
                emptyPanel.setPreferredSize(new Dimension(90, 90));
                emptyPanel.setMinimumSize(new Dimension(90, 90));
                emptyPanel.setMaximumSize(new Dimension(90, 90));
                emptyPanel.setOpaque(false);
                elementsGrid.add(emptyPanel);
            }
        }

        panel.add(elementsGrid, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createElementBlock(Element element) {
        JPanel block = new JPanel();
        block.setName("ElementBlock");
        block.putClientProperty("element", element);

        block.setLayout(new BorderLayout());
        block.setPreferredSize(new Dimension(90, 90));
        block.setMinimumSize(new Dimension(90, 90));
        block.setMaximumSize(new Dimension(90, 90));
        block.setBackground(Color.WHITE);
        block.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 200), 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // Добавляем подсказку о перетаскивании
        JLabel dragHint = new JLabel("⤴", SwingConstants.RIGHT);
        dragHint.setFont(new Font("Dialog", Font.PLAIN, 12));
        dragHint.setForeground(new Color(150, 150, 150));
        dragHint.setPreferredSize(new Dimension(20, 20));
        block.add(dragHint, BorderLayout.NORTH);

        JLabel nameLabel = new JLabel("<html><center>" + element.getName() +
                "<br><font size='-2'>ур." + element.getLevel() +
                "</font></center></html>", SwingConstants.CENTER);
        nameLabel.setFont(Settings.getFontElementName());
        nameLabel.setForeground(new Color(60, 60, 100));
        nameLabel.setPreferredSize(new Dimension(80, 60));

        block.add(nameLabel, BorderLayout.CENTER);

        // Настраиваем DragGestureRecognizer
        DragSource ds = new DragSource();
        ds.createDefaultDragGestureRecognizer(
                block,
                DnDConstants.ACTION_COPY,
                new DragGestureListener() {
                    @Override
                    public void dragGestureRecognized(DragGestureEvent dge) {
                        dge.startDrag(
                                DragSource.DefaultCopyDrop,
                                new ElementTransferable(element),
                                new DragSourceAdapter() {
                                    public void dragDropEnd(DragSourceDropEvent dsde) {
                                        if (dsde.getDropSuccess()) {
                                            System.out.println("Element " + element.getName() + " dropped successfully");
                                        }
                                    }
                                }
                        );
                    }
                }
        );

        block.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                block.setBackground(new Color(230, 230, 250));
                block.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(120, 120, 200), 2),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                block.setBackground(Color.WHITE);
                block.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(180, 180, 200), 2),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ));
            }
        });

        return block;
    }

    private void createCraftPanel() {
        craftPanel = new JPanel(new GridBagLayout());
        craftPanel.setBackground(new Color(245, 245, 250));
        craftPanel.setBorder(new CompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(30, 30, 30, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("⚗️ Комбинируй элементы", SwingConstants.CENTER);
        titleLabel.setFont(Settings.getFontTitle());
        titleLabel.setForeground(new Color(60, 60, 100));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        craftPanel.add(titleLabel, gbc);

        // Панель со слотами
        JPanel slotsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        slotsPanel.setOpaque(false);

        // Создаем слоты
        for (int i = 0; i < 6; i++) {
            slots[i] = new CombinationSlot(i, this);
            slotsPanel.add(slots[i]);
        }

        gbc.gridy = 1;
        gbc.gridwidth = 2;
        craftPanel.add(slotsPanel, gbc);

        // Панель для кнопки комбинирования и результата
        JPanel actionPanel = new JPanel(new GridBagLayout());
        actionPanel.setOpaque(false);

        combineButton = new JButton("Скомбинировать");
        combineButton.setFont(Settings.getFontCombineButton());
        combineButton.setBackground(new Color(100, 100, 180));
        combineButton.setForeground(Color.WHITE);
        combineButton.setFocusPainted(false);
        combineButton.setBorder(new EmptyBorder(10, 20, 10, 20));
//        combineButton.setEnabled(false);

        // Добавляем обработчик для комбинирования
        combineButton.addActionListener(e -> combineElements());

        resultLabel = new JLabel(" ");
        resultLabel.setFont(new Font("Dialog", Font.BOLD, 14));
        resultLabel.setForeground(new Color(70, 70, 140));

        GridBagConstraints actionGbc = new GridBagConstraints();
        actionGbc.gridx = 0;
        actionGbc.gridy = 0;
        actionGbc.insets = new Insets(5, 5, 5, 5);
        actionPanel.add(combineButton, actionGbc);

        actionGbc.gridy = 1;
        actionPanel.add(resultLabel, actionGbc);

        gbc.gridy = 2;
        craftPanel.add(actionPanel, gbc);

        // Кнопка для очистки слотов
        JButton clearButton = new JButton("Очистить");
        clearButton.setFont(new Font("Dialog", Font.PLAIN, 12));
        clearButton.addActionListener(e -> {
            clearCombinationSlots();
            resultLabel.setText(" ");
        });

        actionGbc.gridx = 1;
        actionGbc.gridy = 0;
        actionPanel.add(clearButton, actionGbc);
    }

    private void combineElements() {
        // Проверяем, все ли слоты заполнены
//        if (slots[0].isEmpty() || slots[1].isEmpty() || slots[2].isEmpty()) {
//            return;
//        }

        // Получаем элементы из слотов
        List<Element> elementOn = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Element elem = slots[i].getElement();
            if(elem != null) {
                elementOn.add(elem);
            }
        }

        // Комбинируем элементы
        // 1. Собираем имена элементов в массив строк
        String[] elementNames = elementOn.stream()
                .map(Element::getName) // Предполагаю, что у Element есть метод getName()
                .toArray(String[]::new);

        // 2. Вызываем метод и получаем ответ
        CombineResponse response = gameService.combine(elementNames);
        Element result = response.getResult();
;
        if (result != null) {
            resultLabel.setText("Результат: " + result.getName() + " (ур." + result.getLevel() + ")");

            // Очищаем слоты после успешного комбинирования
            int option = JOptionPane.showConfirmDialog(
                    this,
                    "Получен новый элемент: " + result.getName() + "\nОчистить слоты?",
                    "Комбинация успешна",
                    JOptionPane.YES_NO_OPTION
            );

            if (option == JOptionPane.YES_OPTION) {
                clearCombinationSlots();
            }

            // Обновляем инвентарь и рецепты
            refreshKnownElements();
            refreshRecipes();
        } else {
            resultLabel.setText("Ничего нового не получилось...");
        }
    }

    @Override
    public void onElementDropped(int slotIndex, Element element) {
        System.out.println("Element dropped in slot " + slotIndex + ": " + element.getName());
        updateCombineButtonState();
    }

    @Override
    public void onElementRemoved(int slotIndex) {
        System.out.println("Element removed from slot " + slotIndex);
        updateCombineButtonState();
    }

    private void updateCombineButtonState() {
        boolean hasAllSlotsFilled = true;
        for (CombinationSlot slot : slots) {
            if (slot.isEmpty()) {
                hasAllSlotsFilled = false;
                break;
            }
        }
//        combineButton.setEnabled(hasAllSlotsFilled);
    }

    private void clearCombinationSlots() {
        for (CombinationSlot slot : slots) {
            slot.removeElement();
        }
    }

    private void createRecipePanel() {
        recipePanel = new JPanel();
        recipePanel.setLayout(new BoxLayout(recipePanel, BoxLayout.Y_AXIS));
        recipePanel.setBackground(new Color(250, 250, 250));
        recipePanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        refreshRecipes();
    }

    private void refreshRecipes() {
        recipePanel.removeAll();
        recipePanel.setLayout(new BoxLayout(recipePanel, BoxLayout.Y_AXIS));

        Map<Integer, List<models.Recipe>> recipesByLevel = gameService.getAllRecipesByLevel();

        for (Map.Entry<Integer, List<models.Recipe>> entry : recipesByLevel.entrySet()) {
            int level = entry.getKey();
            List<models.Recipe> recipes = entry.getValue();

            JLabel levelLabel = new JLabel(String.format("⭐ Уровень %d (%d рецептов)", level, recipes.size()));
            levelLabel.setFont(Settings.getFontLevelLabel());
            levelLabel.setForeground(new Color(70, 70, 140));
            levelLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 5, 0));
            levelLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            recipePanel.add(levelLabel);

            JPanel levelRecipesPanel = new JPanel();
            levelRecipesPanel.setLayout(new BoxLayout(levelRecipesPanel, BoxLayout.Y_AXIS));
            levelRecipesPanel.setOpaque(false);
            levelRecipesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            for (models.Recipe recipe : recipes) {
                JPanel recipeCard = createRecipeCard(recipe);

                recipeCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, recipeCard.getPreferredSize().height));
                recipeCard.setPreferredSize(new Dimension(recipeCard.getPreferredSize().width, 50));

                levelRecipesPanel.add(recipeCard);
                levelRecipesPanel.add(Box.createRigidArea(new Dimension(0, 4)));
            }

            recipePanel.add(levelRecipesPanel);
            recipePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        recipePanel.add(Box.createVerticalGlue());

        recipePanel.revalidate();
        recipePanel.repaint();
    }

    private JPanel createRecipeCard(models.Recipe recipe) {
        JPanel recipeCard = new JPanel();
        recipeCard.setLayout(new BoxLayout(recipeCard, BoxLayout.X_AXIS));
        recipeCard.setBackground(gameService.isRecipeResultDiscovered(recipe) ?
                new Color(220, 255, 220) : new Color(250, 250, 250));
        recipeCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(gameService.isRecipeResultDiscovered(recipe) ?
                        new Color(100, 200, 100) : new Color(200, 200, 220), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        recipeCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel ingredientsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 2));
        ingredientsPanel.setOpaque(false);

        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            String ingredient = recipe.getIngredients().get(i);

            JLabel ingredientLabel = new JLabel(ingredient);
            ingredientLabel.setFont(Settings.getFontRecipeIngredient());
            ingredientLabel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(150, 150, 180), 1),
                    BorderFactory.createEmptyBorder(2, 6, 2, 6)
            ));
            ingredientLabel.setBackground(gameService.hasElement(ingredient) ?
                    new Color(200, 255, 200) : new Color(255, 230, 230));
            ingredientLabel.setOpaque(true);

            ingredientsPanel.add(ingredientLabel);

            if (i < recipe.getIngredients().size() - 1) {
                JLabel plusLabel = new JLabel("+");
                plusLabel.setFont(Settings.getFontRecipePlus());
                plusLabel.setForeground(new Color(100, 100, 150));
                ingredientsPanel.add(plusLabel);
            }
        }

        JPanel resultPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        resultPanel.setOpaque(false);

        JLabel arrowLabel = new JLabel("→");
        arrowLabel.setFont(Settings.getFontRecipeArrow());
        arrowLabel.setForeground(new Color(100, 100, 150));
        resultPanel.add(arrowLabel);

        String resultText = String.format("<html><b>%s</b> <font color='#666699' size='-1'>ур.%d</font></html>",
                recipe.getResult(), recipe.getLevel());

        JLabel resultLabel = new JLabel(resultText);
        resultLabel.setFont(Settings.getFontRecipeResult());
        resultLabel.setForeground(new Color(70, 70, 140));
        resultLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 150, 100), 1),
                BorderFactory.createEmptyBorder(3, 10, 3, 10)
        ));
        resultLabel.setBackground(new Color(220, 255, 220));
        resultLabel.setOpaque(true);

        resultPanel.add(resultLabel);

        recipeCard.add(ingredientsPanel);
        recipeCard.add(resultPanel);
        recipeCard.add(Box.createHorizontalGlue());

        return recipeCard;
    }

    private Border createTitledBorder(String title) {
        return BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 200), 2),
                title,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                Settings.getFontSubtitle(),
                new Color(70, 70, 140)
        );
    }
}