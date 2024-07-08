package truyenconvert.server.modules.users.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.common.service.MessageService;
import truyenconvert.server.modules.storage.service.S3FileStorageService;
import truyenconvert.server.modules.users.dtos.ChangePasswordDTO;
import truyenconvert.server.modules.users.exceptions.CurrentPasswordNotMatchException;
import truyenconvert.server.modules.users.exceptions.NewPasswordNotMatchException;
import truyenconvert.server.modules.users.repositories.UserRepository;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Value("${truyencv.bucket-name}")
    private String bucketName;

    @Value("${truyencv.avatars}")
    private String avatarsFolder;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageService messageService;

    private final S3FileStorageService s3FileStorageService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            MessageService messageService,
            S3FileStorageService s3FileStorageService
    ){
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.messageService = messageService;
        this.s3FileStorageService = s3FileStorageService;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public ResponseSuccess<Boolean> changePassword(ChangePasswordDTO dto, User user) {
        boolean isMatchPassword = passwordEncoder.matches(dto.getCurrentPassword(),user.getPassword());
        if(!isMatchPassword){
            throw new CurrentPasswordNotMatchException(messageService.getMessage("user.is-not-match-password"));
        }

        boolean isNewPasswordMatch = dto.getNewPassword().equals(dto.getConfirmNewPassword());
        if(!isNewPasswordMatch){
            throw new NewPasswordNotMatchException(messageService.getMessage("user.new-password-not-match"));
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        this.save(user);
        LOGGER.info("Người dùng {} vừa thay đổi mật khẩu thành công",user.getEmail());
        return new ResponseSuccess<>(messageService.getMessage("user.change-password.success"),true);
    }

    @Override
    @Transactional
    public ResponseSuccess<Boolean> changeAvatar(MultipartFile file, User user) {
        var avatarUrl = s3FileStorageService.saveFile(file,avatarsFolder+ "/" + user.getEmail().split("@")[1],bucketName);

        user.setAvatar(avatarUrl);
        this.save(user);

        LOGGER.info("Người dùng {} vừa thay đổi hình đại diện thành công",user.getEmail());

        return new ResponseSuccess<>(messageService.getMessage("user.change-avatar.success"), true);
    }
}
