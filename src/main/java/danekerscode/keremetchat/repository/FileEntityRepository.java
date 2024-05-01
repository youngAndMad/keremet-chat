package danekerscode.keremetchat.repository;

import danekerscode.keremetchat.model.entity.FileEntity;
import danekerscode.keremetchat.repository.common.CommonRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileEntityRepository extends CommonRepository<FileEntity, Long> {

    @Override
    default Class<FileEntity> getEntityClass() {
        return FileEntity.class;
    }

    Optional<FileEntity> findByPath(String path);
}