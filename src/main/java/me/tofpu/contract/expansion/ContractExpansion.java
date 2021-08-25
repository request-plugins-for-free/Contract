package me.tofpu.contract.expansion;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.tofpu.contract.user.User;
import me.tofpu.contract.user.service.UserService;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ContractExpansion extends PlaceholderExpansion {
    private final PluginDescriptionFile descriptionFile;
    private final UserService userService;

    public ContractExpansion(final PluginDescriptionFile descriptionFile, final UserService userService) {
        this.descriptionFile = descriptionFile;
        this.userService = userService;
    }

    /**
     * The placeholder identifier of this expansion. May not contain {@literal %},
     * {@literal {}} or _
     *
     * @return placeholder identifier that is associated with this expansion
     */
    @Override
    public String getIdentifier() {
        return "contract";
    }

    /**
     * The author of this expansion
     *
     * @return name of the author for this expansion
     */
    @Override
    public String getAuthor() {
        return String.valueOf(descriptionFile.getAuthors());
    }

    /**
     * The version of this expansion
     *
     * @return current version of this expansion
     */
    @Override
    public String getVersion() {
        return descriptionFile.getVersion();
    }

    @Override
    public boolean register() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(final Player player, @NotNull final String params) {
        final String[] args = params.split("_");

        switch (args[0]) {
            case "average-stars":
                final Optional<User> user = this.userService.getUser(player.getUniqueId());
                return user.map(value -> value.averageRating() + "").orElse("");
        }
        return "";
    }
}
