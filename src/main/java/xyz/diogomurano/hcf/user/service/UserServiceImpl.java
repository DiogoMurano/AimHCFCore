package xyz.diogomurano.hcf.user.service;

import com.google.common.collect.ImmutableList;
import xyz.diogomurano.hcf.user.User;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class UserServiceImpl implements UserService {

    private final Set<User> collection;

    public UserServiceImpl() {
        collection = new HashSet<>();
    }

    @Override
    public ImmutableList<User> findAll() {
        return ImmutableList.copyOf(collection);
    }

    @Override
    public void add(User user) {
        this.collection.add(user);
    }

    @Override
    public void remove(User user) {
        this.collection.remove(user);
    }

    @Override
    public User findByUniqueId(UUID uniqueId) {
        return collection.stream().filter(user -> user.getUniqueId().equals(uniqueId)).findAny().orElse(null);
    }
}
