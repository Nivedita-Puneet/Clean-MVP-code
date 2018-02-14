package com.jonbott.knownspies.Activities.SecretDetails;

import android.view.View;

import com.jonbott.knownspies.Helpers.Threading;
import com.jonbott.knownspies.ModelLayer.Database.Realm.Spy;

import io.reactivex.functions.Consumer;
import io.realm.Realm;

class SecretDetailsPresenter {
    private Realm realm = Realm.getDefaultInstance();

    private Spy spy;
    public String password;

    public SecretDetailsPresenter(int spyId) {
        spy = getSpy(spyId);

        password = spy.password;
    }

    public void crackPassword(Consumer<String> finished) {
        Threading.async(()-> {
            //fake processing work
            Thread.sleep(2000);
            return true;
        }, success -> {
            finished.accept(password);
        });
    }

    //region Data loading

    public  Spy getSpy(int id) {
        Spy tempSpy = realm.where(Spy.class).equalTo("id", id).findFirst();
        return realm.copyFromRealm(tempSpy);
    }

    //endregion
}
