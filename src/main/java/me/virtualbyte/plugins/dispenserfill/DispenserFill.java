package me.virtualbyte.plugins.dispenserfill;

import me.virtualbyte.byteutils.utils.DefaultMessages;
import me.virtualbyte.byteutils.utils.Messages;
import me.virtualbyte.plugins.dispenserfill.commands.FillDispensersCommand;
import me.virtualbyte.plugins.dispenserfill.commands.ReloadCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

/**
 * DispenserFill - Developed by VirtualByte (Lewes D. B.)
 */
public class DispenserFill extends JavaPlugin {

    private static DispenserFill instance;
    private        Messages      messages;

    public static DispenserFill getInstance() {
        return DispenserFill.instance;
    }

    @Override
    public void onEnable() {
        DispenserFill.instance = this;

        reloadMessages();

        getCommand("filldispensers").setExecutor(new FillDispensersCommand("filldispensers"));
        getCommand("dfreload").setExecutor(new ReloadCommand("dfreload"));

        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException e) {
        }
    }

    public Messages getMessages() {
        return this.messages;
    }

    public void reloadMessages() {
        this.messages = new Messages(this, new DefaultMessages(this));
    }

}