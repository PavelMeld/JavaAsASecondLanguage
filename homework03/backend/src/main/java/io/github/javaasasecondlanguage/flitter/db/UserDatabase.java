package io.github.javaasasecondlanguage.flitter.db;

import io.github.javaasasecondlanguage.flitter.UserExistException;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserDatabase {
    Map<String, String> userToToken;
    Map<String, String> tokenToUser;
    static final String ALREADY_TAKEN = "This name is already taken";

    public void clear() {
        userToToken = new HashMap();
        tokenToUser = new HashMap();
    }

    public UserDatabase() {
        clear();
    }

    public String addUser(String userName) throws UserExistException {
        if (userToToken.containsKey(userName)) {
            throw new UserExistException(ALREADY_TAKEN);
        }

        // Generate simple MD5 with length salt
        String curTime = Instant.now().toString();
        // Temporary
        curTime = Instant.MIN.toString();

        ByteBuffer rawData = ByteBuffer.allocate(Integer.BYTES
                + userName.length() + curTime.length());

        rawData.putInt(userName.length());
        rawData.put(userName.getBytes());
        rawData.put(curTime.getBytes());

        String userHash = DigestUtils.md5DigestAsHex(rawData.array());

        userToToken.put(userName, userHash);
        tokenToUser.put(userHash, userName);

        return userHash;
    }

    public List<String> getUserList() {
        return userToToken.keySet().stream().collect(Collectors.toList());
    }

    public boolean hasToken(String userToken) {
        return tokenToUser.containsKey(userToken);
    }

    public String getUserByToken(String token) {
        return tokenToUser.get(token);
    }

    public boolean userExists(String userName) {
        return userToToken.containsKey(userName);
    }
}
