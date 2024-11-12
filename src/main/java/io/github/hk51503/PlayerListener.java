package io.github.hk51503;

import org.bukkit.ChatColor;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.Bukkit;

public class PlayerListener implements Listener {
    private final SimplePassword plugin;

    public PlayerListener(SimplePassword plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!plugin.getAuthenticatedPlayers().contains(player.getName())) {
            player.sendMessage(ChatColor.RED + "You cannot move until you enter the password.");
            player.sendMessage(ChatColor.RED + "To enter the password, use /password [PASSWORD]");
            player.sendMessage(ChatColor.RED + "パスワードを入力するまで動けません。");
            player.sendMessage(ChatColor.RED + "パスワードは /password [PASSWORD] で入力してください。");
            // Freeze player
            player.setWalkSpeed(0);
            // Prevent visibility
            player.setInvisible(true); // Prevent visibility
            // Apply blindness effect
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 1, false, false));
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (!plugin.getAuthenticatedPlayers().contains(player.getName())) {
            // Apply blindness effect
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
                // The stuff which should be accomplished after the timer runs out goes here.
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 1, false, false));
            }
            }, 1);
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (!plugin.getAuthenticatedPlayers().contains(player.getName())) {
            String[] args = event.getMessage().split(" ");
            if (args[0].equalsIgnoreCase("/password")) {
                String correctPassword = plugin.getConfig().getString("password");
                if (args.length == 2 && args[1].equals(correctPassword)) {
                    plugin.addAuthenticatedPlayer(player.getName());
                    player.sendMessage(ChatColor.GREEN + "Access granted! You can move freely now.");
                    player.sendMessage(ChatColor.GREEN + "制限が解除されました。");
                    // Unfreeze player
                    player.setWalkSpeed(0.2f);
                    // Make visible again
                    player.setInvisible(false);
                    // Remove blindness effect
                    player.removePotionEffect(PotionEffectType.BLINDNESS);
                } else {
                    player.sendMessage(ChatColor.RED + "Incorrect password. Try again.");
                    player.sendMessage(ChatColor.RED + "正しいパスワードを入力してください");
                }
                event.setCancelled(true); // Prevent command from being processed further
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!plugin.getAuthenticatedPlayers().contains(player.getName())) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You must enter the password to break blocks.");
            player.sendMessage(ChatColor.RED + "パスワードを入力してください。");
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (!plugin.getAuthenticatedPlayers().contains(player.getName())) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You must enter the password to place blocks.");
            player.sendMessage(ChatColor.RED + "パスワードを入力してください。");
        }
    }
}
