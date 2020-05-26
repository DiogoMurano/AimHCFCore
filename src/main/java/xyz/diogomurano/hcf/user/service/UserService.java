package xyz.diogomurano.hcf.user.service;

import com.google.common.collect.ImmutableList;
import xyz.diogomurano.hcf.user.User;

import java.util.UUID;

public interface UserService {

    ImmutableList<User> findAll();

    void add(User user);

    void remove(User user);

    User findByUniqueId(UUID uniqueId);

}
