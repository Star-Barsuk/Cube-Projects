package services;

import models.Element;

public class CombineResponse {
    private final CombineStatus status;
    private final Element element;

    public CombineResponse(CombineStatus status, Element element) {
        this.status = status;
        this.element = element;
    }

    public CombineStatus getStatus() { return status; }
    public Element getElement() { return element; }

    public boolean isSuccess() {
        return status == CombineStatus.SUCCESS;
    }
}
