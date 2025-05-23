package covy.bankservice.service;

import covy.bankservice.dto.UserDto;
import covy.bankservice.jpa.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * <클래스 설명>
 *
 * @author : junni802
 * @date : 2025-02-18
 */
public interface UserService extends UserDetailsService {

  UserDto createUser(UserDto userDto);

  UserDto getUserByUserId(String userId);

  Iterable<UserEntity> getUserByAll();

  UserDto getUserDetsByEmail(String userName);

}
