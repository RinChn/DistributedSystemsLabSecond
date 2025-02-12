package com.exception;

import lombok.Getter;

@Getter
public enum ErrorType {
    HALL_IS_OCCUPIED("A session has already been scheduled in the hall at this time and date."),
    NON_EXISTENT_MOVIE("There is no film from this director with that name in the database."),
    NON_EXISTENT_SESSION("There is no session in this room at this time."),
    REPEAT_BOOKING("This seat is occupied, choose another one."),
    FREE_SEAT("You will not be able to cancel your reservation for this place, as it is already available."),
    NON_EXISTENT_PLACE("There is no place with this number in the hall"),
    DUPLICATE_DIRECTOR("Such a director is already in the database."),
    NON_EXISTENT_DIRECTOR("There is no director with that name in the database."),
    DUPLICATE_MOVIE("Film already exists")
    ;


    private final String message;

    ErrorType(String message) {
        this.message = message;
    }
}
