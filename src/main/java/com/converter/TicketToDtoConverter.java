package com.converter;

import com.dto.TicketResponse;
import com.entity.Ticket;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TicketToDtoConverter implements Converter<Ticket, TicketResponse> {
    @Override
    public TicketResponse convert(Ticket source) {
        return TicketResponse.builder()
                .isBought(source.getBought())
                .rowNumber(source.getRowNumber())
                .seatNumber(source.getPlaceNumber())
                .build();
    }
}
