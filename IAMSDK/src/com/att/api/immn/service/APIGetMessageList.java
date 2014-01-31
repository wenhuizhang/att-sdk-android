package com.att.api.immn.service;

import java.text.ParseException;

import org.json.JSONException;

import android.os.AsyncTask;
import android.os.Handler;

import com.att.api.immn.listener.ATTIAMListener;
import com.att.api.rest.RESTException;

public class APIGetMessageList implements ATTIAMListener {
	int limit = 0;
	int offset = 0;
	private ATTIAMListener iamListener;
	IMMNService immnSrvc;
	protected Handler handler = new Handler();
	
	public APIGetMessageList(int limit, int offset,
			IMMNService immnService, ATTIAMListener iamListener) {

		this.limit = limit;
		this.offset = offset;
		this.iamListener = iamListener;
		this.immnSrvc = immnService;
	}
	
	public void GetMessageList() {
		GetMessageListTask getMessageListTask = new GetMessageListTask();
		getMessageListTask.execute(limit,offset);
	}
	
	public class  GetMessageListTask extends AsyncTask<Integer, Void, MessageList> {

		@Override
		protected MessageList doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			MessageList messageList = null;
			try {
				messageList = immnSrvc.getMessageList(params[0],params[1]);
			} catch (RESTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return messageList;
		}

		@Override
		protected void onPostExecute(MessageList messageList) {
			// TODO Auto-generated method stub
			super.onPostExecute(messageList);
			if( null != messageList ) {
				onSuccess((MessageList) messageList);
			} else {
				onError((MessageList) messageList);
			}
			
		}
		
	}

	@Override
	public void onSuccess(final Object messageList) {
		// TODO Auto-generated method stub
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (null != iamListener) {
					iamListener.onSuccess((MessageList) messageList);
				}
			}
			
		});
		
	}

	@Override
	public void onError(final Object error) {
		// TODO Auto-generated method stub
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(null != iamListener) {
					iamListener.onError((Exception) error);
				}
				
			}
		});
		
	}

}