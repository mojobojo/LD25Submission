package dev.mojobojo.ld25;

import java.util.Random;

public class PlanetNameGen {
	
	String[] prefixes = new String[] 
			{
			"enta",
			"apa",
			"tera",
			"fela",
			"rata",
			"sapa",
			"lana",
			"sen",
			"et",
			"fr",
			"etra",
			"alpha",
			"beta",
			"EST ",
			"ADA ",
			"BEN "
			};
	
	String[] middle = new String[] 
			{
			"tenta",
			"efran",
			"prana",
			"low",
			"lan",
			"prod",
			"mojo",
			"bojo",
			"wham",
			"flan",
			"rex",
			"wen",
			"fene",
			"kika"
			};
	
	String[] suffixes = new String[] 
			{
			"en",
			"al",
			"tana",
			"es",
			"olo",
			"ew",
			"tran",
			"nas",
			"orp",
			"ho",
			"ben",
			" 101",
			" 435",
			" 23",
			" 12",
			" 874"
			};

	public PlanetNameGen() {
		
	}
	
	public String getRandomName() {
		Random r = new Random();
		
		return prefixes[r.nextInt(prefixes.length)] + middle[r.nextInt(middle.length)] + suffixes[r.nextInt(suffixes.length)];
	}
}
