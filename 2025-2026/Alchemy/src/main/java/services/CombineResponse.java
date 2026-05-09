package services;

import models.Element;

/**
 * Ответ на комбинацию элементов
 */
public class CombineResponse {
    private final CombineStatus status;
    private final Element result;

    public CombineResponse(CombineStatus status, Element result) {
        this.status = status;
        this.result = result;
    }

    public CombineStatus getStatus() {
        return status;
    }

    public Element getResult() {
        return result;
    }

    public boolean isSuccess() {
        return status == CombineStatus.SUCCESS;
    }
}