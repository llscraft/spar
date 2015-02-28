package com.li709.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class SparForgeEvent extends Event{
	private static final HandlerList handlers=new HandlerList();
	//作为区分5件装备
	private int num;
	private Player player;
	private double critChance;
	private double critDamage;
	private double power;
	private double health;
	private double lifesteal;
	private double lifestealheal;
	private double armour;
	private double reduction;
	private double dodge;
	private double speed;
	private double realDamage;
	
	
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	public double getCritChance() {
		return critChance;
	}
	public void setCritChance(double critChance) {
		this.critChance = critChance;
	}
	public double getCritDamage() {
		return critDamage;
	}
	public void setCritDamage(double critDamage) {
		this.critDamage = critDamage;
	}
	public double getPower() {
		return power;
	}
	public void setPower(double power) {
		this.power = power;
	}
	public double getHealth() {
		return health;
	}
	public void setHealth(double health) {
		this.health = health;
	}
	public double getLifesteal() {
		return lifesteal;
	}
	public void setLifesteal(double lifesteal) {
		this.lifesteal = lifesteal;
	}
	public double getLifestealheal() {
		return lifestealheal;
	}
	public void setLifestealheal(double lifestealheal) {
		this.lifestealheal = lifestealheal;
	}
	public double getArmour() {
		return armour;
	}
	public void setArmour(double armour) {
		this.armour = armour;
	}
	public double getReduction() {
		return reduction;
	}
	public void setReduction(double reduction) {
		this.reduction = reduction;
	}
	public double getDodge() {
		return dodge;
	}
	public void setDodge(double dodge) {
		this.dodge = dodge;
	}
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	public double getRealDamage() {
		return realDamage;
	}
	public void setRealDamage(double realDamage) {
		this.realDamage = realDamage;
	}

	public static HandlerList getHandlerList(){
		return handlers;
	}
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
