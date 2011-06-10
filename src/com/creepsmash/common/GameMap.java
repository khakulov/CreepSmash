package com.creepsmash.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Describes a map for the game.
 */
public enum GameMap {
	Random_Map("com/creepsmash/client/resources/maps/zufall.map"), 
	REDWORLD("com/creepsmash/client/resources/maps/map_red.map"), 
	ASTEROID("com/creepsmash/client/resources/maps/map_asteroid.map"), 
	BLACKHOLEVECTOR("com/creepsmash/client/resources/maps/map_blackholevector.map"), 
	BLUE("com/creepsmash/client/resources/maps/map_blue.map"), 
	BLUEMAGMA("com/creepsmash/client/resources/maps/map_bluemagma.map"), 
	BLUESTARS("com/creepsmash/client/resources/maps/map_bluestars.map"), 
	BLUEVECTORWATER("com/creepsmash/client/resources/maps/map_bluevectorwater.map"), 
	CHROMOSOMVEKTOR("com/creepsmash/client/resources/maps/map_chromosomvektor.map"), 
	CIRCLE("com/creepsmash/client/resources/maps/map_circle.map"), 
	CIRCLEVECTOR("com/creepsmash/client/resources/maps/map_circlevector.map"), 
	COLORCIRCLEVECTOR("com/creepsmash/client/resources/maps/map_colorcirclevector.map"), 
	CORRODED("com/creepsmash/client/resources/maps/map_corroded.map"), 
	CROSSVECTOR("com/creepsmash/client/resources/maps/map_crossvector.map"), 
	CRYSTAL("com/creepsmash/client/resources/maps/map_crystal.map"), 
	DARKVECTOR("com/creepsmash/client/resources/maps/map_darkvector.map"), 
	DESK("com/creepsmash/client/resources/maps/map_desk.map"), 
	DISEASEDVECTOR("com/creepsmash/client/resources/maps/map_diseasedvector.map"), 
	DOODLEWAR("com/creepsmash/client/resources/maps/map_doodlewar.map"), 
	EMERALDTILES("com/creepsmash/client/resources/maps/map_emeraldtiles.map"), 
	FLOWER("com/creepsmash/client/resources/maps/map_flower.map"), 
	FLYINGANGEL("com/creepsmash/client/resources/maps/map_FlyingAngel.map"), 
	FLYINGORB("com/creepsmash/client/resources/maps/map_flyingorb.map"), 
	GEWITTER("com/creepsmash/client/resources/maps/map_gewitter.map"), 
	GOLDENAGE("com/creepsmash/client/resources/maps/map_goldenage.map"), 
	GREEN("com/creepsmash/client/resources/maps/map_green.map"), 
	GREENCRISTALINE("com/creepsmash/client/resources/maps/map_GreenCristaline.map"), 
	GREENVECTOR("com/creepsmash/client/resources/maps/map_greenvector.map"), 
	GRUNGE("com/creepsmash/client/resources/maps/map_grunge.map"), 
	HANDY("com/creepsmash/client/resources/maps/map_handy.map"),  
	ISLANDS("com/creepsmash/client/resources/maps/map_islands.map"),  
	JUMPINGCREEPS("com/creepsmash/client/resources/maps/map_jumpingcreeps.map"), 
	JUNGLE("com/creepsmash/client/resources/maps/map_jungle.map"), 
	KEEPOFFTHEGRASS("com/creepsmash/client/resources/maps/map_keepoffthegrass.map"), 
	LADDERVECTOR("com/creepsmash/client/resources/maps/map_laddervector.map"), 
	LAVA("com/creepsmash/client/resources/maps/map_lava.map"), 
	LITTLEPUNK("com/creepsmash/client/resources/maps/map_littlepunk.map"), 
	LONG("com/creepsmash/client/resources/maps/map_long.map"),
	LOOMIS("com/creepsmash/client/resources/maps/map_loomis.map"),  
	MAGICMIKE("com/creepsmash/client/resources/maps/map_magicmike.map"), 
	MASTESOFCREEP("com/creepsmash/client/resources/maps/map_mastesofcreep.map"), 
	NOVA("com/creepsmash/client/resources/maps/map_nova.map"), 
	NUCLEARVECTOR("com/creepsmash/client/resources/maps/map_nuclearvector.map"), 
	OLDPAPER("com/creepsmash/client/resources/maps/map_oldpaper.map"), 
	ORANGEVECTOR("com/creepsmash/client/resources/maps/map_orangevector.map"), 
	PINKVECTOR("com/creepsmash/client/resources/maps/map_pinkvector.map"), 
	PLASMAVEKTOR("com/creepsmash/client/resources/maps/map_plasmavektor.map"), 
	PLOX("com/creepsmash/client/resources/maps/map_plox.map"), 
	PRISON("com/creepsmash/client/resources/maps/map_prison.map"),
	RACEWAYS("com/creepsmash/client/resources/maps/map_raceways.map"), 
	RADIALFADE("com/creepsmash/client/resources/maps/map_radialfade.map"), 
	RAINBOW("com/creepsmash/client/resources/maps/map_rainbow.map"), 
	REDCOREWORLD("com/creepsmash/client/resources/maps/map_redcoreworld.map"), 
	REDCREEP("com/creepsmash/client/resources/maps/map_redcreep.map"), 
	REDVECTOR("com/creepsmash/client/resources/maps/map_redvector.map"), 
	RICHTUNGSWECHSEL("com/creepsmash/client/resources/maps/map_richtungswechsel.map"), 
	RUBYTILES("com/creepsmash/client/resources/maps/map_rubytiles.map"), 
	SENFGLAS("com/creepsmash/client/resources/maps/map_senfglas.map"), 
	SHOOPDAWHOOP("com/creepsmash/client/resources/maps/map_shoopdawhoop.map"), 
	SPEEDRACE("com/creepsmash/client/resources/maps/map_speedrace.map"), 
	SPEEDVECTOR("com/creepsmash/client/resources/maps/map_speedvector.map"), 
	SPONGLE("com/creepsmash/client/resources/maps/map_spongle.map"), 
	STAIRSVECTOR("com/creepsmash/client/resources/maps/map_stairsvector.map"), 
	STARS("com/creepsmash/client/resources/maps/map_stars.map"), 
	STONES("com/creepsmash/client/resources/maps/map_stones.map"), 
	SUMMER2("com/creepsmash/client/resources/maps/map_summer2.map"), 
	TR2N("com/creepsmash/client/resources/maps/map_tr2n.map"), 
	TRANSIT("com/creepsmash/client/resources/maps/map_Transit.map"), 
	TRICKY("com/creepsmash/client/resources/maps/map_tricky.map"), 
	VERLAUFEN("com/creepsmash/client/resources/maps/map_verlaufen.map"), 
	VORTEX("com/creepsmash/client/resources/maps/map_vortex.map"), 
	WALDGEIST("com/creepsmash/client/resources/maps/map_waldgeist.map"), 
	WATERDROPS("com/creepsmash/client/resources/maps/map_waterdrops.map"), 
	WHITEVECTOR("com/creepsmash/client/resources/maps/map_whitevector.map");

	private String filename = "";

	GameMap(String flname) {
		this.filename = flname;
	}

	/**
	 * Filename of this Map.
	 * @return String of Filename
	 */
	public String getFilename() {
		return this.filename;
	}

	/**
	 * Get a map by id.
	 * @param id the id of the map
	 * @return the map
	 */
	public static GameMap getMapById(int id) {
		for (GameMap m : values()) {
			if (m.ordinal() == id) {
				return m;
			}
		}
		// default, if the id is wrong
		return REDWORLD;
	}

	/**
	 * geter for the path of the picture of a map.
	 * @param map mapname
	 * @return picture path
	 */
	public String getPicturePath() {
		String path = "zufall.jpg";
		String tempStr = null;
		InputStream res = (InputStream) IConstants.class.getClassLoader()
				.getResourceAsStream(this.getFilename());
		if (res == null) {
			System.out.println("Cannot open map: " + this.getFilename());
		} else {
			BufferedReader br = new BufferedReader(new InputStreamReader(res));
			try {
				while ((tempStr = br.readLine()) != null) {

					if (tempStr.contains(".bmp")
							|| tempStr.contains(".png")
							|| tempStr.contains(".jpg")) {
						path = tempStr.trim();
						break;
					}
				}
			} catch (IOException e) {
				System.out.println("Cannot read map: " + this.getFilename());
			}
		}
		return "com/creepsmash/client/resources/maps/" + path;
	}

	/**
	 * geter for the path of the picture thumbnail of a map.
	 * 
	 * @param map mapname
	 * @return picture path
	 */
	public String getPictureThumbnailPath() {
		String path = "zufall.jpg";
		String tempStr = null;
		InputStream res = (InputStream) IConstants.class.getClassLoader()
				.getResourceAsStream(this.getFilename());
		if (res == null) {
			System.out.println("Cannot open map: " + this.getFilename());
		} else {
			BufferedReader br = new BufferedReader(new InputStreamReader(res));
			try {
				while ((tempStr = br.readLine()) != null) {
					if (tempStr.contains(".bmp")
							|| tempStr.contains(".png")
							|| tempStr.contains(".jpg")) {
						path = tempStr.trim();
						break;
					}
				}
			} catch (IOException e) {
				System.out.println("Cannot read map: " + this.getFilename());
			}
		}
		return "com/creepsmash/client/resources/maps/thumbnail/" + path;
	}
}
