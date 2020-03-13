package com.antilamer.thingTracker.service;

import com.antilamer.thingTracker.dto.UserDTO;
import com.antilamer.thingTracker.exception.UnauthorizedException;
import com.antilamer.thingTracker.exception.ValidationException;
import com.antilamer.thingTracker.domain.UserEntity;
import com.antilamer.thingTracker.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserBO {

    private final UserRepo userRepo;
    private final AuthenticationBO authenticationBO;


    public UserDTO getUser(Integer id) throws ValidationException, UnauthorizedException {
        UserEntity userEntity = userRepo.findById(id).orElseThrow(() -> new ValidationException("There is no user with id: " + id));
        checkUserAuthorization(userEntity);

        UserDTO userDTO = new UserDTO(userEntity);
        userEntity.getRoles().forEach(x -> userDTO.getRoles().add(x.getCode()));
        return userDTO;
    }

    private void checkUserAuthorization(UserEntity userEntity) throws UnauthorizedException {
        UserEntity loggedUser = authenticationBO.getLoggedUser();
        boolean authorized = loggedUser.getGroups()
                .stream().anyMatch(group -> group.getUsers()
                        .stream().anyMatch(user -> user.getId().equals(userEntity.getId())));
        if (!authorized) {
            throw new UnauthorizedException("User does not belong to any of your groups!");
        }
    }

    public List<UserDTO> searchUserSuggestions(String predicate) {
        return userRepo.findTop5ByFullNameOrUsername(PageRequest.of(0,5), predicate).stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }


    public void chceckForDuplicates(List<UserDTO> users) {
        long distinctCount = users.stream().filter(distinctByFullName()).count();

        if (distinctCount < users.size()) {
            throw new RuntimeException();
        }
    }

    private Predicate<UserDTO> distinctByFullName() {
        Set<UniqueUser> seen = new HashSet<>();
        return x -> seen.add(new UniqueUser(x));
    }



    private class UniqueUser {

        private String fullName;
        private String email;

        UniqueUser(UserDTO userDTO) {
            this.fullName = userDTO.getFullName();
            this.email = userDTO.getEmail();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UniqueUser that = (UniqueUser) o;
            return Objects.equals(fullName, that.fullName) &&
                    Objects.equals(email, that.email);
        }

        @Override
        public int hashCode() {
            return Objects.hash(fullName, email);
        }
    }

}
