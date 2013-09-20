package org.jboss.gss.jtstest.client;

import javax.ejb.Remote;

@Remote
public interface ClientInVM {
	public void beginWork() throws Exception;
}