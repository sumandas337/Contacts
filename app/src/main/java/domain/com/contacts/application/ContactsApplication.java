package domain.com.contacts.application;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.facebook.stetho.Stetho;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by sumandas on 29/01/2017.
 */

public class ContactsApplication extends MultiDexApplication {

    protected IAppComponent mAppComponent;


    protected static Context mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp=getApplicationContext();
        initAppComponent();
        initDbFlow();

        Stetho.InitializerBuilder initializerBuilder =
                Stetho.newInitializerBuilder(this);

        initializerBuilder.enableWebKitInspector(
                Stetho.defaultInspectorModulesProvider(this)
        );

        initializerBuilder.enableDumpapp(
                Stetho.defaultDumperPluginsProvider(this)
        );

        Stetho.Initializer initializer = initializerBuilder.build();

        Stetho.initialize(initializer);

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);

    }

    public void initAppComponent() {
        mAppComponent = DaggerAppComponent.builder()
                .build();
    }

    public IAppComponent getmApplicationComponent() {
        return mAppComponent;
    }

    public void initDbFlow() {
        FlowManager.init(new FlowConfig.Builder(this)
                .openDatabasesOnInit(true).build());
    }


    public static ContactsApplication getAppContext() {
        return (ContactsApplication) mApp;
    }

}
