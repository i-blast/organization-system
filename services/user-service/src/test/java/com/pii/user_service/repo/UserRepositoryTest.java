package com.pii.user_service.repo;

import com.pii.user_service.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.pii.user_service.util.TestDataFactory.createTestUser;
import static com.pii.user_service.util.TestDataFactory.createTestUserWithCompany;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveAndRetrieveUser() {
        var user = createTestUser();
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
        var user1 = createTestUserWithCompany(1L);
        var user2 = createTestUserWithCompany(2L);
        userRepository.saveAll(List.of(user1, user2));

        var usersFromCompany1 = userRepository.findByCompanyId(2L);

        assertThat(usersFromCompany1)
                .hasSize(1)
                .first()
                .extracting(User::getFirstName, User::getLastName, User::getCompanyId)
                .containsExactly("John", "Doe", 2L);
    }
}
