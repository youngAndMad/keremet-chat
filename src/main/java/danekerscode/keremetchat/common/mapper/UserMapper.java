package danekerscode.keremetchat.common.mapper;

import danekerscode.keremetchat.model.dto.request.RegistrationRequest;
import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.model.enums.AuthType;
import danekerscode.keremetchat.model.enums.RoleType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(imports = {AuthType.class, RoleType.class})
public interface UserMapper {

    @Mapping(target = "emailVerified", constant = "false")
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "authType", expression = "java(AuthType.MANUAL)")
    @Mapping(target = "password", expression = "java(hashPassword)")
    User registrationRequestToUser(RegistrationRequest request,String hashPassword);

}
