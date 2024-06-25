package truyenconvert.server.modules.users.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.users.dtos.ChangePasswordDTO;
import truyenconvert.server.modules.users.repositories.UserRepository;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
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
        return null;
    }

    @Override
    public ResponseSuccess<Boolean> changeAvatar(MultipartFile file, User user) {
        return null;
    }
}
