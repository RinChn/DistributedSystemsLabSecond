package com.converter;

import com.dto.DirectorDto;
import com.entity.Director;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DtoToDirectorConverter implements Converter<DirectorDto, Director> {
    @Override
    public Director convert(DirectorDto source) {
        List<String> names = List.of(source.getName().split(" "));
        return Director.builder()
                .firstName(names.get(0))
                .lastName(names.get(1))
                .build();
    }
}
