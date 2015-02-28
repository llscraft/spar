package com.li709.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.li709.listener.SparForgeEvent;
import com.li709.main.Spar;

/**
 * 
 * @author li709
 */
public class ItemUtil {
  
	
  public static boolean ifUnperforated(ItemStack item){
	  for (String lore : item.getItemMeta().getLore()) {
		if (lore.contains(Spar.unperforated)) {
			return true;
		}
	}
	  return false;
  }
  public static int ifPunch(ItemStack item){
    int i=0;
    for (String lore : item.getItemMeta().getLore()) {
      //如果装备上lore有宝石名，或者已打孔
      if (ifSpar(lore)||lore.equals(Spar.perforated)) {
        i++;
      }
    }
    return i;
  }
  public static boolean ifSpar(String lore){
    boolean flag=false;
    if (lore.contains(Spar.armour_name)) {
      flag=true;
    }else if (lore.contains(Spar.critChance_name)) {
      flag=true;
    }else if (lore.contains(Spar.damage_name)) {
      flag=true;
    }else if (lore.contains(Spar.dodge_name)) {
      flag=true;
    }else if (lore.contains(Spar.reduction_name)) {
      flag=true;
    }else if (lore.contains(Spar.lifesteal_name)) {
      flag=true;
    }else if (lore.contains(Spar.health_name)) {
      flag=true;
    }
    return flag;
  }
	/**
	 * 获取到装备上的晶石的等级
	 * 
	 * @param item
	 * @return
	 */
	public static Map<String, Integer> getEquipSpar(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (String str : meta.getLore()) {
			str = str.replaceAll("§.", "").trim();
			int level;
			int index;
			// 获取身上宝石等级
			if ((index = str.indexOf(Spar.armour_name)) != -1) {
				level = Integer.parseInt(str.substring(
						index + Spar.armour_name.length(), str.indexOf("级")));
				map.put(Spar.armour_name, level);
			} else if ((index = str.indexOf(Spar.critChance_name)) != -1) {
				level = Integer.parseInt(str.substring(index
						+ Spar.critChance_name.length(), str.indexOf("级")));
				map.put(Spar.critChance_name, level);
			} else if ((index = str.indexOf(Spar.damage_name)) != -1) {
				level = Integer.parseInt(str.substring(
						index + Spar.damage_name.length(), str.indexOf("级")));
				map.put(Spar.damage_name, level);
			} else if ((index = str.indexOf(Spar.dodge_name)) != -1) {
				level = Integer.parseInt(str.substring(
						index + Spar.dodge_name.length(), str.indexOf("级")));
				map.put(Spar.dodge_name, level);
			} else if ((index = str.indexOf(Spar.health_name)) != -1) {
				level = Integer.parseInt(str.substring(
						index + Spar.health_name.length(), str.indexOf("级")));
				map.put(Spar.health_name, level);
			} else if ((index = str.indexOf(Spar.lifesteal_name)) != -1) {
				level = Integer.parseInt(str.substring(index
						+ Spar.lifesteal_name.length(), str.indexOf("级")));
				map.put(Spar.lifesteal_name, level);
			} else if ((index = str.indexOf(Spar.reduction_name)) != -1) {
				level = Integer.parseInt(str.substring(index
						+ Spar.reduction_name.length(), str.indexOf("级")));
				map.put(Spar.reduction_name, level);
			}
		}
		return map;
	}

	public static boolean checkItem(ItemStack item) {
		if (!item.hasItemMeta()) {
			return false;
		}
		ItemMeta meta = item.getItemMeta();
		if (!meta.hasLore()) {
			return false;
		}
		return true;
	}

	/**
	 * 检查是否为宝石，如果是宝石,则对比身上的宝石等级
	 * 
	 * @param gem
	 * @param map
	 * @return
	 */
	public static boolean checkSpar(ItemStack gem, ItemStack item,
			Map<String, Integer> map) {
		boolean sparFlag = false;
		boolean coverFlag = false;
		ItemMeta meta = gem.getItemMeta();
		Integer level = null;
		// 获取宝石等级
		for (String str : meta.getLore()) {
			str = str.replaceAll("§.", "").trim();
			if (str.matches(".* \\d+级")) {
				level = Integer.parseInt(str.substring(str.lastIndexOf(" ")+1,
						str.lastIndexOf("级")));
			}
		}
		if (level == null) {
			return false;
		}
		for (String str : meta.getLore()) {
			// 如果是宝石
			if (str.indexOf(Spar.armour_name) != -1) {
				sparFlag = true;
				// 获取身上宝石等级并对比
				if (map.containsKey(Spar.armour_name)) {
					// 宝石等级大于装备上的
					if (level > map.get(Spar.armour_name)) {
						coverFlag = true;
					}
				}
			} else if (str.indexOf(Spar.critChance_name) != -1) {
				sparFlag = true;
				if (map.containsKey(Spar.critChance_name)) {
					if (level > map.get(Spar.critChance_name)) {
						coverFlag = true;
					}
				}
			} else if (str.indexOf(Spar.damage_name) != -1) {
				sparFlag = true;
				if (map.containsKey(Spar.damage_name)) {
					if (level > map.get(Spar.damage_name)) {
						coverFlag = true;
					}
				}
			} else if (str.indexOf(Spar.dodge_name) != -1) {
				sparFlag = true;
				if (map.containsKey(Spar.dodge_name)) {
					if (level > map.get(Spar.dodge_name)) {
						coverFlag = true;
					}
				}
			} else if (str.indexOf(Spar.health_name) != -1) {
				sparFlag = true;
				if (map.containsKey(Spar.health_name)) {
					if (level > map.get(Spar.health_name)) {
						coverFlag = true;
					}
				}
			} else if (str.indexOf(Spar.lifesteal_name) != -1) {
				sparFlag = true;
				if (map.containsKey(Spar.lifesteal_name)) {
					if (level > map.get(Spar.lifesteal_name)) {
						coverFlag = true;
					}
				}
			} else if (str.indexOf(Spar.reduction_name) != -1) {
				sparFlag = true;
				if (map.containsKey(Spar.reduction_name)) {
					if (level > map.get(Spar.reduction_name)) {
						coverFlag = true;
					}
				}
			}
		}
		// 如果是宝石
		if (sparFlag) {
			// 如果装备上已有对应的宝石。并且等级低于宝石
			if (coverFlag) {
				return true;
			} else if (checkHoles(item)) { // 如果没有对应宝石。则需要检查是否有孔数
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * 检查是否有 已打孔
	 * 
	 * @title
	 * @Description
	 * @param item
	 * @return
	 */
	public static boolean checkHoles(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		List<String> lores = meta.getLore();
		for (String str : lores) {
			if (str.equals(Spar.perforated)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 检查是否为宝石，如果是宝石则看装备有无孔数
	 * 
	 * @param fuel
	 * @param item
	 * @return
	 */
	public static boolean checkSpar(ItemStack fuel, ItemStack item) {
		ItemMeta meta = fuel.getItemMeta();
		boolean flag = false;
		// 判断是否是宝石
		for (String str : meta.getLore()) {
			// 如果是宝石
			if (str.indexOf(Spar.armour_name) != -1) {
				flag = true;
				break;
			} else if (str.indexOf(Spar.critChance_name) != -1) {
				flag = true;
				break;
			} else if (str.indexOf(Spar.damage_name) != -1) {
				flag = true;
				break;
			} else if (str.indexOf(Spar.dodge_name) != -1) {
				flag = true;
				break;
			} else if (str.indexOf(Spar.health_name) != -1) {
				flag = true;
				break;
			} else if (str.indexOf(Spar.lifesteal_name) != -1) {
				flag = true;
				break;
			} else if (str.indexOf(Spar.reduction_name) != -1) {
				flag = true;
				break;
			}
		}
		// 是宝石。检查有孔没
		if (flag) {
			if (checkHoles(item)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 给装备打孔，之前已经验证没有超过3个孔。直接打孔即可
	 * @title 
	 * @Description 
	 * @return
	 */
	public static ItemStack addPunch(ItemStack item){
	  int i=ifPunch(item);
	  double odds=0;
	  switch (i) {
      case 0:
        odds=Spar.punch_1;
        break;
      case 1:
        odds=Spar.punch_2;
        break;
      case 2:
        odds=Spar.punch_3;
        break;
      default:
        odds=Spar.punch_3;
      }
	  if (Math.random()<odds/100) {
        ItemMeta meta=item.getItemMeta();
        List<String> lores=meta.getLore();
        for (int j = 0; j < lores.size(); j++) {
          String lore=lores.get(j);
          //将未打孔改成已打孔
          if (lore.contains(Spar.unperforated)) {
            lores.set(j, Spar.perforated);
            break;
          }
        }
        meta.setLore(lores);
        item.setItemMeta(meta);
        return item;
      }
	  return null;
	}
	/**
	 * 镶嵌宝石，先判断是否有幸运符，然后计算镶嵌成功率。失败后需判断有无保护符
	 * 
	 * @title
	 * @Description
	 * @param item
	 * @param fuel
	 * @return
	 */
	public static Map<String, Object> addLore(ItemStack item, ItemStack fuel) {
    ItemMeta meta = null;
    ItemMeta gemMeta = null;
    //返回镶嵌成功或者失败
    Map<String, Object> map = new HashMap<String, Object>();
    if (item.hasItemMeta()) {
      meta = item.getItemMeta();
    } else {
      map.put("物品", null);
      map.put("几率", Boolean.TRUE);
      return map;
    }
    if (fuel.hasItemMeta()) {
      gemMeta = fuel.getItemMeta();
    } else {
      map.put("物品", null);
      map.put("几率", Boolean.TRUE);
      return map;
    }
    // 如果随机数大于几率
    if (Math.random() > (Spar.ODDS + checkLucky(fuel)) / 100.0) {
      map.put("物品", item);
      map.put("几率", Boolean.FALSE);
      return map;
    }
    //保存物品装备宝石所在lore位置
    Map<String,Integer> itemMap=getItemMap(meta);
    Integer index=null;
    List<Object> list=getGemNameLevelAttribute(gemMeta.getLore());
    String lore="§e§l● <"+list.get(0)+list.get(1)+"级 +"+list.get(2)+list.get(3)+">";
    index=getIndex(itemMap, (String)list.get(0));
    //装备上的lore
    List<String> lores=meta.getLore();
    if (index!=null) {
    	lores.set(index, lore);
	}else {
		for (int i = 0; i < lores.size(); i++) {
			if (lores.get(i).equals(Spar.perforated)) {
				lores.set(i, lore);
				break;
			}
		}
	}
    meta.setLore(lores);
    item.setItemMeta(meta);
    map.put("物品", item);
    map.put("几率", Boolean.TRUE);
    return map;
  }

	// 获取装备上指定宝石的lore位置
	public static Integer getIndex(Map<String, Integer> itemMap, String name) {
		if (itemMap.containsKey(name)) {
			return itemMap.get(name);
		}
		return null;
	}
	// 获取宝石的名字和等级和属性。第一个元素存名字，第二个等级。第3个属性值,第4个属性名
	public static List<Object> getGemNameLevelAttribute(List<String> lores) {
		List<Object> list = new ArrayList<Object>(4);
		list.add("a");
		list.add("a");
		list.add("a");
		list.add("a");
		int num=0;
		for (String str : lores) {
			str = str.replaceAll("§.", "").trim();
			if (str.matches(".* \\d+级")) {
				list.set(1,Integer.parseInt(str.substring(str.lastIndexOf(" ")+1,
						str.lastIndexOf("级"))));
			}
			if (str.indexOf(Spar.armour_name) != -1) {
				list.set(0,Spar.armour_name);
				num=Spar.armour;
				list.set(3,"护甲");
			} else if (str.indexOf(Spar.critChance_name) != -1) {
				list.set(0,Spar.critChance_name);
				num=Spar.critChance;
				list.set(3,"暴击");
			} else if (str.indexOf(Spar.damage_name) != -1) {
				list.set(0,Spar.damage_name);
				num=Spar.damage;
				list.set(3,"伤害");
			} else if (str.indexOf(Spar.dodge_name) != -1) {
				list.set(0,Spar.dodge_name);
				num=Spar.dodge;
				list.set(3,"闪避");
			} else if (str.indexOf(Spar.health_name) != -1) {
				list.set(0,Spar.health_name);
				num=Spar.health;
				list.set(3,"血量");
			} else if (str.indexOf(Spar.lifesteal_name) != -1) {
				list.set(0,Spar.lifesteal_name);
				num=Spar.lifestealheal;
				list.set(3,"吸血");
			} else if (str.indexOf(Spar.reduction_name) != -1) {
				list.set(0,Spar.reduction_name);
				num=Spar.reduction;
				list.set(3,"减伤");
			}
		}
		list.set(2,num*(Integer)list.get(1));
		return list;
	}

	/**
	 * 判断装备上所有宝石类，记录所在条数位置
	 * 
	 * @title
	 * @Description
	 * @return
	 */
	public static Map<String, Integer> getItemMap(ItemMeta meta) {
		List<String> lores = meta.getLore();
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (int i = 0; i < lores.size(); i++) {
			if (lores.get(i).indexOf(Spar.armour_name) != -1) {
				map.put(Spar.armour_name, i);
				continue;
			} else if (lores.get(i).indexOf(Spar.critChance_name) != -1) {
				map.put(Spar.critChance_name, i);
				continue;
			} else if (lores.get(i).indexOf(Spar.damage_name) != -1) {
				map.put(Spar.damage_name, i);
				continue;
			} else if (lores.get(i).indexOf(Spar.dodge_name) != -1) {
				map.put(Spar.dodge_name, i);
				continue;
			} else if (lores.get(i).indexOf(Spar.health_name) != -1) {
				map.put(Spar.health_name, i);
				continue;
			} else if (lores.get(i).indexOf(Spar.lifesteal_name) != -1) {
				map.put(Spar.lifesteal_name, i);
				continue;
			} else if (lores.get(i).indexOf(Spar.reduction_name) != -1) {
				map.put(Spar.reduction_name, i);
				continue;
			}
		}
		return map;
	}

	/**
	 * 检查是否有幸运符加成
	 * 
	 * @title
	 * @Description
	 * @param fuel
	 * @return
	 */
	public static double checkLucky(ItemStack fuel) {
		ItemMeta meta = fuel.getItemMeta();
		List<String> lores = meta.getLore();
		for (String str : lores) {
			int index = str.indexOf(Spar.charm);
			if (index != -1) {
				String lore = str.substring(index + Spar.charm.length() + 2,
						str.indexOf("%"));
				return Double.parseDouble(lore);
			}
		}
		return 0.0;
	}

	/**
	 * 检查是否被保护
	 * 
	 * @param fuel
	 * @return
	 */
	public static ItemStack checkProtection(ItemStack fuel) {
		ItemMeta meta = fuel.getItemMeta();
		List<String> lores = meta.getLore();
		boolean flag = false;
		for (String string : lores) {
			if (string.equals(Spar.mosaic_protection)) {
				flag = true;
				break;
			}
		}
		if (flag) {
			lores.remove(Spar.mosaic_protection);
			meta.setLore(lores);
			fuel.setItemMeta(meta);
			return fuel;
		}
		return null;
	}
	/**
	 * 检查装备上的lore值
	 * @param item
	 * @return
	 */
	public static SparForgeEvent checkItemLore(ItemStack item){
		SparForgeEvent event=null;
		if (item!=null) {
			if (item.hasItemMeta()) {
				ItemMeta meta=item.getItemMeta();
				if (meta.hasLore()) {
					List<String> lores=meta.getLore();
					for (String lore : lores) {
						if (lore.replaceAll("§.", "").startsWith("● <")) {
							event=new SparForgeEvent();
							event=checkLore(lore,event);
						}
					}
				}
			}
		}
		return event;
	}
	
	/**
	 * 检查lore值
	 * @param lore
	 * @return
	 */
	public static  SparForgeEvent checkLore(String lore,SparForgeEvent event){
		lore=lore.replaceAll("§.", "").replaceAll("● <", "");
		if (lore.startsWith(Spar.reduction_name)) {
			int index=lore.indexOf("级 +")+3;
			double num=Double.parseDouble(lore.substring(index, lore.indexOf("减")));
			event.setReduction(num);
		}else if (lore.startsWith(Spar.armour_name)) {
			int index=lore.indexOf("级 +")+3;
			double num=Double.parseDouble(lore.substring(index, lore.indexOf("护")));
			event.setArmour(num);
		}else if (lore.startsWith(Spar.critChance_name)) {
			int index=lore.indexOf("级 +")+3;
			double num=Double.parseDouble(lore.substring(index, lore.indexOf("暴")));
			event.setCritChance(num);
		}else if (lore.startsWith(Spar.damage_name)) {
			int index=lore.indexOf("级 +")+3;
			double num=Double.parseDouble(lore.substring(index, lore.indexOf("伤")));
			event.setPower(num);
		}else if (lore.startsWith(Spar.dodge_name)) {
			int index=lore.indexOf("级 +")+3;
			double num=Double.parseDouble(lore.substring(index, lore.indexOf("闪")));
			event.setDodge(num);
		}else if (lore.startsWith(Spar.health_name)) {
			int index=lore.indexOf("级 +")+3;
			double num=Double.parseDouble(lore.substring(index, lore.indexOf("血")));
			event.setHealth(num);
		}else if (lore.startsWith(Spar.lifesteal_name)) {
			int index=lore.indexOf("级 +")+3;
			double num=Double.parseDouble(lore.substring(index, lore.indexOf("吸")));
			event.setLifesteal(num);
		}
		return event;
	}
}
