package com.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TicketResponse {
    Integer rowNumber;
    Integer seatNumber;
    Boolean isBought;
}
