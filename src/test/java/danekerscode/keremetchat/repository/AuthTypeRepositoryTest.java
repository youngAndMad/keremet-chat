package danekerscode.keremetchat.repository;

import danekerscode.keremetchat.model.entity.AuthType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

//@DataJpaTest
class AuthTypeRepositoryTest {

//    @Autowired
    private AuthTypeRepository authTypeRepository;

//    @Test
    void findByNameIgnoreCase_ExistsByName_shouldReturnAuthType() {
        var authType = new AuthType();
        var randomName = "RANDOM_NAME";
        authType.setName(randomName);
        authTypeRepository.save(authType);

        var actualAuthType = authTypeRepository.findByNameIgnoreCase(randomName);

        assertTrue(actualAuthType.isPresent());
        assertEquals(randomName, actualAuthType.get().getName());
    }

//    @Test
    void findByNameIgnoreCase_NotExistsByName_shouldReturnOptionalEmpty() {
        var randomName = "RANDOM_NAME";
        var actualAuthType = authTypeRepository.findByNameIgnoreCase(randomName);

        assertTrue(actualAuthType.isEmpty());
    }

}