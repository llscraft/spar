package com.li709.main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Spar extends JavaPlugin{
	//属性值
	public static int damage;
	public static int health;
	public static int armour;
	public static int reduction;
	public static int dodge;
	public static int critChance;
	public static int lifesteal;
	public static int lifestealheal;
	//晶石名
	public static String damage_name;
	public static String health_name;
	public static String armour_name;
	public static String reduction_name;
	public static String dodge_name;
	public static String critChance_name;
	public static String lifesteal_name;
	//宝石类型
	public static String spar; 
	//打孔符
	public static String punch_charm;
	//未打孔
	public static String perforated;
	//已打孔
	public static String unperforated;
	//幸运符
	public static String Lucky_charm;
	//高级幸运符
	public static String Advanced_Lucky_charm;
	//幸运符几率
	public static double Lucky_charm_odds;
	//高级幸运符几率
	public static double Advanced_Lucky_charm_odds;
	//幸运符格式
	public static String charm;
	/**
	 * 物品被保护
	 */
	public static String mosaic_protection;
	
	/**
	 * 保护符
	 */
	public static String protection_item;
	
	/**
	 * 失败提示
	 */
	public static String failure_reminds;
	
	
	public static Spar plugin;
	public static List<Integer>itemids;
	
	public Spar() {
		plugin=this;
		itemids = new ArrayList<Integer>();
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String label, String[] args) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		super.onDisable();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onEnable() {
		if(!getDataFolder().exists()){
			getDataFolder().mkdir();
		}
		saveDefaultConfig();
		FurnaceRecipe recipe;
		List<String> list=getConfig().getStringList("id");
		//未完成
		for (String s : list) {
			getServer().getLogger().info(s);
			int a=Integer.parseInt(s);
			itemids.add(a);
			String str[] =spar.split(":");
			ItemStack item=new ItemStack(Integer.parseInt(str[0]));
			if (str.length==2) {
				item.setDurability(Short.parseShort(str[1]));
			};
			recipe = new FurnaceRecipe(new ItemStack(Material.getMaterial(a)),item.getType());
			recipe.setInput(Material.getMaterial(a));
			getServer().addRecipe(recipe);
		}
		damage=getConfig().getInt("Attribute.damage");
		health=getConfig().getInt("Attribute.health");
		armour=getConfig().getInt("Attribute.armour");
		reduction=getConfig().getInt("Attribute.reduction");
		dodge=getConfig().getInt("Attribute.dodge");
		critChance=getConfig().getInt("Attribute.critChance");
		lifesteal=getConfig().getInt("Attribute.lifesteal");
		lifestealheal=getConfig().getInt("Attribute.lifestealheal");
		//取宝石名
		damage_name=getConfig().getString("Name.damage_name");
		health_name=getConfig().getString("Name.health_name");
		armour_name=getConfig().getString("Name.armour_name");
		reduction_name=getConfig().getString("Name.reduction_name");
		dodge_name=getConfig().getString("Name.dodge_name");
		critChance_name=getConfig().getString("Name.critChance_name");
		lifesteal_name=getConfig().getString("Name.lifesteal_name");
		spar=getConfig().getString("晶石");
		punch_charm=getConfig().getString("打孔符");
		perforated=getConfig().getString("未打孔");
		unperforated=getConfig().getString("已打孔");
		Lucky_charm=getConfig().getString("幸运符");
		Advanced_Lucky_charm=getConfig().getString("高级幸运符");
		Lucky_charm_odds=getConfig().getInt("幸运符几率");
		Advanced_Lucky_charm_odds=getConfig().getInt("高级幸运符几率");
		charm=getConfig().getString("幸运符格式");
		protection_item=getConfig().getString("保护符");
		mosaic_protection=getConfig().getString("保护符格式");
		failure_reminds=getConfig().getString("镶嵌失败提示");
		getServer().getLogger().info("【晶石】晶石插件启动成功");
		getServer().getLogger().info("【定制】定制插件，一百起步，QQ：709854423");
	}
	
}
