package meusite.controller.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import meusite.controller.exception.handler.body.ExceptionResponseBody;
import meusite.controller.user.dto.*;
import meusite.repository.user.UserJpaGateWay;
import meusite.repository.user.jpa.UserJpaEntity;
import meusite.repository.user.jpa.UserJpaRepository;
import meusite.service.auth.AuthService;
import meusite.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.security.auth.login.LoginException;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserJpaRepository userJpaRepository;

    @Autowired
    AuthService aAuthService;

    @Autowired
    UserJpaGateWay aGateGay = UserJpaGateWay.build(userJpaRepository);

    @Autowired
    UserService aService = new UserService(aGateGay);

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> userRegister(@RequestBody RegisterRequestDto registerRequestDto){

        aService.createUser(registerRequestDto);

        return ResponseEntity.ok().body(new RegisterResponseDto("Usuario registrado com sucesso, "));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> logUser(@RequestBody LoginRequestDto loginRequest) throws LoginException {

        var aToken = aAuthService.login(loginRequest);

        return ResponseEntity.ok(aToken);
      }

    @PostMapping("/{id}/edit")
    public ResponseEntity<Void> editUser(
            @PathVariable String id, @RequestBody  RequestEditUserDto requestEditUserDto) {

        var aUser = aService.findById(id);

        if (
                aUser.isPresent() && aUser.get().getPassword().equals(
                        requestEditUserDto.password())) {
            aUser.get().setEmail(requestEditUserDto.newEmail());
            aService.save(aUser.get());
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }
    @PostMapping("/login/validate")
    public ResponseEntity<ValidateLoginDtoResponse> validateLogin(@RequestBody ValidateLoginDtoRequest validateLoginDtoRequest){
        var atoken = aAuthService.validateToken(validateLoginDtoRequest.token());

        var isValid = false;

        if(!atoken.isBlank()){
            isValid = true;

        }
        return ResponseEntity.ok().body(new ValidateLoginDtoResponse(isValid));

    }
    @GetMapping("/recoveryEmail")
    public ResponseEntity<RecoveryEmailResponseDto> recoveryEmailResponseDtoResponseEntity(
            @RequestHeader("Authorization")String authorizationheader){

        var token = authorizationheader.substring(7);



         String email = aAuthService.extractSubject(token);

        return ResponseEntity.ok().body(new RecoveryEmailResponseDto(email));
    }

    @GetMapping("/getData/{cookie}")
    public ResponseEntity<GetUserDataResponseDto> getUserData(@PathVariable String cookie){

        var subject  = aAuthService.extractSubject(cookie);

        var User = aService.findUserByEmail(subject).get();


        var userFormated = aService.formatUserToModel(User);

        return ResponseEntity.ok().body(new GetUserDataResponseDto(userFormated.email(), userFormated.password(), userFormated.created_at()));
    }

    @PostMapping("/changepassword")
    public ResponseEntity<ChangePasswordResponseDto> changeUserPassword(
            @RequestBody ChangePasswordRequestDto requestDto, @RequestHeader("Authorization")String header){

        var token = header.substring(7);

        var userEmail = this.aAuthService.extractSubject(token);

        return ResponseEntity.ok().body(new ChangePasswordResponseDto(
                aService.ChangePassword(userEmail, requestDto.password(), requestDto.newpassword())));
    }



}
