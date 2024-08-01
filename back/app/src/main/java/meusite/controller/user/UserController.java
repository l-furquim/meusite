package meusite.controller.user;
import meusite.controller.user.dto.*;
import meusite.repository.user.UserJpaGateWay;
import meusite.repository.user.exception.UserException;
import meusite.repository.user.jpa.UserJpaRepository;
import meusite.repository.verifier.UserVerifierJpaGateway;
import meusite.repository.verifier.jpa.UserVerifierRepository;
import meusite.service.auth.AuthService;
import meusite.service.auth.EmailService;
import meusite.service.user.implementation.UserServiceImplementation;
import meusite.service.verifier.implementation.UserVerifierServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.security.auth.login.LoginException;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserJpaRepository userJpaRepository;

    @Autowired
    AuthService aAuthService;

    @Autowired
    UserVerifierRepository userVerifierRepository;

    @Autowired
    EmailService emailService;


    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> userRegister(@RequestBody RegisterRequestDto registerRequestDto) {
        var aGateway = UserJpaGateWay.build(userJpaRepository);

        var userVerifierGateway = UserVerifierJpaGateway.build(userVerifierRepository);

        var userVerifierService = UserVerifierServiceImplementation.build(userVerifierGateway);


        var aService = UserServiceImplementation.build(aGateway);

        aService.createUser(registerRequestDto, userVerifierService, emailService);

        return ResponseEntity.ok().body(new RegisterResponseDto("Usuario registrado com sucesso, "));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> logUser(@RequestBody LoginRequestDto loginRequest) throws LoginException {

        var aToken = aAuthService.login(loginRequest);

        return ResponseEntity.ok(aToken);
    }

    @PostMapping("/{id}/edit")
    public ResponseEntity<Void> editUser(
            @PathVariable String id, @RequestBody RequestEditUserDto requestEditUserDto) {

        var aGateway = UserJpaGateWay.build(userJpaRepository);

        var aService = UserServiceImplementation.build(aGateway);

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

    @PostMapping("/login/validateToken")
    public ResponseEntity<ValidateLoginDtoResponse> validateLogin(@RequestBody ValidateLoginDtoRequest validateLoginDtoRequest) {
        var aSubject = aAuthService.validateToken(validateLoginDtoRequest.token());


        var isValid = false;

        if (!aSubject.isBlank()) {
            isValid = true;
        }
        return ResponseEntity.ok().body(new ValidateLoginDtoResponse(isValid));

    }

    @GetMapping("/recoveryEmail")
    public ResponseEntity<RecoveryEmailResponseDto> recoveryEmailResponseDtoResponseEntity(
            @RequestHeader("Authorization") String authorizationheader) {

        var token = authorizationheader.substring(7);


        String email = aAuthService.extractSubject(token);

        return ResponseEntity.ok().body(new RecoveryEmailResponseDto(email));
    }

    @GetMapping("/getData/{cookie}")
    public ResponseEntity<GetUserDataResponseDto> getUserData(@PathVariable String cookie) {

        var aGateway = UserJpaGateWay.build(userJpaRepository);

        var aService = UserServiceImplementation.build(aGateway);

        var subject = aAuthService.extractSubject(cookie);

        var User = aService.findUserByEmail(subject).get();


        var userFormated = aService.formatUserToModel(User);

        return ResponseEntity.ok().body(new GetUserDataResponseDto(userFormated.email(), userFormated.password(), userFormated.created_at(), userFormated.followers(), userFormated.following()));
    }

    @PostMapping("/changepassword/logged")
    public ResponseEntity<ChangePasswordResponseDto> changeUserLoggedPassword(
            @RequestBody ChangePasswordRequestDto requestDto, @RequestHeader("Authorization") String header) {

        var aGateway = UserJpaGateWay.build(userJpaRepository);

        var aService = UserServiceImplementation.build(aGateway);

        var token = header.substring(7);

        var userEmail = this.aAuthService.extractSubject(token);

        return ResponseEntity.ok().body(new ChangePasswordResponseDto(
                aService.ChangePassword(userEmail, requestDto.password(), requestDto.newpassword())));
    }

    @PostMapping("/changepassword/notlogged")
    public ResponseEntity<ChangePasswordResponseDto> changeUserNotLoggedPassword(
            @RequestBody ChangePasswordNotLoggedRequestDto requestDto) {

        var aGateway = UserJpaGateWay.build(userJpaRepository);

        var aService = UserServiceImplementation.build(aGateway);

        var userVerifierGateway = UserVerifierJpaGateway.build(userVerifierRepository);

        var userService = UserVerifierServiceImplementation.build(userVerifierGateway);

        return ResponseEntity.ok().body(new ChangePasswordResponseDto(
                aService.ChangePasswordNotLoggedEmail(requestDto.email(), emailService, userService)));
    }


    @PostMapping("/password/verifiercode")
    public ResponseEntity<ChangePasswordNotLoggedrReponseDto> verfierCode(@RequestBody VerifierCodeRequestDto requestDto) {
        var aGateway = UserJpaGateWay.build(userJpaRepository);

        var aService = UserServiceImplementation.build(aGateway);

        var userVerifierGateway = UserVerifierJpaGateway.build(userVerifierRepository);

        var userService = UserVerifierServiceImplementation.build(userVerifierGateway);

        var user = aService.verifierCode(requestDto.code(), userService);


        return ResponseEntity.ok().body(
                new ChangePasswordNotLoggedrReponseDto(aService.ChangePasswordNotLogged(user, requestDto.newpassword())));
    }

    @PostMapping("/register/validate")
    public ResponseEntity<ChangePasswordNotLoggedrReponseDto> verifierLogin(@RequestBody VerifierLoginRequestDto verifierLoginRequestDto) {

        var userVerifierGateway = UserVerifierJpaGateway.build(userVerifierRepository);

        var userVerifierService = UserVerifierServiceImplementation.build(userVerifierGateway);

        var aGateway = UserJpaGateWay.build(userJpaRepository);

        var aService = UserServiceImplementation.build(aGateway);

        return ResponseEntity.ok().body(
                new ChangePasswordNotLoggedrReponseDto(aService.validateRegister(verifierLoginRequestDto.code(), userVerifierService)));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<DeleteProfileResponseDto> deleteProfile(@RequestHeader("Authorization") String header) {

        var token = header.substring(7);

        return ResponseEntity.ok().body(new DeleteProfileResponseDto(this.aAuthService.deleteUser(token)));
    }

    @PutMapping("/follow/{userId}")
    public ResponseEntity<FollowUserResponseDto> followUser(@PathVariable("userId") String userId, @RequestHeader("Authorization") String header){
        var aGateway = UserJpaGateWay.build(userJpaRepository);

        var aService = UserServiceImplementation.build(aGateway);



        var userToFollow = aService.findById(userId);
        var user = aAuthService.extractUserFromToken(header.substring(7));

        if(userToFollow.get().getId().equals(user.get().getId())){
            throw new UserException("Voce nao pode seguir voce mesmo !");
        }

        aService.updateFollowers(userToFollow.get().getFollowers() + 1, userToFollow.get().getId());
        aService.updateFollowing(user.get().getFollowing() + 1, user.get().getId());

        return ResponseEntity.ok().body(new FollowUserResponseDto(true));
    }
    @PutMapping("/unfollow/{userId}")
    public ResponseEntity<FollowUserResponseDto> unfollowUser(@PathVariable("userId") String userId, @RequestHeader("Authorization") String header){
        var aGateway = UserJpaGateWay.build(userJpaRepository);

        var aService = UserServiceImplementation.build(aGateway);


        var userToUnFollow = aService.findById(userId);
        var user = aAuthService.extractUserFromToken(header.substring(7));


        aService.updateFollowers(userToUnFollow.get().getFollowers() - 1, userToUnFollow.get().getId());
        aService.updateFollowing(user.get().getFollowing() + -1, user.get().getId());

        return ResponseEntity.ok().body(new FollowUserResponseDto(false));
    }
}
