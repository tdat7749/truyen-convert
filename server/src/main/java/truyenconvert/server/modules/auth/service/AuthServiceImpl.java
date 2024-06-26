package truyenconvert.server.modules.auth.service;

import ch.qos.logback.classic.LoggerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.modules.auth.dtos.SignInDTO;
import truyenconvert.server.modules.auth.dtos.SignUpDTO;
import truyenconvert.server.modules.auth.exceptions.EmailUsedException;
import truyenconvert.server.modules.auth.vm.TokenVm;
import truyenconvert.server.modules.common.service.MessageService;
import truyenconvert.server.modules.jwt.service.JwtService;
import truyenconvert.server.modules.users.exceptions.AccountHasBeenLockedException;
import truyenconvert.server.modules.users.exceptions.AccountHasNotBeenLockedException;
import truyenconvert.server.modules.users.exceptions.UserHasNotVerifiedException;
import truyenconvert.server.modules.users.exceptions.UserNotFoundException;
import truyenconvert.server.models.enums.Role;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.users.service.UserService;
import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService{

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    private final MessageService messageService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

    public AuthServiceImpl(
            UserService userService,
            JwtService jwtService,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            MessageService messageService
    ){
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.messageService = messageService;
    }

    @Override
    public ResponseSuccess<TokenVm> signIn(SignInDTO dto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                dto.getEmail(),
                dto.getPassword()
        ));

        var user = userService.findByEmail(dto.getEmail()).orElse(null);
        if(user == null){
            throw new UserNotFoundException(messageService.getMessage("user.not-found"));
        }

        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        var result = TokenVm.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        LOGGER.info("Người dùng {} vừa đăng nhập thành công.",dto.getEmail());

        return new ResponseSuccess<>(messageService.getMessage("auth.sign-in.success"), result);
    }

    @Override
    public ResponseSuccess<Boolean> signUp(SignUpDTO dto) {
        var emailLowerCase = dto.getEmail().toLowerCase().trim();
        var user = userService.findByEmail(emailLowerCase).orElse(null);

        if(user != null){
            throw new EmailUsedException(messageService.getMessage("auth.email.used"));
        }
        var newUser = User.builder()
                .email(emailLowerCase)
                .password(passwordEncoder.encode(dto.getPassword()))
                .avatar("default avatar")
                .displayName(dto.getDisplayName())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .role(Role.USER)
                .build();

        userService.save(newUser);

        LOGGER.info("Người dùng {} vừa đăng ký thành công.",dto.getEmail());

        return new ResponseSuccess<>(messageService.getMessage("auth.sign-up.success"), true);
    }

    @Override
    public ResponseSuccess<Boolean> lockAccount(int id, User user) {
        var userFound = userService.findById(id).orElse(null);
        if(userFound == null){
            throw new UserNotFoundException(messageService.getMessage("user.not-found"));
        }
        if (!userFound.isVerify()){
            throw new UserHasNotVerifiedException(messageService.getMessage("user.not-verified"));
        }
        if(userFound.isLock()){
            throw new AccountHasBeenLockedException(messageService.getMessage("user.is-locked"));
        }

        userFound.setLock(true);
        userService.save(userFound);

        LOGGER.info("Khóa tài khoản {} thành công.",userFound.getEmail());

        return new ResponseSuccess<>(messageService.getMessage("auth.locked-account"), true);
    }

    @Override
    public ResponseSuccess<Boolean> unLockAccount(int id) {
        var userFound = userService.findById(id).orElse(null);
        if(userFound == null){
            throw new UserNotFoundException(messageService.getMessage("user.not-found"));
        }
        if (!userFound.isVerify()){
            throw new UserHasNotVerifiedException(messageService.getMessage("user.not-verified"));
        }
        if(!userFound.isLock()){
            throw new AccountHasNotBeenLockedException(messageService.getMessage("user.is-not-locked"));
        }

        userFound.setLock(false);
        userService.save(userFound);

        LOGGER.info("Mở khóa tài khoản {} thành công.",userFound.getEmail());

        return new ResponseSuccess<>(messageService.getMessage("auth.unlocked-account"), true);
    }

    @Override
    public ResponseSuccess<Boolean> verifyAccount(String email, User user) {
        return null;
    }

    @Override
    public ResponseSuccess<Boolean> resendEmail(String email) {
        return null;
    }
}
