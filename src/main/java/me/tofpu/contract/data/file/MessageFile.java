package me.tofpu.contract.data.file;

import com.github.requestpluginsforfree.fileutil.file.PluginFile;

import java.io.File;

public class MessageFile extends PluginFile {
    public MessageFile(final File directory) {
        super(directory, "messages.yml");
    }
}
