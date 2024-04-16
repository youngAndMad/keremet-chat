package danekerscode.keremetchat.repository.common;

import danekerscode.keremetchat.model.exception.EntityNotFoundException;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

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
    @SuppressWarnings("unchecked")
    default Class<Entity> getEntityClass() {
        return (Class) ((ParameterizedType) getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
    }

    boolean existsById(@NonNull Pk id);

    /**
     * Checks if an entity exists by its id.
     *
     * @param id the id of the entity
     * @return true if the entity exists, false otherwise
     */
    default boolean isExistByID(@NonNull Pk id) {
        return existsById(id);
    }


    /**
     * Finds an entity by its id and throws an exception if not found or deleted is true.
     *
     * @param id the id of the entity
     * @return the found entity
     * @throws EntityNotFoundException if the entity is not found or deleted is true
     */
    default Entity findByID(Pk id) {
        return findById(id).orElseThrow(() -> new EntityNotFoundException(getEntityClass(), id.toString()));
    }

}