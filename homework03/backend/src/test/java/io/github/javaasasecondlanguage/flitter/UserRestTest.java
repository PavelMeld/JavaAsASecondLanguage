package io.github.javaasasecondlanguage.flitter;

import io.github.javaasasecondlanguage.flitter.utils.FlitterRestMethods;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;

import static io.github.javaasasecondlanguage.flitter.utils.AssertionUtils.assertSetEquals;
import static io.github.javaasasecondlanguage.flitter.utils.ExpectedStatus.EXPECT_FAIL;
import static io.github.javaasasecondlanguage.flitter.utils.ExpectedStatus.EXPECT_SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserRestTest {

    private FlitterRestMethods rest;

    public UserRestTest(
            @Autowired TestRestTemplate restTemplate,
            @LocalServerPort int port
    ) {
        this.rest = new FlitterRestMethods(restTemplate, port);
    }

    @BeforeEach
    void setUp() {
        rest.clear();
    }

    @Test
    public void test_listUsers_empty() {
        var users = rest.listUsers(EXPECT_SUCCESS);
        assertEquals(0, users.size());
    }

    @Test
    public void test_addUser_noCheck() {
        rest.addUser("Sasha", EXPECT_SUCCESS);
    }

    @Test
    public void test_addUser_singleUser() {
        rest.addUser("Sasha", EXPECT_SUCCESS);
        var users = rest.listUsers(EXPECT_SUCCESS);
        assertEquals(List.of("Sasha"), users);
    }

    @Test
    public void test_addUser_multipleUsers() {
        var inputUsers = List.of("Sasha", "Nikita");

        rest.addAllUsers(inputUsers, EXPECT_SUCCESS);
        var listedUsers = rest.listUsers(EXPECT_SUCCESS);

        assertSetEquals(inputUsers, listedUsers);
    }

    @Test
    void test_addUser_sameName() {
        rest.addUser("Sasha", EXPECT_SUCCESS);
        rest.addUser("Sasha", EXPECT_FAIL);
        var users = rest.listUsers(EXPECT_SUCCESS);
        assertEquals(List.of("Sasha"), users);
    }
}
