package com.antilamer.thingTracker.service;

import com.antilamer.thingTracker.dto.UserDTO;
import com.antilamer.thingTracker.exception.ValidationException;
import com.antilamer.thingTracker.model.UserEntity;
import com.antilamer.thingTracker.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

@Service
public class UserBO {

    @Autowired
    private UserRepo userRepo;


    public UserDTO getUser(Integer id) throws ValidationException {
        UserEntity userEntity = userRepo.findById(id).orElseThrow(() -> new ValidationException("There is no user with id: " + id));
        return new UserDTO(userEntity);
    }


    private void chceckForDuplicates(List<UserDTO> users) {
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

        private String firsName;
        private String lastName;

        UniqueUser(UserDTO userDTO) {
            this.firsName = userDTO.getFirstName();
            this.lastName = userDTO.getLastName();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UniqueUser that = (UniqueUser) o;
            return Objects.equals(firsName, that.firsName) &&
                    Objects.equals(lastName, that.lastName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(firsName, lastName);
        }
    }

}
