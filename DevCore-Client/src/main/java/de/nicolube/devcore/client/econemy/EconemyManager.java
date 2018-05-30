/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nicolube.devcore.client.econemy;

import de.nicolube.devcore.client.Main;
import de.nicolube.devcore.utils.SystemMessage;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author Owner
 */
public class EconemyManager implements Listener {

    private static Map<String, ModelBank> banks;
    private static DecimalFormat formatter;
    
    static {
        EconemyManager.banks = new HashMap<>();
        EconemyManager.formatter = new DecimalFormat("#0.00");
        addBank(Bukkit.getServerId(), Bukkit.getServerName());
    }
    
    public static void addBank(String name, String alias) {
        SystemMessage.INFO.send("Add Bank " + name);
        ModelBank bank =  Main.getPlugin().getDatabase().find(ModelBank.class).where().eq("name", name).findUnique();
        if (bank == null) {
            bank = new ModelBank(name, alias);
            Main.getPlugin().getDatabase().insert(bank);
        } else {
            bank.setAlias(alias);
        }
        EconemyManager.banks.put(name, bank);
    }
    
    public static ModelBank getBank(String name) {
        return banks.get(name);
    }
    
    public static ModelBank getMainBank() {
        return EconemyManager.banks.get(Bukkit.getServerId());
    }
    
    public static String format(double balance) {
        return formatter.format(balance);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onJoin(PlayerLoginEvent event) {
        banks.forEach((id, b) -> b.onJoin(event));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onQuit(PlayerQuitEvent event) {
        banks.forEach((id, b) -> b.onQuit(event));
    }
    
}