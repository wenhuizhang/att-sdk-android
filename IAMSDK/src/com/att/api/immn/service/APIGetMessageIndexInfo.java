package com.att.api.immn.service;

import java.text.ParseException;

import org.apache.http.client.fluent.Async;
import org.json.JSONException;

import android.os.AsyncTask;
import android.os.Handler;

import com.att.api.immn.listener.ATTIAMListener;
import com.att.api.rest.RESTException;

public class APIGetMessageIndexInfo implements ATTIAMListener {
	
	private ATTIAMListener iamListener;
	IMMNService immnSrvc;
	protected Handler handler = new Handler();

	public APIGetMessageIndexInfo(IMMNService immnService, ATTIAMListener attiamListener) {
		this.immnSrvc = immnService;
		this.iamListener = attiamListener;
	}
	
	public void GetMessageIndexInfo() {
		GetMessageIndexInfoTask getMessageIndexInfoTask = new GetMessageIndexInfoTask();
		getMessageIndexInfoTask.execute();
	}
	
	public class GetMessageIndexInfoTask extends AsyncTask<Void, Void, MessageIndexInfo> {

		@Override
		protected MessageIndexInfo doInBackground(Void... params) {
			// TODO Auto-generated method stub
			MessageIndexInfo messageIndexInfo = null;
			try {
				messageIndexInfo = immnSrvc.getMessageIndexInfo();
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
			return messageIndexInfo;
		}

		@Override
		protected void onPostExecute(MessageIndexInfo messageIndexInfo) {
			// TODO Auto-generated method stub
			super.onPostExecute(messageIndexInfo);
			onSuccess(messageIndexInfo);
		}
		
	}

	@Override
	public void onSuccess(Object response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(Object error) {
		// TODO Auto-generated method stub
		
	}

}
