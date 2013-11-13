package edu.brown.hstore.callbacks;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.voltdb.ClientResponseImpl;
import org.voltdb.messaging.FastDeserializer;

import com.google.protobuf.RpcCallback;

import edu.brown.hstore.HStoreConstants;
import edu.brown.hstore.Hstoreservice.Status;
import edu.brown.hstore.Jvmsnapshot.TransactionResponse;
import edu.brown.hstore.txns.LocalTransaction;
import edu.brown.logging.LoggerUtil;
import edu.brown.logging.LoggerUtil.LoggerBoolean;


public class JVMSnapshotTransactionCallback implements RpcCallback<TransactionResponse> {
    private static final Logger LOG = Logger.getLogger(JVMSnapshotTransactionCallback.class);
    private static final LoggerBoolean debug = new LoggerBoolean();
    private static final LoggerBoolean trace = new LoggerBoolean();
    static {
        LoggerUtil.attachObserver(LOG, debug, trace);
    }
    
    private RpcCallback<ClientResponseImpl> clientCallback;
    private long client_handle;
    
	public JVMSnapshotTransactionCallback(
			long client_handle, RpcCallback<ClientResponseImpl> clientCallback) {
		// TODO Auto-generated constructor stub
		this.clientCallback = clientCallback;
		this.client_handle = client_handle;
	}

	@Override
	public void run(TransactionResponse parameter) {
		// TODO Auto-generated method stub
		if (debug.val) LOG.debug("Received callback from the snapshot");
		FastDeserializer in = new FastDeserializer(parameter.getOutput().toByteArray());
		ClientResponseImpl response = new ClientResponseImpl();
		try {
			response.readExternal(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (debug.val) LOG.debug("Msg: "+response.toString());
		
		clientCallback.run(response);
	}

}