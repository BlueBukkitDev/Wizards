package dev.blue.wizards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import dev.blue.wizards.spells.EnergySpell;
import dev.blue.wizards.spells.FireballSpell;
import dev.blue.wizards.spells.GustSpell;
import dev.blue.wizards.spells.HealSpell;
import dev.blue.wizards.spells.HideSpell;
import dev.blue.wizards.spells.LeapSpell;
import dev.blue.wizards.spells.LevitateSpell;
import dev.blue.wizards.spells.LightningSpell;
import dev.blue.wizards.spells.Spell;
import dev.blue.wizards.wands.EnergyWand;
import dev.blue.wizards.wands.FireballWand;
import dev.blue.wizards.wands.GustWand;
import dev.blue.wizards.wands.HealWand;
import dev.blue.wizards.wands.HideWand;
import dev.blue.wizards.wands.LeapWand;
import dev.blue.wizards.wands.LevitateWand;
import dev.blue.wizards.wands.LightningWand;

public class Utils {
	public NamespacedKey key_spellType;
	public NamespacedKey key_gui;
	public NamespacedKey key_wand;
	public NamespacedKey key_leapcount;
	private Random rand;
	private Main main;
	public HashMap<Player, Integer> foodCooldown;
	public Utils(Main main) {
		this.main = main;
		rand = new Random();
		foodCooldown = new HashMap<Player, Integer>();
		key_spellType = new NamespacedKey(main, "wizards-spellType");
		key_gui = new NamespacedKey(main, "wizards-gui");
		key_wand = new NamespacedKey(main, "wizards-wand");
		key_leapcount = new NamespacedKey(main, "wizards-leapcount");
	}
	public void spawnBanner(Location loc) {
		switch((byte)rand.nextInt(13)) {
		case SpellType.NULL:break;
		case SpellType.ENERGY_BEAM:setBanner(loc); createEnergyBanner((Banner) loc.getBlock().getState()); break;
		case SpellType.GUST:setBanner(loc); createGustBanner((Banner) loc.getBlock().getState()); break;
		case SpellType.LIGHTNING:setBanner(loc); createLightningBanner((Banner) loc.getBlock().getState()); break;
		case SpellType.FIREBALL:setBanner(loc); createFireballBanner((Banner) loc.getBlock().getState()); break;
		case SpellType.HEAL:setBanner(loc); createHealBanner((Banner) loc.getBlock().getState()); break;
		case SpellType.LEAP:setBanner(loc); createLeapBanner((Banner) loc.getBlock().getState()); break;
		case SpellType.LEVITATE:setBanner(loc); createLevitateBanner((Banner) loc.getBlock().getState()); break;
		case SpellType.HIDE:setBanner(loc); createHideBanner((Banner) loc.getBlock().getState()); break;
		default:return;
		}
	}
	public void debug(String message) {
		for(Player each:Bukkit.getOnlinePlayers()) {
			if(each.isOp()) {
				each.sendMessage(message);
			}
		}
	}
	private void setBanner(Location loc) {
		loc.getBlock().setType(Material.GRAY_BANNER);
		if(!(loc.getBlock().getState() instanceof Banner)) {
			return;
		}
	}
	
	private void createEnergyBanner(Banner banner) {
		List<Pattern> patterns = new ArrayList<Pattern>();
		patterns.add(new Pattern(DyeColor.CYAN, PatternType.FLOWER));
		patterns.add(new Pattern(DyeColor.WHITE, PatternType.CIRCLE));
		
		banner.setPatterns(patterns);
		banner.getPersistentDataContainer().set(key_spellType, PersistentDataType.BYTE, SpellType.ENERGY_BEAM);
		banner.update();
	}
	
	private void createGustBanner(Banner banner) {
		List<Pattern> patterns = new ArrayList<Pattern>();
		patterns.add(new Pattern(DyeColor.WHITE, PatternType.MOJANG));
		patterns.add(new Pattern(DyeColor.WHITE, PatternType.CIRCLE));
		
		banner.setPatterns(patterns);
		banner.getPersistentDataContainer().set(key_spellType, PersistentDataType.BYTE, SpellType.GUST);
		banner.update();
	}
	
	private void createLightningBanner(Banner banner) {
		List<Pattern> patterns = new ArrayList<Pattern>();
		patterns.add(new Pattern(DyeColor.BLACK, PatternType.GRADIENT));
		patterns.add(new Pattern(DyeColor.CYAN, PatternType.STRIPE_DOWNRIGHT));
		patterns.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_DOWNRIGHT));
		
		banner.setPatterns(patterns);
		banner.getPersistentDataContainer().set(key_spellType, PersistentDataType.BYTE, SpellType.LIGHTNING);
		banner.update();
	}
	
	private void createFireballBanner(Banner banner) {
		List<Pattern> patterns = new ArrayList<Pattern>();
		patterns.add(new Pattern(DyeColor.ORANGE, PatternType.FLOWER));
		patterns.add(new Pattern(DyeColor.YELLOW, PatternType.CIRCLE));
		
		banner.setPatterns(patterns);
		banner.getPersistentDataContainer().set(key_spellType, PersistentDataType.BYTE, SpellType.FIREBALL);
		banner.update();
	}
	
	private void createHealBanner(Banner banner) {
		List<Pattern> patterns = new ArrayList<Pattern>();
		patterns.add(new Pattern(DyeColor.LIME, PatternType.GRADIENT_UP));
		patterns.add(new Pattern(DyeColor.LIME, PatternType.GRADIENT_UP));
		patterns.add(new Pattern(DyeColor.GRAY, PatternType.CURLY_BORDER));
		patterns.add(new Pattern(DyeColor.GRAY, PatternType.GLOBE));
		patterns.add(new Pattern(DyeColor.GRAY, PatternType.SQUARE_TOP_LEFT));
		patterns.add(new Pattern(DyeColor.GRAY, PatternType.SQUARE_BOTTOM_RIGHT));
		patterns.add(new Pattern(DyeColor.GRAY, PatternType.BORDER));
		
		banner.setPatterns(patterns);
		banner.getPersistentDataContainer().set(key_spellType, PersistentDataType.BYTE, SpellType.HEAL);
		banner.update();
	}
	
	private void createLeapBanner(Banner banner) {
		List<Pattern> patterns = new ArrayList<Pattern>();
		patterns.add(new Pattern(DyeColor.MAGENTA, PatternType.RHOMBUS));
		patterns.add(new Pattern(DyeColor.GRAY, PatternType.BORDER));
		patterns.add(new Pattern(DyeColor.GRAY, PatternType.TRIANGLE_BOTTOM));
		
		banner.setPatterns(patterns);
		banner.getPersistentDataContainer().set(key_spellType, PersistentDataType.BYTE, SpellType.LEAP);
		banner.update();
	}
	
	private void createLevitateBanner(Banner banner) {
		List<Pattern> patterns = new ArrayList<Pattern>();
		patterns.add(new Pattern(DyeColor.GRAY, PatternType.BORDER));
		patterns.add(new Pattern(DyeColor.WHITE, PatternType.GRADIENT));
		patterns.add(new Pattern(DyeColor.YELLOW, PatternType.TRIANGLE_BOTTOM));
		
		banner.setPatterns(patterns);
		banner.getPersistentDataContainer().set(key_spellType, PersistentDataType.BYTE, SpellType.LEVITATE);
		banner.update();
	}
	
	private void createHideBanner(Banner banner) {
		banner.getPersistentDataContainer().set(key_spellType, PersistentDataType.BYTE, SpellType.HIDE);
		banner.update();
	}
	
	public Spell getSpell(Banner banner) {
		switch(banner.getPersistentDataContainer().get(key_spellType, PersistentDataType.BYTE)) {
		case SpellType.ENERGY_BEAM:return new EnergySpell(main, null);
		case SpellType.GUST:return new GustSpell(main, null);
		case SpellType.LIGHTNING:return new LightningSpell(main, null);
		case SpellType.FIREBALL:return new FireballSpell(main, null);
		case SpellType.HEAL:return new HealSpell(main, null);
		case SpellType.LEAP:return new LeapSpell(main, null);
		case SpellType.LEVITATE:return new LevitateSpell(main, null);
		case SpellType.HIDE:return new HideSpell(main, null);
		default:return null;
		}
	}
	
	public ItemStack blank() {
		ItemStack blank = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
		ItemMeta meta = blank.getItemMeta();
		meta.setDisplayName(" ");
		meta.getPersistentDataContainer().set(key_gui, PersistentDataType.BYTE, (byte)1);
		blank.setItemMeta(meta);
		return blank;
	}
	public void setupInventory(Player p) {
		p.getInventory().clear();
		for(int i = 9; i < 36; i++) {
			p.getInventory().setItem(i, blank());
		}
	}
	
	public void detectSpellAndApply(Player p, Banner banner) {
		Spell spell = getSpell(banner);
		if (spell instanceof EnergySpell) {
			if(new EnergySpell(main, p).getLevel() < 10) {
				applySpell(p, new EnergySpell(main, p), new EnergyWand(main));
				destroyBanner(banner.getBlock());
				return;
			}
		}
		if(spell instanceof GustSpell) {
			if(new GustSpell(main, p).getLevel() < 10) {
				applySpell(p, new GustSpell(main, p), new GustWand(main));
				destroyBanner(banner.getBlock());
				return;
			}
		}
		if(spell instanceof LightningSpell) {
			if(new LightningSpell(main, p).getLevel() < 10) {
				applySpell(p, new LightningSpell(main, p), new LightningWand(main));
				destroyBanner(banner.getBlock());
				return;
			}
		}
		if(spell instanceof FireballSpell) {
			if(new FireballSpell(main, p).getLevel() < 10) {
				applySpell(p, new FireballSpell(main, p), new FireballWand(main));
				destroyBanner(banner.getBlock());
				return;
			}
		}
		if(spell instanceof HealSpell) {
			if(new HealSpell(main, p).getLevel() < 10) {
				applySpell(p, new HealSpell(main, p), new HealWand(main));
				destroyBanner(banner.getBlock());
				return;
			}
		}
		if(spell instanceof LeapSpell) {
			if(new LeapSpell(main, p).getLevel() < 10) {
				applySpell(p, new LeapSpell(main, p), new LeapWand(main));
				destroyBanner(banner.getBlock());
				return;
			}
		}
		if(spell instanceof LevitateSpell) {
			if(new LevitateSpell(main, p).getLevel() < 10) {
				applySpell(p, new LevitateSpell(main, p), new LevitateWand(main));
				destroyBanner(banner.getBlock());
				return;
			}
		}
		if(spell instanceof HideSpell) {
			if(new HideSpell(main, p).getLevel() < 10) {
				applySpell(p, new HideSpell(main, p), new HideWand(main));
				destroyBanner(banner.getBlock());
				return;
			}
		}
	}
	
	private void applySpell(Player p, Spell spell, ItemStack wand) {
		giveWand(p, spell, wand);
		spell.incrementLevel();
	}
	
	private void giveWand(Player p, Spell spell, ItemStack wand) {
		if(spell.getLevel() > 0) {
			p.getInventory().addItem(wand);
		}else p.getInventory().setItem(getNextAvailableSlot(p), wand);
	}
	
	public void destroyBanner(Block block) {
		block.setType(Material.AIR);
		block.getWorld().playSound(block.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
		block.getWorld().spawnParticle(Particle.SMOKE, block.getX()+0.5, block.getY()+0.5, block.getZ()+0.5,
			20, 0.2, 1, 0.2, 0, null);
	}
	
	private int getNextAvailableSlot(Player p) {
		for(int i = 0; i < 9; i++) {
			if(p.getInventory().getItem(i) == null) {
				return i;
			}
		}
		return 8;
	}
	
	public Vector randomizeVector(Vector direction, float accuracy) {
		return direction.add(new Vector(Math.random() * accuracy - (accuracy/2), Math.random() * accuracy - (accuracy/2), Math.random() * accuracy - (accuracy/2)));
	}
	
	public void resetAllSpells(Player p) {
		new EnergySpell(main, p).resetLevel();
		new GustSpell(main, p).resetLevel();
		new LightningSpell(main, p).resetLevel();
		new FireballSpell(main, p).resetLevel();
		new HealSpell(main, p).resetLevel();
		new LeapSpell(main, p).resetLevel();
		new LevitateSpell(main, p).resetLevel();
		new HideSpell(main, p).resetLevel();
	}
	
	public void resetCombatTimer(Player p) {
		int clock = main.getBrawl().getUtils().getDamageClock(p);
		BukkitRunnable exitCombat = new BukkitRunnable() {
			@Override
			public void run() {
				if(main.getBrawl().getUtils().getDamageClock(p) != clock) {
					return;
				}
				main.getBrawl().getUtils().setAttacker(p, null);
			}
		};
		exitCombat.runTaskLater(main, 20*main.getBrawl().getUtils().combatCooldown);
	}
	
	public void resetPots(Player p) {
		BukkitRunnable clearPots = new BukkitRunnable() {
			@Override
			public void run() {
				for(PotionEffect each:p.getActivePotionEffects()) {
					p.removePotionEffect(each.getType());
				}
				p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, -1, 1, false, false, false));
				p.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, -1, 4, false, false, false));
				p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
			}
		};
		clearPots.runTaskLater(main, 1);
	}
	
	public void useLeap(Player p) {
		p.getPersistentDataContainer().set(key_leapcount, PersistentDataType.INTEGER, getRemainingLeaps(p)-1);
	}
	
	public int getRemainingLeaps(Player p) {
		if(!p.getPersistentDataContainer().has(key_leapcount, PersistentDataType.INTEGER)) {
			resetLeaps(p);
			return 2;
		}else return p.getPersistentDataContainer().get(key_leapcount, PersistentDataType.INTEGER);
	}
	
	public void resetLeaps(Player p) {
		p.getPersistentDataContainer().set(key_leapcount, PersistentDataType.INTEGER, 2);
	}
	
	public static boolean isNumeric(String s) {
		char[] suspects = s.toCharArray();
		for (int i = 0; i < suspects.length; i++) {
			switch (suspects[i]) {
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			case '.':
				break;
			default:
				return false;
			}
		}
		return true;
	}
	
	public void selectRandomMap() {
		Random rand = new Random();
		int mapIndex = rand.nextInt(main.getMaps().getKeys(false).size());
		List<String> maps = new ArrayList<String>(main.getMaps().getKeys(false));
		maps.sort(null);
		main.setCurrentMap(maps.get(mapIndex));
		Bukkit.getConsoleSender().sendMessage("§6[Wizards]§r Loading map "+friendlify(main.getCurrentMap()));
	}
	
	/**
	 *Will return an error if the map is not yet selected. Not ideal but will do for now. 
	 **/
	public Location getCurrentLobby() {
		return main.getMaps().getLocation(main.getCurrentMap()+".Lobby");
	}
	
	public Location getRandomMapSpawn() {
		Random rand = new Random();
		@SuppressWarnings("unchecked")
		List<Location> spawns = new ArrayList<Location>((List<Location>) main.getMaps().getList(main.getCurrentMap()+".PlayerSpawns"));
		int spawnIndex = rand.nextInt(spawns.size());
		return(spawns.get(spawnIndex));
	}
	
	/**
	 *Replaces underscores with spaces, capitalizes all words > 3 letters, first words, and select others
	 **/
	public String friendlify(String unfriendly) {
		unfriendly = unfriendly.replaceAll("_", " ");
		String friendly = "";
		for(String each:unfriendly.split(" ")) {
			each = each.substring(0, 1).toUpperCase() + each.substring(1);
			friendly += " "+each;
		}
		return friendly.strip();
	}
}

