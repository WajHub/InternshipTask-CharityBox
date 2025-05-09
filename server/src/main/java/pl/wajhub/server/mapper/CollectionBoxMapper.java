package pl.wajhub.server.mapper;

import org.mapstruct.Mapper;
import pl.wajhub.server.dto.response.CollectionBoxDtoResponse;
import pl.wajhub.server.model.BoxMoney;
import pl.wajhub.server.model.CollectionBox;

@Mapper(componentModel = "spring")
public interface CollectionBoxMapper {

    default CollectionBoxDtoResponse collectionBoxToCollectionBoxDtoResponse(CollectionBox collectionBox){
        return
            CollectionBoxDtoResponse.builder()
                .uuid(collectionBox.getUuid())
                .isAssigned(collectionBox.getEvent()!=null)
                .isEmpty(collectionBox.isEmpty())
            .build();
    }

}
