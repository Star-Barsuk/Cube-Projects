package services;

import models.Element;

public class CombineResult {
    private final CombineResultType type;
    private final String message;
    private final Element newElement;

    public CombineResult(CombineResultType type, String message, Element newElement) {
        this.type = type;
        this.message = message;
        this.newElement = newElement;
    }

    public CombineResultType getType() { return type; }
    public String getMessage() { return message; }
    public Element getNewElement() { return newElement; }

    public boolean isSuccess() {
        return type == CombineResultType.SUCCESS;
    }
}
