package com.AZEM.diamonduc2.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.AZEM.diamonduc2.Repositry.RequestsRespo;
import com.AZEM.diamonduc2.models.Requests;

import java.util.List;

public class RequestsViewModel extends AndroidViewModel {
    private RequestsRespo requestsRespo;
    private LiveData<List<Requests>> requests;

    public RequestsViewModel(@NonNull Application application) {
        super(application);
        requestsRespo = new RequestsRespo(application);
        requests = requestsRespo.getRequestsList();
    }

    public LiveData<List<Requests>> getAllRequests() {
        return requests;
    }

    public void deleteAllRequests() {
        requestsRespo.deleteAllRequests();
    }

    public void insertRequests(Requests requests) {
        requestsRespo.insertRequest(requests);
    }
}
