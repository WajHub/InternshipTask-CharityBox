package pl.wajhub.server.mapper;

import org.mapstruct.Mapper;
import pl.wajhub.server.dto.response.CollectionBoxDtoResponse;
import pl.wajhub.server.model.CollectionBox;

@Mapper(componentModel = "spring")
public interface CollectionBoxMapper {

    CollectionBoxDtoResponse collectionBoxToCollectionBoxDtoResponse(CollectionBox collectionBox);

}
