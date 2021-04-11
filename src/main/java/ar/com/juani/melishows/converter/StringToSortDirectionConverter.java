package ar.com.juani.melishows.converter;

import org.springframework.core.convert.converter.Converter;

import ar.com.juani.melishows.dao.specification.SortDirection;
import ar.com.juani.melishows.exception.StringToEnumConverterException;

public class StringToSortDirectionConverter implements Converter<String, SortDirection> {

    @Override
    public SortDirection convert(String source) {
        try {
        return SortDirection.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new StringToEnumConverterException(e);
        }
    }
}