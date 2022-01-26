package com.erensayar.hrDayOffModuleApplication.model.enums;

import com.erensayar.hrDayOffModuleApplication.error.exception.BadRequestException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RequestStatusEnum {

    REJECTED(0),
    APPROVED(1),
    AWAITING_APPROVAL(2);

    private final Integer statusId;

    public static RequestStatusEnum getEnumById(Integer id) {
        for (RequestStatusEnum e : values()) {
            if (e.statusId.equals(id))
                return e;
        }
        throw new BadRequestException("Yanlış input. Gerçek bir enum id girin."); //TODO: Property dosyasından al
    }

}
