package com.antilamer.thingTracker.unit.user;

import com.antilamer.thingTracker.Utils;
import com.antilamer.thingTracker.domain.UserEntity;
import com.antilamer.thingTracker.dto.UserDTO;
import com.antilamer.thingTracker.exception.UnauthorizedException;
import com.antilamer.thingTracker.exception.ValidationException;
import com.antilamer.thingTracker.repository.UserRepo;
import com.antilamer.thingTracker.service.AuthenticationService;
import com.antilamer.thingTracker.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class UnitUserServiceTest {

    private static final UserEntity loggedUser = Utils.createDefaultUser();

    @InjectMocks
    private UserService userService;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private UserRepo userRepo;


    @Before
    public void onSetUpGivenUser() {
        given(authenticationService.getLoggedUser()).willReturn(loggedUser);
    }


    // Search user
    @Test
    public void getCurrentUser_Valid() throws ValidationException, UnauthorizedException {
        given(userRepo.findById(1)).willReturn(Optional.of(loggedUser));

        UserDTO userDTO = userService.getUser(1);

        assertThat("Wrong userId", userDTO.getId(), is(1));
    }

    @Test(expected = UnauthorizedException.class)
    public void getUserFromOtherGroup_ExpectUnauthorizedException() throws ValidationException, UnauthorizedException {
        given(userRepo.findById(2)).willReturn(Optional.of(Utils.createDefaultUser(2)));

        UserDTO userDTO = userService.getUser(2);

        assertThat("Wrong userId", userDTO.getId(), is(2));
    }


    // Search User Suggestions
    @Test
    public void searchUserSuggestions_Valid() {
        List<UserEntity> mockUsers = createUsers(5, 1, true);

        given(userRepo.findTop5ByFullNameOrUsername(PageRequest.of(0,5), "integrationTestUser")).willReturn(mockUsers);
        List<UserDTO> users = userService.searchUserSuggestions("integrationTestUser");

        assertThat("Wrong size of users", users.size(), is(5));
    }

    private List<UserEntity> createUsers(int quantity, int offset, boolean distinct) {
        List<UserEntity> users = new ArrayList<>();

        for (int i = offset; i < quantity + offset; i++) {
            UserEntity user = Utils.createDefaultUser(i);
            if (distinct) {
                user.setFullName(user.getFullName() + i);
                user.setEmail(user.getEmail() + i);
            }
            users.add(user);
        }

        return users;
    }


    // check users for duplicates
    @Test(expected = RuntimeException.class)
    public void checkForDuplicates_InValid() {
        List<UserEntity> mockUsers = createUsers(3, 3, false);

        given(userRepo.findTop5ByFullNameOrUsername(PageRequest.of(0,5), "integrationTestUser")).willReturn(mockUsers);
        List<UserDTO> users = userService.searchUserSuggestions("integrationTestUser");
        userService.chceckForDuplicates(users);

        assertThat("Wrong size of users", users.size(), is(3));
    }

    @Test
    public void checkForDuplicates_Valid() {
        List<UserEntity> mockUsers = createUsers(2, 2, true);

        given(userRepo.findTop5ByFullNameOrUsername(PageRequest.of(0,5), "integrationTestUser")).willReturn(mockUsers);
        List<UserDTO> users = userService.searchUserSuggestions("integrationTestUser");
        userService.chceckForDuplicates(users);

        assertThat("Wrong size of users", users.size(), is(2));
    }


}
