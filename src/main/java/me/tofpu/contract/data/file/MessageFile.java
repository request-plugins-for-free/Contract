package me.tofpu.contract.data.file;

import com.github.requestpluginsforfree.fileutil.file.PluginFile;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class MessageFile extends PluginFile {
    public MessageFile(final Plugin plugin, final File directory) {
        super(plugin, directory, "messages.yml");
    }
}
