package danekerscode.keremetchat.repository;

import danekerscode.keremetchat.model.entity.FileEntity;
import danekerscode.keremetchat.repository.common.CommonRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileEntityRepository extends CommonRepository<FileEntity, Long> {

    @Override
    default Class<FileEntity> getEntityClass() {
        return FileEntity.class;
    }
}