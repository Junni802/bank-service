package covy.bankservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import covy.bankservice.dto.UserDto;
import covy.bankservice.jpa.repository.UserRepository;
import covy.bankservice.service.UserService;
import covy.bankservice.vo.RequestLogin;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  @Value("${jwt.token-validity-in-seconds}")
  long tokenValidityInSeconds;
  private UserService userService;
  private Environment env;

  public AuthenticationFilter(AuthenticationManager authenticationManager, UserService userService,
      Environment env) {
    super.setAuthenticationManager(authenticationManager);
    this.userService = userService;
    this.env = env;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {
    try {
      RequestLogin credit = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);

      // email과 password로 권한을 설정
      return getAuthenticationManager().authenticate(
          new UsernamePasswordAuthenticationToken(
              credit.getEmail(),
              credit.getPassword(),
              new ArrayList<>()
          )
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authResult) throws IOException, ServletException {

    String username = ((User) authResult.getPrincipal()).getUsername();
    UserDto userDto = userService.getUserDetsByEmail(username);

    String token = Jwts.builder()
        .setSubject(userDto.getEmail())
        .setExpiration(new Date(System.currentTimeMillis() + "86400000"))
        .signWith(SignatureAlgorithm.HS384, env.getProperty("jwt.secret"))
        .compact();

    response.addHeader("token", token);
    response.addHeader("userId", userDto.getUserId());
    super.successfulAuthentication(request, response, chain, authResult);
  }
}
