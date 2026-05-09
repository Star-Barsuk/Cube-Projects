package services;

/**
 * Статус комбинации
 */
enum CombineStatus {
    SUCCESS,           // Успешно создан новый элемент
    ALREADY_DISCOVERED, // Элемент уже открыт
    NOTHING,           // Ничего не получилось
    MISSING_ELEMENTS,  // Нет одного из элементов в инвентаре
    INVALID_INPUT      // Неверный ввод (меньше 2 элементов)
}
