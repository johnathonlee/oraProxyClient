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

public class UseClientInServerVM {
	
	public static void main(String[] args) throws Exception {
 		UseClientInServerVM c = new UseClientInServerVM(); 
	}

	InitialContext ctx;

	Logger log = Logger.getLogger(this.getClass().getCanonicalName());

	public UseClientInServerVM() throws Exception {
		
		Properties prop=new Properties();
		prop.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
		prop.put(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
		prop.put(Context.PROVIDER_URL,"jnp://127.0.0.1:1099");

		ctx = new InitialContext(prop);

		log.info("InitialContext created for Client");
		
    	log.info("------------------> getClientInVM()");
    	ClientInVM c = null;
		
    	c = (ClientInVM) ctx.lookup("Client/remote");
			
		((ClientInVM) c).beginWork();
		log.info("------------------> Completed");
		
	}

}
