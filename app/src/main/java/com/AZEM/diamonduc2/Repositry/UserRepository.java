package com.AZEM.diamonduc2.Repositry;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.AZEM.diamonduc2.Dao.UserDao;
import com.AZEM.diamonduc2.Databases.UserDatabase;
import com.AZEM.diamonduc2.models.User;

import java.util.List;

public class UserRepository {
    private UserDao userDao;
    private LiveData<List<User>> userLiveData;


    public UserRepository(Application application) {
        UserDatabase database = UserDatabase.getInstance(application);
        userDao = database.userDao();
        userLiveData = userDao.getAllUser();
    }

    public void insertUser(User user) {
        new insetAsyncTask(userDao).execute(user);
    }

    public void updateUser(User user) {
        new updateAsyncTask(userDao).execute(user);
    }

    public void deleteUser(User user) {
        new deleteAsyncTask(userDao).execute();
    }

    public void deleteAllUser() {
        new deleteAllUsersAsyncTask(userDao).execute();
    }

    public LiveData<List<User>> getAllUser() {
        return userLiveData;
    }

    public LiveData<User> getUser(String id) {
        return userDao.getUser(id);
    }

    private static class insetAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        public insetAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.insert(users[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        public updateAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.update(users[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        public deleteAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.delete(users[0]);
            return null;
        }
    }

    private static class deleteAllUsersAsyncTask extends AsyncTask<Void, Void, Void> {
        private UserDao userDao;

        public deleteAllUsersAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            userDao.deleteAllUsers();
            return null;
        }
    }
}
