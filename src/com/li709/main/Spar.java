package com.li709.main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.li709.listener.EventListener;

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
	//打孔类型
	public static String punch; 
	//第1-3孔的打孔几率
	public static double punch_1;
	public static double punch_2;
	public static double punch_3;
	//打孔
	public static String punch_charm;
	//已打孔
	public static String perforated;
	//未打孔
	public static String unperforated;
	//幸运符
	public static String Lucky_charm;
	//高级幸运符
	public static String Advanced_Lucky_charm;
	//幸运符几率
	public static int Lucky_charm_odds;
	//高级幸运符几率
	public static int Advanced_Lucky_charm_odds;
	//镶嵌几率
	public static  double ODDS;
	//幸运符格式
	public static String charm;
	//保护符格式
	public static String mosaic_protection;
	//保护符
	public static String protection_item;
	
	/**
	 * 镶嵌失败提示
	 */
	public static String failure_reminds;
	//打孔失败提示
	public static String punch_reminds;
	
	
	public static Spar plugin;
	public static List<Integer>itemids;
	
	public Spar() {
		plugin=this;
		itemids = new ArrayList<Integer>();
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String label, String[] args) {
		return false;
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}
	public void loadConfig(){
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
		punch_charm=getConfig().getString("打孔符").replaceAll("&", "§");
		perforated=getConfig().getString("已打孔").replaceAll("&", "§");
		unperforated=getConfig().getString("未打孔").replaceAll("&", "§");
		Lucky_charm=getConfig().getString("幸运符").replaceAll("&", "§");
		Advanced_Lucky_charm=getConfig().getString("高级幸运符").replaceAll("&", "§");
		Lucky_charm_odds=getConfig().getInt("幸运符几率");
		Advanced_Lucky_charm_odds=getConfig().getInt("高级幸运符几率");
		charm=getConfig().getString("幸运符格式").replaceAll("&", "§");
		protection_item=getConfig().getString("保护符").replaceAll("&", "§");
		mosaic_protection=getConfig().getString("保护符格式").replaceAll("&", "§");
		failure_reminds=getConfig().getString("镶嵌失败提示").replaceAll("&", "§");
		punch_reminds=getConfig().getString("打孔失败提示").replaceAll("&", "§");
		ODDS=getConfig().getDouble("镶嵌几率");
		punch_1=getConfig().getDouble("第一孔几率");
		punch_2=getConfig().getDouble("第二孔几率");
		punch_3=getConfig().getDouble("第三孔几率");
		punch=getConfig().getString("打孔");
		
	}
	@SuppressWarnings("deprecation")
	@Override
	public void onEnable() {
		if(!getDataFolder().exists()){
			getDataFolder().mkdir();
		}
		saveDefaultConfig();
		FurnaceRecipe recipe;
		FurnaceRecipe punchRecipe;
		loadConfig();
		//宝石id
		String str[] =spar.split(":");
		//符ID   未完成
		List<String> charmList=getConfig().getStringList("charm");
		//导入无序合成
		for (String s : charmList) {
			int a=Integer.parseInt(s);
			ItemStack item=new ItemStack(Integer.parseInt(str[0]));
			if (str.length==2) {
				item.setDurability(Short.parseShort(str[1]));
			};
			ItemMeta meta=item.getItemMeta();
			meta.setDisplayName("哇哈哈");
			item.setItemMeta(meta);
			ShapelessRecipe shapelessRecipe=new ShapelessRecipe(item);
			shapelessRecipe.addIngredient(new ItemStack(a).getData());
			shapelessRecipe.addIngredient(item.getData());
			getServer().addRecipe(shapelessRecipe);
		}
		//装备ID
		List<String> list=getConfig().getStringList("id");
		//打孔id
		String punchStr[] =punch.split(":");
		//导入熔炉配方
		for (String s : list) {
			int a=Integer.parseInt(s);
			itemids.add(a);
			ItemStack item=new ItemStack(Integer.parseInt(str[0]));
			ItemStack punchItem=new ItemStack(Integer.parseInt(punchStr[0]));
			if (punchStr.length==2) {
				punchItem.setDurability(Short.parseShort(punchStr[1]));
			};
			if (str.length==2) {
				item.setDurability(Short.parseShort(str[1]));
			};
			recipe = new FurnaceRecipe(new ItemStack(Material.getMaterial(a)),item.getType());
			recipe.setInput(Material.getMaterial(a));
			punchRecipe=new FurnaceRecipe(new ItemStack(Material.getMaterial(a)),punchItem.getType());
			punchRecipe.setInput(Material.getMaterial(a));
			getServer().addRecipe(recipe);
			getServer().addRecipe(punchRecipe);
		}
		getServer().getPluginManager().registerEvents(new EventListener(), this);
		getServer().getLogger().info("【晶石】晶石插件启动成功");
		getServer().getLogger().info("【定制】定制插件，一百起步，QQ：709854423");
	}
	
}
