 package com.li709.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import com.li709.main.Spar;
import com.li709.util.ItemUtil;

/**
 * 
 * @author li709
 */
public class EventListener implements Listener{
	
	public static final double ODDS=50;
	
	/**
	 * 根据hashcode获取玩家
	 */
	HashMap<Integer, Player> playerMap;
	HashMap<Integer, ItemStack>fuelMap;
	HashMap<Integer, ItemStack>itemMap;
	public EventListener() {
		playerMap=new HashMap<Integer, Player>();
		fuelMap=new HashMap<Integer, ItemStack>();
		itemMap=new HashMap<Integer, ItemStack>();
	}
	/**
	 * 熔炉准备燃烧，获取燃烧材料
	 * @param e
	 */
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onFurnaceBurn(FurnaceBurnEvent e){
		boolean flag=furnCheck((Furnace)e.getBlock().getState());
		if (flag){
			e.setBurning(true);
			e.setBurnTime(240);
		}
		if (!flag) {
			Furnace f=(Furnace) e.getBlock().getState();
			ItemStack item=f.getInventory().getSmelting();
			Integer a=item.getTypeId();
			if (Spar.itemids.contains(a)) {
				e.setBurning(false);
				e.setCancelled(true);
			}
		}
	}
	@EventHandler
	public synchronized void onPlayerInteract(PlayerInteractEvent e){
		if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || !e.hasBlock() || !e.getClickedBlock().getType().equals(Material.FURNACE))
			return;
		Player p = e.getPlayer();
			playerMap.put(Integer.valueOf(e.getClickedBlock().hashCode()), p);
	}
	public boolean furnCheck(Furnace furn){
//		Player p = (Player)playerMap.get(Integer.valueOf(furn.getBlock().hashCode()));
		//装备
		ItemStack item = furn.getInventory().getSmelting();
		//燃料
		ItemStack fuel = furn.getInventory().getFuel();
		if(!(ItemUtil.checkItem(fuel)&&ItemUtil.checkItem(item))){
			return false;
		}
		//获取到装备上的晶石
		Map<String, Integer> map=ItemUtil.getEquipSpar(item);
		boolean flag=true;
		//如果装备上有晶石，则需对比。否则不需要对比
		if (map!=null) {
			flag=ItemUtil.checkSpar(fuel,item,map);
		}else{	//装备没有晶石，则需检查是否为宝石，并且检查是否有孔。 
			flag=ItemUtil.checkSpar(fuel, item);
		}
		if (flag) {
			fuelMap.put(Integer.valueOf(furn.getBlock().hashCode()), fuel);
			itemMap.put(Integer.valueOf(furn.getBlock().hashCode()), item);
		}
		return flag;
	}
	
	/**
	 * 燃烧出结果
	 * @param e
	 */
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onFurnaceSmelt(FurnaceSmeltEvent e){
		int hash = e.getBlock().hashCode();
		//获取玩家
		Player p=playerMap.get(hash);
		//获得装备
		ItemStack source = e.getSource();
		//获得燃料
		ItemStack fuel = fuelMap.get(Integer.valueOf(hash));
		//获得map里的装备。如果map里没有装备。则为熔炉燃烧普通物品。
		ItemStack item = itemMap.get(Integer.valueOf(hash));
		if (item==null) {
			return;
		}
		//如果在燃烧过程把装备换了。则设置燃烧结果为替换后的装备，
		if (source!=item) {
			fuelMap.remove((Integer.valueOf(hash)));
			itemMap.remove(Integer.valueOf(hash));
			e.setResult(source);
			return;
		}
		ItemStack result;
		Map<String, Object> map=new HashMap<String, Object>();
		map=ItemUtil.addLore(source, fuel);
		result=(ItemStack)map.get("物品");
		Boolean flag=(Boolean)map.get("几率");
		//如果镶嵌失败。
		if (!flag) {
			//判断物品是否有保护符
			fuel=ItemUtil.checkProtection(fuel);
			if (fuel!=null) {
				Furnace furnace=(Furnace)e.getBlock().getState();
				furnace.getInventory().setFuel(fuel);
			}
			p.sendMessage(Spar.failure_reminds);
		}
		fuelMap.remove((Integer.valueOf(hash)));
		itemMap.remove(Integer.valueOf(hash));
		if (result==null) {
			return;
		}
		e.setResult(result);
	}
	
//	@EventHandler
//	public void InventoryCline(PlayerItemHeldEvent e){
//		Player player= (Player) e.getPlayer();
//		int i=e.getNewSlot();
//		ItemStack item = player.getInventory().getItem(i);
//		GemForgeEvent event =new GemForgeEvent();
//		event.setPlayer(player);
//		if (item!=null) {
//			if (item.hasItemMeta()) {
//				ItemMeta meta=item.getItemMeta();
//				if (meta.hasLore()) {
//					List<String> lores=meta.getLore();
//					int index=lores.indexOf("§c§b§7-----------镶嵌魂石-----------");
//					if (index!=-1) {
//						String lore=lores.get(index+1);
//						int num=lore.indexOf("害+")+2;
//						int num1=lore.indexOf("§f");
//						lore=lore.substring(num, num1);
//						event.setPower(Double.parseDouble(lore));
//					}
//				}
//			}
//		}
//		GemForge.plugin.getServer().getPluginManager().callEvent(event);
//	}
//
//	@EventHandler(priority = EventPriority.LOW,ignoreCancelled=true)
//	public void InventoryCloseEvent(InventoryCloseEvent e){
//		Player player= (Player) e.getPlayer();
//		PlayerInventory inventory=player.getInventory();
//		GemForgeEvent event =new GemForgeEvent();
//		event.setPlayer(player);
//		//获取头盔上的宝石
//		ItemStack helmet=inventory.getHelmet();
//		if (helmet!=null) {
//			if (helmet.hasItemMeta()) {
//				ItemMeta meta=helmet.getItemMeta();
//				if (meta.hasLore()) {
//					List<String> lores=meta.getLore();
//					int index=lores.indexOf("§c§b§7-----------镶嵌魂石-----------");
//					if (index!=-1) {
//						String lore=lores.get(index+1);
//						int num=lore.indexOf("量+")+2;
//						int num1=lore.indexOf("§f");
//						GemForge.plugin.getLogger().info(lore+"lore"+num+"值"+num1);
//						lore=lore.substring(num, num1);
//						event.setHealth(Double.parseDouble(lore));
//					}
//				}
//			}
//		}
//		//获取鞋子上的宝石
//		ItemStack boots=inventory.getBoots();
//		if (boots!=null) {
//			if (boots.hasItemMeta()) {
//				ItemMeta meta=boots.getItemMeta();
//				if (meta.hasLore()) {
//					List<String> lores=meta.getLore();
//					int index=lores.indexOf("§c§b§7-----------镶嵌魂石-----------");
//					if (index!=-1) {
//						String lore=lores.get(index+1);
//						int num=lore.indexOf("避+")+2;
//						int num1=lore.indexOf("% §f");
//						GemForge.plugin.getLogger().info(lore+"lore"+num+"值"+num1);
//						lore=lore.substring(num, num1);
//						event.setDodge(Double.parseDouble(lore));
//					}
//				}
//			}
//		}
//		//获取衣服
//		ItemStack chestplate=inventory.getChestplate();
//		if (chestplate!=null) {
//			if (chestplate.hasItemMeta()) {
//				ItemMeta meta=chestplate.getItemMeta();
//				if (meta.hasLore()) {
//					List<String> lores=meta.getLore();
//					int index=lores.indexOf("§c§b§7-----------镶嵌魂石-----------");
//					if (index!=-1) {
//						String lore=lores.get(index+1);
//						int num=lore.indexOf("御+")+2;
//						int num1=lore.indexOf("§f");
//						GemForge.plugin.getLogger().info(lore+"lore"+num+"值"+num1);
//						lore=lore.substring(num, num1);
//						event.setArmour(Double.parseDouble(lore));
//					}
//				}
//			}
//		}
//		//获取护腿
//		ItemStack Leggings=inventory.getLeggings();
//		if (Leggings!=null) {
//			if (Leggings.hasItemMeta()) {
//				ItemMeta meta=Leggings.getItemMeta();
//				if (meta.hasLore()) {
//					List<String> lores=meta.getLore();
//					int index=lores.indexOf("§c§b§7-----------镶嵌魂石-----------");
//					if (index!=-1) {
//						String lore=lores.get(index+1);
//						int num=lore.indexOf("击+")+2;
//						int num1=lore.indexOf("§f");
//						GemForge.plugin.getLogger().info(lore+"lore"+num+"值"+num1);
//						lore=lore.substring(num, num1);
//						event.setCritChance(Double.parseDouble(lore));
//					}
//				}
//			}
//		}
//		ItemStack  weapon =player.getItemInHand();
//		if (weapon!=null) {
//			if (weapon.hasItemMeta()) {
//				ItemMeta meta=weapon.getItemMeta();
//				if (meta.hasLore()) {
//					List<String> lores=meta.getLore();
//					int index=lores.indexOf("§c§b§7-----------镶嵌魂石-----------");
//					if (index!=-1) {
//						String lore=lores.get(index+1);
//						int num=lore.indexOf("害+")+2;
//						int num1=lore.indexOf("§f");
//						GemForge.plugin.getLogger().info(lore+"lore"+num+"值"+num1);
//						lore=lore.substring(num, num1);
//						event.setPower(Double.parseDouble(lore));
//					}
//				}
//			}
//		}
//		GemForge.plugin.getServer().getPluginManager().callEvent(event);
//	}
}
