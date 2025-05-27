package covy.bankservice.service;

import covy.bankservice.dto.UserDto;
import covy.bankservice.jpa.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

  UserDto createUser(UserDto userDto);

  UserDto getUserByUserId(String userId);

  Iterable<UserEntity> getUserByAll();

  UserDto getUserDetsByEmail(String userName);

}
