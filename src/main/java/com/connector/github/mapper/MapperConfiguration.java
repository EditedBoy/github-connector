package com.connector.github.mapper;

import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

@MapperConfig(
    unmappedTargetPolicy = ReportingPolicy.IGNORE, // should be changed to ERROR when 1.5.0 released with fix to issue https://github.com/mapstruct/mapstruct/issues/2635
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    builder = @Builder(disableBuilder = true)
)
public interface MapperConfiguration {

}
