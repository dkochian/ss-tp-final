package ar.edu.itba.ss.utils.other;

import ar.edu.itba.ss.schemas.Gear;
import ar.edu.itba.ss.schemas.Schema;
import com.google.inject.AbstractModule;

public class InjectorModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Schema.class).to(Gear.class);
        //bind(Schema.class).to(Beeman.class);
    }
}
