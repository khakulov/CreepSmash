package com.creepsmash.server;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * A singleton that can be used to retrieve an EntityManager.
 */
public final class PersistenceManager {

	private static PersistenceManager persistenceManager;
	
	private final EntityManagerFactory entityManagerFactory;
	
	/**
	 * Private constructor.
	 */
	private PersistenceManager() {
		this.entityManagerFactory = Persistence
		.createEntityManagerFactory("db", Server.getConfigFileDB());
	}
	
	/**
	 * @return the singleton.
	 */
	public static PersistenceManager getInstance() {
		
		if (persistenceManager == null) {
			persistenceManager = new PersistenceManager();
		}
		return persistenceManager;
	}
	
	
	/**
	 * @return the EntityManager.
	 */
	public EntityManager getEntityManager() {
		return this.entityManagerFactory.createEntityManager();
	}
		
}