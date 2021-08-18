package me.tofpu.contract.user.factory;

import me.tofpu.contract.user.User;
import me.tofpu.contract.user.impl.UserImpl;
import me.tofpu.contract.user.service.UserService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class UserFactory {
    private static UserService userService;
    public static void initialize(final UserService userService){
        UserFactory.userService = userService;
    }

    public static User create(final UUID uniqueId){
        final User user = new UserImpl(uniqueId);
        final Player player = Bukkit.getPlayer(uniqueId);
        if (player != null) user.setName(player.getName());
        userService.registerUser(user);

        return user;
    }
}
