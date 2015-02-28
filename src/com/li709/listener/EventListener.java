package com.li709.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import com.li709.main.Spar;
import com.li709.util.ItemUtil;

/**
 * 
 * @author li709
 */
public class EventListener implements Listener {

  /**
   * 根据hashcode获取玩家
   */
  private static final int WEAPONS = 0;
  private static final int HELMET = 1;
  private static final int CHESTPLATE = 2;
  private static final int LEGGINGS = 3;
  private static final int BOOTS = 4;
  HashMap<Integer, Player> playerMap;
  HashMap<Integer, ItemStack> fuelMap;
  HashMap<Integer, ItemStack> itemMap;

  public EventListener() {
    playerMap = new HashMap<Integer, Player>();
    fuelMap = new HashMap<Integer, ItemStack>();
    itemMap = new HashMap<Integer, ItemStack>();
  }

//  /**
//   * 熔炉准备燃烧，获取燃烧材料,准备打孔
//   * 
//   * @param e
//   */
//  @SuppressWarnings("deprecation")
//  @EventHandler
//  public void onFurnaceBurnPunch(FurnaceBurnEvent e) {
//    boolean flag = furnCheckPunch((Furnace) e.getBlock().getState());
//    if (flag) {
//      e.setBurning(true);
//      e.setBurnTime(240);
//    }
//    if (!flag) {
//      Furnace f = (Furnace) e.getBlock().getState();
//      ItemStack item = f.getInventory().getSmelting();
//      Integer a = item.getTypeId();
//      if (Spar.itemids.contains(a)) {
//        e.setBurning(false);
//        e.setCancelled(true);
//      }
//    }
//  }

  @SuppressWarnings("deprecation")
  @EventHandler
  public void onFurnaceBurn(FurnaceBurnEvent e) {
    boolean flag = furnCheck((Furnace) e.getBlock().getState());
    if (flag) {
      e.setBurning(true);
      e.setBurnTime(240);
      return;
    }
    flag = furnCheckPunch((Furnace) e.getBlock().getState());
    if (flag) {
        e.setBurning(true);
        e.setBurnTime(240);
        return;
      }
    if (!flag) {
      Furnace f = (Furnace) e.getBlock().getState();
      ItemStack item = f.getInventory().getSmelting();
      Integer a = item.getTypeId();
      if (Spar.itemids.contains(a)) {
        e.setBurning(false);
        e.setCancelled(true);
      }
    }
  }

  @EventHandler
  public synchronized void onPlayerInteract(PlayerInteractEvent e) {
    if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || !e.hasBlock()
        || !e.getClickedBlock().getType().equals(Material.FURNACE))
      return;
    Player p = e.getPlayer();
    playerMap.put(Integer.valueOf(e.getClickedBlock().hashCode()), p);
  }
	public boolean furnCheck(Furnace furn) {
		// Player p =
		// (Player)playerMap.get(Integer.valueOf(furn.getBlock().hashCode()));
		// 装备
		ItemStack item = furn.getInventory().getSmelting();
		// 燃料
		ItemStack fuel = furn.getInventory().getFuel();
		if (!(ItemUtil.checkItem(fuel) && ItemUtil.checkItem(item))) {
			return false;
		}
		// 获取到装备上的晶石
		Map<String, Integer> map = ItemUtil.getEquipSpar(item);
		boolean flag = true;
		// 如果装备上有晶石，则需对比。否则不需要对比
		if (!map.isEmpty()) {
			flag = ItemUtil.checkSpar(fuel, item, map);
		} else { // 装备没有晶石，则需检查是否为宝石，并且检查是否有孔。
			flag = ItemUtil.checkSpar(fuel, item);
		}
		if (flag) {
			fuelMap.put(Integer.valueOf(furn.getBlock().hashCode()), fuel);
			itemMap.put(Integer.valueOf(furn.getBlock().hashCode()), item);
		}
		return flag;
	}
  public boolean furnCheckPunch(Furnace furn) {
    // 装备
    ItemStack item = furn.getInventory().getSmelting();
    // 燃料
    ItemStack fuel = furn.getInventory().getFuel();
    if (!(ItemUtil.checkItem(fuel) && ItemUtil.checkItem(item))) {
      return false;
    }
    //判断开孔数小于3才可以
    if (ItemUtil.ifPunch(item)<3&&ItemUtil.ifUnperforated(item)) {
      for (String lore : fuel.getItemMeta().getLore()) {
        //如果是打孔符
        if (lore.contains(Spar.punch_charm)) {
          fuelMap.put(Integer.valueOf(furn.getBlock().hashCode()), fuel);
          itemMap.put(Integer.valueOf(furn.getBlock().hashCode()), item);
          return true;
        }
      }
    }
    return false;
  }

  /**
   * 燃烧出结果,打孔结果
   * 
   * @param e
   */
  @SuppressWarnings("deprecation")
  @EventHandler
  public void onFurnaceSmeltPunch(FurnaceSmeltEvent e) {
    int hash = e.getBlock().hashCode();
    // 获取玩家
//    Player p = playerMap.get(hash);
    // 获得装备
    ItemStack source = e.getSource();
    // 获得燃料
    ItemStack fuel = fuelMap.get(Integer.valueOf(hash));
    // 获得map里的装备。如果map里没有装备。则为熔炉燃烧普通物品。
    ItemStack item = itemMap.get(Integer.valueOf(hash));
    if (item == null) {
      return;
    }
    if (fuel==null||!fuel.hasItemMeta()) {
      return;
    }
    boolean flag=true;
    if (fuel.getItemMeta().hasLore()) {
      for (String lore : fuel.getItemMeta().getLore()) {
        //如果燃料是打孔符
        if (lore.contains(Spar.punch_charm)) {
          flag=false;
        }
      }
    }else {
      return;
    }
    
    if (flag) {
      return;
    }
    // 如果在燃烧过程把装备换了。则设置燃烧结果为替换后的装备，
    if (source.getTypeId() != item.getTypeId()) {
      fuelMap.remove((Integer.valueOf(hash)));
      itemMap.remove(Integer.valueOf(hash));
      e.setResult(source);
      //助手
      return;
    }
    ItemStack result=ItemUtil.addPunch(source);
    fuelMap.remove((Integer.valueOf(hash)));
    itemMap.remove(Integer.valueOf(hash));
    if (result == null) {
      e.setResult(source);
      return;
    }
    e.setResult(result);
  }
  /**
   * 燃烧出结果
   * 
   * @param e
   */
  @SuppressWarnings("deprecation")
  @EventHandler
  public void onFurnaceSmelt(FurnaceSmeltEvent e) {
    int hash = e.getBlock().hashCode();
    // 获取玩家
    Player p = playerMap.get(hash);
    // 获得装备
    ItemStack source = e.getSource();
    // 获得燃料
    ItemStack fuel = fuelMap.get(Integer.valueOf(hash));
    // 获得map里的装备。如果map里没有装备。则为熔炉燃烧普通物品。
    ItemStack item = itemMap.get(Integer.valueOf(hash));
    if (item == null) {
      return;
    }
    if (fuel==null||!fuel.hasItemMeta()) {
      return;
    }
    boolean flag=true;
    if (fuel.getItemMeta().hasLore()) {
      for (String lore : fuel.getItemMeta().getLore()) {
        //如果燃料是晶石
        if (ItemUtil.ifSpar(lore)) {
          flag=false;
        }
      }
    }else {
      return;
    }
    
    if (flag) {
      return;
    }
    // 如果在燃烧过程把装备换了。则设置燃烧结果为替换后的装备，
    if (source.getTypeId() != item.getTypeId()) {
      fuelMap.remove((Integer.valueOf(hash)));
      itemMap.remove(Integer.valueOf(hash));
      e.setResult(source);
      return;
    }
    ItemStack result;
    Map<String, Object> map = new HashMap<String, Object>();
    map = ItemUtil.addLore(source, fuel);
    result = (ItemStack) map.get("物品");
    Boolean flag1 = (Boolean) map.get("几率");
    // 如果镶嵌失败。
    if (!flag1) {
      // 判断物品是否有保护符
      fuel = ItemUtil.checkProtection(fuel);
      if (fuel != null) {
        Furnace furnace = (Furnace) e.getBlock().getState();
        furnace.getInventory().setFuel(fuel);
      }
      p.sendMessage(Spar.failure_reminds);
    }
    fuelMap.remove((Integer.valueOf(hash)));
    itemMap.remove(Integer.valueOf(hash));
    if (result == null) {
      return;
    }
    e.setResult(result);
  }

  // 武器切换的时候
  @EventHandler
  public void InventoryCline(PlayerItemHeldEvent e) {
    Player player = (Player) e.getPlayer();
    int i = e.getNewSlot();
    ItemStack item = player.getInventory().getItem(i);
    SparForgeEvent event = ItemUtil.checkItemLore(item);
    if (event != null) {
      event.setPlayer(player);
      event.setNum(EventListener.WEAPONS);
      Spar.plugin.getServer().getPluginManager().callEvent(event);
    }
  }

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void InventoryCloseEvent(InventoryCloseEvent e) {
    Player player = (Player) e.getPlayer();
    PlayerInventory inventory = player.getInventory();
    // 获取头盔上的宝石
    ItemStack helmet = inventory.getHelmet();
    SparForgeEvent helmetEvent = ItemUtil.checkItemLore(helmet);
    if (helmetEvent != null) {
      helmetEvent.setPlayer(player);
      helmetEvent.setNum(HELMET);
      Spar.plugin.getServer().getPluginManager().callEvent(helmetEvent);
    }

    // 获取鞋子上的宝石
    ItemStack boots = inventory.getBoots();
    SparForgeEvent bootsEvent = ItemUtil.checkItemLore(boots);
    if (bootsEvent != null) {
      bootsEvent.setPlayer(player);
      bootsEvent.setNum(BOOTS);
      Spar.plugin.getServer().getPluginManager().callEvent(bootsEvent);
    }
    // 获取衣服
    ItemStack chestplate = inventory.getChestplate();
    SparForgeEvent chestplateEvent = ItemUtil.checkItemLore(chestplate);
    if (chestplateEvent != null) {
      chestplateEvent.setPlayer(player);
      chestplateEvent.setNum(CHESTPLATE);
      Spar.plugin.getServer().getPluginManager().callEvent(chestplateEvent);
    }
    // 获取护腿
    ItemStack Leggings = inventory.getLeggings();
    SparForgeEvent LeggingsEvent = ItemUtil.checkItemLore(Leggings);
    if (LeggingsEvent != null) {
      LeggingsEvent.setPlayer(player);
      LeggingsEvent.setNum(LEGGINGS);
      Spar.plugin.getServer().getPluginManager().callEvent(LeggingsEvent);
    }
    // 获取武器
    ItemStack weapon = player.getItemInHand();
    SparForgeEvent weaponEvent = ItemUtil.checkItemLore(weapon);
    if (weaponEvent != null) {
      weaponEvent.setPlayer(player);
      weaponEvent.setNum(WEAPONS);
      Spar.plugin.getServer().getPluginManager().callEvent(weaponEvent);
    }
  }

  @EventHandler
  public void onCreateItem(CraftItemEvent e) {
    CraftingInventory inv = e.getInventory();
    ItemStack item = inv.getResult();
    boolean flag=true;
    if (item.hasItemMeta()) {
      if (item.getItemMeta().getDisplayName().equals("哇哈哈")) {
        flag=false;
      }
    }
    if (flag) {
      return;
    }
    ItemStack spar = null;
    // 检查是否有宝石
    for (ItemStack i : inv.getMatrix()) {
      if (i != null && i.getType() != Material.AIR) {
        if (!i.hasItemMeta()) {
          e.setCancelled(true);
          ((Player) e.getWhoClicked()).sendMessage("[晶石]" + ChatColor.RED
              + "对不起，不能使用普通材料合成");
          return;
        } else {
          if (i.getItemMeta().hasLore()) {
            for (String lore : i.getItemMeta().getLore()) {
              if (ItemUtil.ifSpar(lore)) {
                spar = i;
                break;
              }
            }
          } else {
            e.setCancelled(true);
            ((Player) e.getWhoClicked()).sendMessage("[晶石]" + ChatColor.RED
                + "对不起，不能使用普通材料合成");
          }
          if (spar!=null) {
            break;
          }
        }
      }
    }
    //如果有宝石
    if (spar!=null) {
      for (ItemStack i : inv.getMatrix()) {
        if (i != null && i.getType() != Material.AIR) {
          if (i.hasItemMeta()) {
            if (i.getItemMeta().hasLore()) {
              for (String lore : i.getItemMeta().getLore()) {
                // 如果是保护符
                if (lore.contains(Spar.protection_item)) {
                  ItemMeta meta = spar.getItemMeta();
                  List<String> lores = meta.getLore();
                  lores.add("§c§b"+Spar.mosaic_protection);
                  meta.setLore(lores);
                  spar.setItemMeta(meta);
                  inv.setResult(spar);
                  return;
                } else if (lore.equals(Spar.Lucky_charm)) {// 幸运符
                  ItemMeta meta = spar.getItemMeta();
                  List<String> lores = meta.getLore();
                  lores.add("§c§b"+Spar.charm + "： " + Spar.Lucky_charm_odds + "%");
                  meta.setLore(lores);
                  spar.setItemMeta(meta);
                  inv.setResult(spar);
                  return;
                } else if (lore.equals(Spar.Advanced_Lucky_charm)) {// 高级幸运符
                  ItemMeta meta = spar.getItemMeta();
                  List<String> lores = meta.getLore();
                  lores.add("§c§b"+Spar.charm + "： " + Spar.Advanced_Lucky_charm_odds
                      + "%");
                  meta.setLore(lores);
                  spar.setItemMeta(meta);
                  inv.setResult(spar);
                  return;
                }
              }
            } else {
              e.setCancelled(true);
              ((Player) e.getWhoClicked()).sendMessage("[晶石]" + ChatColor.RED
                  + "对不起，不能使用普通材料合成");
              return;
            }
          } else {
            e.setCancelled(true);
            ((Player) e.getWhoClicked()).sendMessage("[晶石]" + ChatColor.RED
                + "对不起，不能使用普通材料合成");
            return;
          }
        }
      }
    } else {
      e.setCancelled(true);
      ((Player) e.getWhoClicked()).sendMessage("[晶石]" + ChatColor.RED
          + "对不起，不能使用普通材料合成");
    }
    e.setCancelled(true);
    ((Player) e.getWhoClicked()).sendMessage("[晶石]" + ChatColor.RED
        + "对不起，不能使用普通材料合成");
  }
}
