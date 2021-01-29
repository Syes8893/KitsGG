package me.syes.kits.experience;

import java.util.ArrayList;

import me.syes.kits.kitplayer.KitPlayer;

public class ExpManager {
	
	ArrayList<ExpLevel> levels;
	
	public ExpManager() {
		levels = new ArrayList<ExpLevel>();
		loadLevels();
	}
	
	public void loadLevels() {
		/*new ExpLevel(0, "I", "&f", "&f", this);
		new ExpLevel(10, "II", "&7", "&7");
		new ExpLevel(35, "III", "&e", "&e");
		new ExpLevel(70, "IV", "&b", "&b");
		new ExpLevel(130, "V", "&a", "&a");
		new ExpLevel(200, "VI", "&c", "&c");
		new ExpLevel(290, "VII", "&d", "&d");
		new ExpLevel(410, "VIII", "&8", "&8");
		new ExpLevel(560, "IX", "&9", "&9");
		new ExpLevel(780, "X", "&c", "&4");
		new ExpLevel(1030, "XI", "&e", "&6");
		new ExpLevel(1320, "XII", "&a", "&2");
		new ExpLevel(1740, "XIII", "&b", "&3");
		new ExpLevel(2090, "XIV", "&d", "&5");
		new ExpLevel(2470, "XV", "&7", "&e&l");
		new ExpLevel(2930, "XVI", "&7", "&b&l");
		new ExpLevel(3550, "XVII", "&7", "&a&l");
		new ExpLevel(4210, "XVIII", "&7", "&d&l");
		new ExpLevel(5120, "XIX", "&7", "&c&l");
		new ExpLevel(6370, "XX", "&7", "&6&l");
		new ExpLevel(7540, "XXI", "&3", "&f&l");
		new ExpLevel(8920, "XXII", "&2", "&f&l");
		new ExpLevel(10330, "XXIII", "&4", "&f&l");
		new ExpLevel(12410, "XXIV", "&5", "&f&l");
		new ExpLevel(14630, "XXV", "&6", "&f&l");
		new ExpLevel(16910, "XXVI", "&0", "&e", "&e");
		new ExpLevel(19350, "XXVII", "&0", "&b", "&b");*/
	}
	
	public int getExpForNextLevel(KitPlayer kp) {
		if(getLevel(kp.getExp()) == levels.get(levels.size()-1))
			return 0;
		ExpLevel lvl = levels.get(levels.indexOf(getLevel(kp.getExp()))+1);
		return lvl.getRequiredExp();
	}
	
	public String getProgressBar(KitPlayer kp, int length, String fillColor) {
		if(getLevel(kp.getExp()) == levels.get(levels.size()-1))
			return "§7MAX LVL";
		ExpLevel lvl = levels.get(levels.indexOf(getLevel(kp.getExp()))+1);
		//int percentage = 12 * kp.getExp()/lvl.getRequiredExp() -1;
		int percentage = length * (kp.getExp()-getLevel(kp.getExp()).getRequiredExp())/(lvl.getRequiredExp()-getLevel(kp.getExp()).getRequiredExp()) -1;
		String str = fillColor.replace("&", "§");
		for(int a = 0; a < length; a++) {
			if(percentage >= a) {
				str += "\u25A0";
			}
			else {
				str += "§7\u25A0";
				for(int i = a+1; i < length; i++)
					str += "\u25A0";
				break;
			}
		}
		return str;
	}
	
	public ExpLevel getLevel(int exp) {
		for(ExpLevel level : levels) {
			if(exp < level.getRequiredExp())
				return levels.get(levels.indexOf(level)-1);
		}
		return levels.get(levels.size()-1);
	}
	
	public void registerLevel(ExpLevel l) {
		this.levels.add(l);
	}
	
	public ExpLevel getPlayerLevel(KitPlayer kp) {
		return getLevel(kp.getExp());
	}
	
	public ArrayList<ExpLevel> getLevels() {
		return this.levels;
	}

}
