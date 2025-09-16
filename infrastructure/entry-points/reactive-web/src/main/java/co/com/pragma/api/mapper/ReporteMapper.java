package co.com.pragma.api.mapper;
import co.com.pragma.api.dto.ReporteDTO;
import co.com.pragma.model.reporte.Reporte;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReporteMapper {
    ReporteDTO toDTO(Reporte model);
}
