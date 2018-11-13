package ar.edu.itba.ss.managers;

import ar.edu.itba.ss.utils.other.InjectorModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class InjectorManager {

    private static final Injector injector = Guice.createInjector(new InjectorModule());

    private InjectorManager() {}

    public static Injector getInjector() {
        return injector;
    }

}