package me.tofpu.contract.user.factory;

import me.tofpu.contract.contract.Contract;
import me.tofpu.contract.user.User;
import me.tofpu.contract.user.impl.UserImpl;
import me.tofpu.contract.user.properties.stars.review.UserReview;
import me.tofpu.contract.user.service.UserService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class UserFactory {
    private static UserService userService;
    public static void initialize(final UserService userService){
        UserFactory.userService = userService;
    }

    public static User create(final UUID uniqueId){
        final User user = new UserImpl(uniqueId);
        final Player player = Bukkit.getPlayer(uniqueId);
        if (player != null) user.name(player.getName());
        userService.registerUser(user);

        return user;
    }

    public static User create(final String name, final UUID uniqueId, final List<UserReview> userReviews){
        final User user = new UserImpl(name, uniqueId, null, 0, userReviews);
        final Player player = Bukkit.getPlayer(uniqueId);
        if (player != null) user.name(player.getName());
        userService.registerUser(user);

        return user;
    }

    public static User create(final String name, final UUID uniqueId, double totalRating, final List<UserReview> userReviews){
        final User user = new UserImpl(name, uniqueId, null, totalRating, userReviews);
        final Player player = Bukkit.getPlayer(uniqueId);
        if (player != null) user.name(player.getName());
        userService.registerUser(user);

        return user;
    }

    public static User create(final String name, final UUID uniqueId, final Contract currentContract, double totalRating, final List<UserReview> userReviews){
        final User user = new UserImpl(name, uniqueId, currentContract, totalRating, userReviews);
        final Player player = Bukkit.getPlayer(uniqueId);
        if (player != null) user.name(player.getName());
        userService.registerUser(user);

        return user;
    }
}
