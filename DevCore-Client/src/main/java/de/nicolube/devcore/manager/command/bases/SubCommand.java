package de.nicolube.devcore.manager.command.bases;

import de.nicolube.devcore.Main;
import de.nicolube.devcore.manager.command.CommandManager;
import de.nicolube.devcore.utils.PlayerMessage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public abstract class SubCommand implements CommandBase {

    private static CommandManager manager;

    private final Map<String, SubCommand> subCommands = new HashMap<>();
    private final Map<String, SubCommand> subCommandsAliases = new HashMap<>();
    private final String name;
    private final String description;
    private final String usageMessage;
    private final String permission;
    private final List<String> aliases;
    private final String advanceName;
    private final Main plugin;

    public SubCommand(String name, String advanceName, String description, String usageMessage, String[] aliases, String permission) {
        this.plugin = Main.getPlugin();
        this.name = name.toLowerCase();
        this.advanceName = (advanceName + " " + name);
        this.description = description;
        this.usageMessage = usageMessage;
        this.aliases = Arrays.asList(aliases);
        this.permission = permission;
    }

    @Override
    public void onEnable() {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            subCommands.forEach((n, c) -> c.onEnable());
        }, 1);
    }

    public boolean execute(CommandSender sender, String labal, String[] oldArgs) {
        if (permission != null && !sender.hasPermission(permission)) {
            PlayerMessage.SYSTEM.sendNoPerm(sender);
            return true;
        }
        String[] args = new String[oldArgs.length - 1];
        for (int i = 1; i < oldArgs.length; i++) {
            args[i - 1] = oldArgs[i];
        }

        if (args.length > 0) {
            if (args[0] == null) {
                manager.helpCommand(sender, this);
                return true;
            }
            String arg0 = args[0].toLowerCase();
            if (arg0.equals("help")) {
                manager.helpCommand(sender, this);
            }
            if (subCommands.size() > 0 && subCommandsAliases.containsKey(arg0)) {
                subCommandsAliases.get(arg0).execute(sender, labal, args);
                return true;
            }
            if (!run(sender, labal, args)) {
                manager.helpCommand(sender, this);
                return true;
            }
            return true;
        }
        if (subCommands.size() > 0) {
            manager.helpCommand(sender, this);
            return true;
        }
        if (!run(sender, labal, args)) {
            manager.helpCommand(sender, this);
            return true;
        }
        return true;
    }

    @Override
    public void addSubCommand(SubCommand command) {
        List<String> aliases = command.getAliases();
        if (aliases != null) {
            aliases.forEach((alias) -> subCommandsAliases.put(alias.toLowerCase(), command));
        }
        subCommands.put(command.getName(), command);
        subCommandsAliases.put(command.getName(), command);
    }

    @Override
    public SubCommand getNextCompleter(String name) {
        name = name.toLowerCase();
        if (subCommands.containsKey(name)) {
            return subCommands.get(name);
        }
        return null;
    }

    @Override
    public List<String> getComplets(String startsWith) {
        startsWith = startsWith.toLowerCase();
        if (subCommands.isEmpty()) {
            return getDefaultComplets(startsWith);
        }
        return filterStartsWith(new ArrayList<String>(subCommands.keySet()), startsWith);
    }

    @Override
    public List<String> getComplets() {
        if (subCommands.isEmpty()) {
            return getDefaultComplets(null);
        }
        return new ArrayList<String>(subCommands.keySet());
    }

    private List<String> getDefaultComplets(String startsWith) {
        List<String> players = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach(player -> players.add(player.getName()));
        if (startsWith == null) {
            return players;
        }
        startsWith = startsWith.toLowerCase();
        return filterStartsWith(players, startsWith);
    }

    private List<String> filterStartsWith(List<String> list, String filter) {
        return list.stream().filter(s -> s.startsWith(filter)).collect(Collectors.toList());
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public Map<String, SubCommand> getSubCommands() {
        return subCommands;
    }

    @Override
    public Map<String, SubCommand> getSubCommandsAliases() {
        return subCommandsAliases;
    }

    @Override
    public String getAdvanceName() {
        return advanceName;
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    public String getUsageMessage() {
        return usageMessage;
    }

    public static void setManager(CommandManager manager) {
        SubCommand.manager = manager;
    }
}
