package nz.co.harbour.jay;

import org.glassfish.jersey.internal.inject.AbstractBinder;

import java.util.stream.Stream;

public class ServiceBinder extends AbstractBinder {
    private final Object[] services;

    public ServiceBinder(Object... services) {
        this.services = services;
    }

    @Override
    protected void configure() {

        Stream.of(services).forEach(s -> bind(s).to(s.getClass()));
    }
}
