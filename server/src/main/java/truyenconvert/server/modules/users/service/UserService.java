package truyenconvert.server.modules.users.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.users.dtos.ChangePasswordDTO;

import java.util.Optional;

@Service
public interface UserService {
    Optional<User> findByEmail(String email);
    Optional<User> findById(int id);
    User save(User user);
    ResponseSuccess<Boolean> changePassword(ChangePasswordDTO dto,User user);
    ResponseSuccess<Boolean> changeAvatar(MultipartFile file,User user);
}
