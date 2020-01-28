package com.mkolongo.product_shop.services;

import com.mkolongo.product_shop.domain.entities.Role;
import com.mkolongo.product_shop.domain.entities.User;
import com.mkolongo.product_shop.domain.entities.UserPrincipal;
import com.mkolongo.product_shop.domain.models.binding.UserEditModel;
import com.mkolongo.product_shop.domain.models.service.UserServiceModel;
import com.mkolongo.product_shop.domain.models.view.UserProfileModel;
import com.mkolongo.product_shop.domain.models.view.UserViewModel;
import com.mkolongo.product_shop.exception.EmailExistException;
import com.mkolongo.product_shop.exception.InvalidPassword;
import com.mkolongo.product_shop.exception.PasswordsDontMatchException;
import com.mkolongo.product_shop.exception.UsernameExistException;
import com.mkolongo.product_shop.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findUserByUsername(username)
                .map(UserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException("User with username <u>" + username + "</u> does not exist!"));
    }

    @Override
    public void register(UserServiceModel serviceModel) {
        if (isUserValid(serviceModel)) {
            final User user = mapper.map(serviceModel, User.class);
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            setRole(user);
            userRepository.saveAndFlush(user);
        }
    }

    @Override
    public List<UserViewModel> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> {
                    final UserViewModel viewModel = mapper.map(user, UserViewModel.class);
                    final Set<String> roles = user.getRoles()
                            .stream()
                            .map(role -> role.name().replace("ROLE_", ""))
                            .collect(Collectors.toSet());

                    viewModel.setRoles(roles);
                    return viewModel;
                })
                .sorted(Comparator.comparing(UserViewModel::getUsername))
                .collect(Collectors.toList());
    }

    @Override
    public void editRole(String userId, Role role) {
        userRepository.findById(userId)
                .ifPresentOrElse(user -> {

                    if (Role.ROLE_USER.equals(role)) {
                        user.setRoles(new HashSet<>(Collections.singletonList(Role.ROLE_USER)));
                    } else {
                        user.getRoles().add(role);
                    }

                    userRepository.saveAndFlush(user);
                }, () -> {
                    throw new UsernameNotFoundException("User with id <u>" + userId + "</u> does not exist!");
                });
    }

    @Override
    public void editUser(UserEditModel editModel) {
        final String userId = editModel.getId();

        userRepository.findById(userId)
                .ifPresentOrElse(user -> {

                    // check if old password is valid
                    if (!passwordEncoder.matches(editModel.getOldPassword(), user.getPassword())) {
                        throw new InvalidPassword("Invalid password!");
                    }

                    //check for changes in username
                    final String newUsername = editModel.getUsername();
                    if (!newUsername.equals(user.getUsername())) {
                        if (userRepository.findUserByUsername(newUsername).isPresent()) {
                            throw new UsernameExistException("Username " + newUsername + " is taken!");
                        }
                        user.setUsername(newUsername);
                        ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                                .getUser()
                                .setUsername(newUsername);
                    }

                    // check and confirm if new password is valid
                    final String newPassword = editModel.getNewPassword();
                    final String confirmNewPassword = editModel.getConfirmNewPassword();

                    if (!StringUtils.isEmpty(newPassword)) {
                        if (!newPassword.equals(confirmNewPassword)) {
                            throw new PasswordsDontMatchException("Passwords dont match!");
                        }
                        user.setPassword(passwordEncoder.encode(newPassword));
                    }

                    // check for changes in email
                    final String newEmail = editModel.getEmail();

                    if (!newEmail.equals(user.getEmail())) {
                        if (userRepository.findUserByEmail(newEmail).isPresent()) {
                            throw new EmailExistException("Email " + newEmail + " is taken!");
                        }
                        user.setEmail(newEmail);
                    }

                    userRepository.saveAndFlush(user);

                }, () -> {
                    throw new UsernameNotFoundException("User with id <u>" + userId + "</u> does not exist!");
                });
    }

    @Override
    public UserEditModel getUserById(String id) {
        return userRepository.findById(id)
                .map(user -> mapper.map(user, UserEditModel.class))
                .orElseThrow(() -> new UsernameNotFoundException("User with id <u>" + id + "</u> does not exist!"));
    }

    @Override
    public UserProfileModel getUserByUsername(String username) {
        return userRepository.findUserByUsername(username)
                .map(user -> mapper.map(user, UserProfileModel.class))
                .orElseThrow(() -> new UsernameNotFoundException("User with username <u>" + username + "</u> does not exist!"));
    }

    private boolean isUserValid(UserServiceModel serviceModel) {
        if (userRepository.findUserByUsername(serviceModel.getUsername()).isPresent()) {
            throw new UsernameExistException("Username " + serviceModel.getUsername() + " is taken!");
        } else if (userRepository.findUserByEmail(serviceModel.getEmail()).isPresent()) {
            throw new EmailExistException("Email " + serviceModel.getEmail() + " is taken!");
        } else if (!serviceModel.getPassword().equals(serviceModel.getConfirmPassword())) {
            throw new PasswordsDontMatchException("Passwords dont match!");
        }
        return true;
    }

    private void setRole(User user) {
        if (userRepository.count() == 0) {
            user.setRoles(Set.of(Role.ROLE_ROOT, Role.ROLE_ADMIN, Role.ROLE_MODERATOR, Role.ROLE_USER));
        } else if (userRepository.count() == 1) {
            user.setRoles(Set.of(Role.ROLE_ADMIN, Role.ROLE_MODERATOR, Role.ROLE_USER));
        } else if (userRepository.count() == 2) {
            user.setRoles(Set.of(Role.ROLE_MODERATOR, Role.ROLE_USER));
        } else {
            user.setRoles(Set.of(Role.ROLE_USER));
        }
    }
}
