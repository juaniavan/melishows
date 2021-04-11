package ar.com.juani.melishows.converter;

import org.springframework.core.convert.converter.Converter;

import ar.com.juani.melishows.dao.specification.ShowOrderBy;
import ar.com.juani.melishows.exception.StringToEnumConverterException;

public class StringToShowOrderByConverter implements Converter<String, ShowOrderBy> {

	@Override
	public ShowOrderBy convert(String source) {
		try {
			return ShowOrderBy.valueOf(source.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new StringToEnumConverterException(e);
		}
	}
}