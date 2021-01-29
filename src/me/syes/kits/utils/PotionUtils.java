package me.syes.kits.utils;

import org.bukkit.potion.PotionEffectType;

public class PotionUtils {
	
	public static String translatePotionID(Short dura) {
		if(dura == 8193) return "Regeneration 1 §8(0:45)";
		if(dura == 8194) return "Speed 1 §8(3:00)";
		if(dura == 8195) return "Fire Resistance 1 §8(3:00)";
		if(dura == 8196) return "Poison 1 §8(0:45)";
		if(dura == 8197) return "Instant Health 1";
		if(dura == 8198) return "Night Vision 1 §8(3:00)";
		if(dura == 8200) return "Weakness 1 §8(1:30)";
		if(dura == 8201) return "Strength 1 §8(3:00)";
		if(dura == 8202) return "Slowness 1 §8(1:30)";
		if(dura == 8204) return "Instant Damage 1";
		if(dura == 8205) return "Water Breathing 1 §8(3:00)";
		if(dura == 8206) return "Invisibility 1 §8(3:00)";
		if(dura == 8225) return "Regeneration 2 §8(0:22)";
		if(dura == 8226) return "Speed 2 §8(1:30)";
		if(dura == 8228) return "Poison 2 §8(0:22)";
		if(dura == 8229) return "Instant Health 2";
		if(dura == 8233) return "Strength 2 §8(1:30)";
		if(dura == 8235) return "Jump Boost 2 §8(1:30)";
		if(dura == 8236) return "Instant Damage 2";
		if(dura == 8257) return "Regeneration 1 §8(2:00)";
		if(dura == 8258) return "Speed 1 §8(8:00)";
		if(dura == 8259) return "Fire Resistance 1 §8(8:00)";
		if(dura == 8260) return "Poison 1 §8(2:00)";
		if(dura == 8262) return "Night Vision 1 §8(8:00)";
		if(dura == 8264) return "Weakness 1 §8(4:300)";
		if(dura == 8265) return "Strength 1 §8(8:00)";
		if(dura == 8266) return "Slowness 1 §8(4:00)";
		if(dura == 8267) return "Jump Boost 1 §8(3:00)";
		if(dura == 8269) return "Water Breathing 1 §8(8:00)";
		if(dura == 8270) return "Invisibility 1 §8(8:00)";
		if(dura == 16385) return "Regeneration 1 §8(0:33)";
		if(dura == 16386) return "Speed 1 §8(2:15)";
		if(dura == 16387) return "Fire Resistance 1 §8(2:15)";
		if(dura == 16388) return "Poison 1 §8(0:33)";
		if(dura == 16390) return "Night Vision 1 §8(2:15)";
		if(dura == 16392) return "Weakness 1 §8(1:07)";
		if(dura == 16393) return "Strength 1 §8(2:15)";
		if(dura == 16394) return "Slowness 1 §8(1:07)";
		if(dura == 16396) return "Instant Damage 1";
		if(dura == 16397) return "Water Breathing 1 §8(2:15)";
		if(dura == 16398) return "Invisibility 1 §8(2:15)";
		if(dura == 16417) return "Regeneration 2 §8(0:16)";
		if(dura == 16418) return "Speed 2 §8(1:07)";
		if(dura == 16420) return "Poison 2 §8(0:16)";
		if(dura == 16421) return "Instant Health 2";
		if(dura == 16425) return "Strength 2 §8(1:07)";
		if(dura == 16427) return "Jump Boost 2 §8(1:07)";
		if(dura == 16428) return "Instant Damage 2";
		if(dura == 16449) return "Regeneration 1 §8(1:30)";
		if(dura == 16450) return "Speed 1 §8(6:00)";
		if(dura == 16451) return "Fire Resistance 1 §8(6:00)";
		if(dura == 16452) return "Poison 1 §8(1:30)";
		if(dura == 16453) return "Instant Health 1";
		if(dura == 16454) return "Night Vision 1 §8(6:00)";
		if(dura == 16456) return "Weakness 1 §8(3:00)";
		if(dura == 16457) return "Strength 1 §8(6:00)";
		if(dura == 16458) return "Slowness 1 §8(3:00)";
		if(dura == 16459) return "Jump Boost 1 §8(3:00)";
		if(dura == 16461) return "Water Breathing 1 §8(6:00)";
		if(dura == 16462) return "Invisibility 1 §8(6:00)";
		return "ERROR";
	}
	
	public static String translatePotionEffect(PotionEffectType pe) {
		if(pe.getName().equals("BLINDNESS")) return "Blindness";
		if(pe.getName().equals("SPEED")) return "Speed";
		if(pe.getName().equals("SLOW")) return "Slowness";
		if(pe.getName().equals("ABSORPTION")) return "Absorption";
		if(pe.getName().equals("CONFUSION")) return "Nausea";
		if(pe.getName().equals("DAMAGE_RESISTANCE")) return "Resistance";
		if(pe.getName().equals("FAST_DIGGING")) return "Haste";
		if(pe.getName().equals("FIRE_RESISTANCE")) return "Fire Resistance";
		if(pe.getName().equals("HARM")) return "Instant Damage";
		if(pe.getName().equals("HEAL")) return "Instant Health";
		if(pe.getName().equals("HEALTH_BOOST")) return "Health Boost";
		if(pe.getName().equals("HUNGER")) return "Hunger";
		if(pe.getName().equals("INCREASE_DAMAGE")) return "Strength";
		if(pe.getName().equals("INVISIBLITY")) return "Invisibility";
		if(pe.getName().equals("JUMP")) return "Jump Boost";
		if(pe.getName().equals("NIGHT_VISION")) return "Night Vision";
		if(pe.getName().equals("POISON")) return "Poison";
		if(pe.getName().equals("REGENERATION")) return "Regeneration";
		if(pe.getName().equals("SATURATION")) return "Saturation";
		if(pe.getName().equals("SLOW_DIGGING")) return "Mining Fatigue";
		if(pe.getName().equals("WATER_BREATHING")) return "Water Breathing";
		if(pe.getName().equals("WEAKNESS")) return "Weakness";
		if(pe.getName().equals("WITHER")) return "Wither";
		return pe.getName();
	}
	
}
