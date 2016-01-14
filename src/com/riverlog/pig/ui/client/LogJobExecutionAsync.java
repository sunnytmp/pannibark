package com.riverlog.pig.ui.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The client side stub for the RPC service.
 */
public interface LogJobExecutionAsync {
	void greetServer(String name, AsyncCallback<String> callback);
}
