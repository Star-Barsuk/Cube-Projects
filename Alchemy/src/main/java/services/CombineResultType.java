package services;

public enum CombineResultType {
    SUCCESS,           // Успешно создан новый элемент
    ALREADY_DISCOVERED, // Элемент уже был открыт
    MISSING_ELEMENTS,   // Нет одного из элементов
    NOTHING            // Ничего не произошло
}
