package com.AZEM.diamonduc2.Repositry;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.AZEM.diamonduc2.Dao.RequestsDao;
import com.AZEM.diamonduc2.Databases.RequestDatabase;
import com.AZEM.diamonduc2.models.Requests;

import java.util.List;

public class RequestsRespo {
    private RequestsDao requestsDao;
    private LiveData<List<Requests>> requestsList;

    public RequestsRespo(Application application) {
        RequestDatabase database = RequestDatabase.getInstance(application);
        requestsDao = database.requestsDao();
        requestsList = requestsDao.getAllRequests();
    }

    public LiveData<List<Requests>> getRequestsList() {
        return requestsList;
    }

    public void deleteAllRequests() {
        new deleteAsyncTask(requestsDao).execute();
    }

    public void insertRequest(Requests requests) {
        new insertAsyncTask(requestsDao).execute(requests);
    }


    private static class insertAsyncTask extends AsyncTask<Requests, Void, Void> {
        private RequestsDao dao;

        public insertAsyncTask(RequestsDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Requests... requests) {
            dao.insert(requests[0]);
            return null;
        }
    }


    private static class deleteAsyncTask extends AsyncTask<Void, Void, Void> {
        private RequestsDao dao;

        public deleteAsyncTask(RequestsDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAllRequests();
            return null;
        }
    }
}
