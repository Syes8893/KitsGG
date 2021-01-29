package me.syes.kits.experience;

public class ExpLevel {
	
	private int requiredExp;
	private String name;
	private String primaryColor, secondaryColor, nameColor;
	
	public ExpLevel(int requiredExp, String name, String primaryColor, String secondaryColor, ExpManager expManager) {
		this.requiredExp = requiredExp;
		this.name = name;
		this.primaryColor = primaryColor.replace("&", "§");
		this.secondaryColor = secondaryColor.replace("&", "§");
		expManager.registerLevel(this);
	}
	
	public ExpLevel(int requiredExp, String name, String primaryColor, String secondaryColor, String nameColor, ExpManager expManager) {
		this.requiredExp = requiredExp;
		this.name = name;
		this.primaryColor = primaryColor.replace("&", "§");
		this.secondaryColor = secondaryColor.replace("&", "§");
		this.nameColor = nameColor.replace("&", "§");
		expManager.registerLevel(this);
	}
	
	public String getPrefix() {
		return primaryColor + "[" + secondaryColor + name + primaryColor + "]" + "§f ";
	}

	public int getRequiredExp() {
		return requiredExp;
	}

	public String getName() {
		return name;
	}

	public String getPrimaryColor() {
		return primaryColor;
	}

	public String getSecondaryColor() {
		return secondaryColor;
	}

	public String getNameColor() {
		if(nameColor != null)
			return nameColor;
		return secondaryColor;
	}
	
}
