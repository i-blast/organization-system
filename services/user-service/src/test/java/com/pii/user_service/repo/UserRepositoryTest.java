package com.pii.user_service.repo;

import com.pii.user_service.entity.User;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.pii.user_service.util.TestDataFactory.createUserEntity;
import static com.pii.user_service.util.TestDataFactory.createUserEntityWithCompany;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveAndRetrieveUser() {
        var user = createUserEntity();
        var savedUser = userRepository.save(user);

        var users = userRepository.findAll();

        assertThat(savedUser.getId()).isNotNull();
        assertThat(users).hasSize(1);
        assertThat(users.getFirst())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(user);
    }

    @Test
    void shouldFindByCompanyId() {
        var user1 = createUserEntityWithCompany(1L);
        var user2 = createUserEntityWithCompany(2L);
        userRepository.saveAll(List.of(user1, user2));

        var usersFromCompany1 = userRepository.findByCompanyId(2L);

        assertThat(usersFromCompany1)
                .hasSize(1)
                .first()
                .extracting(User::getFirstName, User::getLastName, User::getCompanyId)
                .containsExactly("John", "Doe", 2L);
    }

    @Test
    void shouldReturnEmptyListWhenCompanyIdNotFound() {
        var users = userRepository.findByCompanyId(999L);
        assertThat(users).isEmpty();
    }

    @Test
    void shouldThrowExceptionWhenCompanyIdIsNull() {
        var user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("+1234567890")
                .companyId(null)
                .build();
        assertThatThrownBy(() -> userRepository.saveAndFlush(user))
                .isInstanceOf(ConstraintViolationException.class);
    }
}
