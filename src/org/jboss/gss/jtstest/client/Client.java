/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the VmTwoBeanOne Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the VmTwoBeanOne
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.gss.jtstest.client;

import java.util.Properties;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import oraProxyTest.MyBeans;

public class Client {
	
	public static void main(String[] args) throws Exception {
 		Client c = new Client();  
	}

	InitialContext ctx;

	int iter = 1;

	private UserTransaction ut = null;
	
	Logger log = Logger.getLogger(this.getClass().getCanonicalName());

	public Client() throws Exception {
		long startTime = System.currentTimeMillis();
		
		getContext();
		
		try {
			if (startUtx()){

				log.info("------------------> startUtx Passed");
				beginWork(iter);

				commitUtx();  

				long stopTime = System.currentTimeMillis();
				long runTime = stopTime - startTime;
				log.info("iterations: " + iter + " Run time: " + runTime);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public Client(int iter) {
		super();
		this.iter = iter;
	}
	
	public void beginWork(int x) throws IllegalStateException, SecurityException, SystemException {

		log.info("beginWork");
		
		
		for (int cnt = 0; cnt < x; cnt++) {
				
				try {
					MyBeans beanOne = (MyBeans) PortableRemoteObject.narrow(ctx.lookup("Beanone/remote"),MyBeans.class);
						
					beanOne.setMeaningless("m1");
					
					beanOne.removeMe();
		
					MyBeans beanTwo = (MyBeans) PortableRemoteObject.narrow(ctx.lookup("Beantwo/remote"),MyBeans.class);
					
					beanTwo.setMeaningless("m2");
					
					beanTwo.removeMe();
						
				} catch (Throwable ex) {
						log.warning("Problem in beginWork:");
						//ut.rollback();
						ex.printStackTrace();
				}
		}
	}
	
	private void commitUtx() throws IllegalStateException, SecurityException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		log.info(Integer.toString(ut.getStatus()));
		if (ut.getStatus() != Status.STATUS_MARKED_ROLLBACK){
			ut.commit();
		}
	}

	private void getContext() {
    	try {
    		if (ctx == null) {
    			Properties prop=new Properties();
    			prop.put(Context.INITIAL_CONTEXT_FACTORY,"org.jnp.interfaces.NamingContextFactory");
    			prop.put(Context.PROVIDER_URL,"127.0.0.1:1099");

    			ctx = new InitialContext(prop);

    			log.info("InitialContext created for Client");
    		}
    	} catch (NamingException e) {
    		e.printStackTrace();
    	}    	
	}

	public int getIter() {
		return iter;
	}
	
    private UserTransaction getUtx() throws Exception {
    	log.info("------------------> getUtx()");
    	javax.transaction.UserTransaction utx = null;
		
    	utx = (UserTransaction) ctx.lookup("UserTransaction");
		log.info("------------------> UTX is initialized!");
		
		return utx;
    }


	public void setIter(int iter) {
		this.iter = iter;
	}

	private boolean startUtx() throws Exception{
		log.info("------------------> startUtx()");
		
		boolean started = false;
		
		if ((ut = getUtx()) == null){
			return started;
		};

		log.info("------------------> ut.begin");
		ut.begin();	
		started = true;

		return started;
	}
}
