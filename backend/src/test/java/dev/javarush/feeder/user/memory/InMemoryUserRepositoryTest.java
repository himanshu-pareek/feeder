package dev.javarush.feeder.user.memory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.javarush.feeder.user.User;
import dev.javarush.feeder.user.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryUserRepositoryTest {

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
    }

    @Test
    void testSaveAndFindById() {
        User user = new User("user-1");
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findById("user-1");

        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());
    }

    @Test
    void testFindByIdNotFoundReturnsEmpty() {
        Optional<User> foundUser = userRepository.findById("non-existent");
        assertTrue(foundUser.isEmpty());
    }
}
