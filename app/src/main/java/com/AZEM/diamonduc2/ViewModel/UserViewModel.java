package com.AZEM.diamonduc2.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.AZEM.diamonduc2.models.User;
import com.AZEM.diamonduc2.Repositry.UserRepository;

import java.util.List;

public class UserViewModel extends AndroidViewModel {
    private UserRepository repository;
    private LiveData<List<User>> userLiveData;

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
        userLiveData = repository.getAllUser();
    }

    public void insert(User user) {
        repository.insertUser(user);
    }

    public void update(User user) {
        repository.updateUser(user);
    }

    public void delete(User user) {
        repository.deleteUser(user);
    }

    public void deleteAllUser() {
        repository.deleteAllUser();
    }

    public LiveData<List<User>> getAllUser() {
        return userLiveData;
    }


    public LiveData<User> getUser(String id) {
        return repository.getUser(id);
    }
}
