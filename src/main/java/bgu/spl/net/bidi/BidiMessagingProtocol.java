package bgu.spl.net.bidi;

import bgu.spl.net.srv.Message.Message;

public interface BidiMessagingProtocol<T>  {
	/**
	 * Used to initiate the current client protocol with it's personal connection ID and the connections implementation
	**/
    void start(int connectionId, Connections<T> connections);
    
    void process(Message message); //changed from process(T message)
	
	/**
     * @return true if the connection should be terminated
     */
    boolean shouldTerminate();
}