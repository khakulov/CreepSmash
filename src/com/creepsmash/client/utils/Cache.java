package com.creepsmash.client.utils;

import java.awt.image.BufferedImage;
import java.util.Hashtable;

import com.creepsmash.common.CreepType;
import com.creepsmash.common.TowerType;


/**
 * Cache for images and shapes. All images used in the game should be placed
 * into this cache. The hashtables with the array of images can be used for
 * animation.
 */
public class Cache {
	private Hashtable<TowerType, BufferedImage> imgTowerCache;
	private Hashtable<CreepType, BufferedImage> imgCreepCache;
	private Hashtable<TowerType, BufferedImage[]> imgArrayTowerCache;
	private Hashtable<CreepType, BufferedImage[]> imgArrayCreepCache;

	/**
	 * Creates the instance of Cache.
	 */
	private final static Cache instance = new Cache();

	private Cache() {
		this.imgTowerCache = new Hashtable<TowerType, BufferedImage>();
		this.imgCreepCache = new Hashtable<CreepType, BufferedImage>();
		this.imgArrayTowerCache = new Hashtable<TowerType, BufferedImage[]>();
		this.imgArrayCreepCache = new Hashtable<CreepType, BufferedImage[]>();
	}

	/**
	 * Method for unit test. Clears all hashtables.
	 */
	public void clearCache() {
		this.imgTowerCache.clear();
		this.imgCreepCache.clear();
		this.imgArrayCreepCache.clear();
		this.imgArrayTowerCache.clear();
	}

	public static Cache getInstance() {
		return instance;
	}

	/**
	 * Tests if there is already an array of image for the given type.
	 * 
	 * @param type
	 *            the Tower type
	 * @return true if theres already an image array in the hashtable
	 */
	public boolean hasArrayTowerImg(TowerType type) {
		return this.imgArrayTowerCache.containsKey(type);
	}

	/**
	 * Gets an image array from the cache.
	 * 
	 * @param type
	 *            the Tower type
	 * @return the BufferedImage array for the type or null if there is no image
	 *         for the Tower type
	 */
	public BufferedImage[] getArrayTowerImg(TowerType type) {
		return this.imgArrayTowerCache.get(type);
	}

	/**
	 * Adds an image array to the cache.
	 * 
	 * @param type
	 *            the Tower type
	 * @param image
	 *            the BufferedImage array
	 */
	public void putArrayTowerImg(TowerType type, BufferedImage[] image) {
		this.imgArrayTowerCache.put(type, image);
	}

	/**
	 * Tests if there is already an image for the given type.
	 * 
	 * @param type
	 *            the tower type
	 * @return true if theres already an image in the hashtable
	 */
	public boolean hasTowerImg(TowerType type) {
		return this.imgTowerCache.containsKey(type);
	}

	/**
	 * Gets an image from the cache.
	 * 
	 * @param type
	 *            the tower type
	 * @return the BufferedImage for the type or null if there is no image for
	 *         the tower type
	 */
	public BufferedImage getTowerImg(TowerType type) {
		return this.imgTowerCache.get(type);
	}

	/**
	 * Adds an image to the cache.
	 * 
	 * @param type
	 *            the tower type
	 * @param image
	 *            the BufferedImage
	 */
	public void putTowerImg(TowerType type, BufferedImage image) {
		this.imgTowerCache.put(type, image);
	}

	/**
	 * Tests if there is already an array of image for the given type.
	 * 
	 * @param type
	 *            the Creep type
	 * @return true if theres already an image array in the hashtable
	 */
	public boolean hasArrayCreepImg(CreepType type) {
		return this.imgArrayCreepCache.containsKey(type);
	}

	/**
	 * Gets an image array from the cache.
	 * 
	 * @param type
	 *            the Creep type
	 * @return the BufferedImage array for the type or null if there is no image
	 *         for the Creep type
	 */
	public BufferedImage[] getArrayCreepImg(CreepType type) {
		return this.imgArrayCreepCache.get(type);
	}

	/**
	 * Adds an image array to the cache.
	 * 
	 * @param type
	 *            the Creep type
	 * @param image
	 *            the BufferedImage array
	 */
	public void putArrayCreepImg(CreepType type, BufferedImage[] image) {
		this.imgArrayCreepCache.put(type, image);
	}

	/**
	 * Tests if there is already an image for the given type.
	 * 
	 * @param type
	 *            the Creep type
	 * @return true if theres already an image in the hashtable
	 */
	public boolean hasCreepImg(CreepType type) {
		return this.imgCreepCache.containsKey(type);
	}

	/**
	 * Gets an image from the cache.
	 * 
	 * @param type
	 *            the Creep type
	 * @return the BufferedImage for the type or null if there is no image for
	 *         the Creep type
	 */
	public BufferedImage getCreepImg(CreepType type) {
		return this.imgCreepCache.get(type);
	}

	/**
	 * Adds an image to the cache.
	 * 
	 * @param type
	 *            the Creep type
	 * @param image
	 *            the BufferedImage
	 */
	public void putCreepImg(CreepType type, BufferedImage image) {
		this.imgCreepCache.put(type, image);
	}
}
