package com.att.api.immn.service;

import android.os.AsyncTask;
import android.os.Handler;

import com.att.api.error.InAppMessagingError;
import com.att.api.error.Utils;
import com.att.api.immn.listener.ATTIAMListener;
import com.att.api.rest.RESTException;

public class APIGetMessageContent implements ATTIAMListener {
	
	String messageId = null;
	String partNumber = null;
	private ATTIAMListener iamListener;
	IMMNService immnSrvc;
	protected Handler handler = new Handler();

	public APIGetMessageContent (String msgId, String partNumber, IMMNService immnService, 
								 ATTIAMListener iamListener) {
		this.messageId = msgId;
		this.partNumber = partNumber;
		this.immnSrvc = immnService;
		this.iamListener = iamListener;		
	}
	
	public void GetMessageContent() {
		GetMessageContentTask getMessageContentTask = new GetMessageContentTask();
		getMessageContentTask.execute(messageId, partNumber);
	}
	
	public class GetMessageContentTask extends AsyncTask<String, Void, MessageContent> {

		@Override
		protected MessageContent doInBackground(String... params) {
			// TODO Auto-generated method stub
			MessageContent msgContent = null;
			InAppMessagingError errorObj = new InAppMessagingError();

			try {
				msgContent = immnSrvc.getMessageContent(params[0], params[1]);
			} catch (RESTException e) {
				errorObj = Utils.CreateErrorObjectFromException( e );
				onError( errorObj );
			}
			return msgContent;
		}

		@Override
		protected void onPostExecute(MessageContent msgContent) {
			// TODO Auto-generated method stub
			super.onPostExecute(msgContent);
			if(null !=  msgContent) {
				onSuccess((MessageContent) msgContent);
			}
		}
		
	}
	

	@Override
	public void onSuccess(final Object msgContent) {
		// TODO Auto-generated method stub
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(null != iamListener) {
					iamListener.onSuccess((MessageContent) msgContent);
				}
				
			}
		});
		
	}

	@Override
	public void onError(final InAppMessagingError error) {
		// TODO Auto-generated method stub
		handler.post(new Runnable() {
			@Override
			public void run() {
				if (null != iamListener) {
					iamListener.onError(error);
				}
			}
		});
		
	}

}
