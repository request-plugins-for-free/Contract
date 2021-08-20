package me.tofpu.contract.user.factory;

import me.tofpu.contract.contract.Contract;
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
        return create("", uniqueId);
    }

    public static User create(final String name, final UUID uniqueId){
        return create(name, uniqueId, null, -1);
    }

    public static User create(final String name, final UUID uniqueId, final Contract currentContract, double totalRating){
        final User user = new UserImpl(name, uniqueId, currentContract, totalRating);
        final Player player = Bukkit.getPlayer(uniqueId);
        if (player != null) user.name(player.getName());

        return user;
    }
}
