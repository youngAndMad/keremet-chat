package danekerscode.keremetchat.core.mapper;

import danekerscode.keremetchat.model.dto.request.RegistrationRequest;
import danekerscode.keremetchat.model.dto.response.UserResponseDto;
import danekerscode.keremetchat.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {

    @Mapping(target = "password", expression = "java(hashPassword)")
    User registrationRequestToUser(RegistrationRequest request,String hashPassword);

    UserResponseDto toResponseDto(User user);
}