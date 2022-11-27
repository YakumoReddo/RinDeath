package com.satsukirin.RinDeath;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.RayTraceResult;

public class RDCommand implements TabExecutor {
	
	
	private static String[] t0arg= {"respawn","revive","reviveitem","help"};
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (!(alias.equalsIgnoreCase("rind")||alias.equalsIgnoreCase("rindeath")))return null;
		if (args.length==1) return Arrays.stream(t0arg).filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length<=1) {
			if (args.length==0 || args[0].equalsIgnoreCase("help")) {
				sender.sendMessage("################");
				sender.sendMessage("#   RinDeath   #");
				sender.sendMessage("################");
				sender.sendMessage("/rind respawn                - ���������ص�������");
				sender.sendMessage("/rind revive                 - ���������е����");
				sender.sendMessage("/rind reviveitem             - ����������Ʒ�����ڸ���");
			}

			if (!(sender instanceof Player)) {
				sender.sendMessage("ֻ����ҿ���ִ����������!");
			}
			Player player = (Player)sender;
			if (args[0].equalsIgnoreCase("respawn")) {
				if(!player.hasPermission("rindeath.cmd.respawn")) {
					player.sendMessage(ChatColor.RED+"��û��ʹ�ô������Ȩ��!");
					return true;
				}
				RDSecondThread.getInstance().removePlayer(player, true);
			}
			if(args[0].equalsIgnoreCase("reviveitem")) {
				if( !player.hasPermission("rindeath.cmd.reviveitem")) {
					player.sendMessage(ChatColor.RED+"��û��ʹ�ô������Ȩ��!");
					return true;
				}
				if(player.getEquipment().getItemInMainHand().getType().equals(Material.AIR)) {
					player.sendMessage("����δ������Ʒ");
					return true;
				}
				ItemStack handitem=player.getEquipment().getItemInMainHand();
				ItemMeta meta = handitem.getItemMeta();
				meta.getPersistentDataContainer().set(new NamespacedKey(RinDeath.getInstance(), "revive"), PersistentDataType.INTEGER, 1);
				handitem.setItemMeta(meta);
			}
			if(args[0].equalsIgnoreCase("revive")) {
				if(!player.hasPermission("rindeath.cmd.revive")) {
					player.sendMessage(ChatColor.RED+"��û��ʹ�ô������Ȩ��!");
					return true;
				}
				RayTraceResult rtr= player.getWorld().rayTraceEntities(player.getEyeLocation(), player.getEyeLocation().getDirection(), 5,1.0,a -> a.getType().equals(EntityType.ARMOR_STAND));
				if (rtr==null || rtr.getHitEntity()==null) {
					player.sendMessage("�㻹û��ѡ����ҷ�Ĺ");
					return true;
				}
				ArmorStand as = (ArmorStand)rtr.getHitEntity();
				if (!as.getPersistentDataContainer().has(new NamespacedKey(RinDeath.getInstance(),"playeruuid"),PersistentDataType.STRING)) {
					player.sendMessage("�㻹û��ѡ����ҷ�Ĺ");
					return true;
				}
				RDSecondThread.getInstance().removePlayer(UUID.fromString(as.getPersistentDataContainer().get(new NamespacedKey(RinDeath.getInstance(), "playeruuid"), PersistentDataType.STRING)), false);
			}
			
		}
		return true;
	}

}
