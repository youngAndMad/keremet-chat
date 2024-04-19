package danekerscode.keremetchat.service.impl;

import danekerscode.keremetchat.model.entity.AuthType;
import danekerscode.keremetchat.repository.AuthTypeRepository;
import danekerscode.keremetchat.service.AuthTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthTypeServiceImpl implements AuthTypeService {

    private final AuthTypeRepository authTypeRepository;

    @Override
    public AuthType getOrCreateByName(String name) {
        return authTypeRepository
                .findByNameIgnoreCase(name)
                .orElseGet(() -> {
                    var authType = new AuthType();
                    authType.setName(name.toUpperCase());
                    return authTypeRepository.save(authType);
                });
    }
}
