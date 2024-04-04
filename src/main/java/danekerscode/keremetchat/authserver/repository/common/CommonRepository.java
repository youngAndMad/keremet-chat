package danekerscode.keremetchat.authserver.repository.common;

import danekerscode.keremetchat.authserver.model.exception.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.Optional;

/**
 * CommonRepository is a generic interface that extends JpaRepository.
 * It provides common methods for all repositories.
 *
 * @param <Entity> the type of the entity
 * @param <Pk>     the type of the primary key of the entity
 */
@NoRepositoryBean
public interface CommonRepository<Entity, Pk extends Serializable> extends JpaRepository<Entity, Pk> {


    /**
     * Returns the class of the entity.
     *
     * @return the class of the entity
     */
    Class<?> entityClass();

    /**
     * Finds an entity by its id and where deleted is false.
     *
     * @param id the id of the entity
     * @return an Optional of the entity if found, empty Optional otherwise
     */
    Optional<Entity> findByIdAndDeletedFalse(Pk id);

    /**
     * Finds an entity by its id and throws an exception if not found or deleted is true.
     *
     * @param id the id of the entity
     * @return the found entity
     * @throws EntityNotFoundException if the entity is not found or deleted is true
     */
    default Entity findByID(Pk id) {
        return findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException(entityClass(), id.toString()));
    }

}