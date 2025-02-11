package com.converter;

import com.dto.DirectorDto;
import com.entity.Director;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component

public class DirectorToDtoConverter implements Converter<Director, DirectorDto> {
    @Override
    public DirectorDto convert(Director source) {
        return DirectorDto.builder()
                .name(source.getFirstName() + " " + source.getLastName())
                .build();
    }
}
